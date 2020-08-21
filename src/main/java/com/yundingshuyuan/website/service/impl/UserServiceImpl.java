package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.constants.SysConstant;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.dao.*;
import com.yundingshuyuan.website.enums.ESEnums;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.exception.SysException;
import com.yundingshuyuan.website.form.*;
import com.yundingshuyuan.website.pojo.*;
import com.yundingshuyuan.website.repository.redis.IRedisRepository;
import com.yundingshuyuan.website.repository.redis.IdentityRepository;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.util.*;
import com.yundingshuyuan.website.util.redisUtils.RedisUtils;
import com.yundingshuyuan.website.vo.UserCardVO;
import com.yundingshuyuan.website.vo.UserInfoVO;
import com.yundingshuyuan.website.vo.UserSearchVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    private final IRedisRepository redisRepository;
    private final UserRoleMapper userRoleMapper;
    private final UserRightMapper userRightMapper;
    private final IdentityRepository identityRepository;
    private final CoreMapper coreMapper;
    private final UserExtendMapper userExtendMapper;
    private final ArticleMapper articleMapper;

    public UserServiceImpl(UserMapper userMapper,
                           StringRedisTemplate redisTemplate,
                           IRedisRepository redisRepository,
                           UserRoleMapper userRoleMapper,
                           UserRightMapper userRightMapper,
                           IdentityRepository identityRepository,
                           CoreMapper coreMapper,
                           UserExtendMapper userExtendMapper, ArticleMapper articleMapper) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.redisRepository = redisRepository;
        this.userRoleMapper = userRoleMapper;
        this.userRightMapper = userRightMapper;
        this.identityRepository = identityRepository;
        this.coreMapper = coreMapper;
        this.userExtendMapper = userExtendMapper;
        this.articleMapper = articleMapper;
    }

    @Override
    public ServerResponse register(RegisterForm registerForm) {
        /*
         * 验证账号是否可用
         */
        if (checkPhone(registerForm.getPhone())) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USERNAME_ERROR);
        }
        /*
         * 手机验证码校验
         */
        /*从缓存获取验证码*/
        String phone_code = RedisUtils.get(redisTemplate, "r:%s" + registerForm.getPhone());
        if (!registerForm.getPhoneCode().equals(phone_code)) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PHONE_CODE_ERROR);
        }

        /*
         * 用户信息编辑
         */
        User user = new User("0");
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        String userId = String.valueOf(idWorker.nextId());
        /*雪花算法生成用户id*/
        user.setId(userId);
        BeanUtil.copyProperties(registerForm, user);
        /*md5加密*/
        user.setPassword(Md5PswdUtil.md5Pswd(registerForm.getPassword()));
        /*生成用户名*/
        user.setNickname(generateName());
        user.setCreatedAt(LocalDateTime.now());
        user.setImage(UserConstant.USER_IMAGE);
        user.setCover(UserConstant.USER_COVER);
        user.setSignature(UserConstant.SIGNATURE);
        /*
         *用户信息保存
         */
        userMapper.insert(user);
        //用户数量+1
        userMapper.userCountPlusOne();
        //初始化user_core
        LocalDateTime dateTime = LocalDateTime.now();
        int time = dateTime.getYear() * 100 + dateTime.getMonthValue();
        Core core = new Core(userId, 0, 0, 0, 0, 0, 0, 0, time);
        coreMapper.insert(core);
        //初始化user_extend
        UserExtend userExtend = new UserExtend(userId);
        userExtendMapper.insert(userExtend);
        //注册成功授予点赞权限
        UserRight right1 = new UserRight(userId, RightEnum.RIGHT_ENUM1.getId());
        UserRight right3 = new UserRight(userId, RightEnum.RIGHT_ENUM3.getId());
        userRightMapper.insert(right1);
        userRightMapper.insert(right3);
        RedisUtils.del(redisTemplate, "r:%s" + registerForm.getPhone());

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse auth(LoginForm loginForm) {
        /*
         *验证账号密码
         */
        User user = new User();
        user.setPhone(loginForm.getUsername());
        user.setPassword(Md5PswdUtil.md5Pswd(loginForm.getPassword()));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
        User user1 = userMapper.selectOne(userQueryWrapper);
        if (user1 == null) {
            /*连续登录三次输入密码错误*/
            String a = RedisUtils.get(redisTemplate, UserConstant.USER_LONGIN_TIME + loginForm.getUsername());
            if (a == null) {
                a = "1";
            }
            System.out.println("连续登录错误次数:" + a);
            if (Integer.parseInt(a) < 3) {
                Integer b = Integer.parseInt(a) + 1;
                RedisUtils.setByMinutes(redisTemplate, UserConstant.USER_LONGIN_TIME + loginForm.getUsername(), String.valueOf(b), 10);
                return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PASSWORD_ERROR);
            } else {
                return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PASSWORD_MORE_ERROR);
            }
        }
        //获取userId
        String userId = user1.getId();
        /*
         * 获取用户角色并存redis
         */
        Role[] roles = userRoleMapper.listRoleById(userId);
        for (Role value : roles) {
            System.out.println(roles.length);
            System.out.println(value.toString());
        }
        identityRepository.saveIdentity(userId, roles);
        Right right = new Right();
        right.setId(RightEnum.RIGHT_ENUM1.getId());
        //登录权限验证
        if (!authentication(userId, right)) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_ERROR);

        }

