package com.bhsd.bustayo.dto;

public class LostGoodsInfo{

    private String lgImage, lgnNum, lgTitle, lgPlace, lgDate;

    public LostGoodsInfo(String lgImage, String lgnNum, String lgTitle, String lgPlace, String lgDate) {
        this.lgImage = lgImage;
        this.lgnNum = lgnNum;
        this.lgTitle = lgTitle;
        this.lgPlace = lgPlace;
        this.lgDate = lgDate;
    }

    public String getLgImage() {
        return lgImage;
    }

    public void setLgImage(String lgImage) {
        this.lgImage = lgImage;
    }

    public String getLgNum() {
        return lgnNum;
    }

    public void setLgNml(String lgnNml) {
        this.lgnNum = lgnNml;
    }

    public String getLgTitle() {
        return lgTitle;
    }

    public void setLgTitle(String lgTitle) {
        this.lgTitle = lgTitle;
    }

    public String getLgPlace() {
        return lgPlace;
    }

    public void setLgPlace(String lgPlace) {
        this.lgPlace = lgPlace;
    }

    public String getLgDate() {
        return lgDate;
    }

    public void setLgDate(String lgDate) {
        this.lgDate = lgDate;
    }
}
