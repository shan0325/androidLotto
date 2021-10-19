package com.shan.mylotto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shan.mylotto.common.ProgressDialog;
import com.shan.mylotto.lotto.domain.Lotto;
import com.shan.mylotto.lotto.service.LottoService;
import com.shan.mylotto.lotto.service.impl.LottoServiceImpl;
import com.shan.mylotto.util.CommonUtil;
import com.shan.mylotto.util.DateUtil;
import com.shan.mylotto.util.JsoupTaskUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int SAVED_LOTTO_LIST = 1001;
    public static final int QR_ACTIVITY = 1002;

    private LottoService lottoService;

    public static Toast mToast;
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
    private AdView mAdView;
    private ImageView qrScanImg;

    // qr code scanner object
    private IntentIntegrator qrScan;

    private ProgressDialog customProgressDialog;

    private boolean isSaved;
    private boolean isQrScanImg;
    private String thisWeekRound;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 첫 인트로 페이지 시작
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        // 첫 인트로 페이지 끝

        init();
        eventHandlerInit();

        // AdMob 초기화
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // 이번주 회차 정보 넣기
        this.thisWeekRound = lottoService.getLottoRoundByDhlottery(getApplicationContext());
        this.round.setText(thisWeekRound);
    }

    public void init() {
        this.lottoService = new LottoServiceImpl(this);

        this.mToast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) mToast.getView();
        TextView msgTextView = (TextView) group.getChildAt(0);
        msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

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
        this.qrScanImg = findViewById(R.id.qrScanImg);

        this.qrScan = new IntentIntegrator(this);
        this.qrScan.setOrientationLocked(false);
        this.qrScan.setBeepEnabled(false);
        this.qrScan.setCaptureActivity(QrReaderActivity.class);
        this.qrScan.setPrompt("QR코드를 인식해주세요.");

        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 포커스
        //makeLottosLength.requestFocus();
        //키보드 보이게 하는 부분
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(makeLottosLength, 0);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void eventHandlerInit() {
        // 숫자 버튼 터치 시
        Button.OnClickListener makeBtnListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = findViewById(v.getId());
                String makeNumber = btn.getText().toString();

                // 로딩창 show
                customProgressDialog.show();

                // 이전 로또 지우기
                disLayout.removeAllViews();
                disLayout.setVisibility(View.INVISIBLE);

                displayLottos(lottoService.makeLotto(Integer.parseInt(makeNumber)));

                // 회차 정보 넣기
                round.setText(thisWeekRound);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.cancel();

                        Animation animation = new AlphaAnimation(0, 1);
                        animation.setDuration(300);
                        disLayout.setAnimation(animation);
                        disLayout.setVisibility(View.VISIBLE);

                        saveBtn.setText("저장");
                        isSaved = false;

                        showToast(getApplicationContext(),"생성을 완료하였습니다.");
                    }
                }, 1100);
            }
        };
        this.makeOne.setOnClickListener(makeBtnListener);
        this.makeTwo.setOnClickListener(makeBtnListener);
        this.makeThree.setOnClickListener(makeBtnListener);
        this.makeFour.setOnClickListener(makeBtnListener);
        this.makeFive.setOnClickListener(makeBtnListener);

        // 저장하기 버튼 터치 시
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

        // qr 스캔 이미지 터치 시
        this.qrScanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQrScanImg = true;
                qrScan.initiateScan();
            }
        });
    }

    public void displayLottos(List<Lotto> lottos) {
        if(lottos != null && lottos.size() > 0) {
            for(int i = 0; i < lottos.size(); i++) {
                Lotto lotto = lottos.get(i);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, CommonUtil.getConvertToDeviceDP(getResources(), 5));
                lp.gravity = Gravity.CENTER;

                int left = CommonUtil.getConvertToDeviceDP(getResources(), 10);
                int top  = CommonUtil.getConvertToDeviceDP(getResources(), 7);
                int right = CommonUtil.getConvertToDeviceDP(getResources(), 10);
                int bottom = CommonUtil.getConvertToDeviceDP(getResources(), 7);

                final LinearLayout lottoLayout = new LinearLayout(this);
                lottoLayout.setLayoutParams(lp);
                lottoLayout.setPadding(left, top, right, bottom);
                lottoLayout.setOrientation(LinearLayout.HORIZONTAL);
                //lottoLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.layout_border));

                lottoLayout.addView(makeLottoBtn(lotto.getNumOne()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumTwo()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumThree()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumFour()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumFive()));
                lottoLayout.addView(makeLottoBtn(lotto.getNumSix()));

                /*Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(500);
                lottoLayout.setVisibility(View.VISIBLE);
                lottoLayout.setAnimation(animation);*/

                disLayout.addView(lottoLayout);
            }
        }
    }

    public Button makeLottoBtn(int lottoNum) {
        int left = CommonUtil.getConvertToDeviceDP(getResources(), 3);
        int right = CommonUtil.getConvertToDeviceDP(getResources(), 3);

        LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(CommonUtil.getConvertToDeviceDP(getResources(), 40), CommonUtil.getConvertToDeviceDP(getResources(), 40));
        buttonLp.setMargins(left, 0, right, 0);

        Button button = new Button(this);
        button.setLayoutParams(buttonLp);
        button.setText(String.valueOf(lottoNum));
        button.setBackground(ContextCompat.getDrawable(this, this.lottoService.getLottoColor(lottoNum)));
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
            case R.id.qrScan :
                isQrScanImg = false;
                qrScan.initiateScan();
//                Intent qrIntent = new Intent(getApplicationContext(), QrReaderActivity.class);
//                startActivityForResult(qrIntent, QR_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // QR코드 스캔 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                 showToast(getApplicationContext(), "결과를 찾을 수 없습니다.");
            } else {
                final String contents = intentResult.getContents();
                // ((QrReaderActivity) QrReaderActivity.mContext).showContents(contents);

                if(contents != null && contents.startsWith("http")) {
                    if(isQrScanImg) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(contents)));
                        /*new AlertDialog.Builder(this)
                                    .setTitle(R.string.app_name)
                                    .setMessage(contents)
                                    .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(contents)));
                                            dialog.dismiss();
                                        }
                                    }).show();*/
                    } else {
                        lottoList = getLottoListByQrWeb(contents);
                        if(lottoList != null && lottoList.size() > 0) {
                            // 이전 로또 지우기
                            disLayout.removeAllViews();
                            
                            // my lotto number 입력
                            displayLottos(lottoList);

                            // 회차 정보 입력
                            Integer myRound = lottoList.get(0).getRound();
                            if(myRound != null) {
                                round.setText(String.valueOf(myRound));
                                showToast(getApplicationContext(), "[" + myRound + "회] 나의 로또 번호를 가져왔습니다.");
                            } else {
                                showToast(getApplicationContext(), "나의 로또 번호를 가져왔습니다.");
                            }
                        } else {
                            showToast(getApplicationContext(), "결과를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    showToast(getApplicationContext(), "URL 정보를 확인할 수 없습니다.");
                    // showToast(getApplicationContext(), intentResult.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private List<Lotto> getLottoListByQrWeb(String contents) {
        JsoupTaskUtil jsoupTaskUtil = new JsoupTaskUtil();

        try {
            // 동행복권 URL
            if(contents.indexOf("m.dhlottery.co.kr") != -1) {
                List<Lotto> lottos = new ArrayList<Lotto>();
                Integer round = null;

                contents = "https://m.dhlottery.co.kr/qr.do?method=winQr&" + contents.substring(contents.indexOf("v="));
                Elements contentsEl = jsoupTaskUtil.execute(contents, "body .contents").get();
                if(contentsEl == null) {
                    return null;
                }

                Elements roundEl = contentsEl.select(".key_clr1");
                if(roundEl != null) {
                    String roundTxt = roundEl.text();
                    if(roundTxt != null) {
                        roundTxt = roundTxt.substring(roundTxt.indexOf("제") + 1, roundTxt.indexOf("회"));
                        if(roundTxt != null && CommonUtil.isNumeric(roundTxt)) {
                            round = Integer.parseInt(roundTxt);
                        }
                    }
                }

                Elements myNumberEl = contentsEl.select(".list_my_number .tbl_basic");
                if(myNumberEl != null) {
                    Iterator<Element> tableIter = myNumberEl.select("table tbody tr").iterator();
                    while (tableIter.hasNext()) {
                        List<Integer> myLottoNumList = new ArrayList<>();

                        Element next = tableIter.next();
                        Iterator<Element> tdIter = next.select("td span.clr").iterator();
                        while (tdIter.hasNext()) {
                            Element tdNext = tdIter.next();
                            myLottoNumList.add(Integer.parseInt(tdNext.text()));
                        }

                        if(myLottoNumList.size() != 6) {
                            return null;
                        }

                        Lotto lotto = new Lotto();
                        lotto.setNumOne(myLottoNumList.get(0));
                        lotto.setNumTwo(myLottoNumList.get(1));
                        lotto.setNumThree(myLottoNumList.get(2));
                        lotto.setNumFour(myLottoNumList.get(3));
                        lotto.setNumFive(myLottoNumList.get(4));
                        lotto.setNumSix(myLottoNumList.get(5));
                        lotto.setMakeDate(DateUtil.formatByDate(new Date()));

                        if(round != null) {
                            lotto.setRound(round);
                        }

                        lottos.add(lotto);
                    }
                }
                return lottos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        /*Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, CommonUtil.getConvertToDeviceDP(getResources(), 120));
        ViewGroup group = (ViewGroup) toast.getView();
        TextView msgTextView = (TextView) group.getChildAt(0);
        msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        toast.show();*/

        mToast.setText(message);
        mToast.setGravity(Gravity.BOTTOM, 0, CommonUtil.getConvertToDeviceDP(getResources(), 120));
        mToast.show();
    }
}
