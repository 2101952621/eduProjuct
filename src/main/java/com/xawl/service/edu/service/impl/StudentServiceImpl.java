package com.xawl.service.edu.service.impl;

import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.MD5;

import com.xawl.service.edu.entity.Class;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.query.StudentQuery;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.mapper.StudentMapper;
import com.xawl.service.edu.service.ClassService;
import com.xawl.service.edu.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


    @Autowired
    ClassService classService;


    @Override
    public Page<Student> pageByCondition(Long pageSize, Long pageInfo, StudentQuery studentQuery) {

        Page<Student> page = new Page<>(pageSize, pageInfo);
        QueryWrapper<Student> wrapper = new QueryWrapper<Student>();
        if (studentQuery != null) {
            wrapper.eq(studentQuery.getClassId() != null, "class_id", studentQuery.getClassId())
                    .ge(studentQuery.getBegin() != null, "gmt_create", studentQuery.getBegin())
                    .le(studentQuery.getEnd() != null, "gmt_modified", studentQuery.getEnd())
                    .eq(studentQuery.getName() != null, "name", studentQuery.getName());
            wrapper.orderByDesc("gmt_create");
            Page<Student> studentPage = baseMapper.selectPage(page, wrapper);
            List<Student> records = studentPage.getRecords();
            records.forEach(a -> {
                String classId = a.getClassId();
                if (!StringUtils.isEmpty(classId)) {
                    Class byId = classService.getById(classId);
                    a.setClassName(byId.getClassName());
                }
            });
            return studentPage;
        }
        Page<Student> studentPage = baseMapper.selectPage(page, null);
        List<Student> list = studentPage.getRecords();
        list.forEach(a -> {
            String classId = a.getClassId();
            if (!StringUtils.isEmpty(classId)) {
                Class byId = classService.getById(classId);
                a.setClassName(byId.getClassName());
            }
        });
        return studentPage;
    }

    @Override
    public List<Student> getListByClassId(Long pageSize, Long pageInfo, String classId) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", classId);
        Page<Student> page = new Page<>(pageSize, pageInfo);
        Page<Student> studentPage = baseMapper.selectPage(page, wrapper);
        return studentPage.getRecords();
    }

    //添加学生
    @Override
    public boolean saveInfo(Student student) {
        student.setStatus("0");
        student.setPassword(MD5.encrypt("123456"));
        int insert = baseMapper.insert(student);
        if (insert == 1) {
            //判断是否加入班级
            if (!StringUtils.isEmpty(student.getClassId())) {
                Class classInfo = classService.getById(student.getClassId());
                if (classInfo != null) {
                    Integer classNumber = classInfo.getClassNumber();
                    if (classNumber >= 0) {
                        classInfo.setClassNumber(classNumber + 1);
                    }
                    classService.updateById(classInfo);
//                    //添加班级学生关联信息
//                    StudentClass studentClass = new StudentClass();
//                    studentClass.setStudentId(student.getId());
//                    studentClass.setClassId(student.getClassId());
//                    studentClassService.save(studentClass);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Student getByNumber(String number) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number);
        Student student = baseMapper.selectByNumber(number);
        return student;
    }

    //删除学生同时更新班级信息
    @Override
    public boolean removeStuById(String id) {
        Student student = baseMapper.selectById(id);
        String classId = student.getClassId();
        if (StringUtils.isEmpty(student.getClassId())) {
            baseMapper.deleteById(id);
            return true;
        }
        Class classInfo = classService.getById(classId);
        Integer number = classInfo.getClassNumber();
        if (number > 0) {
            classInfo.setClassNumber(number - 1);
        } else {
            throw new BusinessException(20001, "数据异常,请稍后再试");
        }
        boolean b = classService.updateById(classInfo);
        if (b) {
            baseMapper.deleteById(id);
        }
        return b;
    }

    @Override
    public List<Student> getByClassId(String id) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", id);
        List<Student> students = baseMapper.selectList(wrapper);
        return students;
    }

    @Override
    public boolean updateStu(Student student) {
        Student studentInfo = baseMapper.selectById(student.getId());
        //获取原有学生加入班级信息
        String classId = studentInfo.getClassId();
        //判断是否修改班级信息
        if (student.getClassId().equals(classId)) {
            baseMapper.updateById(student);
        } else {
            //修改原来班级人数信息
            Class classInfo = classService.getById(classId);
            if (!StringUtils.isEmpty(classInfo)) {
                if (classInfo.getClassNumber() > 0) {
                    classInfo.setClassNumber(classInfo.getClassNumber() - 1);
                }
                classService.updateById(classInfo);
            }
            //修改更新的班级信息
            Class updateClassInfo = classService.getById(student.getClassId());
            if (!StringUtils.isEmpty(updateClassInfo)) {
                updateClassInfo.setClassNumber(updateClassInfo.getClassNumber() + 1);
                classService.updateById(updateClassInfo);
            }
            //修改学生信息
            baseMapper.updateById(student);
        }
        return true;
    }

    @Override
    public Integer getUserSta(String day) {
        Integer count =  baseMapper.selectUserByDay(day);
        return count;
    }

    @Override
    public String studentLogin(String number, String password) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number);
//        Student student = baseMapper.selectByNumber(number);
        Student student = null;
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number",number);
        student = baseMapper.selectOne(queryWrapper);
        if (student == null) {
            throw new BusinessException(20001, "不存在学号");
        }
        String id = student.getId();
        String jwtToken = JwtUtils.getJwtToken(id, student.getNickname());
        return jwtToken;
    }

    @Override
    public boolean updateStuById(String studentId) {
        Student student = baseMapper.selectById(studentId);
        String classId = student.getClassId();
        student.setClassId("");
        int a = baseMapper.updateById(student);
        //  更新班级信息
        if (a == 1) {
            Class classInfo = classService.getById(classId);
            Integer classNumber = classInfo.getClassNumber();
            classInfo.setClassNumber(classNumber - 1);
            boolean result = classService.updateById(classInfo);
            return result;
        }
        return false;
    }
}
