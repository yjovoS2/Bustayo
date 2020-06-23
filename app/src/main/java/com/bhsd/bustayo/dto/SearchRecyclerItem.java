package com.bhsd.bustayo.dto;

////////////////////////////////////////////////////
// 검색 화면에서 리사이클러뷰의 한 행에 사용되는 아이템 클래스
//   - 버스와 정류장 각각의 검색 히스토리를 출력
//   - 검색 시 버스와 정류장에 대한 결과 리스트를 출력
////////////////////////////////////////////////////
public class SearchRecyclerItem {

    //////////////////////////////////////////////////////////////
    // 버스와 정류장이 동일한 아이템 항목을 가지고 있으므로
    // 버스, 정류장 각각에 대한 변수명 대신 공용 변수명 사용
    // 공　용          historyId        historyTitle      historySub1      historySub2
    // 버　스         버스노선 아이디      버스 번호           버스 타입          버스 방향
    // 정류장         정류장 아이디        정류장명         정류장 고유번호          ?
    //////////////////////////////////////////////////////////////
    private String  historyId, historyTitle, historySub1, historySub2;

    public SearchRecyclerItem(String historyId, String historyTitle, String historySub1, String historySub2) {
        this.historyId    = historyId;
        this.historyTitle = historyTitle;
        this.historySub1  = historySub1;
        this.historySub2  = historySub2;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public String getHistorySub1() {
        return historySub1;
    }

    public void setHistorySub1(String historySub1) {
        this.historySub1 = historySub1;
    }

    public String getHistorySub2() {
        return historySub2;
    }

    public void setHistorySub2(String historySub2) {
        this.historySub2 = historySub2;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
