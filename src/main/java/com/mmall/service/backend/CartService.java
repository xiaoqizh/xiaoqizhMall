package com.mmall.service.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 9:49 2018/3/28
 * @Description:
 */

public interface CartService {
    PageInfo<Cart> selectAll(Integer pageNumber, Integer pageSize);

    List<Cart> selectAll();

    ServerResponse deleteProducts(List<String> ids, Integer userId);

    ServerResponse addOrUpdate(Integer userId, Integer productId, Integer count);

    ServerResponse checkOne(Integer userId, Integer productId, Integer selected);

    ServerResponse checkAll(Integer userId, Integer checked);

    ServerResponse productCountInCart(Integer userId);
}
