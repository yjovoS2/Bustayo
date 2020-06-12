package com.bhsd.bustayo;

public class StationListItem {
    private String stop_name;
    private int previous, next;

    public StationListItem(String stop_name, int previous, int next) {
        this.stop_name = stop_name;
        this.previous = previous;
        this.next = next;
    }

    void setStopName(String stop_name) {
        this.stop_name = stop_name;
    }

    String getStopName() {
        return stop_name;
    }

    void setPreviousColor(int colorID) {
        previous = colorID;
    }

    int getPreviousColor() {
        return previous;
    }

    void setNextColor(int colorID) {
        next = colorID;
    }

    int getNextColor() {
        return next;
    }
}