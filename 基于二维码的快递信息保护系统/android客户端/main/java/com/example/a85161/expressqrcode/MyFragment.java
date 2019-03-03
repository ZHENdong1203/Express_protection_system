package com.example.a85161.expressqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a85161.expressqrcode.view.wave.WaveView;

public class MyFragment extends Fragment {

    private ImageView imageView;
    private WaveView waveView;
    private item_view logout;
    private item_view about;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_tab, container, false);

        imageView = (ImageView) view.findViewById(R.id.image);
        waveView = (WaveView) view.findViewById(R.id.wave_view);
        about=(item_view)view.findViewById(R.id.about);
        logout=(item_view)view.findViewById(R.id.logout);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2,-2);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0,0,0,(int)y+2);
                imageView.setLayoutParams(lp);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),AboutActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent();
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                logoutIntent.setClass(getActivity(), LoginActivity.class);
                startActivity(logoutIntent);
            }
        });

        return view;
    }
}
