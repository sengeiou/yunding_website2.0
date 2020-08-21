package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.WorkMapper;
import com.yundingshuyuan.website.enums.LikesTypeEnum;
import com.yundingshuyuan.website.form.BelongForm;
import com.yundingshuyuan.website.form.WorkForm;
import com.yundingshuyuan.website.form.WorkPageForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.Work;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import com.yundingshuyuan.website.service.IWorkService;
import com.yundingshuyuan.website.util.SnowFlake;
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
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {
    private final
    IBelongService belongService;

    public WorkServiceImpl(IBelongService belongService) {
        this.belongService = belongService;
    }

    @Override
    public ServerResponse addWork(WorkForm workForm) {
        Work work = new Work();
        work.setId(String.valueOf(SnowFlake.nextId()));

        BeanUtil.copyProperties(workForm,work, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
        //如果没有指定期数,默认设置当前年月为期数
        if(null==work.getTime()){
            Integer time = LocalDateTime.now().getYear()*100+LocalDateTime.now().getMonthValue();
            work.setTime(time);
        }
        if(this.saveOrUpdate(work)){
            if(workForm.getBelongForms()!=null){
            for (BelongForm belongForm : workForm.getBelongForms()) {
                Belong belong = new Belong(work.getId(),belongForm.getUserId(), LikesTypeEnum.work.name());
                if (!belongService.add(belong)) {
                    return ServerResponse.createByErrorMessage("添加成员错误");
                }
            }}
            return ServerResponse.createBySuccess(work);
        }else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse listByWorkForm(WorkPageForm workPageForm) {
        //分页获取work
        IPage<Work> workIPage = new Page<>(workPageForm.getPageNum(),workPageForm.getPageSize());
        Work work = new Work();
        QueryWrapper<Work> workQueryWrapper = new QueryWrapper<>();
        if(workPageForm.getOrderBy()==null){
            workQueryWrapper.orderByDesc("created_at");
        }else {
            workQueryWrapper.orderBy(true,workPageForm.getOrder(),workPageForm.getOrderBy());
        }
        //如果通过id单独获取，浏览量+1
        if(workPageForm.getId() !=null){
            this.baseMapper.viewsAddOne(workPageForm.getId());
        }
        BeanUtil.copyProperties(workPageForm,work,CopyOptions.create());
        workQueryWrapper.setEntity(work);
        IPage page= this.page(workIPage,workQueryWrapper);
        return ServerResponse.createBySuccess(page);
    }

}
