package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Subject;
import com.xawl.service.edu.entity.vo.SubjectVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface SubjectService extends IService<Subject> {

    void saveSubjectData(MultipartFile file);

    List<SubjectVO> getSubList();
}