//        if (Integer.valueOf(user1.getDepartment()) < 1) {
//            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USER_IDENTITY_ERROR);
//        }
        /*
         * 生成token
         */
        String accessToken = TokenUtils.genToken();
        System.out.println("accessToken:" + accessToken);
        /*
         *获取旧token
         */


        String oldToken = redisRepository.findAccessTokenByUserId(userId);

        if (oldToken != null) {
            //TODO 消息提醒当前账号异地登录
            //删除旧token
            redisRepository.deleteAccessToken(oldToken);
        }
        //保存id与token的关系
        redisRepository.saveUserAccessToken(userId, accessToken);
        //保存token
        redisRepository.saveAccessToken(userId, accessToken);
        Map<String, Object> map = new HashMap<>();
        map.put("token", accessToken);
        map.put("userId", userId);
        return ServerResponse.createBySuccess("用户认证成功", map);

    }



    @Override
    public ServerResponse updatePassword(PasswordForm passwordForm) {
        /*
         * 验证账号是否存在
         */
        if (!checkPhone(passwordForm.getPhone())) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USERNAME_ERROR);
        }
        /*
         * 手机验证码校验
         */
        /*从缓存获取验证码*/
        String phone_code = RedisUtils.get(redisTemplate, "p:%s" + passwordForm.getPhone());
        if (!passwordForm.getPhoneCode().equals(phone_code)) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PHONE_CODE_ERROR);
        }
        User user = new User();
        user.setPassword(Md5PswdUtil.md5Pswd(passwordForm.getPassword()));
        User user1 = new User();
        user1.setPhone(passwordForm.getPhone());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user1);
        update(user, queryWrapper);
        RedisUtils.del(redisTemplate, "p:%s" + passwordForm.getPhone());
        return ServerResponse.createBySuccessMessage("密码修改成功");
    }


    /**
     * 生成默认唯一用户name
     */
    private String generateName() {
        while (true) {
            String num = userMapper.selectSequence();
            String name = "user_";
            name = name + num;
            userMapper.updateSequence();
            System.out.println(name);
            String userByName = userMapper.getUserByName(name);
            if (userByName == null) {
                return name;
            }
        }
    }

    /**
     * 查询账号是否可用
     *
     * @param phone 帐号
     * @return boolen
     */
    private Boolean checkPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
        User user1 = userMapper.selectOne(userQueryWrapper);
        return user1 != null;
    }

    @Override
    public ServerResponse authByPhone(LoginForm loginForm) {
        User user = new User();
        user.setPhone(loginForm.getUsername());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User user1 = userMapper.selectOne(queryWrapper);
        if (user1 == null) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USERNAME_ERROR);
        }
        /*手机验证*/
        String phone_code = RedisUtils.get(redisTemplate, "p:%s" + loginForm.getUsername());
        if (!phone_code.equals(loginForm.getPassword())) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.PHONE_CODE_ERROR);
        }
        String userId = user1.getId();
        /*
         * 获取用户角色并存redis
         */
        Role[] roles = userRoleMapper.listRoleById(userId);
        for (Role role : roles) {
            System.out.println(roles.length);
            System.out.println(role.toString());
        }
        identityRepository.saveIdentity(userId, roles);
        Right right = new Right();
        right.setId(RightEnum.RIGHT_ENUM1.getId());
        //登录权限验证
        if (!authentication(userId, right)) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_ERROR);

        }
        /*
         * 生成token
         */
        String accessToken = TokenUtils.genToken();
        System.out.println("accessToken:" + accessToken);
        /*
         *获取旧token
         */

        String oldToken = redisRepository.findAccessTokenByUserId(userId);

        if (oldToken != null) {
            //TODO 消息提醒当前账号异地登录
            //删除旧token
            redisRepository.deleteAccessToken(oldToken);
        }
        //保存id与token的关系
        redisRepository.saveUserAccessToken(userId, accessToken);
        //保存token
        redisRepository.saveAccessToken(userId, accessToken);
        Map<String, Object> map = new HashMap<>();
        map.put("token", accessToken);
        map.put("userId", userId);
        return ServerResponse.createBySuccess("用户认证成功", map);

    }

    @Override
    public Boolean authentication(String userId, Role role, Right right) {

        /*角色判定*/
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(userId);
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.setEntity(userRole);
        UserRole userRole1 = userRoleMapper.selectOne(userRoleQueryWrapper);
        if (null == userRole1) {
            return false;
        }

        /*权限判定*/
        UserRight userRight = new UserRight();
        userRight.setUserId(userId);
        userRight.setRightId(right.getId());
        QueryWrapper<UserRight> userRightQueryWrapper = new QueryWrapper<>();
        userRightQueryWrapper.setEntity(userRight);
        UserRight userRight1 = userRightMapper.selectOne(userRightQueryWrapper);
        return null != userRight1;
    }

    @Override
    public Boolean authentication(Role[] roles, Role[] roles1) {
        int[] a = new int[roles.length];
        for (int i = 0; i < roles.length; i++) {
            a[i] = roles[i].getId();
        }
        int c = bubbleSort1(a, roles.length);
        System.out.println("当前用户角色最高为" + c);
        for (Role role : roles1) {
            if (c > role.getId()) {
                return true;
            }
        }

        return false;
    }

    private static int bubbleSort1(int[] a, int n) {
        int i, j;

        for (i = 0; i < n; i++) {//表示n次排序过程。
            for (j = 1; j < n - i; j++) {
                if (a[j - 1] < a[j]) {//前面的数字大于后面的数字就交换
                    //交换a[j-1]和a[j]
                    int temp;
                    temp = a[j - 1];
                    a[j - 1] = a[j];
                    a[j] = temp;
                }
            }
        }
        return a[0];
    }

    @Override
    public ServerResponse getStudentList(UserForm userForm) {
        User user = new User();
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
            if(null!=userForm.getDepartment()){
                queryWrapper.like("department",userForm.getDepartment());
                userForm.setDepartment(null);
            }
            BeanUtil.copyProperties(userForm, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));

            queryWrapper.isNotNull("name");


            if (userForm.getOrderBy() == null) {
                userForm.setOrder(false);
                userForm.setOrderBy("created_at");
                queryWrapper.orderBy(true, userForm.getOrder(), userForm.getOrderBy());
            } else {
                queryWrapper.orderBy(true, userForm.getOrder(), userForm.getOrderBy());
            }
