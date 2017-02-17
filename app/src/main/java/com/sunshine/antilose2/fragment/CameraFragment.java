package com.sunshine.antilose2.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sunshine.antilose2.R;

import app.mvp.MvpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends MvpFragment<CameraFragmentPresenter.CameraFragmentView, CameraFragmentPresenter>
        implements CameraFragmentPresenter.CameraFragmentView {


    public CameraFragment() {
        // Required empty public constructor
    }

    private static CameraFragment cameraFragment;


    public static CameraFragment getCameraFragment() {
        if (cameraFragment == null) {
            cameraFragment = new CameraFragment();
        }
        return cameraFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    protected CameraFragmentPresenter createPresenter() {
        return new CameraFragmentPresenter();
    }
}
