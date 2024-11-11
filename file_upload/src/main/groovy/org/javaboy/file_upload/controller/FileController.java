package org.javaboy.file_upload.controller;

import org.javaboy.file_upload.model.FileInfo;
import org.javaboy.file_upload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    /**
     * 秒传接口
     * @param file
     * @param md5
     * @return
     */
    @PostMapping("/upload1")
    public ResponseEntity<String> secondUpload(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam(required = false,value = "md5") String md5) {
        try {
            // 检查数据库中是否已存在该文件
            if (fileService.existsByMd5(md5)) {
                return ResponseEntity.ok("文件已存在");
            }
            // 保存文件到服务器
            file.transferTo(new File("/path/to/save/" + file.getOriginalFilename()));
            // 保存文件信息到数据库
            fileService.save(new FileInfo(file.getOriginalFilename(), DigestUtils.md5DigestAsHex(file.getInputStream())));
            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败");
        }
    }

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";

    /**
     * 上传文件到指定位置
     *
     * @param file 上传的文件
     * @param start 文件开始上传的位置
     * @return ResponseEntity<String> 上传结果
     */
    @PostMapping("/upload2")
    public ResponseEntity<String> resumeUpload(@RequestParam("file") MultipartFile file, @RequestParam("start") long start,@RequestParam("fileName") String fileName) {
        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File targetFile = new File(UPLOAD_DIR + fileName);
            RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
            FileChannel channel = randomAccessFile.getChannel();

            channel.position(start);
            channel.transferFrom(file.getResource().readableChannel(), start, file.getSize());

            channel.close();
            randomAccessFile.close();

            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            System.out.println("上传失败: "+e.getMessage());
            return ResponseEntity.status(500).body("上传失败");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Long> checkFile(@RequestParam("filename") String filename) {
        File file = new File(UPLOAD_DIR + filename);
        if (file.exists()) {
            return ResponseEntity.ok(file.length());
        } else {
            return ResponseEntity.ok(0L);
        }
    }
}
