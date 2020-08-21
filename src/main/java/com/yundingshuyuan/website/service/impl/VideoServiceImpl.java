package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.VideoMapper;
import com.yundingshuyuan.website.enums.LikesTypeEnum;
import com.yundingshuyuan.website.form.BelongForm;
import com.yundingshuyuan.website.form.VideoForm;
import com.yundingshuyuan.website.form.VideoPageForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.Video;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import com.yundingshuyuan.website.service.IVideoService;
import com.yundingshuyuan.website.util.SnowFlake;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {
    private final
    IBelongService belongService;

    public VideoServiceImpl(IBelongService belongService) {
        this.belongService = belongService;
    }

    @Override
    public ServerResponse addVideo(VideoForm videoForm) {
        Video video = new Video();
            video.setId(String.valueOf(SnowFlake.nextId()));
        BeanUtil.copyProperties(videoForm,video, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        if (this.saveOrUpdate(video)) {
            if (videoForm.getBelongForms()!=null){
            for (BelongForm belongForm : videoForm.getBelongForms()) {
                Belong belong = new Belong(video.getId(),belongForm.getUserId(), LikesTypeEnum.video.name());
                if (!belongService.add(belong)) {
                    return ServerResponse.createByErrorMessage("添加成员错误");
                }
            }}
            return ServerResponse.createBySuccess(video);
        }else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse listVideo(VideoPageForm videoPageForm) {
        IPage<Video> iPage = new Page<>(videoPageForm.getPageNum(),videoPageForm.getPageSize());
        Video video = new Video();
        BeanUtil.copyProperties(videoPageForm,video);
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.setEntity(video);
        if(videoPageForm.getOrderBy()!=null){
            videoQueryWrapper.orderBy(true,videoPageForm.getOrder(),videoPageForm.getOrderBy());
        }else {
            videoQueryWrapper.orderByDesc("created_at");
        }
        //通过id获取视频详情，浏览量+1
        if(videoPageForm.getId()!= null){
            this.baseMapper.viewsAddOne(videoPageForm.getId());
        }
        return ServerResponse.createBySuccess(this.page(iPage,videoQueryWrapper));
    }
}
