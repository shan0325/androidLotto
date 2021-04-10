package com.shan.mylotto.lotto.service;

import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;

import java.util.List;
import java.util.Map;

public interface LottoService {

    public List<Lotto> makeLotto(int length);

    public Lotto makeLotto();

    public int getLottoColor(int lottoNum);

    public int insertLottoList(int round, List<Lotto> lottoList);

    public String getLottoRoundByDhlottery();

    public Map<String, Object> getLottoResultByDrwNo(int drwNo);

    public List<Integer> findLottoRounds();

    public List<LottoGame> findSavedLottoByRound(int round);

    public void deleteLottoGame(int lottoGameId);
}
