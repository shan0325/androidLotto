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

import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.lotto.service.LottoGameService;
import com.shan.mylotto.lotto.service.impl.LottoGameServiceImpl;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;

import java.util.List;
import java.util.Map;

import static com.shan.mylotto.R.id.round;

public class SavedLottoListActivity extends AppCompatActivity {

    private LottoGameService lottoGameService;
    private TableLayout listTableLayout;
    private Toolbar myToolbar;
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
        this.lottoGameService = new LottoGameServiceImpl(new LottoServiceImpl());
        this.listTableLayout = findViewById(R.id.listTableLayout);
        this.myToolbar = findViewById(R.id.myToolbar);
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
                displayResultLottoNumber(rounds.get(position));
                displaySavedLottoList(rounds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void displayResultLottoNumber(Integer round) {
        Map<String, String> resultLottoMap = lottoGameService.getLottoResultByDrwNo(round);
        String returnValue = resultLottoMap.get("returnValue");
        if(returnValue != null && "success".equals(returnValue)) {
            this.resultLayout.setVisibility(View.VISIBLE);
            this.resultOneNumber.setText(String.valueOf(resultLottoMap.get("drwtNo1")));
            this.resultTwoNumber.setText(String.valueOf(resultLottoMap.get("drwtNo2")));
            this.resultThreeNumber.setText(String.valueOf(resultLottoMap.get("drwtNo3")));
            this.resultFourNumber.setText(String.valueOf(resultLottoMap.get("drwtNo4")));
            this.resultFiveNumber.setText(String.valueOf(resultLottoMap.get("drwtNo5")));
            this.resultBonusNumber.setText(String.valueOf(resultLottoMap.get("bnusNo")));
        } else {
            this.resultLayout.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("WrongViewCast")
    private void displaySavedLottoList(Integer round) {
        listTableLayout.removeAllViews();

        TableRow headTr = new TableRow(this);
        headTr.setBackgroundResource(R.drawable.border);
        headTr.setPadding(0,10,0,10);
        makeTableRow(headTr, "아이디");
        makeTableRow(headTr, "번호");
        makeTableRow(headTr, "생성일자");
        listTableLayout.addView(headTr);

        List<LottoGame> list = lottoGameService.findByRound(getBaseContext(), round);
        for(int i = 0; i < list.size(); i++) {
            LottoGame lottoGame = list.get(i);
            List<Lotto> lottos = lottoGame.getLottos();

            for(int j = 0; j < lottos.size(); j++) {
                Lotto lotto = lottos.get(j);
                List<Integer> numbers = lotto.getNumbers();

                TableRow bodyTr = new TableRow(this);
                bodyTr.setBackgroundResource(R.drawable.border);
                bodyTr.setPadding(0,10,0,10);
                makeTableRow(bodyTr, String.valueOf(lottoGame.getId()));
                makeTableRow(bodyTr, numbers.toString());
                makeTableRow(bodyTr, lotto.getMakeDate());
                listTableLayout.addView(bodyTr);
            }
        }
    }

    public void makeTableRow(TableRow tr, String text) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tr.addView(tv);
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
