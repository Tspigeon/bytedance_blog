package com.ty.controller;

import com.ty.domain.http.Result;

import com.ty.utils.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("upload")
@Api(tags = "上传接口")
public class UploadController {

    @Resource
    private  QiniuUtils qiniuUtils;

    @PostMapping
    @ApiOperation("图片上传")
    public Result upload(@RequestParam("image") MultipartFile file) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //唯一文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传至七牛云服务器

        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
