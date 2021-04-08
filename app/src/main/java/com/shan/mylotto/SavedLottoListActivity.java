package com.shan.mylotto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.shan.mylotto.util.CommonUtil;

import java.util.Arrays;
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
    private Button resultSixNumber;
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
        this.resultSixNumber = findViewById(R.id.resultSixNumber);
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

            this.resultSixNumber.setText(String.valueOf(resultLottoMap.get("drwtNo6")));
            this.resultSixNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor((Integer) resultLottoMap.get("drwtNo6"))));

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
        headTr.setBackgroundResource(R.drawable.border_head);
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
            bodyTr.setPadding(0,20,0,20);
            bodyTr.addView(makeTableRowByTextView(String.valueOf(list.size() - i)));

            TableLayout lottoTl = new TableLayout(this);
            for(int j = 0; j < lottos.size(); j++) {
                Lotto lotto = lottos.get(j);
                List<Integer> numbers = lotto.getNumbers();

                TableRow lottoTr = new TableRow(this);
                lottoTr.setGravity(Gravity.CENTER);
                for (int k = 0; k < numbers.size(); k++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(CommonUtil.getConvertToDP(getResources(), 22), CommonUtil.getConvertToDP(getResources(), 22));
                    lp.setMargins(5, 5, 5, 5);

                    Button button = new Button(this);
                    button.setLayoutParams(lp);
                    button.setTextSize(12);
                    button.setText(String.valueOf(numbers.get(k)));
                    if(checkPrizeLottoNumber(resultLottoMap, numbers.get(k)) == 1) { // 번호가 맞을경우
                        button.setTextColor(Color.WHITE);
                        button.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(numbers.get(k))));
                    } else if(checkPrizeLottoNumber(resultLottoMap, numbers.get(k)) == 2) { // 보너스일경우
                        button.setTextColor(Color.BLACK);
                        button.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(numbers.get(k))));
                    } else {
                        button.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.lotto_default));
                    }

                    lottoTr.addView(button);
                }
                lottoTl.addView(lottoTr);
            }
            bodyTr.addView(lottoTl);

            String dateTime = "";
            if(lottoGame.getMakeDate() != null && lottoGame.getMakeDate().length() == 19) {
                String date = lottoGame.getMakeDate().substring(0, 10);
                String time = lottoGame.getMakeDate().substring(11);
                dateTime = date + "\n" + time;
            }
            bodyTr.addView(makeTableRowByTextView(dateTime));
            listTableLayout.addView(bodyTr);
        }
    }

    // 당첨번호인지 확인
    public int checkPrizeLottoNumber(Map<String, Object> resultLottoMap, int lottoNumber) {
        int result = 0;
        if(resultLottoMap == null) {
            return result;
        }

        String returnValue = (String) resultLottoMap.get("returnValue");
        if(returnValue == null || !"success".equals(returnValue)) {
            return result;
        }

        int drwtNo1 = (int) resultLottoMap.get("drwtNo1");
        int drwtNo2 = (int) resultLottoMap.get("drwtNo2");
        int drwtNo3 = (int) resultLottoMap.get("drwtNo3");
        int drwtNo4 = (int) resultLottoMap.get("drwtNo4");
        int drwtNo5 = (int) resultLottoMap.get("drwtNo5");
        int drwtNo6 = (int) resultLottoMap.get("drwtNo6");
        int bnusNo = (int) resultLottoMap.get("bnusNo");

        if(Arrays.asList(drwtNo1, drwtNo2, drwtNo3, drwtNo4, drwtNo5, drwtNo6).contains(lottoNumber)) {
            result = 1;
        } else if(bnusNo == lottoNumber) {
            result = 2;
        }

        return result;
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