//            queryWrapper.orderByDesc("score");
            IPage<User> iPage = new Page<>(userForm.getPageNum(), userForm.getPageSize());
            IPage<User> userIPage = userMapper.selectPage(iPage, queryWrapper);
            List<UserInfoVO> userInfoVOS = new ArrayList<>();
            for (User u : userIPage.getRecords()
            ) {
                //if (u.getName() == null || u.getName().equals("")) {
                //    //如果name 为空则不展示
                //} else {
                    UserInfoVO userInfoVO = new UserInfoVO();
                    BeanUtil.copyProperties(u, userInfoVO, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));

                    QueryWrapper<Core> coreQueryWrapper = new QueryWrapper<>();
                    if (userForm.getCoreTime() == null) {
                        userForm.setCoreTime(LocalDateTime.now().getYear() * 100 + LocalDateTime.now().getMonthValue());
                    }
                    coreQueryWrapper.eq("user_id", u.getId()).eq("time", userForm.getCoreTime());
                    Core coreServiceOne = coreMapper.selectOne(coreQueryWrapper);
                    if (coreServiceOne != null) {
                        userInfoVO.setCore(coreServiceOne);
                    }else {
                        userInfoVO.setCore(null);
                    }
                    userInfoVOS.add(userInfoVO);
                //}
            }
            Page<UserInfoVO> page = new Page<>(userForm.getPageNum(), userForm.getPageSize());
            page.setRecords(userInfoVOS);
            Map<String, Object> map = new HashMap<>();
            map.put("studentList", userInfoVOS);
            map.put("pages", userIPage.getPages());
            return ServerResponse.createBySuccess(map);
        } catch (Exception e) {
            return ServerResponse.createByError();
        }
    }

    @Override
    public List<Core> getUserScore(ScoreForm scoreForm) {
        QueryWrapper<Core> coreQueryWrapper = new QueryWrapper<>();
        coreQueryWrapper
                .eq("user_id", scoreForm.getUserId())
                .orderByDesc("time");
        if (null != scoreForm.getTime()) {
            coreQueryWrapper.eq("time", scoreForm.getTime());
        }
        return coreMapper.selectList(coreQueryWrapper);
    }

    @Override
    public ServerResponse getUserCard(UserCardForm userCardForm) {
        String targetUserId = userCardForm.getUserId();
        UserCardVO userCardVO = new UserCardVO();
        /*
         * 用户基础信息
         */
        User user = userMapper.selectById(targetUserId);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USER_ERROR);
        }
        String con = user.getContribution();
        String[] cons= con.split(",");
        userCardVO.setContributions(cons);

        if (null != user.getAdmissionTime()) {
            userCardVO.setYunOld(LocalDateTime.now().getYear() - user.getAdmissionTime() + 1);
        }
        BeanUtil.copyProperties(user, userCardVO, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        /*
         * 用户扩展信息获取
         */
        UserExtend userExtend = userExtendMapper.selectOne(new QueryWrapper<UserExtend>().eq("user_id",targetUserId));
        System.out.println(userExtend);
        BeanUtil.copyProperties(userExtend,userCardVO, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));
        return ServerResponse.createBySuccess(userCardVO);
    }

    @Override
    public ServerResponse authorizationArticle(AuthorizationArticleForm authorizationArticleForm) {
        Article article = new Article();
        article.setId(authorizationArticleForm.getArticleId());
        article.setStatus(authorizationArticleForm.getStatus().name());
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.setEntity(article);
        articleMapper.updateById(article);
        Article article1= articleMapper.selectById(article.getId());
        if(article1!=null){
            //存在则存对象,添加文章到es
            String jsonObject = JsonUtils.toJson(article1);
            ElasticsearchUtil.addData(JSONObject.parseObject(jsonObject), SysConstant.ES_INDEX, ESEnums.article.name(),article.getId());
            return ServerResponse.createBySuccess(article1);
        }else {
            return ServerResponse.createByErrorMessage("文章授权失败");
        }

    }

    @Override
    public boolean authentication(String userId, Right right) {
        /*权限判定*/
        UserRight userRight = new UserRight();
        userRight.setUserId(userId);
        userRight.setRightId(right.getId());
        QueryWrapper<UserRight> userRightQueryWrapper = new QueryWrapper<>();
        userRightQueryWrapper.setEntity(userRight);
        UserRight userRight1 = userRightMapper.selectOne(userRightQueryWrapper);
        return null != userRight1;
    }

    @Override
    public void addSiteInfo() {
        LocalDateTime localDateTime = LocalDateTime.now();

        Integer time = localDateTime.getYear() * 10000
                + localDateTime.getMonthValue() * 100
                + localDateTime.getDayOfMonth();
        Integer userCount = this.count();
        LocalDateTime yesterday= localDateTime.plusDays(-1);
        Integer yesterdayTime = yesterday.getYear() * 10000
                + yesterday.getMonthValue() * 100
                + yesterday.getDayOfMonth();
        JSONObject jsonObject = userMapper.selectSiteAdmin(yesterdayTime);
        Long siteView = jsonObject.getLong("site_view");
        Long sumSiteView = jsonObject.getLong("sum_site_view") +siteView;
        if(userMapper.selectSiteAdmin(time)==null) {
            userMapper.addSiteInfo(userCount, time,sumSiteView);

        }
    }

    @Override
    public String getUserNameById(String userId) {
        return userMapper.selectNameById(userId);
    }

    /**
     * 获取用户全部信息
     *
     * @param userId
     */
    @Override
    public List getUserInfo(String userId) {

        if (userId.isEmpty()) {
            throw new SysException(ErrorCodeEnum.PARAM_ERROR);
        } else {
            List<User> user = userMapper.selectList(new QueryWrapper<User>().eq("id", userId));

            return user;
        }
    }

    @Override
    public void viewsAddOne() {
        LocalDateTime dateTime = LocalDateTime.now();
        Integer time = dateTime.getYear() * 10000 + dateTime.getMonthValue() * 100 + dateTime.getDayOfMonth();
        this.userMapper.viewsAddOne(time);
    }

    @Override
    public ServerResponse getSiteAdmin(SiteAdminForm siteAdminForm) {
        Integer getTime = siteAdminForm.getTime();
        getTime = getTime * 100 + 1;
        String time = String.valueOf(getTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        List<JSONObject> list = new ArrayList();
        Long site_view_sum = 0L;
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("格式错误");
        }
        Integer size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < size; i++) {
            JSONObject jsonObject = this.userMapper.selectSiteAdmin(getTime);
            getTime++;
            if (jsonObject == null) {
                continue;
            } else {
                Long a = jsonObject.getLongValue("site_view");
                site_view_sum = site_view_sum + a;
                list.add(jsonObject);

            }
        }

        //while (flag) {
        //    JSONObject jsonObject = this.userMapper.selectSiteAdmin(time);
        //
        //    if (jsonObject == null) {
        //        flag = false;
        //    } else {
        //        Long a = jsonObject.getLongValue("site_view");
        //        site_view_sum = site_view_sum + a;
        //        list.add(jsonObject);
        //    }
        //}
        if (list.isEmpty()) {
            return ServerResponse.createByErrorMessage("未查到当前时间的数据");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("site_admin_month", site_view_sum);
            LocalDateTime now = LocalDateTime.now();
            Integer time1 = now.getYear() * 10000 + now.getMonthValue() * 100 + now.getDayOfMonth();
            JSONObject siteAdmin = this.userMapper.selectSiteAdmin(time1);
            Long sum = (long) siteAdmin.get("sum_site_view")+(long) siteAdmin.get("site_view");
            map.put("site_admin_sum",sum);
            return ServerResponse.createBySuccess(map);
        }
    }

    @Override
    public ServerResponse getUserList(UserForm userForm) {
        User user = new User();
        try {
            BeanUtil.copyProperties(userForm, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
            if (userForm.getOrderBy() == null) {
                userForm.setOrder(false);
                userForm.setOrderBy("created_at");
                queryWrapper.orderBy(true, userForm.getOrder(), userForm.getOrderBy());
            } else {
                queryWrapper.orderBy(true, userForm.getOrder(), userForm.getOrderBy());
            }
            IPage<User> iPage = new Page<>(userForm.getPageNum(), userForm.getPageSize());
            IPage<User> userIPage = userMapper.selectPage(iPage, queryWrapper);
            List<UserInfoVO> userInfoVOS = new ArrayList<>();
            for (User u : userIPage.getRecords()) {
            UserInfoVO userInfoVO = new UserInfoVO();
            //获取用户角色
            /*
             * 获取用户角色并存redis
             */
            Role[] roles = userRoleMapper.listRoleById(u.getId());
            for (Role role : roles) {
                System.out.println(roles.length);
                System.out.println(role.toString());
            }
            identityRepository.saveIdentity(u.getId(), roles);
            u.setPassword(null);
            userInfoVO.setRoles(roles);
            BeanUtil.copyProperties(u, userInfoVO, CopyOptions.create().setIgnoreCase(true).setIgnoreNullValue(true));

            QueryWrapper<Core> coreQueryWrapper = new QueryWrapper<>();
            if (userForm.getCoreTime() == null) {
                userForm.setCoreTime(LocalDateTime.now().getYear() * 100 + LocalDateTime.now().getMonthValue());
            }
            coreQueryWrapper.eq("user_id", u.getId()).eq("time", userForm.getCoreTime());
            Core coreServiceOne = coreMapper.selectOne(coreQueryWrapper);
            if (coreServiceOne != null) {
                userInfoVO.setCore(coreServiceOne);
            }
            userInfoVOS.add(userInfoVO);
            }
            Page<UserInfoVO> page = new Page<>(userForm.getPageNum(), userForm.getPageSize());
            page.setRecords(userInfoVOS);
            Map<String, Object> map = new HashMap<>();
            map.put("studentList", userInfoVOS);
            map.put("pages", userIPage.getPages());
            return ServerResponse.createBySuccess(map);
        } catch (Exception e) {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse getUserByName(UserSearchForm userSearchForm) {
        String name = userSearchForm.getUserName();
        String username = "%";
        username = username+name+username;
        List<User> users = userMapper.selectList(new QueryWrapper<User>().like("name",userSearchForm.getUserName()));
        List<UserSearchVO> userSearchVOList = new ArrayList<>();
        for (User user :users) {
            List a= userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id",user.getId()));
            if(a.size()>0) {
                UserSearchVO userSearchVO = new UserSearchVO();
                BeanUtil.copyProperties(user, userSearchVO);
                userSearchVOList.add(userSearchVO);
            }

        }
        return ServerResponse.createBySuccess(userSearchVOList);
    }

    @Override
    public void updateScore(String userId, Integer score) {
        this.userMapper.updateScore(userId,score);
    }

    @Override
    public void addContribution(int time, String userId) {
       String oldContribution  = this.getById(userId).getContribution();
       StringBuilder con = new StringBuilder();
       if(oldContribution!=null) {
           con.append(oldContribution);
       }
       con.append(time);
       con.append(",");
       String newContribution = con.toString();
       this.baseMapper.addContribution(newContribution,userId);
    }

    @Override
    public Integer getUserPopularity(String id) {
        return this.baseMapper.getUserPopularity(id);
    }
}
