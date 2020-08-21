package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.AlbumFileMapper;
import com.yundingshuyuan.website.form.AlbumFileForm;
import com.yundingshuyuan.website.form.AlbumFilePageForm;
import com.yundingshuyuan.website.pojo.AlbumFile;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IAlbumFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 相册内容 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class AlbumFileServiceImpl extends ServiceImpl<AlbumFileMapper, AlbumFile> implements IAlbumFileService {
    @Override
    public ServerResponse save(AlbumFileForm albumFileForm) {
        //saveOrUpdate
        AlbumFile albumFile = new AlbumFile();
        BeanUtil.copyProperties(albumFileForm, albumFile, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
        boolean saveOrUpdate = this.saveOrUpdate(albumFile);
        if (saveOrUpdate) {
            //相册图片数+1
            this.baseMapper.albumNumAdd(albumFileForm.getAlbumId());
            return ServerResponse.createBySuccess(albumFile);
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse delete(AlbumFileForm albumFileForm) {
        boolean removeById = this.removeById(albumFileForm.getId());
        if(removeById) {
            //相册图片数-1
            this.baseMapper.albumNumMinus(albumFileForm.getAlbumId());
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }

    }

    @Override
    public ServerResponse list(AlbumFilePageForm albumFilePageForm) {
        IPage<AlbumFile> iPage = new Page<>(albumFilePageForm.getPageNum(),albumFilePageForm.getPageSize());
        AlbumFile albumFile = new AlbumFile();
        BeanUtil.copyProperties(albumFilePageForm,albumFile,CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        QueryWrapper<AlbumFile> albumFileQueryWrapper = new QueryWrapper<>();
        albumFileQueryWrapper.setEntity(albumFile);
        albumFileQueryWrapper.orderByDesc("created_at");

        return ServerResponse.createBySuccess(this.page(iPage,albumFileQueryWrapper));
    }
}
