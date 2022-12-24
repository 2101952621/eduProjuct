package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.ClassCourseTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author javaCoder
 * @since 2022-12-23
 */
public interface ClassCourseTaskService extends IService<ClassCourseTask> {

    List<ClassCourseTask> getTaskByCourseId(String id);
}
