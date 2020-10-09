package com.bhsd.bustayo.dto;

public class BusPositions {

    private String plainNo;
    private double posX;
    private double posY;

    //수정
    private String sectionId;

   public BusPositions(String plainNo, String posX, String posY, String sectionId) {
        this.plainNo = plainNo;
        this.posX = Double.parseDouble(posX);
        this.posY = Double.parseDouble(posY);
       this.sectionId = sectionId;
    }

    public String getPlainNo() {
        return plainNo;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosy() {
        return posY;
    }

    public void setPlainNo(String plainNo) {
        this.plainNo = plainNo;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosy(double posY) {
        this.posY = posY;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
