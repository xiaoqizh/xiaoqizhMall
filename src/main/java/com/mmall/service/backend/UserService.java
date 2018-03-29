package com.mmall.service.backend;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 13:01 2018/3/10
 * @Description:
 */

public interface UserService {
     ServerResponse login(String username, String password);

     ServerResponse<String> register(User user);

     ServerResponse<String> checkValid(String beCheckedStr, String type);

     ServerResponse<String> getForgettedQuestion(String username);

     ServerResponse<String> checkQuestionAnswer(String username, String question, String answer);

     ServerResponse<String> resetPassword(String username, String passwordNew);

     ServerResponse<String> resetPasswordNoLogin(String username, String passwordNew, String question, String answer);

     ServerResponse<String> checkAdmin(HttpSession session);

}
