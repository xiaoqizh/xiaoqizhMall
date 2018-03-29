package com.mmall.service.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 16:03 2018/3/22
 * @Description:
 */


public interface ProductService {

    ServerResponse savaUpdate(Product product);

    ServerResponse setStatus(Integer productid, Integer status);

    ServerResponse productDetail(Integer id);

    ServerResponse productList(Integer pageNum, Integer pagesize);

    ServerResponse searchProduct(Integer productId, String productName, Integer pageNum, Integer pagesize);

    //    ServerResponse
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize);


}
