package com.xawl.service.edu.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.VodService;
import com.xawl.service.edu.utils.InitVodClient;
import com.xawl.service.edu.utils.VodConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("阿里云视频点播")
@CrossOrigin //跨域
@RestController
@RequestMapping("/api/vod/file")
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("/upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        String videoId = vodService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    @DeleteMapping("/remove/{id}")
    public R remove(
            @PathVariable("id") String id){
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(VodConfiguration.KEY_ID,VodConfiguration.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok().message("删除视频成功");
        } catch (ClientException e) {
            e.printStackTrace();
            throw  new BusinessException(20001,"删除视频失败");
        }
    }

//    @GetMapping("/get/auth/{videoId}")
//    public R getVideoPlayAuth(
//            @PathVariable("videoId") String videoId) throws Exception {
//        String accessKeyId = VodConfiguration.KEY_ID;
//        String accessKeySecret = VodConfiguration.KEY_SECRET;
//        DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
//
//        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
//        request.setVideoId(videoId);
//
//        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
//
//        String playAuth = response.getPlayAuth();
//
//        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
//    }

}
