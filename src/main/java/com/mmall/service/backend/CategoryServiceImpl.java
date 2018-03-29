package com.mmall.service.backend;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import net.sf.jsqlparser.schema.Server;
import org.omg.CosNaming.IstringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:45 2018/3/21
 * @Description:
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper ;

    @Override
    public ServerResponse insertCategory(String  categorynew, Integer  parentid) {
        //创建一个新的品类
        Category category = new Category();
        category.setParentId(parentid);
        category.setName(categorynew);
        int i = categoryMapper.insert(category);
        if (i != 1) {
            return ServerResponse.createByError("添加条目失败");
        }
        return ServerResponse.createBySuccess("添加条目成功");
    }

    @Override
    public ServerResponse updateCategory(Integer id, String categorynew) {
        //更新一个品类
        Category category = new Category();
        category.setName(categorynew);
        category.setId(id);
        //选择性的进行插入
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        if (i != 1) {
            return ServerResponse.createByError("更新条目失败");
        }
        return ServerResponse.createBySuccess("更新条目成功");
    }

    @Override
    public ServerResponse<List<Category>> parallelChildren(Integer parentid) {
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(parentid);
        return ServerResponse.createBySuccess(categories);
    }
    /**
     * 递归调用一个节点的所有子节点
     * @param parentId
     */
    @Override
    public List<Integer> recursionChildren(Integer parentId) {
        Set<Category> set = Sets.newHashSet();
        recursionGet(set, parentId);
        List<Integer> list = Lists.newArrayList();
        for (Category category : set) {
            list.add(category.getId());
        }
        return list;
    }

    public void  recursionGet(Set<Category> set,Integer parentId) {
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if (category != null) {
            set.add(category);
        }
        //得到这个节点的所有子节点
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(category.getId());
        if (categories == null) {
            return;
        }
        for (Category cate : categories) {
            recursionGet(set, cate.getId());
        }
    }
//    public ServerResponse<List<Category>>  getChildren(Integer parentId) {
//        //得到一个节点的所有子节点
//        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(parentId);
//        if (categories != null) {
//            return ServerResponse.createBySuccess(categories);
//        } else {
//            return ServerResponse.createByError();
//        }
//    }



}
