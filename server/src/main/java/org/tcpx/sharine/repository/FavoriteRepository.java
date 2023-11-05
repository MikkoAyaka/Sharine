package org.tcpx.sharine.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tcpx.sharine.constants.DatabaseConst;
import org.tcpx.sharine.entity.Favorite;

import java.util.List;

@CacheConfig(cacheNames = DatabaseConst.FAVOURITE)
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Cacheable(unless = "#result==null")
    Integer countByUserId(Long userId);
    @Cacheable(unless = "#result==null")
    Integer countByVideoId(Long videoId);

    void deleteByUserIdAndVideoId(Long userId, Long videoId);

    boolean existsByUserIdAndVideoId(Long userId, Long videoId);

    List<Favorite> findByUserId(Long userId, Pageable pageable);
    List<Favorite> findByUserId(Long userId);
}
