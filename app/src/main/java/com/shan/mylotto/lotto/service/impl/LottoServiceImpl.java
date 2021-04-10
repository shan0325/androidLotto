package com.shan.mylotto.lotto.service.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shan.mylotto.R;
import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.lotto.service.dao.LottoDAO;
import com.shan.mylotto.lotto.service.dao.LottoGameDAO;
import com.shan.mylotto.util.DateUtil;
import com.shan.mylotto.util.JsoupTaskUtil;
import com.shan.mylotto.util.OpenApiUtil;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LottoServiceImpl implements LottoService {

    public static final int LOTTO_NUMBERS_LENGTH = 6;

    private LottoDAO lottoDAO;
    private LottoGameDAO lottoGameDAO;

    public LottoServiceImpl(Context context) {
        this.lottoDAO = new LottoDAO(context);
        this.lottoGameDAO = new LottoGameDAO(context);
    }

    @Override
    public List<Lotto> makeLotto(int length) {
        List<Lotto> lottos = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            lottos.add(makeLotto());
        }
        return lottos;
    }

    /**
     * 로또 번호 만들기
     */
    @Override
    public Lotto makeLotto() {
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < LOTTO_NUMBERS_LENGTH; i++) {
            numbers.add(getLottoRandomNumber());
            for (int j = 0; j < i; j++) {
                if(numbers.get(i) == numbers.get(j)) {
                    numbers.remove(i);
                    i--;
                    break;
                }
            }
        }
        Collections.sort(numbers);

        Lotto lotto = new Lotto();
        lotto.setNumOne(numbers.get(0));
        lotto.setNumTwo(numbers.get(1));
        lotto.setNumThree(numbers.get(2));
        lotto.setNumFour(numbers.get(3));
        lotto.setNumFive(numbers.get(4));
        lotto.setNumSix(numbers.get(5));
        lotto.setMakeDate(DateUtil.formatByDate(new Date()));
        return lotto;
    }

    @Override
    public int getLottoColor(int lottoNum) {
        int lottoColor = 0;
        if(lottoNum < 10) {
            lottoColor = R.drawable.lotto_1;
        } else if(lottoNum < 20) {
            lottoColor = R.drawable.lotto_2;
        } else if(lottoNum < 30) {
            lottoColor = R.drawable.lotto_3;
        } else if(lottoNum < 40) {
            lottoColor = R.drawable.lotto_4;
        } else {
            lottoColor = R.drawable.lotto_5;
        }
        return lottoColor;
    }

    @Override
    public int insertLottoList(int round, List<Lotto> lottoList) {
        if(lottoList != null && lottoList.size() > 0) {
            LottoGame lottoGame = new LottoGame();
            lottoGame.setRound(round);
            int lottoGameId = lottoGameDAO.insertLottoGame(lottoGame);

            for (int i = 0; i < lottoList.size(); i++) {
                Lotto lotto = lottoList.get(i);
                lotto.setLottoGameId(lottoGameId);
                lotto.setRound(round);
                lottoDAO.insertLotto(lotto);
            }
        }
        return 0;
    }

    @Override
    public String getLottoRoundByDhlottery() {
        String result = "";

        JsoupTaskUtil jsoupTaskUtil = new JsoupTaskUtil();
        try {

            Elements elements = jsoupTaskUtil.execute("https://dhlottery.co.kr/common.do?method=main", "#lottoDrwNo").get();
            if(elements != null) {
                result = String.valueOf(Integer.parseInt(elements.text()) + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 로또 결과 api로 가져오기
    @Override
    public Map<String, Object> getLottoResultByDrwNo(int drwNo) {
        // 로또 결과 url
        String urlStr = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + drwNo;
        System.out.println("===============================");
        System.out.println(urlStr);
        System.out.println("===============================");

        AsyncTask<String, Integer, Map<String, Object>> lottoResultApi = new AsyncTask<String, Integer, Map<String, Object>>() {
            @Override
            protected Map<String, Object> doInBackground(String... params) {
                Map<String, Object> resultMap = new HashMap<>();
                String resultJsonStr = OpenApiUtil.getRestApiByGetMethod(params[0]);
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    if(resultJsonStr != null && !resultJsonStr.isEmpty()) {
                        resultMap = objectMapper.readValue(resultJsonStr, Map.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resultMap;
            }
        };

        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = lottoResultApi.execute(urlStr).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public List<Integer> findLottoRounds() {
        return lottoDAO.findLottoRounds();
    }

    @Override
    public List<LottoGame> findSavedLottoByRound(int round) {
        List<LottoGame> lottoGameList = lottoGameDAO.findSavedLottoGameByRound(round);
        System.out.println("=======================================");
        System.out.println(lottoGameList);
        System.out.println("=======================================");

        if(lottoGameList != null && lottoGameList.size() > 0) {
            for (int i = 0 ; i < lottoGameList.size(); i++) {
                LottoGame lottoGame = lottoGameList.get(i);
                lottoGame.setLottos(lottoDAO.findSavedLottoByLottoGameId(lottoGame.getId()));
            }
        }
        return lottoGameList;
    }

    /**
     * 로또 번호 랜덤 생성
     * @return
     */
    public int getLottoRandomNumber() {
        return (int) (Math.random() * 45) + 1;
    }

}
