package com.thingsenz.energymeter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;

public class PaymentDialog extends Dialog  {

    public Activity a;
    public Dialog d;
    public LottieAnimationView animationView;


    public PaymentDialog(Activity a){
        super(a);
        this.a=a;
    }


    @Override
    protected void onCreate(Bundle saved){
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_anim);
        animationView=findViewById(R.id.animationview);
        animationView.setProgress(0);
        animationView.playAnimation();

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },1500);*/


    }

}
