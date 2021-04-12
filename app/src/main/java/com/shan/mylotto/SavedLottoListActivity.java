package com.shan.mylotto;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;
import com.shan.mylotto.util.CommonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SavedLottoListActivity extends AppCompatActivity {

    private LottoService lottoService;

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
    private LinearLayout roundLayout;
    private Spinner roundSpinner;

    private Integer curRound;
    private List<Integer> roundList;
    private Map<String, Object> resultLottoMap;
    private List<LottoGame> roundSavedList;
    private Button.OnClickListener delBtnListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lotto_list);

        init();
        roundSpinnerInit();
        eventHandlerInit();
    }

    @SuppressLint("WrongViewCast")
    public void init() {
        this.lottoService = new LottoServiceImpl(this);

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
        this.roundLayout = findViewById(R.id.roundLayout);
        this.roundSpinner = findViewById(R.id.roundsSpinner);

        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
    }

    public void eventHandlerInit() {
        // 삭제버튼 클릭 시
        this.delBtnListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                String id = btn.getHint().toString();

                lottoService.deleteLottoGame(Integer.parseInt(id));
                displaySavedLottoList(curRound);
            }
        };

        // 회차 셀렉트박스 선택 시
        this.roundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curRound = roundList.get(position);
                resultLottoMap = displayResultLottoNumber(curRound);
                displaySavedLottoList(curRound);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // 회자 스피너 셋팅
    private void roundSpinnerInit() {
        this.roundList = lottoService.findLottoRounds();

        if(this.roundList == null || this.roundList.size() == 0) {
            this.roundLayout.setVisibility(View.INVISIBLE);
            this.resultText.setText("저장된 내역이 없습니다.");
            return;
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roundList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.roundSpinner.setAdapter(spinnerAdapter);

        this.roundLayout.setVisibility(View.VISIBLE);
    }

    // 당첨결과 로또번호 출력
    private Map<String, Object> displayResultLottoNumber(Integer round) {
        Map<String, Object> resultLottoMap = lottoService.getLottoResultByDrwNo(getApplicationContext(), round);

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
    private void displaySavedLottoList(Integer round) {
        listTableLayout.removeAllViews();

        TableRow headTr = new TableRow(this);
        headTr.setBackgroundResource(R.drawable.border_head);
        headTr.setPadding(0,10,0,10);
        headTr.addView(makeTableRowByTextView("순서"));
        headTr.addView(makeTableRowByTextView("로또번호"));
        headTr.addView(makeTableRowByTextView("생성일시"));
        headTr.addView(makeTableRowByTextView("삭제"));
        listTableLayout.addView(headTr);

        this.roundSavedList = lottoService.findSavedLottoByRound(round);
        if(this.roundSavedList == null || this.roundSavedList.size() == 0) {
            return;
        }

        for(int i = 0; i < this.roundSavedList.size(); i++) {
            LottoGame lottoGame = this.roundSavedList.get(i);
            List<Lotto> lottos = lottoGame.getLottos();

            TableRow bodyTr = new TableRow(this);
            bodyTr.setGravity(Gravity.CENTER_VERTICAL);
            bodyTr.setBackgroundResource(R.drawable.border);
            bodyTr.setPadding(0,15,0,15);
            bodyTr.addView(makeTableRowByTextView(String.valueOf(this.roundSavedList.size() - i)));

            TableLayout lottoTl = new TableLayout(this);
            for(int j = 0; j < lottos.size(); j++) {
                Lotto lotto = lottos.get(j);
                TableRow lottoTr = new TableRow(this);
                lottoTr.setGravity(Gravity.CENTER);
                lottoTr.addView(makeLottoBtn(lotto.getNumOne()));
                lottoTr.addView(makeLottoBtn(lotto.getNumTwo()));
                lottoTr.addView(makeLottoBtn(lotto.getNumThree()));
                lottoTr.addView(makeLottoBtn(lotto.getNumFour()));
                lottoTr.addView(makeLottoBtn(lotto.getNumFive()));
                lottoTr.addView(makeLottoBtn(lotto.getNumSix()));
                lottoTl.addView(lottoTr);
            }
            bodyTr.addView(lottoTl);

            String dateTime = "";
            if(lottoGame.getRegDate() != null && lottoGame.getRegDate().length() == 19) {
                String date = lottoGame.getRegDate().substring(0, 10);
                String time = lottoGame.getRegDate().substring(11);
                dateTime = date + "\n" + time;
            }
            bodyTr.addView(makeTableRowByTextView(dateTime));

            TableRow.LayoutParams delBtnLp = new TableRow.LayoutParams(CommonUtil.getConvertToDeviceDP(getResources(), 20), CommonUtil.getConvertToDeviceDP(getResources(), 20));
            delBtnLp.gravity = Gravity.CENTER;

            Button delBtn = new Button(this);
            delBtn.setLayoutParams(delBtnLp);
            delBtn.setText("삭제");
            delBtn.setTextSize(12);
            delBtn.setTextColor(ContextCompat.getColor(this, R.color.colorTextBasic));
            delBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.delete_button));
            delBtn.setHint(String.valueOf(lottoGame.getId()));
            delBtn.setOnClickListener(this.delBtnListener);
            bodyTr.addView(delBtn);

            listTableLayout.addView(bodyTr);
        }
    }

    public Button makeLottoBtn(int lottoNum) {
        TableRow.LayoutParams lp = new TableRow.LayoutParams(CommonUtil.getConvertToDeviceDP(getResources(), 22), CommonUtil.getConvertToDeviceDP(getResources(), 22));
        lp.setMargins(5, 5, 5, 5);

        Button button = new Button(this);
        button.setLayoutParams(lp);
        button.setTextSize(11);
        button.setText(String.valueOf(lottoNum));
        if(checkPrizeLottoNumber(this.resultLottoMap, lottoNum) == 1) { // 번호가 맞을경우
            button.setTextColor(Color.WHITE);
            button.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(lottoNum)));
        } else if(checkPrizeLottoNumber(this.resultLottoMap, lottoNum) == 2) { // 보너스일경우
            button.setTextColor(Color.BLACK);
            button.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(lottoNum)));
        } else {
            button.setTextColor(ContextCompat.getColor(this, R.color.colorTextBasic));
            button.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.lotto_default));
        }
        return button;
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
