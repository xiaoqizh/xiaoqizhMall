package com.mmall.service.backend;

import com.google.common.collect.Lists;
import com.mmall.util.FTPUtil;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:27 2018/3/24
 * @Description:
 *
 *
 *    基本的文件服务器 就是用ftp设置工作路径
 *    然后通过nginx把工作区间设置到ftp的工作路径上
 */


@Service
public class FileServiceImpl implements FileService {

    //实现基本的上传

    /**
     *  因为这是部署在win上面的 不是linux上面 所以需要在win上面进行一个中转的文件
     * @param file
     * @param path
     * @return
     */

    @Override
    public String upload(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        //得到文件的拓展名  abc.jpg
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //生成文件名
        String fileName = UUID.randomUUID().toString()+"." + extension;
        File fileDir = new File(path);
        //创建文件
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //在path路径下 上传文件
        File target = new File(path, fileName);
        String targetFileName = "";
        try {
            file.transferTo(target);
            // 将文件传到ftp服务器
            FTPUtil.uploadFile(Lists.newArrayList(target));
            // 删除webapp下面的文件
             targetFileName = target.getName();
            target.delete();
        } catch (IOException e) {
            System.out.println("文件上传失败");
            e.printStackTrace();
            return null;
        }
        //必须返回文件把名字
        return targetFileName;
    }

//    /**
////     * @Description:
//     */
//    @Test
//    public void fff(){
//        String a = "abv.less";
//        System.out.println(a.substring(0, a.lastIndexOf(".")));
//        String substring = a.substring(a.lastIndexOf(".")+1, a.length());
//        System.out.println(substring);
//    }
}
