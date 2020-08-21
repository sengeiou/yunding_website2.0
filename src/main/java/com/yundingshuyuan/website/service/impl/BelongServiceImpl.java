package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.BelongMapper;
import com.yundingshuyuan.website.dao.LikesMapper;
import com.yundingshuyuan.website.dao.UserExtendMapper;
import com.yundingshuyuan.website.dao.UserMapper;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.LikesTypeEnum;
import com.yundingshuyuan.website.enums.UserExtendEnums;
import com.yundingshuyuan.website.form.BelongForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import com.yundingshuyuan.website.vo.BelongVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 归属表 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class BelongServiceImpl extends ServiceImpl<BelongMapper, Belong> implements IBelongService {
    private final
    LikesMapper likesMapper;
    private final
    UserMapper userMapper;
    private final
    UserExtendMapper userExtendMapper;

    public BelongServiceImpl(LikesMapper likesMapper, UserMapper userMapper, UserExtendMapper userExtendMapper) {
        this.likesMapper = likesMapper;
        this.userMapper = userMapper;
        this.userExtendMapper = userExtendMapper;
    }

    @Override
    public ServerResponse add(BelongForm belongForm) {
        Boolean aBoolean = likesMapper.judge(belongForm.getEntityId(), belongForm.getEntityType().name(), belongForm.getUserId());
        if (aBoolean == null || !aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PARAM_ERROR);
        }
        Belong belong = new Belong();
        BeanUtil.copyProperties(belongForm, belong, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
        QueryWrapper<Belong> belongQueryWrapper = new QueryWrapper<Belong>().setEntity(belong);
        Belong a = this.getOne(belongQueryWrapper);
        if (a == null) {
            if (this.save(belong)) {
                if(belong.getEntityType().equals(LikesTypeEnum.work.name())){
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.work_size.name(),belong.getUserId(),"+");
                }
                return ServerResponse.createBySuccess();
            } else {
                if(belong.getEntityType().equals(LikesTypeEnum.work.name())){
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.work_size.name(),belong.getUserId(),"-");
                }
                return ServerResponse.createByError();
            }
        } else {
            this.remove(belongQueryWrapper);
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.BELONG_EXIST);
        }
    }

    @Override
    public ServerResponse listBelong(BelongForm belongForm) {
        Belong belong = new Belong();
        BeanUtil.copyProperties(belongForm, belong, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        QueryWrapper<Belong> belongQueryWrapper = new QueryWrapper<>();
        belongQueryWrapper.setEntity(belong);
        List<Belong> belongList = this.list(belongQueryWrapper);
        List<BelongVO> belongVOList = new ArrayList<>();
        for (Belong belong1:belongList){
            User a= userMapper.selectById(belong1.getUserId());
            BelongVO belongVO = new BelongVO();
            a.setPassword(null);
            belongVO.setUser(a);
            belongVO.setDescription(belong1.getDescription());
            belongVOList.add(belongVO);
        }
        return ServerResponse.createBySuccess(belongVOList);
    }

    @Override
    public boolean add(Belong belong) {
        QueryWrapper<Belong> belongQueryWrapper = new QueryWrapper<Belong>().setEntity(belong);
        Belong a = this.getOne(belongQueryWrapper);
        if (a == null) {
            this.save(belong);
            return true;
        } else {
            this.remove(belongQueryWrapper);
            return false;
        }
    }
}
