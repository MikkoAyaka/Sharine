package org.tcpx.sharine.service;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.tcpx.sharine.constants.RedisPrefixConst;
import org.tcpx.sharine.constants.UserConst;
import org.tcpx.sharine.dto.UserPass;
import org.tcpx.sharine.entity.User;
import org.tcpx.sharine.enums.StatusCodeEnum;
import org.tcpx.sharine.exception.ErrorException;
import org.tcpx.sharine.exception.WarnException;
import org.tcpx.sharine.repository.BookmarkRepository;
import org.tcpx.sharine.repository.UserRepository;
import org.tcpx.sharine.utils.EncryptionUtil;
import org.tcpx.sharine.utils.StringUtils;
import org.tcpx.sharine.vo.UserDetailVO;
import org.tcpx.sharine.vo.UserSimpleVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    final UserRepository userRepository;
    final BookmarkRepository bookmarkRepository;

    final FavoriteService favoriteService;

    final UserRelationService userRelationService;

    final RedisService redisService;

    final EmailService emailService;

    public UserService(UserRepository userRepository,
                       FavoriteService favoriteService,
                       UserRelationService userRelationService,
                       RedisService redisService,
                       EmailService emailService,
                       BookmarkRepository bookmarkRepository) {
        this.userRepository = userRepository;
        this.favoriteService = favoriteService;
        this.userRelationService = userRelationService;
        this.redisService = redisService;
        this.emailService = emailService;
        this.bookmarkRepository = bookmarkRepository;
    }

    public UserSimpleVO login(UserPass userPass) {
        Optional<User> byUsername = userRepository.findByAccount(userPass.getAccount());
        if (byUsername.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        User user = byUsername.get();
        if (!EncryptionUtil.match(userPass.getPassword(), user.getPassword())) {
            throw new ErrorException(StatusCodeEnum.PASSWORD_NOT_MATCHED);
        }
        StpUtil.login(user.getId());
        return buildUserSimpleVO(user);
    }

    public UserSimpleVO register(UserPass userPass) {
        String account = userPass.getAccount();
        emailService.mailVerify(account, userPass.getVerificationCode());

        // 用户已存在
        if (userRepository.existsByAccount(account).equals(true)) {
            throw new ErrorException(StatusCodeEnum.DATA_EXIST);
        }

        User user = User.builder()
                .account(account)
                .password(EncryptionUtil.encode(userPass.getPassword()))
                .nickname(UserConst.DEFAULT_NICKNAME)
                .avatar(UserConst.DEFAULT_AVATAR)
                .content(UserConst.DEFAULT_CONTENT)
                .build();

        user = userRepository.save(user);
        return buildUserSimpleVO(user);
    }

    public UserSimpleVO changePassword(UserPass userPass) {
        String account = userPass.getAccount();
        boolean checked = StringUtils.checkEmail(account);
        // 非邮箱
        if (!checked) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        String code = (String) redisService.get(RedisPrefixConst.TOKEN + account);
        // 验证码错误
        if (code == null || !code.equals(userPass.getVerificationCode())) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        // 用户不存在
        Optional<User> byUsername = userRepository.findByAccount(account);
        if (byUsername.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        // 修改数据
        User user = byUsername.get();
        user.setPassword(EncryptionUtil.encode(userPass.getPassword()));
        user = userRepository.save(user);

        return buildUserSimpleVO(user);
    }

    public void requestForCode(UserPass userPass) {
        String username = userPass.getAccount();
        boolean checked = StringUtils.checkEmail(username);
        // 非邮箱
        if (!checked) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        if (redisService.get(username) != null) {
            throw new WarnException(StatusCodeEnum.FAIL.getCode(), "请在" + redisService.getExpire(username) + "秒后重新发送");
        }

        // 五分钟过期
        String code = StringUtils.getRandomCode(6);
        redisService.set(RedisPrefixConst.TOKEN + username, code, 5 * 60);

        try {
            emailService.sendCode(username, code, 5L);
        } catch (MessagingException e) {
            throw new ErrorException("邮件发送失败");
        }
    }

    private static UserSimpleVO buildUserSimpleVO(User user) {
        return UserSimpleVO.of(user);
    }

    private static UserDetailVO buildUserDetailVO(User user) {
        return UserDetailVO.of(user);
    }

    /**
     * 查询用户粗略档案信息
     *
     * @param userId 用户ID
     * @return 用户粗略档案信息
     */
    public UserSimpleVO findUserSimpleInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WarnException(StatusCodeEnum.DATA_NOT_EXIST));
        return buildUserSimpleVO(user);
    }
    public UserSimpleVO findUserSimpleInfo(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new WarnException(StatusCodeEnum.DATA_NOT_EXIST));
        return buildUserSimpleVO(user);
    }

    /**
     * 批量查询用户粗略信息
     *
     * @param userIds 用户ID
     * @return 用户粗略信息
     */
    public Map<Long, UserSimpleVO> findUserSimpleInfo(List<Long> userIds) {
        userIds = userIds.stream().distinct().collect(Collectors.toList());

        List<User> allByIdIn = userRepository.findAllById(userIds);

        return allByIdIn.stream().map(UserService::buildUserSimpleVO)
                .collect(
                        Collectors.toMap(UserSimpleVO::getId, person -> person)
                );
    }

    /**
     * 查询用户详细信息
     *
     * @param userId 用户ID
     * @return 用户详细信息
     */
    public UserDetailVO findUserDetailInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WarnException(StatusCodeEnum.DATA_NOT_EXIST));

        UserDetailVO userDetailVO = UserDetailVO.of(user);
        userDetailVO.setFavouriteCount(favoriteService.countUserFavorite(userId));
        userDetailVO.setFollowingCount(userRelationService.countUserFollowing(userId));
        userDetailVO.setFollowedCount(userRelationService.countUserFollowed(userId));
        userDetailVO.setBookmarkCount(bookmarkRepository.countByUserId(userId));

        return userDetailVO;
    }

    /**
     * 批量查询用户详细信息
     *
     * @param userIds 用户ID
     * @return 用户详细信息
     */
    public Map<Long, UserDetailVO> findUserDetailInfo(List<Long> userIds) {
        userIds = userIds.stream().distinct().collect(Collectors.toList());

        List<User> users = userRepository.findAllById(userIds);

        return users.stream().map(user -> {
                    UserDetailVO result = buildUserDetailVO(user);
                    result.setBookmarkCount(bookmarkRepository.countByUserId(user.getId()));
                    result.setFollowedCount(userRelationService.countUserFollowed(user.getId()));
                    result.setFollowingCount(userRelationService.countUserFollowing(user.getId()));
                    result.setFavouriteCount(favoriteService.countUserFavorite(user.getId()));
                    return result;
                })
                .collect(
                        Collectors.toMap(UserDetailVO::getId, person -> person)
                );
    }
}
