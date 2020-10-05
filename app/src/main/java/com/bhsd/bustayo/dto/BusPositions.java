package com.bhsd.bustayo.dto;

public class BusPositions {

    private String plainNo;
    private double posX;
    private double posY;

    //수정
    private String nextStdId;

   public BusPositions(String plainNo, String posX, String posY, String nextStdId) {
        this.plainNo = plainNo;
        this.posX = Double.parseDouble(posX);
        this.posY = Double.parseDouble(posY);

        //수정
       this.nextStdId = nextStdId;
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

    //수정

    public String getNextStdId() {
        return nextStdId;
    }

    public void setNextStdId(String nextStdId) {
        this.nextStdId = nextStdId;
    }
}
