package com.shan.mylotto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SqlTemplate extends LottoDBHelper {
    private SQLiteDatabase sqlDb;

    public SqlTemplate(Context context) {
        super(context);
        sqlDb = getWritableDatabase();
    }

    public void insert(String query) {
        sqlDb.execSQL(query);
    }

    public long insert(String tableName, ContentValues contentValues) {
        return sqlDb.insert(tableName, null, contentValues);
    }

    public void update(String query) {
        sqlDb.execSQL(query);
    }

    public void delete(String query) {
        sqlDb.execSQL(query);
    }

    public <T> List<T> select(String query, RowMapper<T> rowMapper) {
        Cursor cursor = sqlDb.rawQuery(query, null);

        List<T> list = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                list.add(rowMapper.mapRow(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }


}
