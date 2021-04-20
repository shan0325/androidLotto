package com.shan.mylotto.lotto.service.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.shan.mylotto.db.RowMapper;
import com.shan.mylotto.db.SqlTemplate;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.util.DateUtil;

import java.util.Date;
import java.util.List;

public class LottoGameDAO {

    private SqlTemplate sqlTemplate;

    public LottoGameDAO(Context context) {
        sqlTemplate = new SqlTemplate(context);
    }

    public int insertLottoGame(LottoGame lottoGame) {
        ContentValues values = new ContentValues();
        values.put("round", lottoGame.getRound());
        values.put("reg_date", DateUtil.formatByDate(new Date()));
        return (int) sqlTemplate.insert("lotto_game", values);
    }

    public List<LottoGame> findSavedLottoGameByRound(int round) {
        return sqlTemplate.select("SELECT id, round, reg_date FROM lotto_game WHERE round = " + round + " ORDER BY id desc", new RowMapper<LottoGame>() {
            @Override
            public LottoGame mapRow(Cursor cursor) {
                LottoGame lottoGame = new LottoGame();
                lottoGame.setId(cursor.getInt(0));
                lottoGame.setRound(cursor.getInt(1));
                lottoGame.setRegDate(cursor.getString(2));
                return lottoGame;
            }
        });
    }

    public List<Integer> findLottoRounds() {
        return sqlTemplate.select("SELECT DISTINCT round FROM lotto_game ORDER BY round DESC", new RowMapper<Integer>() {
            @Override
            public Integer mapRow(Cursor cursor) {
                return cursor.getInt(0);
            }
        });
    }

    public void deleteLottoGameById(int id) {
        sqlTemplate.delete("DELETE FROM lotto_game WHERE id = " + id);
    }
}
