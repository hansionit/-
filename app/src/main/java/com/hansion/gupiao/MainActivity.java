package com.hansion.gupiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hansion.gupiao.utils.RegexUtils;
import com.hansion.gupiao.utils.SharedPrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mNumber)
    EditText mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        startService(new Intent(this, GetInfoService.class));

        String value = SharedPrefsUtil.getValue(this, "num", "num", "");
        if(!TextUtils.isEmpty(value)) {
            mNumber.setText(value);
        }
    }


    @OnClick(R.id.mConfirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mConfirm:
                String trim = mNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(trim) && RegexUtils.isNumeric(trim) && trim.length() == 6) {
                    SharedPrefsUtil.putValue(this, "num", "num", trim);
                } else {
                    Toast.makeText(this, "请输入正确的代号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
