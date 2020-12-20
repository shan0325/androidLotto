package com.shan.mylotto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import static com.shan.mylotto.R.id.round;

public class SavedLottoListActivity extends AppCompatActivity {

    private LottoGameService lottoGameService;
    private LinearLayout listLayout;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lotto_list);

        init();
    }

    @SuppressLint("WrongViewCast")
    public void init() {
        this.lottoGameService = new LottoGameServiceImpl(new LottoServiceImpl());
        this.listLayout = findViewById(R.id.listLayout);
        this.myToolbar = findViewById(R.id.myToolbar);
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
                System.out.println("11111111111111");
                displaySavedLottoList(rounds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("2222222222222");
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void displaySavedLottoList(Integer round) {
        System.out.println(round);
        listLayout.removeAllViews();
        List<LottoGame> list = lottoGameService.findByRound(getBaseContext(), round);
        System.out.println(list);



        for(int i = 0; i < list.size(); i++) {
            LottoGame lottoGame = list.get(i);
            System.out.println(lottoGame);
            Integer id = lottoGame.getId();

            List<Lotto> lottos = lottoGame.getLottos();
            for(int j = 0; j < lottos.size(); j++) {
                Lotto lotto = lottos.get(j);
                List<Integer> numbers = lotto.getNumbers();
                System.out.println(numbers.toString());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView lottoGameId = new TextView(this);
                lottoGameId.setText(String.valueOf(id));
                row.addView(lottoGameId);

                TextView lottoNumbers = new TextView(this);
                lottoNumbers.setText(numbers.toString());
                row.addView(lottoNumbers);

                listLayout.addView(row);
            }
        }
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
