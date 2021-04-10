package com.shan.mylotto.db;

import android.database.Cursor;

public interface RowMapper<T> {

    public T mapRow(Cursor cursor);

}
