package org.wolflink.sharine.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.wolflink.sharine.api.constants.DatabaseConst;
import org.wolflink.sharine.api.dto.UploadVideoDTO;
import org.wolflink.sharine.api.utils.BeanCopyUtils;

/**
 * 视频
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = DatabaseConst.VIDEO)
public class Video {

    public static Video of(UploadVideoDTO uploadVideoDTO) {
        return BeanCopyUtils.copyObject(uploadVideoDTO, Video.class);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    String title;

    @Column
    String content;

    @Column(nullable = false)
    String url;

    @Column(nullable = false)
    String coverUrl;

    @Column(nullable = false)
    @ColumnDefault("0")
    Long viewCount;

    @UpdateTimestamp
    Long updateTime;

    @CreationTimestamp
    Long createTime;
}