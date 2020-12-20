package com.shan.mylotto.lotto.service;

import android.content.Context;

import com.shan.mylotto.lotto.domain.LottoGame;

import java.util.List;

public interface LottoGameService {

    public LottoGame makeLottoGame(int length);

    public String getLottoRoundByDhlottery();

    public int saveFileByLottoGame(Context mBase, LottoGame lottoGame);

    public List<LottoGame> findAll(Context mBase);

    public List<Integer> findLottoGameRounds(Context mBase);

    public List<LottoGame> findByRound(Context mBase, Integer round);
}
