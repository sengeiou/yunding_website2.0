package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.AlbumFileForm;
import com.yundingshuyuan.website.form.AlbumFilePageForm;
import com.yundingshuyuan.website.pojo.AlbumFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 * 相册内容 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IAlbumFileService extends IService<AlbumFile> {

    /**
     * 保存或修改
     * @param albumFileForm 相册文件表单
     * @return ServerResponse
     */
    ServerResponse save(AlbumFileForm albumFileForm);

    /**
     * 删除某个相册图片
     * @param albumFileForm 相册表单
     * @return ServerResponse
     */
    ServerResponse delete(AlbumFileForm albumFileForm);

    /**
     * 展示分页相册图片
     * @param albumFilePageForm 相册图片分页表单
     * @return ServerResponse
     */
    ServerResponse list(AlbumFilePageForm albumFilePageForm);
}
