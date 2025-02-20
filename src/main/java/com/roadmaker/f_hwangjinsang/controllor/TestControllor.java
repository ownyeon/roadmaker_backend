package com.roadmaker.f_hwangjinsang.controllor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllor {

    @PostMapping("/test")
    public String test() {
        return "hello";
    }
}
