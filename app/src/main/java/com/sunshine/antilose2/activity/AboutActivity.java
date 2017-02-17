package com.sunshine.antilose2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sunshine.antilose2.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.toolsfinal.ManifestUtils;

public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.tv_version_about)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        tvVersion.setText("软件版本:" + ManifestUtils.getVersionCode(this) * 1.0);
    }
}
