package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 9:56 2018/3/28
 * @Description:
 */

public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalPrice;

    private String imageHost;

    private Boolean isChecked;


}
