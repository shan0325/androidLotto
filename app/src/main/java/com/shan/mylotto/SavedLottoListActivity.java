package com.shan.mylotto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.lotto.service.LottoGameService;
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.lotto.service.impl.LottoGameServiceImpl;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.shan.mylotto.R.id.round;

public class SavedLottoListActivity extends AppCompatActivity {

    private LottoService lottoService;
    private LottoGameService lottoGameService;
    private TableLayout listTableLayout;
    private Toolbar myToolbar;
    private TextView resultText;
    private LinearLayout resultLayout;
    private Button resultOneNumber;
    private Button resultTwoNumber;
    private Button resultThreeNumber;
    private Button resultFourNumber;
    private Button resultFiveNumber;
    private Button resultBonusNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lotto_list);

        init();
    }

    @SuppressLint("WrongViewCast")
    public void init() {
        this.lottoService = new LottoServiceImpl();
        this.lottoGameService = new LottoGameServiceImpl(this.lottoService);

        this.listTableLayout = findViewById(R.id.listTableLayout);
        this.myToolbar = findViewById(R.id.myToolbar);
        this.resultText = findViewById(R.id.resultText);
        this.resultLayout = findViewById(R.id.resultLayout);
        this.resultOneNumber = findViewById(R.id.resultOneNumber);
        this.resultTwoNumber = findViewById(R.id.resultTwoNumber);
        this.resultThreeNumber = findViewById(R.id.resultThreeNumber);
        this.resultFourNumber = findViewById(R.id.resultFourNumber);
        this.resultFiveNumber = findViewById(R.id.resultFiveNumber);
        this.resultBonusNumber = findViewById(R.id.resultBonusNumber);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        displayRoundsSpinner();
    }

    private void displayRoundsSpinner() {
        final List<Integer> rounds = lottoGameService.findLottoGameRounds(getBaseContext());
        System.out.println("===============================");
        System.out.println(rounds);
        System.out.println("===============================");

        Spinner spinner = (Spinner) findViewById(R.id.roundsSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rounds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> resultLottoMap = displayResultLottoNumber(rounds.get(position));
                displaySavedLottoList(rounds.get(position), resultLottoMap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // 당첨결과 로또번호 출력
    private Map<String, Object> displayResultLottoNumber(Integer round) {
        Map<String, Object> resultLottoMap = lottoGameService.getLottoResultByDrwNo(round);
        String returnValue = (String) resultLottoMap.get("returnValue");
        if(returnValue != null && "success".equals(returnValue)) {
            this.resultLayout.setVisibility(View.VISIBLE);
            this.resultText.setText("당첨번호");

            this.resultOneNumber.setText(String.valueOf(resultLottoMap.get("drwtNo1")));
            this.resultOneNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo1"))));

            this.resultTwoNumber.setText(String.valueOf(resultLottoMap.get("drwtNo2")));
            this.resultTwoNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo2"))));

            this.resultThreeNumber.setText(String.valueOf(resultLottoMap.get("drwtNo3")));
            this.resultThreeNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo3"))));

            this.resultFourNumber.setText(String.valueOf(resultLottoMap.get("drwtNo4")));
            this.resultFourNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo4"))));

            this.resultFiveNumber.setText(String.valueOf(resultLottoMap.get("drwtNo5")));
            this.resultFiveNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo5"))));

            this.resultBonusNumber.setText(String.valueOf(resultLottoMap.get("bnusNo")));
            this.resultBonusNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("bnusNo"))));
        } else {
            this.resultLayout.setVisibility(View.INVISIBLE);
            this.resultText.setText("당첨번호 결과가 없습니다.");
        }
        return resultLottoMap;
    }

    @SuppressLint("WrongViewCast")
    private void displaySavedLottoList(Integer round, Map<String, Object> resultLottoMap) {
        listTableLayout.removeAllViews();

        TableRow headTr = new TableRow(this);
        headTr.setBackgroundResource(R.drawable.border);
        headTr.setPadding(0,10,0,10);
        headTr.addView(makeTableRowByTextView("순서"));
        headTr.addView(makeTableRowByTextView("로또번호"));
        headTr.addView(makeTableRowByTextView("생성일시"));
        listTableLayout.addView(headTr);

        List<LottoGame> list = lottoGameService.findByRound(getBaseContext(), round);
        Collections.sort(list, new Comparator<LottoGame>() {
            @Override
            public int compare(LottoGame o1, LottoGame o2) {
                return o2.getMakeDate().compareTo(o1.getMakeDate());
            }
        });

        for(int i = 0; i < list.size(); i++) {
            LottoGame lottoGame = list.get(i);
            List<Lotto> lottos = lottoGame.getLottos();

            TableRow bodyTr = new TableRow(this);
            bodyTr.setGravity(Gravity.CENTER_VERTICAL);
            bodyTr.setBackgroundResource(R.drawable.border);
            bodyTr.setPadding(0,10,0,10);
            bodyTr.addView(makeTableRowByTextView(String.valueOf(list.size() - i)));

            TableLayout lottoTl = new TableLayout(this);
            for(int j = 0; j < lottos.size(); j++) {
                Lotto lotto = lottos.get(j);
                List<Integer> numbers = lotto.getNumbers();
                String numbersStr = "";
                for (int k = 0; k < numbers.size(); k++) {
                    numbersStr += numbers.get(k) + " ";
                }

                TableRow lottoTr = new TableRow(this);
                lottoTr.setGravity(Gravity.CENTER);
                lottoTr.addView(makeTableRowByTextView(numbersStr));
                lottoTl.addView(lottoTr);
            }
            bodyTr.addView(lottoTl);
            bodyTr.addView(makeTableRowByTextView(lottoGame.getMakeDate()));
            listTableLayout.addView(bodyTr);
        }
    }

    // 당첨번호인지 확인
    public boolean isPrizeLottoNumber(Map<String, Object> resultLottoMap, int lottoNumber) {
        if(resultLottoMap == null) {
            return false;
        }

        String returnValue = (String) resultLottoMap.get("returnValue");
        if(returnValue == null || !"success".equals(returnValue)) {
            return false;
        }

        int drwtNo1 = (int) resultLottoMap.get("drwtNo1");

        // TODO 여기 할차례

        return true;
    }

    public TextView makeTableRowByTextView(String text) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        return tv;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
