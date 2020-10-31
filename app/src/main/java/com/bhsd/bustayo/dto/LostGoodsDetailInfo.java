package com.bhsd.bustayo.dto;

import java.io.Serializable;

public class LostGoodsDetailInfo implements Serializable {
    private String lgImage, lgnNum, lgTitle, lgPlace, lgTime,
            lgCategory, lgState, lgJurisdiction, lgContact;

    public LostGoodsDetailInfo(String lgImage, String lgnNum, String lgTitle, String lgPlace, String lgTime,
                               String lgCategory, String lgState, String lgJurisdiction, String lgContact) {
        this.lgImage = lgImage;                 // 이미지 주소
        this.lgnNum = lgnNum;                   // 관리번호
        this.lgTitle = lgTitle;                 // 제목
        this.lgPlace = lgPlace;                 // 장소
        this.lgTime = lgTime;                   // 분실일(날짜 + 시간)
        this.lgCategory = lgCategory;           // 분류
        this.lgState = lgState;                 // 상태
        this.lgJurisdiction = lgJurisdiction;   // 관할관서
        this.lgContact = lgContact;             // 연락처
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

    public String getLgTime() {
        return lgTime;
    }

    public void setLgTime(String lgTime) {
        this.lgTime = lgTime;
    }

    public String getLgCategory() { return lgCategory; }

    public void setLgCategory(String lgCategory) { this.lgCategory = lgCategory; }

    public String getLgState() { return lgState; }

    public void setLgState(String lgState) { this.lgState = lgState; }

    public String getLgJurisdiction() { return lgJurisdiction; }

    public void setLgJurisdiction(String lgJurisdiction) { this.lgJurisdiction = lgJurisdiction; }

    public String getLgContact() { return lgContact; }

    public void setLgContact(String lgContact) { this.lgContact = lgContact; }
}
