package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.constants.SysConstant;
import com.yundingshuyuan.website.dao.ArticleMapper;
import com.yundingshuyuan.website.dao.UserExtendMapper;
import com.yundingshuyuan.website.enums.*;
import com.yundingshuyuan.website.form.ArticleForm;
import com.yundingshuyuan.website.form.ListArticleForm;
import com.yundingshuyuan.website.form.ListArticleRandomForm;
import com.yundingshuyuan.website.form.RemoveArticleForm;
import com.yundingshuyuan.website.pojo.*;
import com.yundingshuyuan.website.repository.redis.IdentityRepository;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IArticleService;
import com.yundingshuyuan.website.service.ILikesService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.util.ElasticsearchUtil;
import com.yundingshuyuan.website.util.RedisManager;
import com.yundingshuyuan.website.util.SnowFlake;
import com.yundingshuyuan.website.vo.ArticleVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    private final
    IUserService userService;
    private final
    IdentityRepository identityRepository;
    private final
    RedisManager redisManager;
    private final
    ArticleMapper articleMapper;
    private final
    ILikesService likesService;
    private final
    UserExtendMapper userExtendMapper;

    public ArticleServiceImpl(IUserService userService, IdentityRepository identityRepository, RedisManager redisManager, ArticleMapper articleMapper, ILikesService likesService, UserExtendMapper userExtendMapper) {
        this.userService = userService;
        this.identityRepository = identityRepository;
        this.redisManager = redisManager;
        this.articleMapper = articleMapper;
        this.likesService = likesService;
        this.userExtendMapper = userExtendMapper;
    }

    @Override
    public ServerResponse uploadArticle(ArticleForm articleForm) {
        Article article = redisManager.getValue(RedisPrefix.ATRICLE, articleForm.getId());
        if (article != null) {
            article.setUserId(articleForm.getUserId());
        } else {
            return ServerResponse.createByError();
        }
        if (article.getId() == null) {
            BeanUtil.copyProperties(articleForm, article, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
            if (article.getId() == null || article.getId().equals("")) {
                article.setId(String.valueOf(SnowFlake.nextId()));
            }
        }
        if(ArticleStatusEnum.FINISH!=articleForm.getStatus()) {
            article.setStatus(articleForm.getStatus().name());
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PARAM_ERROR);
        }

        boolean saveOrUpdate= this.saveOrUpdate(article);
        //上传用户extend的文章数+1
        if (saveOrUpdate){
            //
            switch (article.getLabel()){
                case "1":
                case "2":
                case "3":
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.article_size.name(),article.getUserId(),"+");
                    break;
                case "7":
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.tip_size.name(),article.getUserId(),"+");
                    break;
                default:
                    break;
            }
        }

        return ServerResponse.createBySuccess(this.getById(article.getId()));
    }



    @Override
    public Boolean auth(String userId, String label) {
        String stringBuilder = "ARTICLE_TYPE_ENUM" +
                label;
        Role[] roles = identityRepository.findIdentityByUserId(userId);
        //如果是管理员，则肯定有权限
        for (Role role : roles) {
            if (role.getId().equals(RoleEnum.ROLE_ENUM6.getId())) {
                return true;
            }
        }
        switch (ArticleTypeEnum.valueOf(stringBuilder)) {
            case ARTICLE_TYPE_ENUM7:
                return true;
            case ARTICLE_TYPE_ENUM1:
                return userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM5.getId()), new Right(RightEnum.RIGHT_ENUM4.getId()));
            case ARTICLE_TYPE_ENUM2:
                return userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM2.getId()), new Right(RightEnum.RIGHT_ENUM4.getId()));
            case ARTICLE_TYPE_ENUM3:
                return userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM3.getId()), new Right(RightEnum.RIGHT_ENUM4.getId()));
            case ARTICLE_TYPE_ENUM4:
            case ARTICLE_TYPE_ENUM5:
            case ARTICLE_TYPE_ENUM6:
            case ARTICLE_TYPE_ENUM8:
                return userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM4.getId()));
            default:
                return false;
        }
    }

    @Override
    public ServerResponse listArticle(ListArticleForm listArticleForm) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        if (null != listArticleForm.getOrderBy()) {
            articleQueryWrapper.orderBy(true, listArticleForm.getOrder(), listArticleForm.getOrderBy());
        } else {
            articleQueryWrapper.orderByDesc("created_at");
        }

        Article article = new Article();
        BeanUtil.copyProperties(listArticleForm, article, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
        articleQueryWrapper.setEntity(article);

        IPage<Article> articleIPage = new Page<>(listArticleForm.getPageNum(), listArticleForm.getPageSize());
        IPage<Article> articlePage=  this.page(articleIPage, articleQueryWrapper);
        Page<ArticleVO> page = new Page<>();
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article articlePageRecord : articlePage.getRecords()) {
            ArticleVO articleVO = new ArticleVO();
            User user = userService.getById(articlePageRecord.getUserId());
            if(user!=null){
                user.setPassword(null);
                articleVO.setUser(user);

            }
            articleVO.setArticle(articlePageRecord);
            articleVOList.add(articleVO);
        }
        //如果有传文章id，则对应文章id的浏览量+1
        if(listArticleForm.getId() !=null){
            articleMapper.viewsAdd(listArticleForm.getId());
        }

        page.setRecords(articleVOList);
        page.setCurrent(articlePage.getCurrent());
        page.setSize(articlePage.getSize());
        page.setTotal(articlePage.getTotal());
        return ServerResponse.createBySuccess(page);
    }

    @Override
    public ServerResponse removeArticle(RemoveArticleForm removeArticleForm) {
        Article article = articleMapper.selectById(removeArticleForm.getArticleId());
        if (null == article) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.ARTICLE_UNEXIST);
        }
        //判断文章权限
        boolean auth= this.auth(removeArticleForm.getUserId(), article.getLabel());
        if(auth){
            //对应用户的文章-1
            //
            switch (article.getLabel()){
                case "1":
                case "2":
                case "3":
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.article_size.name(),article.getUserId(),"-");
                    break;
                case "7":
                    userExtendMapper.sizeUpdateByString(UserExtendEnums.tip_size.name(),article.getUserId(),"-");
                    break;
                default:
                    break;
            }
            //判断是否为管理员
            if(userService.authentication(removeArticleForm.getUserId(),new Role(RoleEnum.ROLE_ENUM6.getId()),new Right(RightEnum.RIGHT_ENUM4.getId()))){
                //在es内删除
                ElasticsearchUtil.deleteDataById(SysConstant.ES_INDEX, ESEnums.article.name(), article.getId());
                //删除所有喜欢这个文章的记录
                likesService.remove(new QueryWrapper<Likes>().eq("entity_id", article.getId()).eq("type", LikesTypeEnum.article.name()));
                articleMapper.deleteById(article.getId());
            }else {
                //在es内删除
                ElasticsearchUtil.deleteDataById(SysConstant.ES_INDEX, ESEnums.article.name(), article.getId());
                //删除所有喜欢这个文章的记录
                likesService.remove(new QueryWrapper<Likes>()
                        .eq("entity_id", article.getId())
                        .eq("type", LikesTypeEnum.article.name())
                );
                articleMapper.delete(new QueryWrapper<Article>()
                        .eq("id",article.getId())
                        .eq("userId",removeArticleForm.getUserId())
                );
            }
           return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        }
    }

    @Override
    public ServerResponse listArticleRandom(ListArticleRandomForm articleForm) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .must(QueryBuilders.matchQuery("status","FINISH"))
                .must(QueryBuilders.matchQuery("label",articleForm.getLabel()))
                .must(QueryBuilders.queryStringQuery(articleForm.getTitle()).analyzeWildcard(true).defaultField("*"))
        //.should(QueryBuilders.matchPhraseQuery("author",articleSearchForm.getSearchWord()).analyzer("ik_smart"))
        //.must(QueryBuilders.matchQuery("title",articleSearchForm.getSearchWord()).analyzer("ik_max_word"))

        ;
        EsPage esPage = ElasticsearchUtil.searchDataPage(SysConstant.ES_INDEX,ESEnums.article.name(),1,8,boolQuery,"id,title,author,views,introduce,cover,createdAt,label","_score",null);
        return ServerResponse.createBySuccess(esPage);
    }

    @Override
    public ServerResponse topArticle(ArticleForm articleForm) {
        Article article = new Article();
        article.setId(articleForm.getId());
        article.setTop(articleForm.getTop());
        this.articleMapper.updateById(article);
        return ServerResponse.createBySuccess(this.getById(article.getId()));
    }
}
