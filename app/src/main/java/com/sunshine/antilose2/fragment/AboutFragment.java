package com.sunshine.antilose2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshine.antilose2.R;
import com.sunshine.antilose2.activity.AboutActivity;

import app.mvp.MvpFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends MvpFragment<AboutFragmentPresenter.AboutFragmentView, AboutFragmentPresenter>
        implements AboutFragmentPresenter.AboutFragmentView {


    @Bind(R.id.tv_version)
    TextView tvVersion;

    public AboutFragment() {
    }

    private static AboutFragment aboutFragment;


    public static AboutFragment getAboutFragment() {
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
        }
        return aboutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    protected AboutFragmentPresenter createPresenter() {
        return new AboutFragmentPresenter();
    }

    @OnClick(R.id.rl_version)
    public void onAboutClick() {
        startActivity(new Intent(getContext(), AboutActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
