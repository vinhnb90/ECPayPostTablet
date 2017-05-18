package views.ecpay.com.postabletecpay.view.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.ILoginPresenter;
import views.ecpay.com.postabletecpay.presenter.LoginPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.view.BaseActivity;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

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

        super.runAnimationClickViewScale(tvMessage, R.anim.scale_view_pull);

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
    //endregion

    //region onClick
    @OnClick(R.id.btLogin)
    public void clickLogin(View view) {
        super.runAnimationClickViewScale(view, R.anim.scale_view_push);
        showPbarLogin();

        String userName = etUsername.getText().toString();
        String pass = etPass.getText().toString();

        mILoginPresenter.validateInput(userName, pass);
    }
    //endregion

    //region private param
    @BindView(R.id.etTaiKhoan)
    EditText etUsername;
    @BindView(R.id.etMatKhau)
    EditText etPass;
    @BindView(R.id.cbNhoMatKhau)
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
