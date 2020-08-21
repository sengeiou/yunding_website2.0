/*
 * FileName:CodeController
 * Author:leeyf
 * Date:19-1-14 下午4:00
 * Description:
 */
package com.yundingshuyuan.website.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.util.VerificationCode.Captcha;
import com.yundingshuyuan.website.util.VerificationCode.GifCaptcha;
import com.yundingshuyuan.website.util.aliyunCode.CodeUtil;
import com.yundingshuyuan.website.util.redisUtils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static com.yundingshuyuan.website.util.aliyunCode.Code.sendSms;
import static com.yundingshuyuan.website.util.aliyunCode.Code.sendSmsEmail;

@Api(tags = "验证码API")
@RestController
@RequestMapping(value = "/code")
@Slf4j
public class CodeController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    IUserService userService;

    /**
     * 获取图片验证码（Gif版本）
     *
     * @param response
     */
    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/getGifCode/{random}", method = RequestMethod.GET)
    public void getGifCode(HttpServletResponse response, HttpServletRequest request, @PathVariable String random) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146, 33, 4);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            HttpSession session = request.getSession(true);
            //存入Session
            session.setAttribute("captchaCode", uuid);
            //response.
            //输出
            captcha.out(response.getOutputStream());

            //将验证码以<key,value>形式缓存到redis

            RedisUtils.setByMinutes(redisTemplate, uuid, captcha.text().toLowerCase(), 5);

            System.out.println("验证码为:" + RedisUtils.get(redisTemplate, uuid));
            //将验证码key，返回

        } catch (Exception e) {
            e.printStackTrace();
            //throw new SysException(ErrorCodeEnum.CODE_ERROR);
        }

    }

    @ApiOperation("图片验证码校验")
    @GetMapping("/isCode/{code}")
    public ServerResponse isCaptchaCode(@PathVariable String code,
                                        HttpServletRequest request) {
        /*将验证码转小写*/
        code = code.toLowerCase();
        String uuid = (String) request.getSession().getAttribute("captchaCode");
        System.out.println(uuid);
        //redis连接
        String codeReal = RedisUtils.get(redisTemplate, uuid);

        if (code != null) {
            if (codeReal.equals(code)) {
                return ServerResponse.createBySuccessMessage("验证成功");
            }
        }
        return ServerResponse.createByErrorMessage("验证码错误");
    }

    /**
     * yundingshuyuan
     * 手机验证提交
     */
    @ApiOperation("获取手机验证码")
    @GetMapping("/getPhoneCode/{word}/{phone}")
    @ResponseBody
    public ServerResponse code(@PathVariable String phone, @PathVariable String word) {
        /*10分钟内允许发送3条短信*/
        String a = RedisUtils.get(redisTemplate, UserConstant.SEND_PHONE_TIME + phone);
        if (a == null) {
            a = "1";
        }
        if (Integer.valueOf(a) <= 3) {
            Integer b = Integer.valueOf(a) + 1;
            RedisUtils.setByMinutes(redisTemplate, UserConstant.SEND_PHONE_TIME + phone, String.valueOf(b), 30);
        } else {
            return ServerResponse.createByErrorMessage("30分钟内发送短信次数3次");
        }
        //先判断手机号是否正确
        boolean isphone = CodeUtil.isPhone(phone);
        if (!isphone) {
            return ServerResponse.createByErrorMessage("手机号格式错误");
        } else {
            //生成验证码
            String smsCode = CodeUtil.smsCode();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            if ("r".equals(word)) {
                try {
                    /*发送验证码到手机*/
                    sendSms(phone, smsCode);
                    System.out.println("验证码为:" + smsCode);
                } catch (Exception e) {
                    throw new RuntimeException("发送短信失败");
                }
                //将手机验证码存入缓存
                RedisUtils.set(redisTemplate,"r:%s"+phone, smsCode);
                ////将验证码key，返回
                return ServerResponse.createBySuccess("短信验证发送成功");
            } else if ("p".equals(word)) {
                try {
                    /*发送验证码到手机*/
                    sendSmsEmail(phone, smsCode);
                    System.out.println("忘记密码的验证码为:" + smsCode);
                } catch (Exception e) {
                    throw new RuntimeException("发送短信失败");
                }
                //将手机验证码存入缓存
                //RedisUtils.set(redisTemplate, uuid, smsCode);
                RedisUtils.set(redisTemplate, "p:%s"+phone, smsCode);
                ////将验证码key，返回
                return ServerResponse.createBySuccess("短信验证发送成功");
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    @ApiOperation("用户手机号验证唯一性")
    @GetMapping("/phone/{phone}")
    public ServerResponse checkPhone(@PathVariable String phone) {
        if (!CodeUtil.isPhone(phone)) {
            return ServerResponse.createByErrorMessage("手机号格式错误");
        } else {
            User user = new User();
            user.setPhone(phone);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
            User user1 = userService.getOne(queryWrapper);
            if (user1 == null) {
                return ServerResponse.createBySuccess("账号可用");
            } else {
                return ServerResponse.createByErrorMessage("账号不可用");
            }
        }
    }
}
