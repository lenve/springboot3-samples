package org.javaboy.demo.utils;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class SealSample {
    public static void main(String[] args) throws Exception {
        Seal seal = new Seal();
        seal.setSize(200);
        SealCircle sealCircle = new SealCircle();
        sealCircle.setLine(4);
        sealCircle.setWidth(95);
        sealCircle.setHeight(95);
        seal.setBorderCircle(sealCircle);
        SealFont mainFont = new SealFont();
        mainFont.setText("江南一点雨股份有限公司");
        mainFont.setSize(22);
        mainFont.setFamily("隶书");
        mainFont.setSpace(22.0);
        mainFont.setMargin(4);
        seal.setMainFont(mainFont);
        SealFont centerFont = new SealFont();
        centerFont.setText("★");
        centerFont.setSize(60);
        seal.setCenterFont(centerFont);
        SealFont titleFont = new SealFont();
        titleFont.setText("财务专用章");
        titleFont.setSize(16);
        titleFont.setSpace(8.0);
        titleFont.setMargin(54);
        seal.setTitleFont(titleFont);
        seal.draw("公章1.png");
    }
}
