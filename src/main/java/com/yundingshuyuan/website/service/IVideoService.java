package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.VideoForm;
import com.yundingshuyuan.website.form.VideoPageForm;
import com.yundingshuyuan.website.pojo.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IVideoService extends IService<Video> {

    ServerResponse addVideo(VideoForm videoForm);

    ServerResponse listVideo(VideoPageForm videoPageForm);
}
