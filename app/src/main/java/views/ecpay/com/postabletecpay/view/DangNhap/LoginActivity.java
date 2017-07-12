package views.ecpay.com.postabletecpay.view.DangNhap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ResponseLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.BodyGetPCInfoRespone;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.GetPCInfoRespone;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.BaseActivity;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by macbook on 4/28/17.
 */

public class LoginActivity extends BaseActivity implements ILoginView {
    private static final int MY_REQUEST_CODE = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideActionBar();

        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, MY_REQUEST_CODE);
            }
        }else {
            initSource();
        }
        ButterKnife.bind(this);
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
        hidePbarLogin();
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED
                        || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginActivity.this, "Unable to show permission required", Toast.LENGTH_LONG).show();
                }
                initSource();
                return;
            }
        }
    }
    @Override
    public void hideTextMessage() {
        tvMessage.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void showMainScreen(String edong) {
        hidePbarLogin();
        hideTextMessage();

        if (edong == null || edong.isEmpty() || edong.trim().equals("")) {
            showTextMessage(Common.MESSAGE_NOTIFY.LOGIN_ERR_EDONG.toString());
            return;
        }

        //this.LoadPCInfo(edong, "01683861612");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(KEY_EDONG, edong);
        startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected  void LoadPCInfo(String edong, String phoneName)
    {
        Context context = this.getContext();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.GET_PC_INFO.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


        String json = SoapAPI.getJsonGetPCInfo(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                "",
                "",
                "",
                "",
                "",
                configInfo.getAccountId()
        );


        if (json == null)
            return;


        try {
            final SoapAPI.AsyncSoapGetPCInfo soap = new SoapAPI.AsyncSoapGetPCInfo(phoneName, new SoapAPI.AsyncSoapGetPCInfo.AsyncSoapGetPCInfoCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoapGetPCInfo soap) {

                }

                @Override
                public void onUpdate(String message) {

                }

                @Override
                public void onPost(GetPCInfoRespone response, String phone) {
                    Log.d("LOG", "phone = " + phone);

                    String responseLoginResponseData = ((BodyGetPCInfoRespone)response.getBody()).getListEvnPCLoginResponse();
                    // định dạng kiểu Object JSON
                    Type type = new TypeToken<List<ListEvnPCLoginResponse>>() {
                    }.getType();
                    List<ListEvnPCLoginResponse> responseLoginResponse = null;
                    try {
                        responseLoginResponse = new Gson().fromJson(responseLoginResponseData, type);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapGetPCInfo soap) {

                }
            });

            if (soap.getStatus() != AsyncTask.Status.RUNNING) {
                soap.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChangePassResponse changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                        } finally {
                            if (changePassResponse == null) {
                                soap.callCountdown(soap);
                            }
                        }
                    }
                });

                soapChangePassThread.start();
            }
        } catch (Exception e) {
            return;
        }
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
            Common.makeRootFolderAndGetDataHelp(this);
            Common.makeRootFolderLog();
        } catch (Exception e) {
            showTextMessage(e.getMessage());
            return;
        }
        SQLiteConnection.getInstance(this).getWritableDatabase();

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
