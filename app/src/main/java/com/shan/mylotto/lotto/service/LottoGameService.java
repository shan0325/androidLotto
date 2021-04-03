package com.shan.mylotto.lotto.service;

import android.content.Context;

import com.shan.mylotto.lotto.domain.LottoGame;

import java.util.List;
import java.util.Map;

public interface LottoGameService {

    public LottoGame makeLottoGame(int length);

    public String getLottoRoundByDhlottery();

    public int saveFileByLottoGame(Context mBase, LottoGame lottoGame);

    public List<LottoGame> findAll(Context mBase);

    public List<Integer> findLottoGameRounds(Context mBase);

    public List<LottoGame> findByRound(Context mBase, Integer round);

    public Map<String, String> getLottoResultByDrwNo(int drwNo);
}
