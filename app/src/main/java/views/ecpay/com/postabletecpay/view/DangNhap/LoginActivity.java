package views.ecpay.com.postabletecpay.view.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.ILoginPresenter;
import views.ecpay.com.postabletecpay.presenter.LoginPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.view.BaseActivity;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by macbook on 4/28/17.
 */

public class LoginActivity extends BaseActivity implements ILoginView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideActionBar();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initSource();
        initView();
        setAction(savedInstanceState);
    }

    //region ILoginView
    @Override
    public Context getContextView() {
        return this.getApplicationContext();
    }

    @Override
    public void showPbarLogin() {
        if (pbarLogin.getVisibility() == View.GONE) {
            btLogin.setVisibility(View.GONE);
            pbarLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidePbarLogin() {
        if (pbarLogin.getVisibility() == View.VISIBLE) {
            pbarLogin.setVisibility(View.GONE);
            btLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showTextMessage(String message) {
        if (message == null || message.isEmpty())
            return;

        Common.runAnimationClickViewScale(tvMessage, R.anim.scale_view_pull, Common.TIME_DELAY_ANIM);

        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    @Override
    public void hideTextMessage() {
        tvMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMainScreen() {
        hidePbarLogin();
        hideTextMessage();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }

    @Override
    public void showTextUserPass(String userName, String pass) {
        if (userName == null)
            return;
        if (pass == null)
            return;

        etUsername.setText(userName);
        etPass.setText(pass);
    }

    @Override
    public void showTickCheckbox(boolean isSaveLogin) {
        cbRememberLogin.setChecked(isSaveLogin);
    }
    //endregion

    //region Imp parent class
    @Override
    protected void initView() {
        hidePbarLogin();
        hideTextMessage();
    }

    @Override
    protected void initSource() {
        mILoginPresenter = new LoginPresenter(this);
    }

    @Override
    protected void setAction(@Nullable Bundle savedInstanceState) {
        Common.verifyStoragePermissions(this);

        try {
            Common.makeRootFolderAndGetDataConfig(this);
        } catch (Exception e) {
            showTextMessage(e.getMessage());
            return;
        }
        SQLiteConnection.getInstance(this);

        Common.loadFolder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mILoginPresenter.showInfoSharePrefLogin();
    }

    //endregion

    //region onClick
    @OnClick(R.id.btLogin)
    public void clickLogin(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);

        final String userName = etUsername.getText().toString();
        final String pass = etPass.getText().toString();

        if (cbRememberLogin.isChecked()) {
            mILoginPresenter.writeSharedPrefLogin(userName, pass);
        } else
            mILoginPresenter.clearSharedPrefLogin();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPbarLogin();
                mILoginPresenter.validateInput(userName, pass);
            }
        }, TIME_DELAY_ANIM);
    }

    //endregion

    //region private param
    @BindView(R.id.etTaiKhoan)
    EditText etUsername;
    @BindView(R.id.etMatKhau)
    EditText etPass;
    @BindView(R.id.cb_ac_login_save_info_login)
    CheckBox cbRememberLogin;
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.pbar_ac_login_login)
    ProgressBar pbarLogin;
    @BindView(R.id.tv_ac_login_message)
    TextView tvMessage;
    @BindViews({R.id.btLogin, R.id.tv_ac_login_message})
    List<View> viewsLogin;

    private ILoginPresenter mILoginPresenter;
    //endregion
}
