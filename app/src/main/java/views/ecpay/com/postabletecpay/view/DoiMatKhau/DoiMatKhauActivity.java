package views.ecpay.com.postabletecpay.view.DoiMatKhau;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;

/**
 * Created by TungNV on 5/3/17.
 */

public class DoiMatKhauActivity extends ActionBarActivity implements View.OnClickListener{

    @BindView(R.id.ibBack) ImageButton ibBack;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvSDT) TextView tvSDT;
    @BindView(R.id.tvLoi) TextView tvLoi;
    @BindView(R.id.ivAvatar) ImageView ivAvatar;
    @BindView(R.id.etMatKhauCu) EditText etMatKhauCu;
    @BindView(R.id.etMatKhauMoi) EditText etMatKhauMoi;
    @BindView(R.id.etNhapLaiMatKhauMoi) EditText etNhapLaiMatKhauMoi;
    @BindView(R.id.btDoiMatKhau) Button btDoiMatKhau;

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

            setContentView(R.layout.activity_doi_mat_khau);
            ButterKnife.bind(this);

            ibBack.setOnClickListener(this);
            btDoiMatKhau.setOnClickListener(this);
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBack:
                DoiMatKhauActivity.this.finish();
                break;
            case R.id.btDoiMatKhau:
                break;
        }
    }
}
