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
import com.shan.mylotto.util.OpenApiUtil;

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




}
