package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yundingshuyuan.website.form.AlbumPageForm;
import com.yundingshuyuan.website.pojo.Album;
import com.yundingshuyuan.website.dao.AlbumMapper;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 相册 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements IAlbumService {
    @Override
    public ServerResponse list(AlbumPageForm albumPageForm) {
        IPage<Album> iPage = new Page<>(albumPageForm.getPageNum(), albumPageForm.getPageSize());
        Album album = new Album();
        QueryWrapper<Album> albumQueryWrapper = new QueryWrapper<>();
        BeanUtil.copyProperties(albumPageForm, album, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        albumQueryWrapper.setEntity(album);
        if(albumPageForm.getOrderBy()!=null){
            albumQueryWrapper.orderBy(true,albumPageForm.getOrder(),albumPageForm.getOrderBy());
        }else {
            albumQueryWrapper.orderByDesc("created_at");
        }
        return ServerResponse.createBySuccess(this.page(iPage, albumQueryWrapper));
    }
}
