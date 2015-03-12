package com.moses.io.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.moses.io.R;

/**
 * Created by Moses on 2015.1.25
 */
public class PagerHttpImgFragment extends Fragment {
    ImageView imageView;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.img_fragment_layout,container,false);
        imageView  = (ImageView) view.findViewById(R.id.fragment_image_view);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_img_progress_bar);
        return view;

    }


}
