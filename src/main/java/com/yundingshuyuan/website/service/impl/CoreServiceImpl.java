package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.CoreMapper;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.form.CoreForm;
import com.yundingshuyuan.website.pojo.Core;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.ICoreService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class CoreServiceImpl extends ServiceImpl<CoreMapper, Core> implements ICoreService {
    @Override
    public ServerResponse score(CoreForm coreForm) {
        //如果是老师的id,则禁止打分
        if(coreForm.getUserId().equals("609706495432458241")){
            return ServerResponse.createByErrorCodeMessage(500,"请给学生打分");
        }
        //被打分人id为空
        if(coreForm.getUserId() == null){
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PARAM_ERROR);
        }
        if(coreForm.getTime() ==null){
            coreForm.setTime(LocalDateTime.now().getYear()*100+LocalDateTime.now().getMonthValue());
        }
        Core core = new Core();
        BeanUtil.copyProperties(coreForm,core, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
        boolean save= this.saveOrUpdate(core);
        if(save){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.UK_KNOW_ERROR);
        }
    }
}
