package com.shan.mylotto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;
import com.shan.mylotto.util.CommonUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int SAVED_LOTTO_LIST = 1001;

    private LottoService lottoService;

    private Toast mToast;
    private List<Lotto> lottoList;
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
    private View disScrollView;

    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        eventHandlerInit();

        // 회차 정보 넣기
        this.round.setText(lottoService.getLottoRoundByDhlottery());
    }

    public void init() {
        this.lottoService = new LottoServiceImpl(this);

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
        this.disScrollView = findViewById(R.id.disScrollView);
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

                lottoList = lottoService.makeLotto(Integer.parseInt(makeNumber));
                displayLottos(lottoList);

                showToast(getApplicationContext(),"생성을 완료하였습니다.");
                saveBtn.setText("저장");
                isSaved = false;
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
                if(lottoList == null || lottoList.size() == 0) {
                    showToast(getApplicationContext(),"로또를 생성해주세요");
                    return;
                }
                if(isSaved) {
                    showToast(getApplicationContext(),"이미 저장을 완료하였습니다.");
                    return;
                }

                String roundStr = round.getText().toString().trim();
                if("".equals(roundStr)) {
                    showToast(getApplicationContext(),"회차를 입력해주세요");
                    round.requestFocus();
                    return;
                }

                int result = lottoService.insertLottoList(Integer.parseInt(roundStr), lottoList);
                if(result == 0) {
                    showToast(getApplicationContext(),"저장을 완료하였습니다.");
                    saveBtn.setText("저장 완료");
                    isSaved = true;
                } else {
                    showToast(getApplicationContext(),"저장을 실패하였습니다.");
                    saveBtn.setText("저장 실패");
                }
            }
        });
    }

    public void displayLottos(List<Lotto> lottos) {
        if(lottos != null && lottos.size() > 0) {
            for(int i = 0; i < lottos.size(); i++) {
                Lotto lotto = lottos.get(i);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;

                LinearLayout lottoLayout = new LinearLayout(this);
                lottoLayout.setLayoutParams(lp);
                lottoLayout.setOrientation(LinearLayout.HORIZONTAL);
                lottoLayout.addView(makeLottoBtn(lotto.getNumOne()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumTwo()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumThree()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumFour()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumFive()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumSix()));

                this.disLayout.addView(lottoLayout);
            }
        }
    }

    public Button makeLottoBtn(int lottoNum) {
        int left = CommonUtil.getConvertToDeviceDP(getResources(), 3);
        int right = CommonUtil.getConvertToDeviceDP(getResources(), 3);
        int bottom = CommonUtil.getConvertToDeviceDP(getResources(), 15);

        LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(CommonUtil.getConvertToDeviceDP(getResources(), 40), CommonUtil.getConvertToDeviceDP(getResources(), 40));
        buttonLp.setMargins(left, 0, right, bottom);

        Button button = new Button(this);
        button.setLayoutParams(buttonLp);
        button.setText(String.valueOf(lottoNum));
        button.setBackgroundDrawable(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(lottoNum)));
        button.setTextColor(Color.WHITE);
        return button;
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

    public void showToast(Context context, String message) {
        if(mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }
}
