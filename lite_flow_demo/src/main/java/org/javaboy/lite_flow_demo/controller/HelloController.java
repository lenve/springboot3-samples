package org.javaboy.lite_flow_demo.controller;

import com.yomahub.liteflow.core.FlowExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
public class HelloController {

    @Autowired
    FlowExecutor flowExecutor;
    @GetMapping("/hello")
    public void hello() {
        flowExecutor.execute2Resp("orderProcessChain");
    }
}
