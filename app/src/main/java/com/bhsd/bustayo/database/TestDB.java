package com.bhsd.bustayo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

public class TestDB extends SQLiteOpenHelper {
        public TestDB(Context context) {
            super(context, "BustayoDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*
             * 버스 검색 히스토리 테이블
             * busRouteId   : 버스 노선 ID
             * busRouteNm   : 버스 노선 번호
             * routeType    : 지선/간선 등 버스타입
             * stStationNm  : 기점
             * edStationNm  : 종점
             * timestamp    : 등록시간
             */
            db.execSQL("CREATE TABLE busHistoryTB(" +
                    "busRouteId VARCHAR(9) PRIMARY KEY ON CONFLICT REPLACE, " +
                    "busRouteNm VARCHAR(30), " +
                    "routeType VARCHAR(1), " +
                    "stStationNm VARCHAR(60), " +
                    "edStationNm VARCHAR(60), " +
                    "timestamp TIMESTAMP)"
            );

            /*
             * 정류장 검색 히스토리 테이블
             * stId       : 정류장 ID
             * stNm       : 정류장명
             * arsId      : 정류장 고유번호
             * nxtStn     : 다음 정류장
             * timestamp  : 등록시간
             */
            db.execSQL("CREATE TABLE stationHistoryTB(" +
                    "stId VARCHAR(5) PRIMARY KEY ON CONFLICT REPLACE, " +
                    "stNm VARCHAR(60), " +
                    "arsId VARCHAR(5), " +
                    "nxtStn VARCHAR(60), " +
                    "timestamp TIMESTAMP)"
            );

            /*
             * 불편신고 접수 리스트 테이블
             * complaintId  : 접수 Id
             * busNum       : 버스번호
             * content      : 신고내용
             * year         : 연
             * month        : 월
             * date         : 일
             * hour         : 시
             * minute       : 분
             */
            db.execSQL("CREATE TABLE complaintsTB(" +
                    "complaintId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "busNum VARCHAR(30), " +
                    "content VARCHAR(140), " +
                    "year VARCHAR(4), " +
                    "month VARCHAR(2), " +
                    "date VARCHAR(2), " +
                    "hour VARCHAR(2), " +
                    "minute VARCHAR(2))"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS BustayoDB");
            onCreate(db);
        }
}
