package org.javaboy.demo.utils;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class SealFont {
    private String text;
    private String family;
    private Integer size;
    private Boolean bold;
    private Double space;
    private Integer margin;

    public String getFamily() {
        return family == null ? "宋体" : family;
    }

    public boolean getBold() {
        return bold == null ? true : bold;
    }

    public SealFont append(String text) {
        this.text += text;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Double getSpace() {
        return space;
    }

    public void setSpace(Double space) {
        this.space = space;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }
}