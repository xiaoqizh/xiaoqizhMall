package com.mmall.service.backend;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;


/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:45 2018/3/21
 * @Description:
 */

public interface CategoryService {

    ServerResponse insertCategory(String categorynew, Integer parentid);

    ServerResponse updateCategory(Integer id, String categorynew);

    List<Integer> recursionChildren(Integer parentId);

    ServerResponse<List<Category>> parallelChildren(Integer id);

}
