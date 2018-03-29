package com.mmall.service.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 13:01 2018/3/10
 * @Description:
 */

@Service
public class UserServiceImpl implements UserService {
    /**
      Service层以来dao层
     */
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByError("用户名不存在");
        }
        /**
           为了方便 就不用 加密的密码
         */
      //  String md5Passwd = MD5Util.MD5EncodeUtf8(password);
        //如果用户名存在就判断密码是否一致
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByError("密码错误");
        }
        //设置查询出来的密码为空
        user.setPassword(StringUtils.EMPTY);
        //如果不为null 那么就是用户名密码都正确  把用户向上传递过去
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
         //用户进行注册的方法 首先判断用户名是否存在 其次判断email是否存在
        ServerResponse<String> usernamValid = checkValid(user.getUsername(), Const.TYPE.USERNAME);
        if (!usernamValid.isSuccess()) {
            return ServerResponse.createByError("用户名已被注册");
        }
        ServerResponse<String> emaliValid = checkValid(user.getEmail(), Const.TYPE.EMAIL);
        if (!emaliValid.isSuccess()) {
            return ServerResponse.createByError("邮箱已被注册");
        }
        //如果邮箱 用户名都未被注册时 直接插入到数据库
        user.setRole(Const.Role.USER_CUSTOMER);
        int insert = userMapper.insert(user);
        if (insert == 0) {
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String beCheckedStr, String type) {
        //分类进行检查
        if (StringUtils.isBlank(type)) {
            return ServerResponse.createByError("类型出错");
        }
        //检查邮箱是否有效
        if (Const.TYPE.EMAIL.equals(type)) {
            int emailCount = userMapper.checkEmail(beCheckedStr);
            if (emailCount > 0) {
                return ServerResponse.createByError("该邮箱已被注册");
            }
        }
        //检查用户名是否有效
        if (Const.TYPE.USERNAME.equals(type)) {
            int userCount = userMapper.checkUsername(beCheckedStr);
            if (userCount > 0) {
                return ServerResponse.createByError("该用户名已存在");
            }
        }
        return ServerResponse.createBySuccess("检测有效");
    }

    @Override
    public ServerResponse<String> getForgettedQuestion(String username) {
        //首先校验有用户名是否存在
        ServerResponse<String> valid = checkValid(username, Const.TYPE.USERNAME);
        if (valid.isSuccess()) {
            return ServerResponse.createByError("用户名不存在");
        }
        //下一步就是得到用户名对应的问题
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByError("该问题为空");
    }

    @Override
    public ServerResponse<String> checkQuestionAnswer(String username, String question, String answer) {
         //校验用户名的问题以及答案是否匹配
        int answerValid = userMapper.checkAnswer(username, question, answer);
        if (answerValid > 0) {
            return ServerResponse.createBySuccess("密保问题答案正确");
        }
        return ServerResponse.createByError("密保问题答案错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String passwordNew) {

        String md5passwd = MD5Util.MD5EncodeUtf8(passwordNew);
        int i = userMapper.updatePasswordByUsername(username, passwordNew);
        if (i != 1) {
            return ServerResponse.createByError("修改错误");
        }
        return ServerResponse.createBySuccess("成功修改密码");
    }

    @Override
    public ServerResponse<String> resetPasswordNoLogin(String username, String passwordNew,String question,String answer) {
        int i = userMapper.resetPasswordNoLogIn(username, passwordNew,question, answer);
        if (i == 1) {
            return ServerResponse.createBySuccess("成功修改密码");
        } else {
            return ServerResponse.createByError("用户信息错误");
        }

    }

    @Override
    public ServerResponse<String> checkAdmin(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null || user.getRole() == 0) {
            return ServerResponse.createByError("不是管理员权限");
        }
        return ServerResponse.createBySuccess("成功");
    }

}
