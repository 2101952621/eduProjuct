package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Video;
import com.xawl.service.edu.service.VideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@CrossOrigin
@RequestMapping("/edu/video")
public class VideoController {

//    @Resource
//    VodRemoteApi vodRemoteApi;

    @Resource
    VideoService videoService;

    @ApiOperation("添加")
    @PostMapping("/add/video")
    public R addVideo(
            @RequestBody Video video) {
        System.out.println(video.getVideoSourceId());
        System.out.println(video.getVideoOriginalName());
        videoService.save(video);
        return R.ok().message("添加小节成功");
    }


    @ApiOperation("删除")
    @DeleteMapping("/remove/video/{id}")
    public R removeVideo(
            @PathVariable("id") String id) {
//        //获取视频id
//        Video byId = videoService.getById(id);
//        //删除视频,通过视频id
//        if (!StringUtils.isEmpty(byId.getVideoSourceId())) {
//            vodRemoteApi.remove(byId.getVideoSourceId());
//        }
        boolean b = videoService.removeById(id);
        if (b) {
            return R.ok().message("删除小节成功");
        } else {
            return R.error().message("删除小节失败");
        }
    }

    @ApiOperation(value = "根据ID获取小节内容")
    @GetMapping("/get/video/by/{id}")
    public R getById(
            @PathVariable("id") String id) {
        Video byId = videoService.getById(id);
        return R.ok().data("data", byId);
    }

    @ApiOperation(value = "修改小节内容")
    @PostMapping("/update")
    public R updateVideo(@RequestBody Video video) {
        boolean b = videoService.updateById(video);
        if (b) {
            return R.ok().message("修改小节成功");
        } else {
            return R.error().message("修改小节失败");
        }
    }

}

