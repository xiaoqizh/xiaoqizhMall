package com.mmall.service.backend;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 10:27 2018/3/24
 * @Description:
 */

public interface FileService {
     String upload(MultipartFile file, String path);

}
