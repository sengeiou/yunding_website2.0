package com.yundingshuyuan.website.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSONObject;
import com.yundingshuyuan.website.constants.SysConstant;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ESEnums;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.form.*;
import com.yundingshuyuan.website.pojo.Article;
import com.yundingshuyuan.website.pojo.EsPage;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IArticleService;
import com.yundingshuyuan.website.util.ElasticsearchUtil;
import com.yundingshuyuan.website.util.JsonUtils;
import com.yundingshuyuan.website.util.RedisManager;
import com.yundingshuyuan.website.util.SnowFlake;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private final
    IArticleService articleService;
    private final
    RedisManager redisManager;

    public ArticleController(IArticleService articleService, RedisManager redisManager) {
        this.articleService = articleService;
        this.redisManager = redisManager;
    }

    @ApiOperation("上传/更新文章")
    @PostMapping("/upload")
    @Transactional
    public ServerResponse addArticle(@RequestBody ArticleForm articleForm, HttpServletRequest request) {
        //文章鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        Boolean aBoolean = articleService.auth(userId, articleForm.getLabel());
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            articleForm.setUserId(userId);
            return articleService.uploadArticle(articleForm);
        }
    }

    @ApiOperation("上传/更新文章到缓存")
    @PostMapping("/addArticleRedis")
    public ServerResponse addArticleRedis(@RequestBody ArticleForm articleForm) {
        Article article = new Article();
        BeanUtil.copyProperties(articleForm, article, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        if (article.getId().equals("") || article.getId() == null) {
            article.setId(String.valueOf(SnowFlake.nextId()));
        }
        redisManager.setValue(RedisPrefix.ATRICLE, article.getId(), article);
        return ServerResponse.createBySuccess(article.getId());
    }
    @ApiOperation("置顶文章")
    @PostMapping("top")
    public ServerResponse top(@RequestBody ArticleForm articleForm,HttpServletRequest request){
        //文章鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        Boolean aBoolean = articleService.auth(userId, articleForm.getLabel());
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            return articleService.topArticle(articleForm);
        }
    }

    @ApiOperation("分页条件获取文章")
    @PostMapping("/list")
    @Transactional
    public ServerResponse listArticle(@RequestBody ListArticleForm listArticleForm) {
        return articleService.listArticle(listArticleForm);
    }

    @ApiOperation("删除文章")
    @PostMapping("/remove")
    @Transactional
    public ServerResponse removeArticle(@RequestBody RemoveArticleForm removeArticleForm, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        removeArticleForm.setUserId(userId);
        return articleService.removeArticle(removeArticleForm);
    }

    @ApiOperation("随机获取文章")
    //@PostMapping("/random")
    @Transactional
    public ServerResponse listArticleRandom(ListArticleRandomForm articleForm){

        return articleService.listArticleRandom(articleForm);
    }

    @PostMapping("/databaseToES")
    public void databaseToES(){
        List<Article> articles=articleService.list();
        for (Article article:articles
        ) {
            //第一步，判断是否存在索引 os_user
            if(ElasticsearchUtil.isIndexExist(SysConstant.ES_INDEX)){
                //存在则存对象
                String jsonObject = JsonUtils.toJson(article);
                ElasticsearchUtil.addData(JSONObject.parseObject(jsonObject),SysConstant.ES_INDEX, ESEnums.article.name(),article.getId());
            }else {
                ElasticsearchUtil.createIndex(SysConstant.ES_INDEX);
            }
        }
    }


    @PostMapping("search")
    public ServerResponse search(@RequestBody ArticleSearchForm articleSearchForm){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .must(QueryBuilders.matchQuery("status",articleSearchForm.getStatus()))
                .must(QueryBuilders.matchQuery("label",articleSearchForm.getType()))
                .must(QueryBuilders.queryStringQuery(articleSearchForm.getSearchWord()).analyzeWildcard(true).defaultField("*"))
                //.should(QueryBuilders.matchPhraseQuery("author",articleSearchForm.getSearchWord()).analyzer("ik_smart"))
                //.must(QueryBuilders.matchQuery("title",articleSearchForm.getSearchWord()).analyzer("ik_max_word"))

                ;
        EsPage esPage = ElasticsearchUtil.searchDataPage(SysConstant.ES_INDEX,ESEnums.article.name(),articleSearchForm.getPageNum(),articleSearchForm.getPageSize(),boolQuery,"id,title,author,views,introduce,cover,createdAt,label","_score",null);
        return ServerResponse.createBySuccess(esPage);
    }
}
