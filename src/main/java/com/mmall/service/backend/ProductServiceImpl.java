package com.mmall.service.backend;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 16:03 2018/3/22
 * @Description:
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;



    @Override
    public ServerResponse savaUpdate(Product product) {
        if (product == null) {
            return ServerResponse.createByError("没有信息");
        }
        if (product.getId() != null) {
            //不是null的情况下 就是更新他
            int i = productMapper.updateByPrimaryKeySelective(product);
            if (i > 0) {
                return ServerResponse.createBySuccess("更新成功");
            } else {
                return ServerResponse.createByError("更新失败");
            }
        } else {
            //如果没有id 的情况下 那么就是保存一个product
            int insert = productMapper.insert(product);
            if (insert > 0) {
                return ServerResponse.createBySuccess("保存成功");
            } else {
                return ServerResponse.createByError("保存失败");
            }
        }
    }

    @Override
    public ServerResponse setStatus(Integer productid, Integer status) {
        Product product = new Product();
        product.setStatus(status);
        product.setId(productid);
        int insert = productMapper.updateByPrimaryKeySelective(product);
        if (insert > 0) {
            return ServerResponse.createBySuccess("保存成功");
        } else {
            return ServerResponse.createByError("保存失败");
        }
    }

    @Override
    public ServerResponse productDetail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            return ServerResponse.createByError("没有该产品");
        }
        String property = PropertiesUtil.getProperty("ftp.server.ip", "192.168.25.139");
        ProductDetail productDetail = new ProductDetail(product, property);
        return ServerResponse.createBySuccess(productDetail);
    }

    @Override
    public ServerResponse productList(Integer pageNum, Integer pagesize) {
        PageHelper.startPage(pageNum, pagesize);
        List<Product> products = productMapper.selectList();
        PageInfo pageInfo = new PageInfo(products);
        return ServerResponse.createBySuccess(pageInfo);
    }



    @Override
    public ServerResponse searchProduct(Integer productId, String productName, Integer pageNum, Integer pagesize) {
        //对商品进行模糊查询  根据productName进行模糊查询 或者根据productId进行准确查询
        //like 操作必须是使用% 通配符
        PageHelper.startPage(pageNum, pagesize);
        StringBuilder stringBuilder = new StringBuilder();
        String s = stringBuilder.append("%").append(productName).append("%").toString();
        List<Product> products = productMapper.selectByNameAndProductId(s, productId);
        //pageHelper默认进行是是一个SpringAOP操作  select末尾加上limit
        if (products == null) {
            return ServerResponse.createByError("查询失败");
        }
        PageInfo pageInfo = new PageInfo(products);
        return ServerResponse.createBySuccess(pageInfo);
    }


    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize) {

        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByError();
        }
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            //如果是分类为空 并且关键字也为空
            if (category == null && StringUtils.isBlank(keyword)) {
                return ServerResponse.createBySuccess("无相关信息","");
            }
            PageHelper.startPage(pageNum, pageSize);
            //如果二者都不为空 就在该分类中查询所有包含该关键字的 信息
            String ker = "%" + keyword + "%";
            List<Product> products = productMapper.selectByNameAndCategoryIds(ker, Lists.newArrayList(categoryId));
            PageInfo pageInfo = new PageInfo(products);
            return ServerResponse.createBySuccess(pageInfo);
        }
        return ServerResponse.createBySuccess();
    }
}
