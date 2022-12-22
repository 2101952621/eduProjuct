package com.xawl.service.edu.service.impl;

import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.mapper.ChapterMapper;
import com.xawl.service.edu.service.VideoService;
import com.xawl.service.edu.entity.Chapter;
import com.xawl.service.edu.entity.Video;
import com.xawl.service.edu.entity.vo.ChapterVO;
import com.xawl.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    VideoService videoService;


    //获取章节内容
    @Override
    public List<ChapterVO> getChapterAndVideo(String courseId) {
        List<ChapterVO> chapters = new ArrayList<>();
        //通过课程id,获取章节数据
        QueryWrapper<Chapter> characterWrapper = new QueryWrapper<>();
        characterWrapper.eq("course_id",courseId);
        List<Chapter> list = baseMapper.selectList(characterWrapper);
        list.forEach(a ->{
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(a,chapterVO);
            //获取章节数据
            String id = a.getId();
            //根据当前章节id查询小节信息
            List<Video> videos = videoService.getVideoByChapterList(id);
            chapterVO.setVideos(videos);
            chapters.add(chapterVO);
        });
        return chapters;
    }

    @Override
    public boolean removeChapterById(String id) {

        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        int count = videoService.count(wrapper);
        if (count > 0){
            throw new BusinessException(20001,"不能删除");
        }else {
            int i = baseMapper.deleteById(id);
            return  i>0;
        }
    }

    @Override
    public void removeByCourseId(String courseId) {
        //TODO
//        删除视频内容
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
