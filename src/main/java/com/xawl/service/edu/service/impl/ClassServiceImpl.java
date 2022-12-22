package com.xawl.service.edu.service.impl;

import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.StudentService;
import com.xawl.service.edu.entity.Class;
import com.xawl.service.edu.entity.ClassCourseInfo;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.query.ClassQuery;
import com.xawl.service.edu.mapper.ClassMapper;
import com.xawl.service.edu.service.ClassCourseService;
import com.xawl.service.edu.service.ClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    StudentService studentService;

    @Autowired
    ClassCourseService classCourseService;

    @Override
    public IPage<Class> pageByCondition(Long pageSize, Long pageInfo, ClassQuery classQuery) {
        Page<Class> page = new Page<>(pageSize, pageInfo);
        QueryWrapper<Class> wrapper = new QueryWrapper<Class>();
        if (classQuery != null) {
            wrapper.le(classQuery.getEnd() != null, "gmt_modified", classQuery.getEnd())
                    .ge(classQuery.getBegin() != null, "gmt_create", classQuery.getBegin())
                    .eq(classQuery.getClassName() != null, "class_name", classQuery.getClassName())
                    .eq(classQuery.getClassTeacherId() != null, "class_teacher_id", classQuery.getClassTeacherId());
            wrapper.orderByDesc("gmt_create");
            return baseMapper.selectPage(page, wrapper);
        }
        wrapper.orderByDesc("gmt_create");
        IPage<Class> iPage = baseMapper.selectPage(page, null);
        return iPage;
    }

    @Override
    public String saveClassInfo(Class classInfo) {
        if (classInfo.getClassName() == null) {
            throw new BusinessException(20001, "班级不能为空");
        }
        String className = classInfo.getClassName();
        //保证添加课程名称不能重复
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name", className);
        Class selectOne = baseMapper.selectOne(wrapper);
        if (selectOne != null) {
            throw new BusinessException(20001, "当前班级名称已存在请重新输入");
        }
        classInfo.setClassNumber(0);
        classInfo.setStatus(0);
        classInfo.setHeadImage("https://gimg2.baidu.com/image_search/" +
                "src=http%3A%2F%2Fwww.luyx.cn%2Fuploads%" +
                "2F2020%2F05%2F10%2F09193435414.gif&refer" +
                "=http%3A%2F%2Fwww.luyx.cn&app=2002&size=f9999,1000" +
                "0&q=a80&n=0&g=0n&fmt=jpeg?sec=1645356481&t=126b6395e" +
                "bc8eb70d0aa0b354a165f0f");
        int insert = baseMapper.insert(classInfo);
        return classInfo.getId();

    }

    @Override
    public Integer getCountById(String classId) {
        if (!StringUtils.isEmpty(classId)){
            Class aClass = baseMapper.selectById(classId);
            Integer classNumber = aClass.getClassNumber();
            return classNumber;
        }
       return 0;
    }

    @Override
    public Map<String, Object> getCourseFront(Page<Class> pageParam) {
        baseMapper.selectPage(pageParam, null);
        List<Class> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public boolean stuJoinClass(String stuId, String classId) {
        Student student = studentService.getById(stuId);
        if (StringUtils.isEmpty(student)) {
            throw new BusinessException(20001, "请登录学生端加入班级");
        }
        String id = student.getClassId();
        if (!StringUtils.isEmpty(id) && id.equals(classId)) {
            throw new BusinessException(20001, "该学生已加入到该班级");
        }
        student.setClassId(classId);
        boolean result = studentService.updateById(student);
        //更新班级信息
        Class classInfo = baseMapper.selectById(classId);
        classInfo.setClassNumber(classInfo.getClassNumber()+1);
        baseMapper.updateById(classInfo);
        return result;
    }

    @Override
    public boolean stuExitClass(String number, String classId) {
        Student student = studentService.getById(number);
        if (StringUtils.isEmpty(student)) {
            throw new BusinessException(20001, "当前身份不符合条件");
        }
        String id = student.getClassId();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(20001, "当前没有加入班级");
        }
        if (!id.equals(classId)) {
            throw new BusinessException(20001, "当前不在该班级");
        }
        student.setClassId("");
        boolean result = studentService.updateById(student);
        //更新班级信息
        Class classInfo = baseMapper.selectById(classId);
        if (classInfo.getClassNumber() > 0){
            classInfo.setClassNumber(classInfo.getClassNumber()-1);
            baseMapper.updateById(classInfo);
        }
        return result;
    }

    //判断是否当前教师任职班主任
    @Override
    public boolean getClassInfoByTeacherId(String id) {
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        wrapper.eq("class_teacher_id", id);
        Integer integer = baseMapper.selectCount(wrapper);
        return integer != 0 ? true : false;
    }


    @Override
    public void updateByTeacherId(String id) {
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        wrapper.eq("class_teacher_id", id);
        Class selectOne = baseMapper.selectOne(wrapper);
        selectOne.setClassTeacherId("");
        baseMapper.updateById(selectOne);
    }

    //删除班级-》删除班级中所有学生
    @Override
    public boolean removeClassById(String id) {
        //删除班级学生信息
        List<Student> list = studentService.getByClassId(id);
        if (list != null || list.size() > 0) {
            //更新学生信息退出班级
            list.forEach(a ->{
                a.setClassId("");
                studentService.updateById(a);
            });
        }
        //删除班级课程信息
        QueryWrapper<ClassCourseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",id);
        classCourseService.remove(queryWrapper);
        //删除班级信息
        baseMapper.deleteById(id);
        return true;
    }
}
