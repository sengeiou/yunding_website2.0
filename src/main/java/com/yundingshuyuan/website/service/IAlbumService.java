package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.AlbumPageForm;
import com.yundingshuyuan.website.pojo.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IAlbumService extends IService<Album> {

    /**
     * 分页展示相册
     * @param albumPageForm 相册分页表单
     * @return ServerResponse
     */
    ServerResponse list(AlbumPageForm albumPageForm);
}
