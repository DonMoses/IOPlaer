package com.moses.io.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.moses.io.R;

/**
 * Created by Moses on 2015.1.25
 */
public class PagerFragmentHttpImgActivity extends Activity {
    ViewPager mViewPager;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_fragmment_http_img);


    }

    public void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager_fragment_img);
        mTextView = (TextView) findViewById(R.id.text_view_img_count);


    }

    class MyViewPagerAdapter extends FragmentPagerAdapter{

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }

    }


}
