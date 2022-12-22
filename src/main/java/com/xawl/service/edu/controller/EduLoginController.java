package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(description = "后台登录接口")
@RestController
@RequestMapping("/edu/service/user")
@CrossOrigin
public class EduLoginController {

    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[Admin]")
                .data("name","Admin")
                .data("avatar","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F19%2F07%2F16%2F4a6171ef00006e4deb61f5719fb444a6.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1644313316&t=aa494b9da5565fff508978fb608dd659");

    }

}
