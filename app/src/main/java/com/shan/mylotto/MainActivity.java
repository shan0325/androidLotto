package com.shan.mylotto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
        // 숫자 버튼 클릭 시
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

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;

                LinearLayout lottoLayout = new LinearLayout(this);
                lottoLayout.setLayoutParams(lp);
                lottoLayout.setOrientation(LinearLayout.HORIZONTAL);

                for(int j = 0; j < numbers.size(); j++) {
                    Integer lottoNum = numbers.get(j);

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

                    LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(120, 120);
                    buttonLp.setMargins(13, 0, 13, 50);
                    buttonLp.width = 140;
                    buttonLp.height = 140;

                    Button button = new Button(this);
                    button.setLayoutParams(buttonLp);
                    button.setText(String.valueOf(numbers.get(j)));
                    button.setBackgroundDrawable(ContextCompat.getDrawable(this, lottoColor));
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

    // 다른곳 터치 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
