package com.xawl.service.edu.mapper;

import com.xawl.service.edu.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer selectUserByDay(String day);
}
