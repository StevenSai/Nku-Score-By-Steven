package com.Steven.NkuScore;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Steven on 2015/10/7.
 */
public class AboutActivity extends BaseActivity {

    ImageView imageView;
    TextView textView1,textView2;
    ScrollView scrollView;
    final List<View> views = new ArrayList<>();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            View i = views.get(msg.what);
//            if(msg.what==0){
//                ObjectAnimator.ofFloat(imageView,"rotation",0f,360f).setDuration(1000).start();
//            }
            ObjectAnimator.ofFloat(i,"alpha",0f,1f).setDuration(500).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        imageView = (ImageView) findViewById(R.id.imageLogo);
        textView1 = (TextView) findViewById(R.id.textVersion);
        textView2 = (TextView) findViewById(R.id.textEnd);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imageView.setAlpha(0f);
        textView1.setAlpha(0f);
        textView2.setAlpha(0f);
        scrollView.setAlpha(0f);
        views.add(imageView);
        views.add(textView1);
        views.add(scrollView);
        views.add(textView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(imageView,"rotation",0f,360f).setDuration(1000).start();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startAni(views);
            }
        },500);
    }

    public void startAni(final List<View> views){
        final Handler h = this.handler;
        for (int  i = 0; i < views.size(); i++) {
            final int x = i;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    h.sendEmptyMessage(x);
                }
            },i*200);
        }
    }
}
