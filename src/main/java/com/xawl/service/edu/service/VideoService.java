package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;



public interface VideoService extends IService<Video> {

    List<Video> getVideoByChapterList(String id);

    void removeByCourseId(String courseId);
}
