package com.shan.mylotto.lotto.service.impl;

import com.shan.mylotto.R;
import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.service.LottoService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LottoServiceImpl implements LottoService {

    public static final int LOTTO_NUMBERS_LENGTH = 6;

    @Override
    public void makeLottoNumbers(Lotto lotto) {
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
        lotto.setNumbers(numbers);
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

    public int getLottoRandomNumber() {
        return (int) (Math.random() * 45) + 1;
    }
}
