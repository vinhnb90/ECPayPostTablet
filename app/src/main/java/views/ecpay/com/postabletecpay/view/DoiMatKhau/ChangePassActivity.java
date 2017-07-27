package views.ecpay.com.postabletecpay.view.DoiMatKhau;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.ChangePassPresenter;
import views.ecpay.com.postabletecpay.presenter.IChangePassPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;

import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by TungNV on 5/3/17.
 */

public class ChangePassActivity extends ActionBarActivity implements IChangePassView {
    @BindView(R.id.ibtn_frag_user_info_back)
    ImageButton ibBack;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvSDT)
    TextView tvSDT;
    @BindView(R.id.iv_frag_main_avatar)
    ImageView ivAvatar;
    @BindView(R.id.etMatKhauCu)
    EditText etPassOld;
    @BindView(R.id.etMatKhauMoi)
    EditText etPassNew;
    @BindView(R.id.etNhapLaiMatKhauMoi)
    EditText etPassRetype;
    @BindView(R.id.btn_ac_change_pass)
    Button btDoiMatKhau;
    @BindView(R.id.pbar_ac_change_pass_change)
    ProgressBar pbarChangePass;
    @BindView(R.id.tv_ac_change_pass_message)
    TextView tvMessage;

    private String mEdong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }

            mEdong = getIntent().getExtras().getString(Common.KEY_EDONG, null);
            if (mEdong == null)
                return;

            setContentView(R.layout.activity_doi_mat_khau);
            ButterKnife.bind(this);
            hideText();
            hidePbar();
            mIChangePassPresenter = new ChangePassPresenter(this);
            mIChangePassPresenter.callInfo(mEdong);

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //region onClick
    @OnClick(R.id.ibtn_frag_user_info_back)
    public void clickBack(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ChangePassActivity.this.finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, TIME_DELAY_ANIM);
    }

    @OnClick(R.id.btn_ac_change_pass)
    public void clickChangePass(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_pull, TIME_DELAY_ANIM);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPbar();

                String passOld = etPassOld.getText().toString();
                String passNew = etPassNew.getText().toString();
                String passRetype = etPassRetype.getText().toString();
                mIChangePassPresenter.validateInputChangePass(mEdong, passOld, passNew, passRetype);
            }
        }, TIME_DELAY_ANIM);
    }
    //endregion


    //region IChangePassView
    @Override
    public void showPbar() {
        if (pbarChangePass.getVisibility() == View.GONE) {
            btDoiMatKhau.setVisibility(View.GONE);
            pbarChangePass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidePbar() {
        if (pbarChangePass.getVisibility() == View.VISIBLE) {
            pbarChangePass.setVisibility(View.GONE);
            btDoiMatKhau.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showText(String message) {
        if (message == null || message.isEmpty())
            return;

        hidePbar();
        Common.runAnimationClickViewScale(tvMessage, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    @Override
    public void hideText() {
        tvMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showInfo(String name, String mEdong) {
        boolean fail = TextUtils.isEmpty(name) || TextUtils.isEmpty(mEdong);
        if (fail)
            return;

        tvUsername.setText(name);
        tvSDT.setText(mEdong);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if(view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if(viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if(rect.contains(x, y)) {
                        return consumed;
                    }
                }
                else if(viewNew instanceof EditText) {
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void showLoginForm() {
        Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    //endregion

    private IChangePassPresenter mIChangePassPresenter;

    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this, "CODE: " + code + "\n DESCRIPTION: " + description, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public Context getContextView() {
        return this;
    }
}
