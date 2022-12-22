package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Chapter;
import com.xawl.service.edu.entity.vo.ChapterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ChapterService extends IService<Chapter> {

    List<ChapterVO> getChapterAndVideo(String courseId);

    boolean removeChapterById(String id);

    void removeByCourseId(String courseId);
}
