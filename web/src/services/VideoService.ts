import {ResponsePack} from "@/models/ResponsePack";
import {httpClient, pack} from "@/utilities/HttpUtility";
import {Video} from "@/models/Video";
import {timeStrParse} from "@/utilities/ResourceUtility";
import {User} from "@/models/User";
import {Category} from "@/models/Category";
import {VideoComment} from "@/models/VideoComment";
import {ref} from "vue";

export class VideoService {

    // videos = ref(new Array<Video>())
    // parseCommentVO(commentVO: any):VideoComment {
    //
    //     const comment = new VideoComment()
    //
    //     comment.createTime = timeStrParse(commentVO.createTime)
    //     comment.updateTime = timeStrParse(commentVO.updateTime)
    //     comment.id = commentVO.id
    //     comment.replyId = commentVO.replyId
    //     comment.content = commentVO.content
    //     comment.videoId = commentVO.videoId
    //     const author = new User()
    //     author.id = commentVO.author.id
    //     author.nickname = commentVO.author.nickname
    //     author.avatar = commentVO.author.avatar
    //     author.content = commentVO.author.content
    //     comment.author = author
    //     return comment
    // }
    // parseVideoVO(videoVO: any):Video {
    //
    //     const video = new Video()
    //
    //     video.createTime = timeStrParse(videoVO.createTime)
    //     video.updateTime = timeStrParse(videoVO.updateTime)
    //     video.url = videoVO.url
    //     video.title = videoVO.title
    //     video.content = videoVO.content
    //     video.coverUrl = videoVO.coverUrl
    //     video.id = videoVO.id
    //     video.favoriteCount = videoVO.favoriteCount
    //     video.bookmarkCount = videoVO.bookmarkCount
    //     video.viewCount = videoVO.viewCount
    //
    //     const author = new User()
    //     author.nickname = videoVO.author.nickname
    //     author.bookmarkCount = videoVO.author.bookmarkCount
    //     author.avatar = videoVO.author.avatar
    //     author.favoriteCount = videoVO.author.favoriteCount
    //     author.followingCount = videoVO.author.followingCount
    //     author.followedCount = videoVO.author.followedCount
    //     author.content = videoVO.author.content
    //     author.id = videoVO.author.id
    //     video.author = author
    //
    //     const category = new Category()
    //     category.url = videoVO.category.url
    //     category.id = videoVO.category.id
    //     category.title = videoVO.category.title
    //
    //     video.category = category
    //     return video
    // }
}