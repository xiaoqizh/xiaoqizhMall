package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.backend.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 12:40 2018/3/10
 * @Description:
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆的方法
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> userServerResponse = userService.login(username, password);
        if (userServerResponse.isSuccess()) {
            //就把用户放到session当中去
            User user = userServerResponse.getData();
            session.setAttribute(Const.CURRNET_USER, user);
        }
        return userServerResponse;
    }

    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "checkValid.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(@RequestParam("str") String str, @RequestParam("type") String type) {
        /**
         * str不能进行大写 要么就加上@RequestParam
         */
        //检查用户名或者邮箱是否可用
        System.out.println(str + "----" + type);
        return userService.checkValid(str, type);

    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        //取消登陆
        try {
            session.removeAttribute(Const.CURRNET_USER);
        } catch (Exception e) {
            return ServerResponse.createByError("系统发生错误");
        }
        return ServerResponse.createBySuccess("注销成功");
    }

    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        //通过session得到用户的信息
        if (user == null) {
            return ServerResponse.createByError("用户已注销或未登录");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "getForgettedQuestion.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getForgettedQuestion(String username) {
        //得到密码提示问题
        return userService.getForgettedQuestion(username);
    }

    @RequestMapping(value = "checkQuestionAnswer.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkQuestionAnswer(String username, String question, String answer) {
        //校验用户名的问题以及答案是否匹配
        return userService.checkQuestionAnswer(username, question, answer);
    }


    @RequestMapping(value = "resetPassword1.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPasswordLogIn( String newpassword, HttpSession session) {
        //直接在登陆状态下修改密码
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("请登陆后在进行修改");
        }
        return userService.resetPassword(user.getUsername(), newpassword);
    }
    @RequestMapping(value = "resetPassword2.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPasswordNoLogIn(String username,String passwordnew, String question,String answer) {
        //在未登录状态下修改密码
        ServerResponse<String> reset = userService.resetPasswordNoLogin(username, passwordnew, question, answer);
        if (!reset.isSuccess()) {
            return ServerResponse.createByError("修改错误");
        } else {
            return ServerResponse.createBySuccess("修改正确");
        }
    }

}




