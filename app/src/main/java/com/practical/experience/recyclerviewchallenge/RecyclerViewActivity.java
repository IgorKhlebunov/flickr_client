package com.practical.experience.recyclerviewchallenge;

import android.support.v4.app.Fragment;

import com.practical.experience.recyclerviewchallenge.common.SimpleFragmentActivity;

public class RecyclerViewActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ImageFragment();
    }
}
