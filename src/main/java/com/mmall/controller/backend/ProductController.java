package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.backend.FileService;
import com.mmall.service.backend.ProductService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 15:58 2018/3/22
 * @Description:
 */

@Controller
@RequestMapping("/manage/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @RequestMapping("saveupdate.do")
    @ResponseBody
    public ServerResponse saveOrUpdate(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }
        return productService.savaUpdate(product);
    }

    @RequestMapping("set_status.do")
    @ResponseBody
    public ServerResponse setStatus(Integer productid,Integer status, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }
        return  productService.setStatus(productid, status);
    }

    @RequestMapping("product_detail.do")
    @ResponseBody
    public ServerResponse productDetail(Integer id, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }
        //得到一个产品
        return productService.productDetail(id);
    }

    @RequestMapping("product_list.do")
    @ResponseBody
    public ServerResponse productList(HttpSession session,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pagesize", defaultValue = "10")    Integer pagesize) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }
        //使用pageHelper插件得到产品列表
        return productService.productList(pageNum, pagesize);
    }

    @RequestMapping("search_product.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productname,Integer productid,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pagesize", defaultValue = "10")    Integer pagesize) {
        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }

        return productService.searchProduct(productid, productname, pageNum, pagesize);
    }

    @RequestMapping("upload_product.do")
    @ResponseBody
    public ServerResponse productUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                        HttpServletRequest request) {

        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登陆");
        }
        if (user.getRole() != 1) {
            return ServerResponse.createByError("不是管理员权限");
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        System.out.println(path);
        //上传到绝对路径 webapp下面的 upload下面去
        String fileName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
        Map<String ,String > fileMap = Maps.newHashMap();
        fileMap.put("url", url);
        fileMap.put("uri", fileName);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping("upload_richtext.do")
    @ResponseBody
    public Map productUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                        HttpServletRequest request, HttpServletResponse response) {

        User user = (User) session.getAttribute(Const.CURRNET_USER);
        if (user == null) {
            Map map = Maps.newHashMap();
            map.put("success", false);
            map.put("msg", "用户未登录");
            map.put("file_path", "");
            return map;
        }
        if (user.getRole() != 1) {
            Map map = Maps.newHashMap();
            map.put("success", false);
            map.put("msg", "不是管理员权限");
            map.put("file_path", "");
            return  map;
        }

        //一般富文本编辑器 都要求固定的json返回结果
//        {   文本编辑器
//            success: true/false
//        msg:
//        file_path
//
//         }

        String path = request.getSession().getServletContext().getRealPath("upload");
        System.out.println(path);
        //上传到绝对路径 webapp下面的 upload下面去
        String fileName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("success", true);
        fileMap.put("msg", "图片上传成功");
        fileMap.put("file_path", url);
        return fileMap;
    }

    @RequestMapping("getProductByKeywordCategory.do")
    @ResponseBody
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId,
                                               @RequestParam(required = false,defaultValue = "0") Integer pageNum,
                                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        ServerResponse<PageInfo> response = productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize);
        return response;
    }

}
