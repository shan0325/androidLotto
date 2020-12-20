package com.shan.mylotto.util;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupTaskUtil extends AsyncTask<String, Integer, Elements> {

    @Override
    protected Elements doInBackground(String... strings) {
        Elements elements = null;

        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url).get();
            elements = doc.select(strings[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return elements;
    }

}
