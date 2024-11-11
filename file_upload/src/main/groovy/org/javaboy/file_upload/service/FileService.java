package org.javaboy.file_upload.service;

import org.javaboy.file_upload.model.FileInfo;
import org.springframework.stereotype.Service;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Service
public class FileService {
    public boolean existsByMd5(String md5) {
        System.out.println("查询文件:" + md5);
        return true;
    }

    public void save(FileInfo fileInfo) {
        System.out.println("保存文件:" + fileInfo.getMd5());
    }
}
