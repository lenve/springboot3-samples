package org.javaboy.lite_flow_demo.node;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Component
public class OrderNode extends NodeComponent {
    @Override
    public void process() {
        System.out.println("订单确认");
    }
}
