package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.LikesMapper;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.form.LikesForm;
import com.yundingshuyuan.website.form.ListLikesForm;
import com.yundingshuyuan.website.pojo.Likes;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.ILikesService;
import com.yundingshuyuan.website.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {
    private final
    LikesMapper likesMapper;
    private final
    IUserService userService;

    public LikesServiceImpl(LikesMapper likesMapper, IUserService userService) {
        this.likesMapper = likesMapper;
        this.userService = userService;
    }

    @Override
    public ServerResponse add(LikesForm likesForm) {
        Boolean aBoolean = likesMapper.judge(likesForm.getEntityId(), likesForm.getType().name(), likesForm.getUserId());
        if (aBoolean == null) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PARAM_ERROR);
        }
        if (!userService.authentication(likesForm.getUserId(), new Right(RightEnum.RIGHT_ENUM3.getId()))) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        }
        //判断是否点赞
        Likes likes = new Likes();
        BeanUtil.copyProperties(likesForm, likes, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        QueryWrapper<Likes> likesQueryWrapper = new QueryWrapper<>();
        likesQueryWrapper.setEntity(likes);
        Likes selectOne = likesMapper.selectOne(likesQueryWrapper);
        if (null != selectOne) {
            likesMapper.delete(likesQueryWrapper);
            //对应点赞数-1
            likesMapper.minus(likesForm.getType().name(),likes.getEntityId());
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.ARTICLE_SUPPORTED);
        } else {
            likesMapper.insert(likes);
            //对应表点赞数+1
            likesMapper.add(likesForm.getType().name(),likes.getEntityId());
            return ServerResponse.createBySuccess();
        }
    }

    @Override
    public Boolean check(LikesForm likesForm) {
        Likes likes = new Likes();
        BeanUtil.copyProperties(likesForm, likes, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        QueryWrapper<Likes> likesQueryWrapper = new QueryWrapper<>();
        likesQueryWrapper.setEntity(likes);
        Likes selectOne = likesMapper.selectOne(likesQueryWrapper);
        return null != selectOne;
    }

    @Override
    public ServerResponse list(ListLikesForm likesForm) {
        Likes likes = new Likes();
        BeanUtil.copyProperties(likesForm,likes,CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        QueryWrapper<Likes> likesQueryWrapper = new QueryWrapper<>();
        likesQueryWrapper.setEntity(likes);
        IPage<Likes> page = new Page<>(likesForm.getPageNum(),likesForm.getPageSize());
        List<Likes> likesList= this.list(likesQueryWrapper);
        List<Object> list = new ArrayList<>();
        for (Likes like:likesList){
            if(likesForm.getArticleLabel()==null) {
                JSONObject o = this.likesMapper.listByType(like.getEntityId(), like.getType());
                if (o != null) {
                    String created_at= o.getStr("created_at");
                    o.remove("created_at");
                    o.put("createdAt",getCreatedAt(created_at));
                    list.add(o);
                }
            }else {
                JSONObject o = this.likesMapper.listArticleByType(like.getEntityId(),like.getType(),likesForm.getArticleLabel());
                if (o != null) {
                    String created_at= o.getStr("created_at");
                    o.remove("created_at");
                    o.put("createdAt",getCreatedAt(created_at));
                    list.add(o);
                }
            }
        }
        //所有符合条件的实体对象个数
        int size = list.size();
        int num = likesForm.getPageNum();
        int rows = likesForm.getPageSize();
        int pageStart=num==1?0:(num-1)*rows;//截取的开始位置//截取的开始位置
        int pageEnd=size<num*rows?size:num*rows;//截取的结束位置
        int totalPage=list.size()/rows; //总页数
        Map<String,Object> map =new HashMap<>();
        map.put("pages",totalPage);
        if(size>pageStart){
            List<Object> objectList = list.subList(pageStart,pageEnd);
            map.put("list",objectList);

            return ServerResponse.createBySuccess(map);
        }else {
            map.put("list",null);
            return ServerResponse.createBySuccess(map);
        }



    }

    public static int[] getCreatedAt(String created_at){

        String a = "2019-09-25 00:06:33.0";
        a = created_at;
        String[] b = a.split("-");
        String[] c = b[2].split(" ");
        String[] d = c[1].split(":");
        float e = Float.parseFloat(d[2]);
        int f = (int) e;

        int[] g ={Integer.parseInt(b[0]),
                Integer.parseInt(b[1]),
                Integer.parseInt(c[0]),
                Integer.parseInt(d[0]),
                Integer.parseInt(d[1]),
                f};
        return g;
    }


}
