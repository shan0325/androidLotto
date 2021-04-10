package com.shan.mylotto.lotto.service.dao;

import android.content.Context;
import android.database.Cursor;

import com.shan.mylotto.db.RowMapper;
import com.shan.mylotto.db.SqlTemplate;
import com.shan.mylotto.lotto.domain.Lotto;

import java.util.List;

public class LottoDAO {

    private SqlTemplate sqlTemplate;

    public LottoDAO(Context context) {
        sqlTemplate = new SqlTemplate(context);
    }

    public void insertLotto(Lotto lotto) {
        sqlTemplate.insert("INSERT INTO lotto (lotto_game_id, round, num_one, num_two, num_three, num_four, num_five, num_six, make_date, reg_date) " +
                                    "VALUES(" + lotto.getLottoGameId() +
                                            ", " + lotto.getRound() +
                                            ", " + lotto.getNumOne() +
                                            ", " + lotto.getNumTwo() +
                                            ", " + lotto.getNumThree() +
                                            ", " + lotto.getNumFour() +
                                            ", " + lotto.getNumFive() +
                                            ", " + lotto.getNumSix() +
                                            ", '" + lotto.getMakeDate() +
                                            "', DATETIME('now', 'localtime'))");
    }

    public List<Lotto> findSavedLottoByRound(int round) {
        return sqlTemplate.select("SELECT id, lotto_game_id, round, num_one, num_two, num_three, num_four, num_five, num_six, make_date, reg_date FROM lotto WHERE round = " + round + " ORDER BY id DESC", new RowMapper<Lotto>() {
            @Override
            public Lotto mapRow(Cursor cursor) {
                Lotto lotto = new Lotto();
                lotto.setId(cursor.getInt(0));
                lotto.setLottoGameId(cursor.getInt(1));
                lotto.setRound(cursor.getInt(2));
                lotto.setNumOne(cursor.getInt(3));
                lotto.setNumTwo(cursor.getInt(4));
                lotto.setNumThree(cursor.getInt(5));
                lotto.setNumFour(cursor.getInt(6));
                lotto.setNumFive(cursor.getInt(7));
                lotto.setNumSix(cursor.getInt(8));
                lotto.setMakeDate(cursor.getString(9));
                lotto.setRegDate(cursor.getString(10));
                return lotto;
            }
        });
    }

    public List<Lotto> findSavedLottoByLottoGameId(int lottoGameId) {
        return sqlTemplate.select("SELECT id, lotto_game_id, round, num_one, num_two, num_three, num_four, num_five, num_six, make_date, reg_date FROM lotto WHERE lotto_game_id = " + lottoGameId + " ORDER BY id DESC", new RowMapper<Lotto>() {
            @Override
            public Lotto mapRow(Cursor cursor) {
                Lotto lotto = new Lotto();
                lotto.setId(cursor.getInt(0));
                lotto.setLottoGameId(cursor.getInt(1));
                lotto.setRound(cursor.getInt(2));
                lotto.setNumOne(cursor.getInt(3));
                lotto.setNumTwo(cursor.getInt(4));
                lotto.setNumThree(cursor.getInt(5));
                lotto.setNumFour(cursor.getInt(6));
                lotto.setNumFive(cursor.getInt(7));
                lotto.setNumSix(cursor.getInt(8));
                lotto.setMakeDate(cursor.getString(9));
                lotto.setRegDate(cursor.getString(10));
                return lotto;
            }
        });
    }

    public void deleteLottoByLottoGameId(int lottoGameId) {
        sqlTemplate.delete("DELETE FROM lotto WHERE lotto_game_id = " + lottoGameId);
    }
}
