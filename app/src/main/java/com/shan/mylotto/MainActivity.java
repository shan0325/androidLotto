package com.shan.mylotto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.domain.LottoGame;
import com.shan.mylotto.lotto.service.LottoGameService;
import com.shan.mylotto.lotto.service.impl.LottoGameServiceImpl;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int SAVED_LOTTO_LIST = 1001;

    private LottoGameService lottoGameService;
    private LottoGame lottoGame;
    private EditText round;
    private Button makeOne;
    private Button makeTwo;
    private Button makeThree;
    private Button makeFour;
    private Button makeFive;
    private Button saveBtn;
    private LinearLayout roundLayout;
    private LinearLayout disLayout;
    private LinearLayout saveBtnLayout;
    private TextView message;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        eventHandlerInit();
    }

    public void init() {
        this.lottoGameService = new LottoGameServiceImpl(new LottoServiceImpl());

        this.round = findViewById(R.id.round);
        this.makeOne = findViewById(R.id.makeOne);
        this.makeTwo = findViewById(R.id.makeTwo);
        this.makeThree = findViewById(R.id.makeThree);
        this.makeFour = findViewById(R.id.makeFour);
        this.makeFive = findViewById(R.id.makeFive);
        this.saveBtn = findViewById(R.id.saveBtn);
        this.roundLayout = findViewById(R.id.roundLayout);
        this.disLayout = findViewById(R.id.disLayout);
        this.saveBtnLayout = findViewById(R.id.saveBtnLayout);
        this.message = findViewById(R.id.message);
        this.myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(this.myToolbar);

        // 포커스
        //makeLottosLength.requestFocus();
        //키보드 보이게 하는 부분
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(makeLottosLength, 0);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void eventHandlerInit() {
        Button.OnClickListener makeBtnListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = findViewById(v.getId());
                String makeNumber = btn.getText().toString();

                // 이전 로또 지우기
                disLayout.removeAllViews();

                lottoGame = lottoGameService.makeLottoGame(Integer.parseInt(makeNumber));
                displayLottos(lottoGame.getLottos());

                displayMessage("생성이 완료되었습니다.");
            }
        };
        this.makeOne.setOnClickListener(makeBtnListener);
        this.makeTwo.setOnClickListener(makeBtnListener);
        this.makeThree.setOnClickListener(makeBtnListener);
        this.makeFour.setOnClickListener(makeBtnListener);
        this.makeFive.setOnClickListener(makeBtnListener);

        // 생성하기 버튼 클릭 시
        /*this.makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lottosLenStr = makeLottosLength.getText().toString().trim();
                if("".equals(lottosLenStr)) {
                    displayMessage("생성 갯수를 입력해주세요.");
                    return;
                }

                int lottosLen = 0;
                try {
                    lottosLen = Integer.parseInt(lottosLenStr);
                    if(lottosLen == 0) return;
                    if(lottosLen > 5) {
                        displayMessage("5개 까지 생성할 수 있습니다.");
                        makeLottosLength.setText("");
                        return;
                    }
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                    displayMessage("숫자만 입력할 수 있습니다.");
                    makeLottosLength.setText("");
                    return;
                }

                // 이전 로또 지우기
                disLayout.removeAllViews();

                lottoGame = lottoGameService.makeLottoGame(lottosLen);
                displayLottos(lottoGame.getLottos());

                displayMessage("생성이 완료되었습니다.");
                //makeLottosLength.setText("");
            }
        });*/


        // 저장하기 버튼 클릭 시
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roundStr = round.getText().toString().trim();
                if("".equals(roundStr)) {
                    displayMessage("회차를 입력해주세요.");
                    round.requestFocus();
                    return;
                }
                lottoGame.setRound(Integer.parseInt(roundStr));

                //int result = FileUtil.writeJsonFile(getBaseContext(), lottoGame);
                int result = lottoGameService.saveFileByLottoGame(getBaseContext(), lottoGame);
                if(result == 0) {
                    displayMessage("저장을 완료하였습니다.");
                    saveBtnLayout.setVisibility(View.INVISIBLE);
                } else {
                    displayMessage("저장을 실패하였습니다.");
                }
            }
        });
    }

    public void displayMessage(String message) {
        //this.message.setText(message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void displayLottos(List<Lotto> lottos) {
        if(lottos != null) {
            for(int i = 0; i < lottos.size(); i++) {
                Lotto lotto = lottos.get(i);
                List<Integer> numbers = lotto.getNumbers();

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout lottoLayout = new LinearLayout(this);
                lottoLayout.setOrientation(LinearLayout.HORIZONTAL);
                lottoLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                lottoLayout.setLayoutParams(lp);
                lottoLayout.setWeightSum(6);

                for(int j = 0; j < numbers.size(); j++) {
                /*LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLp.setMargins(15, 10, 15, 10);
                buttonLp.weight = 1;*/
                    LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(120, 120);
                    buttonLp.setMargins(20, 10, 20, 10);

                    Button button = new Button(this);
                    button.setText(String.valueOf(numbers.get(j)));
                    button.setLayoutParams(buttonLp);
                    button.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.button_shape));
                    button.setTextColor(Color.WHITE);

                    lottoLayout.addView(button);
                }
                this.disLayout.addView(lottoLayout);
            }
            this.roundLayout.setVisibility(View.VISIBLE);
            this.saveBtnLayout.setVisibility(View.VISIBLE);

            // 회차 정보 넣기
            this.round.setText(lottoGameService.getLottoRoundByDhlottery());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout :
                ActivityCompat.finishAffinity(this);
                System.exit(0);
                break;
            case R.id.savedLottoList :
                Intent intent = new Intent(getApplicationContext(), SavedLottoListActivity.class);
                startActivityForResult(intent, SAVED_LOTTO_LIST); // 액티비티 띄우기
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
