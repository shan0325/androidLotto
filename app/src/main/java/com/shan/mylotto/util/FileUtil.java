package com.shan.mylotto.util;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileUtil {

    private static final String FILE_NAME = "lottos.json";

    public static List<LottoGame> getJsonObjByJsonFile(Context mBase) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LottoGame> list = new ArrayList<LottoGame>();

        File distFile = new File(mBase.getFilesDir() + File.separator + FILE_NAME);
        if(!distFile.exists()) {
            return list;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(mBase.openFileInput(FILE_NAME)));
            StringBuffer jsonStr = new StringBuffer();
            String temp = "";
            while ((temp = br.readLine()) != null) {
                jsonStr.append(temp);
            }
            System.out.println("jsonStr : " + jsonStr.toString());

            if(!"".equals(jsonStr.toString())) {
                list = objectMapper.readValue(jsonStr.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, LottoGame.class));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try { br.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }

        return list;
    }

    public static int writeJsonFile(Context mBase, List<LottoGame> jsonList) {
        int result = 1;
        ObjectMapper objectMapper = new ObjectMapper();

        BufferedOutputStream bos = null;
        try {
            String jsonStr = objectMapper.writeValueAsString(jsonList);
            System.out.println("output jsonStr : " + jsonStr);

            bos = new BufferedOutputStream(mBase.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            bos.write(jsonStr.getBytes());
            result = 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(bos != null) {
                try { bos.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }

        return result;
    }
}
