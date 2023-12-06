package org.wolflink.sharine.api.repository;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.wolflink.sharine.api.constants.DatabaseConst;
import org.wolflink.sharine.api.entity.Bookmark;

import java.util.List;

@CacheConfig(cacheNames = DatabaseConst.BOOKMARK)
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @NotNull
    @CachePut(unless = "#result==null")
    <S extends Bookmark> S save(@NotNull S entity);

    Bookmark findByUserIdAndVideoId(Long userId, Long videoId);

    boolean existsByUserIdAndVideoId(Long userId, Long videoId);

    @Modifying
    @Transactional
    void deleteByUserIdAndVideoId(Long userId, Long videoId);

//    @Cacheable(unless = "#result==null")
    Integer countByUserId(Long userId);

//    @Cacheable(unless = "#result==null")
    Integer countByVideoId(Long videoId);


    List<Bookmark> findByUserId(Long userId, Pageable pageable);
    List<Bookmark> findByUserId(Long userId);
}