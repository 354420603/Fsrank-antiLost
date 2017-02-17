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
public class LocationFragment extends MvpFragment<LocationFragmentPresenter.LocationFragmentView, LocationFragmentPresenter>
        implements LocationFragmentPresenter.LocationFragmentView {


    public LocationFragment() {
        // Required empty public constructor
    }
    private static LocationFragment locationFragment;


    public static LocationFragment getLocationFragment() {
        if (locationFragment == null) {
            locationFragment = new LocationFragment();
        }
        return locationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    protected LocationFragmentPresenter createPresenter() {
        return new LocationFragmentPresenter();
    }
}
