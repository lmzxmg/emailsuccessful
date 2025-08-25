package com.shanzhu.st.controller;

import com.shanzhu.st.entity.User;
import com.shanzhu.st.enums.ErrorMsg;
import com.shanzhu.st.service.UserService;
import com.shanzhu.st.service.EmailService;
import com.shanzhu.st.vo.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 用户相关 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;


    /**
     * 注册账号
     *
     * @param map 用户信息
     * @return 结果
     *
     */
    @PostMapping("sign-in")
    //public R signIn(@RequestBody User user) {
    public R signIn(@RequestBody Map<String, String> map) {
        User user = new User();
        user.setAccountNumber(map.get("accountNumber"));
        user.setUserPassword(map.get("userPassword"));
        user.setNickname(map.get("nickname"));
        String code = map.get("code");
        if (!emailService.verifyCode(user.getAccountNumber(), code)) {
            return R.fail(ErrorMsg.CODE_ERROR);
        }
        user.setSignInTime(new Timestamp(System.currentTimeMillis()));
        if (user.getAvatar() == null || "".equals(user.getAvatar())) {
            user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        }
        if (userService.userSignIn(user)) {
            return R.success(user);
        }
        return R.fail(ErrorMsg.REGISTER_ERROR);
    }

    /**
     * 发送验证码
     *
     * @param upi 学生UPI
     * @return 结果
     */
    @GetMapping("code")
    public R sendCode(@RequestParam("upi") String upi) {
        try {
            emailService.sendCode(upi);
            return R.success();
        } catch (Exception e) {
            return R.fail(ErrorMsg.EMAIL_SEND_ERROR);
        }
    }



    /**
     * 登录
     *
     * @param accountNumber 账号
     * @param userPassword  密码
     * @return 登录结果
     */
    @RequestMapping("login")
    public R login(
            @RequestParam("accountNumber") @NotEmpty @NotNull String accountNumber,
            @RequestParam("userPassword") @NotEmpty @NotNull String userPassword,
            HttpServletResponse response
    ) {
        User user = userService.userLogin(accountNumber, userPassword);
        if (null == user) {
            return R.fail(ErrorMsg.EMAIL_LOGIN_ERROR);
        }
        if (user.getUserStatus() != null && user.getUserStatus().equals((byte) 1)) {
            return R.fail(ErrorMsg.ACCOUNT_Ban);
        }
        Cookie cookie = new Cookie("shUserId", String.valueOf(user.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        return R.success(user);
    }

    /**
     * 退出登录
     *
     * @param shUserId 用户id
     * @return 结果
     */
    @RequestMapping("logout")
    public R logout(
            @CookieValue(value = "shUserId", defaultValue = "") String shUserId, HttpServletResponse response
    ) {
        if (shUserId.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        Cookie cookie = new Cookie("shUserId", shUserId);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return R.success();
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("info")
    public R getOneUser(
            @CookieValue(value = "shUserId", defaultValue = "") String id
    ) {
        if (id.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        return R.success(userService.getUser(Long.valueOf(id)));
    }

    /**
     * 修改用户公开信息
     *
     * @param id   用户id
     * @param user 用户信息
     * @return 修改结果
     */
    @PostMapping("/info")
    public R updateUserPublicInfo(@CookieValue(value = "shUserId", defaultValue = "") String id, @RequestBody User user) {
        if (id.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        user.setId(Long.valueOf(id));
        if (userService.updateUserInfo(user)) {
            return R.success();
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }


    /**
     * 修改密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @GetMapping("/password")
    public R updateUserPassword(
            @CookieValue(value = "shUserId", defaultValue = "") String id,
            @RequestParam("oldPassword") @NotEmpty @NotNull String oldPassword,
            @RequestParam("newPassword") @NotEmpty @NotNull String newPassword) {
        if (id.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        if (userService.updatePassword(newPassword, oldPassword, Long.valueOf(id))
        ) {
            return R.success();
        }
        return R.fail(ErrorMsg.PASSWORD_RESET_ERROR);
    }
}
