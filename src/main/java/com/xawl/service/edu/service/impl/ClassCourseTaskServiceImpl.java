package com.xawl.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xawl.service.edu.controller.FileController;
import com.xawl.service.edu.entity.ClassCourseTask;
import com.xawl.service.edu.mapper.ClassCourseTaskMapper;
import com.xawl.service.edu.service.ClassCourseTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author javaCoder
 * @since 2022-12-23
 */
@Service
public class ClassCourseTaskServiceImpl extends ServiceImpl<ClassCourseTaskMapper, ClassCourseTask> implements ClassCourseTaskService {
    private Logger log = LoggerFactory.getLogger(ClassCourseTaskServiceImpl.class);
    @Override
    public List<ClassCourseTask> getTaskByCourseId(String id) {
        QueryWrapper<ClassCourseTask> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id",id);
        List<ClassCourseTask> classCourseTasks = baseMapper.selectList(wrapper);
        log.info("班级课程信息表=" + classCourseTasks);
        return classCourseTasks;
    }
}
