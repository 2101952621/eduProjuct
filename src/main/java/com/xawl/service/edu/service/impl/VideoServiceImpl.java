package com.xawl.service.edu.service.impl;


import com.xawl.service.edu.service.VideoService;
import com.xawl.service.edu.entity.Video;
import com.xawl.service.edu.mapper.VideoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    VideoService videoService;

    @Override
    public List<Video> getVideoByChapterList(String id) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        List<Video> videos = baseMapper.selectList(wrapper);
        return videos;
    }

    //删除小节内容
    @Override
    public void removeByCourseId(String courseId) {
        //通过课程id获取所有小节信息
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        //删除所有小节的视频
        List<Video> videos = baseMapper.selectList(wrapper);
        if (videos.size() > 0){
            videos.forEach(a ->{
                if (!StringUtils.isEmpty(a.getVideoSourceId())){
                    videoService.removeById(a.getVideoSourceId());
                }
            });
            baseMapper.delete(wrapper);
        }
    }
}
