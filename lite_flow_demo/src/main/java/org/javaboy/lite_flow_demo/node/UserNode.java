package org.javaboy.lite_flow_demo.node;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import org.springframework.stereotype.Component;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Component
public class UserNode extends NodeComponent {
    @Override
    public void process() {
        System.out.println("用户身份验证");
    }
}
