package org.javaboy.demo.utils;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Seal {
    // 起始位置
    private static final int INIT_BEGIN = 5;

    // 尺寸
    private Integer size;
    // 颜色
    private Color color;
    // 主字
    private SealFont mainFont;
    // 副字
    private SealFont viceFont;
    // 抬头
    private SealFont titleFont;
    // 中心字
    private SealFont centerFont;
    // 边线圆
    private SealCircle borderCircle;
    // 内边线圆
    private SealCircle borderInnerCircle;
    // 内线圆
    private SealCircle innerCircle;
    // 边线框
    private Integer borderSquare;
    // 加字
    private String stamp;

    /**
     * 画公章
     */
    public boolean draw(String pngPath) throws Exception {
        if (borderSquare != null) {
            return draw2(pngPath); // 画私章
        }

        //1.画布
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        //2.画笔
        Graphics2D g2d = bi.createGraphics();

        //2.1抗锯齿设置
        //文本不抗锯齿，否则圆中心的文字会被拉长
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        //其他图形抗锯齿
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);

        //2.2设置背景透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0));

        //2.3填充矩形
        g2d.fillRect(0, 0, size, size);

        //2.4重设透明度，开始画图
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1));


        //2.5设置画笔颜色
        g2d.setPaint(color == null ? Color.RED : color);


        // 背景色，要搭配clearRect才可以设置背景生效
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, size, size);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形,

        //3.画边线圆
        if (borderCircle != null) {
            drawCircle(g2d, borderCircle, INIT_BEGIN, INIT_BEGIN);
        } else {
            throw new Exception("BorderCircle can not null！");
        }

        int borderCircleWidth = borderCircle.getWidth();
        int borderCircleHeight = borderCircle.getHeight();

        //4.画内边线圆
        if (borderInnerCircle != null) {
            int x = INIT_BEGIN + borderCircleWidth - borderInnerCircle.getWidth();
            int y = INIT_BEGIN + borderCircleHeight - borderInnerCircle.getHeight();
            drawCircle(g2d, borderInnerCircle, x, y);
        }

        //5.画内环线圆
        if (innerCircle != null) {
            int x = INIT_BEGIN + borderCircleWidth - innerCircle.getWidth();
            int y = INIT_BEGIN + borderCircleHeight - innerCircle.getHeight();
            drawCircle(g2d, innerCircle, x, y);
        }

        //6.画弧形主文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(g2d, borderCircle, mainFont, true);
        } else {
            drawArcFont4Circle(g2d, borderCircleHeight, mainFont, true);
        }

        //7.画弧形副文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(g2d, borderCircle, viceFont, false);
        } else {
            drawArcFont4Circle(g2d, borderCircleHeight, viceFont, false);
        }

        //8.画中心字
        drawFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, centerFont);

        //9.画抬头文字
        drawFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, titleFont);

        g2d.dispose();

        return ImageIO.write(bi, "PNG", new File(pngPath));
    }

    /**
     * 绘制圆弧形文字
     */
    private static void drawArcFont4Circle(Graphics2D g2d, int circleRadius, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int textLen = font.getText().length();

        //2.字体大小，默认根据字体长度动态设定
        int size = font.getSize() == null ? (55 - textLen * 2) : font.getSize();

        //3.字体样式
        int style = font.getBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFamily(), style, size);

        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D rectangle = f.getStringBounds(font.getText(), context);

        //5.文字之间间距，默认动态调整
        double space;
        if (font.getSpace() != null) {
            space = font.getSpace();
        } else {
            if (textLen == 1) {
                space = 0;
            } else {
                space = rectangle.getWidth() / (textLen - 1) * 0.9;
            }
        }

        //6.距离外圈距离
        int margin = font.getMargin() == null ? INIT_BEGIN : font.getMargin();

        //7.写字
        double newRadius = circleRadius + rectangle.getY() - margin;
        double radianPerInterval = 2 * Math.asin(space / (2 * newRadius));

        double fix = 0.04;
        if (isTop) {
            fix = 0.18;
        }
        double firstAngle;
        if (!isTop) {
            if (textLen % 2 == 1) {
                firstAngle = Math.PI + Math.PI / 2 - (textLen - 1) * radianPerInterval / 2.0 - fix;
            } else {
                firstAngle = Math.PI + Math.PI / 2 - ((textLen / 2.0 - 0.5) * radianPerInterval) - fix;
            }
        } else {
            if (textLen % 2 == 1) {
                firstAngle = (textLen - 1) * radianPerInterval / 2.0 + Math.PI / 2 + fix;
            } else {
                firstAngle = (textLen / 2.0 - 0.5) * radianPerInterval + Math.PI / 2 + fix;
            }
        }

        for (int i = 0; i < textLen; i++) {
            double theta;
            double thetaX;
            double thetaY;

            if (!isTop) {
                theta = firstAngle + i * radianPerInterval;
                thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
                thetaY = newRadius * Math.cos(theta - Math.PI / 2);
            } else {
                theta = firstAngle - i * radianPerInterval;
                thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
                thetaY = newRadius * Math.cos(theta - Math.PI / 2);
            }

            AffineTransform transform;
            if (!isTop) {
                transform = AffineTransform.getRotateInstance(Math.PI + Math.PI / 2 - theta);
            } else {
                transform = AffineTransform.getRotateInstance(Math.PI / 2 - theta + Math.toRadians(8));
            }
            Font f2 = f.deriveFont(transform);
            g2d.setFont(f2);
            g2d.drawString(font.getText().substring(i, i + 1), (float) (circleRadius + thetaX + INIT_BEGIN), (float) (circleRadius - thetaY + INIT_BEGIN));
        }
    }

    /**
     * 绘制椭圆弧形文字
     */
    private static void drawArcFont4Oval(Graphics2D g2d, SealCircle sealCircle, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }
        float radiusX = sealCircle.getWidth();
        float radiusY = sealCircle.getHeight();
        float radiusWidth = radiusX + sealCircle.getLine();
        float radiusHeight = radiusY + sealCircle.getLine();

        //1.字体长度
        int textLen = font.getText().length();

        //2.字体大小，默认根据字体长度动态设定
        int size = font.getSize() == null ? 25 + (10 - textLen) / 2 : font.getSize();

        //3.字体样式
        int style = font.getBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFamily(), style, size);

        //5.总的角跨度
        double totalArcAng = font.getSpace() * textLen;

        //6.从边线向中心的移动因子
        float minRat = 0.90f;

        double startAngle = isTop ? -90f - totalArcAng / 2f : 90f - totalArcAng / 2f;
        double step = 0.5;
        int alCount = (int) Math.ceil(totalArcAng / step) + 1;
        double[] angleArr = new double[alCount];
        double[] arcLenArr = new double[alCount];
        int num = 0;
        double accArcLen = 0.0;
        angleArr[num] = startAngle;
        arcLenArr[num] = accArcLen;
        num++;
        double angR = startAngle * Math.PI / 180.0;
        double lastX = radiusX * Math.cos(angR) + radiusWidth;
        double lastY = radiusY * Math.sin(angR) + radiusHeight;
        for (double i = startAngle + step; num < alCount; i += step) {
            angR = i * Math.PI / 180.0;
            double x = radiusX * Math.cos(angR) + radiusWidth, y = radiusY * Math.sin(angR) + radiusHeight;
            accArcLen += Math.sqrt((lastX - x) * (lastX - x) + (lastY - y) * (lastY - y));
            angleArr[num] = i;
            arcLenArr[num] = accArcLen;
            lastX = x;
            lastY = y;
            num++;
        }
        double arcPer = accArcLen / textLen;
        for (int i = 0; i < textLen; i++) {
            double arcL = i * arcPer + arcPer / 2.0;
            double ang = 0.0;
            for (int p = 0; p < arcLenArr.length - 1; p++) {
                if (arcLenArr[p] <= arcL && arcL <= arcLenArr[p + 1]) {
                    ang = (arcL >= ((arcLenArr[p] + arcLenArr[p + 1]) / 2.0)) ? angleArr[p + 1] : angleArr[p];
                    break;
                }
            }
            angR = (ang * Math.PI / 180f);
            Float x = radiusX * (float) Math.cos(angR) + radiusWidth;
            Float y = radiusY * (float) Math.sin(angR) + radiusHeight;
            double qxang = Math.atan2(radiusY * Math.cos(angR), -radiusX * Math.sin(angR));
            double fxang = qxang + Math.PI / 2.0;

            int subIndex = isTop ? i : textLen - 1 - i;
            String c = font.getText().substring(subIndex, subIndex + 1);

            //获取文字高宽
//            FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
            FontMetrics fm = g2d.getFontMetrics();
            int w = fm.stringWidth(c), h = fm.getHeight();

            if (isTop) {
                x += h * minRat * (float) Math.cos(fxang);
                y += h * minRat * (float) Math.sin(fxang);
                x += -w / 2f * (float) Math.cos(qxang);
                y += -w / 2f * (float) Math.sin(qxang);
            } else {
                x += (h * minRat) * (float) Math.cos(fxang);
                y += (h * minRat) * (float) Math.sin(fxang);
                x += w / 2f * (float) Math.cos(qxang);
                y += w / 2f * (float) Math.sin(qxang);
            }

            // 旋转
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(0.8, 1);
            if (isTop)
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
            else
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI + 180 - 90)), 0, 0);
            Font f2 = f.deriveFont(affineTransform);
            g2d.setFont(f2);
            g2d.drawString(c, x.intValue() + INIT_BEGIN, y.intValue() + INIT_BEGIN);
        }
    }

    /**
     * 画文字
     */
    private static void drawFont(Graphics2D g2d, int circleWidth, int circleHeight, SealFont font) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int textLen = font.getText().length();

        //2.字体大小，默认根据字体长度动态设定
        int size = font.getSize() == null ? (55 - textLen * 2) : font.getSize();

        //3.字体样式
        int style = font.getBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFamily(), style, size);
        g2d.setFont(f);

        FontRenderContext context = g2d.getFontRenderContext();
        String[] fontTexts = font.getText().split("\n");
        if (fontTexts.length > 1) {
            int y = 0;
            for (String fontText : fontTexts) {
                y += Math.abs(f.getStringBounds(fontText, context).getHeight());
            }
            //5.设置上边距
            float margin = INIT_BEGIN + (float) (circleHeight / 2 - y / 2);
            for (String fontText : fontTexts) {
                Rectangle2D rectangle2D = f.getStringBounds(fontText, context);
                g2d.drawString(fontText, (float) (circleWidth / 2 - rectangle2D.getCenterX()), margin);
                margin += Math.abs(rectangle2D.getHeight());
            }
        } else {
            Rectangle2D rectangle2D = f.getStringBounds(font.getText(), context);
            //5.设置上边距，默认在中心
            float margin = font.getMargin() == null ?
                    (float) (circleHeight / 2 - rectangle2D.getCenterY()) :
                    (float) (circleHeight / 2 - rectangle2D.getCenterY()) + (float) font.getMargin();
            g2d.drawString(font.getText(), (float) (circleWidth / 2 - rectangle2D.getCenterX()), margin);
        }
    }

    /**
     * 画圆
     */
    private static void drawCircle(Graphics2D g2d, SealCircle circle, int x, int y) {
        if (circle == null) {
            return;
        }

        //1.圆线条粗细默认是圆直径的1/35
        int lineSize = circle.getLine() == null ? circle.getHeight() * 2 / (35) : circle.getLine();

        //2.画圆
        g2d.setStroke(new BasicStroke(lineSize));
        g2d.drawOval(x, y, circle.getWidth() * 2, circle.getHeight() * 2);
    }

    /**
     * 画私章
     */
    public boolean draw2(String pngPath) throws Exception {
        if (mainFont == null || mainFont.getText().length() < 2 || mainFont.getText().length() > 4) {
            throw new IllegalArgumentException("请输入2-4个字");
        }

        int fixH = 18;
        int fixW = 2;

        //1.画布
        BufferedImage bi = new BufferedImage(size, size / 2, BufferedImage.TYPE_4BYTE_ABGR);// 这种是背景色马赛克的
        //2.画笔
        Graphics2D g2d = bi.createGraphics();

        //2.1设置画笔颜色
        g2d.setPaint(Color.RED);

        // 背景色，要搭配clearRect才可以设置背景生效
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, size, size / 2);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形,

        //2.2抗锯齿设置
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //3.写签名
        int marginW = fixW + borderSquare;
        float marginH;
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D rectangle;
        Font f;

        if (mainFont.getText().length() == 2) {
            if (stamp != null && stamp.trim().length() > 0) {
                bi = drawThreeFont(bi, g2d, mainFont.append(stamp), borderSquare, size, fixH, fixW, true);
            } else {
                f = new Font(mainFont.getFamily(), Font.BOLD, mainFont.getSize());
                g2d.setFont(f);
                rectangle = f.getStringBounds(mainFont.getText().substring(0, 1), context);
                marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH - 4;
                g2d.drawString(mainFont.getText().substring(0, 1), marginW, marginH);
                marginW += Math.abs(rectangle.getCenterX()) * 2 + (mainFont.getSpace() == null ? INIT_BEGIN : mainFont.getSpace());
                g2d.drawString(mainFont.getText().substring(1), marginW, marginH);

                //拉伸
                BufferedImage nbi = new BufferedImage(size, size, bi.getType());
                Graphics2D ng2d = nbi.createGraphics();
                ng2d.setPaint(Color.RED);
                ng2d.drawImage(bi, 0, 0, size, size, null);
//                ng2d.setBackground(Color.WHITE);

                //画正方形
                ng2d.setStroke(new BasicStroke(borderSquare));
                ng2d.drawRect(0, 0, size, size);
                ng2d.dispose();
                bi = nbi;
            }
        } else if (mainFont.getText().length() == 3) {
            if (stamp != null && stamp.trim().length() > 0) {
                bi = drawFourFont(bi, mainFont.append(stamp), borderSquare, size, fixH, fixW);
            } else {
                bi = drawThreeFont(bi, g2d, mainFont, borderSquare, size, fixH, fixW, false);
            }
        } else {
            bi = drawFourFont(bi, mainFont, borderSquare, size, fixH, fixW);
        }

        g2d.dispose();

        return ImageIO.write(bi, "PNG", new File(pngPath));
    }

    /**
     * 画三字
     */
    private static BufferedImage drawThreeFont(BufferedImage bi, Graphics2D g2d, SealFont font, int lineSize, int imageSize, int fixH, int fixW, boolean isWithYin) {
        fixH -= 9;
        int marginW = fixW + lineSize;
        //设置字体
        Font f = new Font(font.getFamily(), Font.BOLD, font.getSize());
        g2d.setFont(f);
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D rectangle = f.getStringBounds(font.getText().substring(0, 1), context);
        float marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH;
        int oldW = marginW;

        if (isWithYin) {
            g2d.drawString(font.getText().substring(2, 3), marginW, marginH);
            marginW += rectangle.getCenterX() * 2 + (font.getSpace() == null ? INIT_BEGIN : font.getSpace());
        } else {
            marginW += rectangle.getCenterX() * 2 + (font.getSpace() == null ? INIT_BEGIN : font.getSpace());
            g2d.drawString(font.getText().substring(0, 1), marginW, marginH);
        }

        //拉伸
        BufferedImage nbi = new BufferedImage(imageSize, imageSize, bi.getType());
        Graphics2D ng2d = nbi.createGraphics();
        ng2d.setPaint(Color.RED);
        ng2d.drawImage(bi, 0, 0, imageSize, imageSize, null);

        //画正方形
        ng2d.setStroke(new BasicStroke(lineSize));
        ng2d.drawRect(0, 0, imageSize, imageSize);
        ng2d.dispose();
        bi = nbi;

        g2d = bi.createGraphics();
        g2d.setPaint(Color.RED);
        g2d.setFont(f);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isWithYin) {
            g2d.drawString(font.getText().substring(0, 1), marginW, marginH += fixH);
            rectangle = f.getStringBounds(font.getText(), context);
            marginH += Math.abs(rectangle.getHeight());
            g2d.drawString(font.getText().substring(1), marginW, marginH);
        } else {
            g2d.drawString(font.getText().substring(1, 2), oldW, marginH += fixH);
            rectangle = f.getStringBounds(font.getText(), context);
            marginH += Math.abs(rectangle.getHeight());
            g2d.drawString(font.getText().substring(2, 3), oldW, marginH);
        }
        return bi;
    }

    /**
     * 画四字
     */
    private static BufferedImage drawFourFont(BufferedImage bi, SealFont font, int lineSize, int imageSize, int fixH, int fixW) {
        int marginW = fixW + lineSize;
        //拉伸
        BufferedImage nbi = new BufferedImage(imageSize, imageSize, bi.getType());
        Graphics2D ng2d = nbi.createGraphics();
        ng2d.setPaint(Color.RED);

        ng2d.drawImage(bi, 0, 0, imageSize, imageSize, null);

        //画正方形
        ng2d.setStroke(new BasicStroke(lineSize));
        ng2d.drawRect(0, 0, imageSize, imageSize);
        ng2d.dispose();
        bi = nbi;

        Graphics2D g2d = bi.createGraphics();
        g2d.setPaint(Color.RED);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext context = g2d.getFontRenderContext();

        Font f = new Font(font.getFamily(), Font.BOLD, font.getSize());
        g2d.setFont(f);
        Rectangle2D rectangle = f.getStringBounds(font.getText().substring(0, 1), context);
        float marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH;

        g2d.drawString(font.getText().substring(2, 3), marginW, marginH);
        int oldW = marginW;
        marginW += Math.abs(rectangle.getCenterX()) * 2 + (font.getSpace() == null ? INIT_BEGIN : font.getSpace());

        g2d.drawString(font.getText().substring(0, 1), marginW, marginH);
        marginH += Math.abs(rectangle.getHeight());

        g2d.drawString(font.getText().substring(3, 4), oldW, marginH);

        g2d.drawString(font.getText().substring(1, 2), marginW, marginH);

        return bi;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public SealFont getMainFont() {
        return mainFont;
    }

    public void setMainFont(SealFont mainFont) {
        this.mainFont = mainFont;
    }

    public SealFont getViceFont() {
        return viceFont;
    }

    public void setViceFont(SealFont viceFont) {
        this.viceFont = viceFont;
    }

    public SealFont getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(SealFont titleFont) {
        this.titleFont = titleFont;
    }

    public SealFont getCenterFont() {
        return centerFont;
    }

    public void setCenterFont(SealFont centerFont) {
        this.centerFont = centerFont;
    }

    public SealCircle getBorderCircle() {
        return borderCircle;
    }

    public void setBorderCircle(SealCircle borderCircle) {
        this.borderCircle = borderCircle;
    }

    public SealCircle getBorderInnerCircle() {
        return borderInnerCircle;
    }

    public void setBorderInnerCircle(SealCircle borderInnerCircle) {
        this.borderInnerCircle = borderInnerCircle;
    }

    public SealCircle getInnerCircle() {
        return innerCircle;
    }

    public void setInnerCircle(SealCircle innerCircle) {
        this.innerCircle = innerCircle;
    }

    public Integer getBorderSquare() {
        return borderSquare;
    }

    public void setBorderSquare(Integer borderSquare) {
        this.borderSquare = borderSquare;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }
}
