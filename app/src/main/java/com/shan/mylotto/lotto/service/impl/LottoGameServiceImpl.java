package com.shan.mylotto.lotto.service.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.lotto.service.LottoGameService;
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.util.DateUtil;
import com.shan.mylotto.util.FileUtil;
import com.shan.mylotto.util.JsoupTaskUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LottoGameServiceImpl implements LottoGameService {

    private LottoService lottoService;

    public LottoGameServiceImpl(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @Override
    public LottoGame makeLottoGame(int length) {
        LottoGame lottoGame = new LottoGame();
        List<Lotto> lottos = new ArrayList<>();

        for(int i = 0; i < length; i++) {
            Lotto lotto = new Lotto();
            lotto.setId(i);
            lotto.setMakeDate(DateUtil.formatByDate(new Date()));
            lottoService.makeLottoNumbers(lotto);

            lottos.add(lotto);
        }
        lottoGame.setLottos(lottos);
        lottoGame.setMakeDate(DateUtil.formatByDate(new Date()));

        return lottoGame;
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

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int saveFileByLottoGame(Context mBase, LottoGame lottoGame) {
        int result = 1;

        List<LottoGame> jsonList = FileUtil.getJsonObjByJsonFile(mBase);
        if(lottoGame != null) {
            int id = getLottoGameId(mBase, jsonList);
            lottoGame.setId(id);
            lottoGame.setSaveDate(DateUtil.formatByDate(new Date()));

            List<Lotto> lottos = lottoGame.getLottos();
            for (Lotto lotto : lottos) {
                lotto.setLottoGameId(id);
            }
            jsonList.add(lottoGame);

            return FileUtil.writeJsonFile(mBase, jsonList);
        }

        return result;
    }

    @Override
    public List<LottoGame> findAll(Context mBase) {
        return FileUtil.getJsonObjByJsonFile(mBase);
    }

    @Override
    public List<Integer> findLottoGameRounds(Context mBase) {
        List<Integer> rounds = new ArrayList<>();
        List<LottoGame> list = FileUtil.getJsonObjByJsonFile(mBase);
        for(int i = 0; i < list.size(); i++) {
            LottoGame lottoGame = list.get(i);
            Integer round = lottoGame.getRound();
            if(round != null) {
                if(!rounds.contains(round)) {
                    rounds.add(round);
                }
            }
        }

        if(rounds.size() > 0) {
            Collections.sort(rounds, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);
                }
            });
        }
        return rounds;
    }

    @Override
    public List<LottoGame> findByRound(Context mBase, Integer round) {
        List<LottoGame> resultList = new ArrayList<>();
        List<LottoGame> list = FileUtil.getJsonObjByJsonFile(mBase);
        for(int i = 0; i < list.size(); i++) {
            LottoGame lottoGame = list.get(i);
            if(round.equals(lottoGame.getRound())) {
                resultList.add(lottoGame);
            }
        }
        return resultList;
    }

    // 로또 결과 api로 가져오기
    @Override
    public Map<String, String> getLottoResultByDrwNo(int drwNo) {
        String urlStr = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + drwNo;
        System.out.println("===============================");
        System.out.println(urlStr);
        System.out.println("===============================");

        AsyncTask<String, Integer, Map<String, String>> lottoResultApi = new AsyncTask<String, Integer, Map<String, String>>() {
            @Override
            protected Map<String, String> doInBackground(String... params) {
                Map<String, String> resultMap = new HashMap<>();
                BufferedReader reader = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(10000); //10초 기다림
                        conn.setRequestMethod("GET");

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            StringBuffer resultJsonStr = new StringBuffer();
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                resultJsonStr.append(line);
                            }
                            System.out.println("==========================");
                            System.out.println(resultJsonStr.toString());
                            System.out.println("==========================");

                            ObjectMapper objectMapper = new ObjectMapper();
                            resultMap = objectMapper.readValue(resultJsonStr.toString(), Map.class);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return resultMap;
            }
        };

        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = lottoResultApi.execute(urlStr).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public int getLottoGameId(Context mBase, List<LottoGame> jsonList) {
        int id = 0;
        if(jsonList != null && jsonList.size() > 0) {
            Collections.sort(jsonList, new Comparator<LottoGame>() {
                @Override
                public int compare(LottoGame o1, LottoGame o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });

            LottoGame maxLottoGame = jsonList.get(0);
            Integer maxId = maxLottoGame.getId();
            if(maxId == null) maxId = 0;

            id = maxId + 1;
        }
        return id;
    }


}
