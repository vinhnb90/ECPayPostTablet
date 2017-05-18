package views.ecpay.com.postabletecpay.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import views.ecpay.com.postabletecpay.R;

/**
 * Created by VinhNB on 5/11/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void hideActionBar() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            getSupportActionBar().hide();
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected Context getContext(){
        return super.getApplicationContext();
    }

    protected void runAnimationClickViewScale(final View view, int idAnimation)
    {
        if(view == null)
            return;
        if(idAnimation<=0)
            return;

        view.startAnimation(AnimationUtils.loadAnimation(this, idAnimation));
    }

    protected abstract void initView();

    protected abstract void initSource();

    protected abstract void setAction(@Nullable Bundle savedInstanceState);

}
