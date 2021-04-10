package com.shan.mylotto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LottoDBHelper extends SQLiteOpenHelper {

    // 나중에 데이터베이스를 변경하려면 버전을 증가시키면 됩니다.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lotto.db";

    public LottoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL("CREATE TABLE lotto (id INTEGER PRIMARY KEY AUTOINCREMENT, round INTEGER, num_one INTEGER, num_two INTEGER, num_three INTEGER, num_four INTEGER, num_five INTEGER, num_six INTEGER, make_date TEXT, save_date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 버전이 증가하면 해당 테이블을 삭제하고 다시 생성합니다.
        db.execSQL("DROP TABLE IF EXISTS lotto");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
