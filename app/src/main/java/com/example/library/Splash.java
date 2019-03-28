package com.example.library;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    LinearLayout l;
    ImageView i;
    Animation uptodown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        i=(ImageView) findViewById(R.id.i);
        l=(LinearLayout) findViewById(R.id.l1);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        l.setAnimation(uptodown);
//        i.setOnClickListener(new View.OnClickListener() {
            new Timer().schedule(new TimerTask(){

                        public void run(){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, 3000);
            /*public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }*/
        };
    }

