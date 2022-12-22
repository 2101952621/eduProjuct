package com.xawl.service.edu.service.impl;

import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.MD5;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.mapper.TeacherMapper;
import com.xawl.service.edu.service.ClassService;
import com.xawl.service.edu.service.TeacherService;
import com.xawl.service.edu.entity.vo.TeacherQuery;
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
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Autowired
    ClassService classService;

    //条件查询教师信息
    @Override
    public IPage<Teacher>  pageByCondition(Long pageSize, Long pageInfo, TeacherQuery teacherQuery) {
        Page<Teacher> page = new Page<>(pageSize, pageInfo);
        QueryWrapper<Teacher> wrapper = new QueryWrapper<Teacher>();
        System.out.println(teacherQuery.getName());
        if (teacherQuery != null) {
            wrapper.eq(teacherQuery.getLevel() != null, "level", teacherQuery.getLevel())
                    .ge(teacherQuery.getBegin() != null, "gmt_create", teacherQuery.getBegin())
                    .le(teacherQuery.getEnd() != null, "gmt_modified", teacherQuery.getEnd())
                    .like(teacherQuery.getName() != null, "name", teacherQuery.getName());
            wrapper.orderByDesc("gmt_create");
            return  baseMapper.selectPage(page, wrapper);
        }
        wrapper.orderByDesc("gmt_create");
        IPage<Teacher> iPage = baseMapper.selectPage(page, wrapper);
        System.out.println(iPage.toString());
        return iPage;
    }

    @Override
    public List<Teacher> getTeacherIndex() {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id")
                .last("limit  4 ");
        List<Teacher> teachers = baseMapper.selectList(wrapper);
        return teachers;
    }

    @Override
    public Map<String, Object> seleteTeacherPage(Page<Teacher> page) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Teacher> pageParam = baseMapper.selectPage(page, wrapper);
        List<Teacher> records = pageParam.getRecords();
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
    public boolean saveTeacher(Teacher teacher) {
        String password = teacher.getPassword();
        String name = teacher.getName();
        String career = teacher.getCareer();
        String mobile = teacher.getMobile();
        Integer level = teacher.getLevel();
        String nickname = teacher.getNickname();
        if (StringUtils.isEmpty(name)
                || StringUtils.isEmpty(career)
                || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(level)
                || StringUtils.isEmpty(nickname)
        ){
            throw  new BusinessException(20001,"添加或注册失败");
        }
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Teacher selectOne = baseMapper.selectOne(wrapper);
        if (selectOne != null){
            throw  new BusinessException(20001,"该手机号已经被使用");
        }
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",nickname);
        Teacher one = baseMapper.selectOne(queryWrapper);
        if (one != null){
            throw  new BusinessException(20001,"该昵称已经被使用");
        }
        if (password == "" ||password ==null){
            teacher.setPassword(MD5.encrypt("123456"));
        }
        teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        teacher.setLevel(1);
        baseMapper.insert(teacher);
        return true;
    }

    @Override
    public String teacherLogin(String mobile, String password) {
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new BusinessException(20001,"登录失败");
        }
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Teacher teacher = baseMapper.selectOne(wrapper);
        if (teacher == null) {
            throw new BusinessException(20001,"手机号不存在");
        }
        String encrypt = MD5.encrypt(password);
        String onePassword = teacher.getPassword();
        String id = teacher.getId();
        if (encrypt.equals(onePassword)) {
            String token = JwtUtils.getJwtToken(id, password);
            return token;
        }else {
            throw new BusinessException(20001,"密码错误");
        }
    }

    //删除教师&删除所带班级
    @Override
    public boolean removeTeacherById(String id) {
        boolean result = classService.getClassInfoByTeacherId(id);
        if (result){
            classService.updateByTeacherId(id);
        }
        baseMapper.deleteById(id);
        return true;
    }
}
