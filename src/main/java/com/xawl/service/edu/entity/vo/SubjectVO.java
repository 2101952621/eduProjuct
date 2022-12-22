package com.xawl.service.edu.entity.vo;

import com.xawl.service.edu.entity.Subject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class SubjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private List<Subject> children;
}
