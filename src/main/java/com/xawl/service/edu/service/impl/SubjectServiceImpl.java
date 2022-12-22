package com.xawl.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.xawl.service.edu.entity.Subject;
import com.xawl.service.edu.entity.dto.SubjectDTO;
import com.xawl.service.edu.entity.vo.SubjectVO;
import com.xawl.service.edu.listener.SubjectListener;
import com.xawl.service.edu.mapper.SubjectMapper;
import com.xawl.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    //数据导入
    @Override
    public void saveSubjectData(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectDTO.class,new SubjectListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //课程信息的显示
    @Override
    public List<SubjectVO> getSubList() {
        List<SubjectVO> lists = new ArrayList<>();
        List<Subject> list = baseMapper.selectList(null);
        list.forEach( a ->{
            if (a.getParentId().equals("0")) {
                List<Subject> lits = getChildren(a.getId());
                SubjectVO vo = new SubjectVO();
                vo.setId(a.getId());
                vo.setTitle(a.getTitle());
                vo.setChildren(lits);
                lists.add(vo);
            }
        });
        return lists;
    }

    //查看当前节点是否有子节点
    public List<Subject> getChildren(String id){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Subject> list = baseMapper.selectList(wrapper);
        return  list;
    }

}
