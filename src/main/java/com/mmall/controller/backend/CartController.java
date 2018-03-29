package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.User;
import com.mmall.service.backend.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:02 2018/3/28
 * @Description:
 */

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse cartList(HttpSession session, @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        PageInfo<Cart> cartPageInfo = cartService.selectAll(pageNumber, pageSize);
        List<Cart> list = cartPageInfo.getList();
        return ServerResponse.createBySuccess(list);
    }
    @RequestMapping("list2.do")
    @ResponseBody
    public ServerResponse cartList() {

        List<Cart> carts = cartService.selectAll();
        return ServerResponse.createBySuccess(carts);
    }

    @RequestMapping("addOrUpdate.do")
    @ResponseBody
    public ServerResponse saveOrUpdate(HttpSession session,Integer userId, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        return  cartService.addOrUpdate(userId, productId, count);
    }
    @RequestMapping("deleteProducts.do")
    @ResponseBody
    public ServerResponse saveOrUpdate(HttpSession session,List<String> ids, Integer userId) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        return cartService.deleteProducts(ids, userId);
    }

    @RequestMapping("checkOne.do")
    @ResponseBody
    public ServerResponse checkOne(HttpSession session,Integer userId, Integer productId, Integer checked) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        ServerResponse serverResponse = cartService.checkOne(userId, productId, checked);
        return serverResponse;
    }

    @RequestMapping("checkAll.do")
    @ResponseBody
    public ServerResponse checkAll(HttpSession session,Integer userId, Integer checked) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }

        return cartService.checkAll(userId, checked);
    }

    @RequestMapping("productCountInCart.do")
    @ResponseBody
    public ServerResponse productCountInCart(HttpSession session,Integer userId) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        return cartService.productCountInCart(userId);
    }

}
