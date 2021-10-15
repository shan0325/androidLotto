package com.shan.mylotto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrReaderActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    public static Context mContext;

    private CaptureManager m_captureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);

        mContext = this;

        /*findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Intents.Scan.RESULT, "cancel");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.btn_skip_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Intents.Scan.RESULT, "http://...");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });*/

        DecoratedBarcodeView decoratedBarcodeView = findViewById(R.id.qr_reader_view);
        decoratedBarcodeView.setTorchListener(this);

        m_captureManager = new CaptureManager(this, decoratedBarcodeView);
        m_captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        m_captureManager.decode();
    }

    public void showContents(String contents) {
        System.out.println("contents : " + contents);
    }

    @Override
    public void onTorchOn() {
    }

    @Override
    public void onTorchOff() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        m_captureManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_captureManager.onDestroy();
    }
}

