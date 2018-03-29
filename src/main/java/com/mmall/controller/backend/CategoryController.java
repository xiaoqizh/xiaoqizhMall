package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.backend.CategoryService;
import com.mmall.service.backend.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:44 2018/3/21
 * @Description:
 */

@Controller
@RequestMapping("/manage/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(String categorynew,
                                      @RequestParam(value = "parentid", defaultValue = "0") Integer parentid, HttpSession session) {
        ServerResponse<String> checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess()) {
            return ServerResponse.createByError("未登陆或不是管理员");
        }
        ServerResponse serverResponse = categoryService.insertCategory(categorynew, parentid);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess("成功加入条目");
        }
        return ServerResponse.createByError("条目添加失败");
    }

    @RequestMapping("update_category.do")
    @ResponseBody
    public ServerResponse updateCategory(String categorynew, Integer id, HttpSession session) {
        //检查是否登陆
       ServerResponse<String> checkAdmin = userService.checkAdmin(session);
       if (!checkAdmin.isSuccess()) {
           return ServerResponse.createByError("未登陆或不是管理员");
       }
       //选择性的进行更新
       ServerResponse serverResponse = categoryService.updateCategory(id, categorynew);
       if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess("成功加入条目");
        }
        return ServerResponse.createByError("条目添加失败");
    }

    @RequestMapping("parallelChildren.do")
    @ResponseBody
    public ServerResponse parallelChildren(@RequestParam(value = "parentid", defaultValue = "0") Integer parentid) {
        return categoryService.parallelChildren(parentid);
    }

    @RequestMapping("recursionChildren.do")
    @ResponseBody
    public ServerResponse recursionChildren(@RequestParam(value = "parentid", defaultValue = "0") Integer parentid) {
        List<Integer> integers = categoryService.recursionChildren(parentid);
        return ServerResponse.createBySuccess(integers);
    }



}
