package com.mmall.service.backend;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.vo.CartProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 9:49 2018/3/28
 * @Description:
 */

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse addOrUpdate(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //如果不存在则创建新纪录
            Cart cartNew = new Cart();
            cartNew.setUserId(userId);
            cartNew.setProductId(productId);
            cartNew.setQuantity(count);
            cartNew.setUpdateTime(new Date());
            cartNew.setChecked(1);
            cartMapper.insert(cartNew);
            return ServerResponse.createBySuccess(cartNew);
        } else {
            //更新购物车
            cart.setQuantity(cart.getQuantity() + count);
            cart.setUpdateTime(new Date());
            cartMapper.updateByPrimaryKeySelective(cart);
            return ServerResponse.createBySuccess(cart);
        }
    }


    @Override
    public PageInfo<Cart> selectAll(Integer pageNumber,Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Cart> carts = cartMapper.selectAll();
        System.out.println(carts);
        PageInfo pageInfo = new PageInfo(carts);
        return pageInfo;
    }
    @Override
    public List<Cart> selectAll() {
        List<Cart> carts = cartMapper.selectAll();
        return carts;
    }

    @Override
    public ServerResponse deleteProducts(List<String> ids, Integer userId) {
        //增加库存
        for (String id : ids) {
            Cart cart = cartMapper.selectCartByUserIdProductId(userId, Integer.parseInt(id));
            Product product = productMapper.selectByPrimaryKey(Integer.parseInt(id));
            Integer quantity = cart.getQuantity();
            product.setStock(quantity + product.getStock());
            productMapper.updateByPrimaryKeySelective(product);
        }
        int i = cartMapper.deleteByUserIdProductIds(userId, ids);
        if (i < 0) {
            return ServerResponse.createByError();
        } else {
            return ServerResponse.createBySuccess();
        }
    }
    @Override
    public ServerResponse checkOne(Integer userId, Integer productId, Integer checked) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        Product product = productMapper.selectByPrimaryKey(productId);
        if (cart == null) {
            return ServerResponse.createByError();
        }
        cart.setChecked(checked);
        CartProductVo cartProductVo = new CartProductVo();
        BeanUtils.copyProperties(cart, cartProductVo);
        BeanUtils.copyProperties(product, cartProductVo);
        return ServerResponse.createBySuccess(cartProductVo);
    }

    @Override
    public ServerResponse checkAll(Integer userId, Integer checked) {
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        for (Cart cart : carts) {
            cart.setChecked(checked);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse productCountInCart(Integer userId) {
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        BigDecimal count = new BigDecimal("0");
        for (Cart cart : carts) {
            count.add(new BigDecimal(cart.getQuantity()));
        }
        return ServerResponse.createBySuccess(count.intValue());
    }
}
