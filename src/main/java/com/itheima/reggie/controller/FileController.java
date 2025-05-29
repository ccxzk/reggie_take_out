package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class FileController {
    @Resource
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        log.info("文件上传：{}",file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return R.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return R.error("上传失败");
    }
}
