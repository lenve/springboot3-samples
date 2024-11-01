package org.javaboy.params_validation_demo.controller;

import org.javaboy.params_validation_demo.group.RegisterGroup;
import org.javaboy.params_validation_demo.group.UpdateGroup;
import org.javaboy.params_validation_demo.model.UserDto;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
public class UserController {
    @GetMapping("/hello")
    public String hello(@Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 处理校验失败情况
            System.out.println(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return "200";
    }

    @PostMapping("/register")
    public String register(@Validated(RegisterGroup.class) @RequestBody UserDto user) {
        // 注册逻辑
        return "注册成功";
    }

    @PostMapping("/update")
    public String update(@Validated(UpdateGroup.class) @RequestBody UserDto user) {
        // 更新逻辑
        return "更新成功";
    }
}
