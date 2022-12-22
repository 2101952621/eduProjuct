package com.xawl.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xawl.service.edu.entity.Subject;
import com.xawl.service.edu.entity.dto.SubjectDTO;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectListener extends AnalysisEventListener<SubjectDTO> {


    private SubjectMapper sub;
    public SubjectListener(SubjectMapper baseMapper) {
        this.sub = baseMapper;
    }
    public SubjectListener() {}

    @Override
    public void invoke(SubjectDTO data, AnalysisContext context) {
        if (data == null){
            throw  new BusinessException(20001,"文件数据为空");
        }
        String sname = data.getToSub();
        //查询一级菜单
        Subject byParId = getSubByOne(data.getOneSub());
        if (byParId == null){
            byParId = new Subject();
            byParId.setParentId("0");
            byParId.setTitle(data.getOneSub());
            sub.insert(byParId);
        }
        //查询二级菜单
        Subject subByTow = getSubByTow(sname, byParId.getId());
        if (subByTow == null){
            subByTow = new Subject();
            subByTow.setTitle(sname);
            subByTow.setParentId(byParId.getId());
            sub.insert(subByTow);
        }
    }


    //查询一级菜单是否已经存入
    public Subject getSubByOne(String title){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title)
                .eq("parent_id","0");
        Subject subject = sub.selectOne(wrapper);
        return  subject;
    }

    //查询二级菜单是否已经存入
    public Subject getSubByTow(String name,String parId){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name)
                .eq("parent_id",parId);
        Subject subject = sub.selectOne(wrapper);
        return subject;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {


    }
}
