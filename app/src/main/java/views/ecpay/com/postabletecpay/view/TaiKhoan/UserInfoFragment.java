package views.ecpay.com.postabletecpay.view.TaiKhoan;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.ILogoutPresenter;
import views.ecpay.com.postabletecpay.presenter.IUserInfoPresenter;
import views.ecpay.com.postabletecpay.presenter.LogoutPresenter;
import views.ecpay.com.postabletecpay.presenter.MainPagePresenter;
import views.ecpay.com.postabletecpay.presenter.UserInfoPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.ChangePassActivity;
import views.ecpay.com.postabletecpay.view.Logout.ILogoutView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by macbook on 4/28/17.
 */

public class UserInfoFragment extends Fragment implements IUserInfoView, ILogoutView {

    private IUserInfoPresenter mIUserInfoPresenter;
    private ILogoutPresenter mILogoutPresenter;
    private String mEdong;

    @Nullable
    @BindView(R.id.ibtn_frag_user_info_back)
    ImageButton ibBack;
    @Nullable
    @BindView(R.id.tvTaiKhoan)
    TextView tvTaiKhoan;
    @Nullable
    @BindView(R.id.tvNgayDangKy)
    TextView tvNgayDangKy;
    @Nullable
    @BindView(R.id.tvTenTaiKhoan)
    TextView tvTenTaiKhoan;
    @Nullable
    @BindView(R.id.tvCMND)
    TextView tvCMND;
    @Nullable
    @BindView(R.id.tvDienThoai)
    TextView tvDienThoai;
    @Nullable
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @Nullable
    @BindView(R.id.tvDiaChi)
    TextView tvDiaChi;
    @Nullable
    @BindView(R.id.tvSoTaiKhoan)
    TextView tvSoTaiKhoan;
    @Nullable
    @BindView(R.id.tv_frag_taikhoan_sodu)
    TextView tvSoDuKhaDung;
    @Nullable
    @BindView(R.id.tvLoaiTaiKhoan)
    TextView tvLoaiTaiKhoan;
    @Nullable
    @BindView(R.id.btn_frag_tai_khoan_change_pass)
    Button btDoiMatKhau;
    @Nullable
    @BindView(R.id.btn_frag_user_info_logout)
    Button btDangXuat;

    //dialog logout
    @Nullable
    @BindView(R.id.rv_diaglog_logout_process)
    RelativeLayout rvLogoutProcess;
    @Nullable
    @BindView(R.id.tv_diaglog_logout_response)
    TextView tvLogoutResponse;
    @Nullable
    @BindView(R.id.pbar_diaglog_logout)
    ProgressBar pbarLogout;
    @Nullable
    @BindView(R.id.btn_dialog_logout_cancel)
    TextView btnLogoutCancel;
    @Nullable
    @BindView(R.id.btn_dialog_logout_ok)
    TextView btnLogoutOK;

    private OnFragmentInteractionListener listener;
    private Dialog dialogLogout;
    private Unbinder unbinder;
    View viewfragment;

    public static UserInfoFragment newInstance(String edong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, edong);

        UserInfoFragment userInfoFragment = new UserInfoFragment();
        userInfoFragment.setArguments(bundle);

        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIUserInfoPresenter = new UserInfoPresenter(this);
        mILogoutPresenter = new LogoutPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewfragment = inflater.inflate(R.layout.fragment_tai_khoan, container, false);
        unbinder = ButterKnife.bind(this, viewfragment);

        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);
        ((MainActivity)this.getContextView()).switchNavigationBottomMenu(MainActivity.ID_MENU_BOTTOM.ACCOUNT);
        mIUserInfoPresenter.getInfoUser(mEdong);
        return viewfragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPayFragmentInteractionListener");
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup viewGroup = (ViewGroup) getView();
        assert viewGroup != null;
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null); viewGroup.addView(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //region onClick fragment
    @Optional
    @OnClick(R.id.btn_frag_tai_khoan_change_pass)
    public void clickChangePass(View v) {
        Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(UserInfoFragment.this.getActivity(), ChangePassActivity.class).putExtra(KEY_EDONG, mEdong));
            }
        }, Common.TIME_DELAY_ANIM);
    }

    @Optional
    @OnClick(R.id.btn_frag_user_info_logout)
    public void clickLogout(View view) {

        Common.runAnimationClickViewScale(view, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogLogoutUserInfo();
            }
        }, TIME_DELAY_ANIM);
    }

    @Optional
    @OnClick(R.id.ibtn_frag_user_info_back)
    public void clickBack(View v) {
        Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Fragment fragment = MainPageFragment.newInstance(mEdong);
                    if (fragment != null) {
                        FragmentTransaction fragmentTransaction = UserInfoFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, fragment);
                        fragmentTransaction.commit();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, Common.TIME_DELAY_ANIM);
    }

    //endregion

    //region onClick dialog Logout
    @Optional
    @OnClick(R.id.btn_dialog_logout_ok)
    public void clickLogoutOK(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, TIME_DELAY_ANIM);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIUserInfoPresenter.callLogout(mEdong);
                mILogoutPresenter.callLogout(mEdong);
            }
        }, TIME_DELAY_ANIM);
    }

    @Optional
    @OnClick(R.id.btn_dialog_logout_cancel)
    public void clickLogoutCancel(View view) {
        if (dialogLogout != null)
            dialogLogout.dismiss();
            unbinder = ButterKnife.bind(this, viewfragment);
    }
    //endregion

    //region interface
    @Override
    public void showInfoUser(String userName, String dateRegister, String accountName, String indentityCard, String phone, String email, String address, String numberAccount,
                             long balance, int typeAccount) {
        if (userName == null)
            userName = "";
        if (dateRegister == null)
            dateRegister = "";
        if (accountName == null)
            accountName = "";
        if (indentityCard == null)
            indentityCard = "";
        if (phone == null)
            phone = "";
        if (email == null)
            email = "";
        if (address == null)
            address = "";
        if (numberAccount == null)
            numberAccount = "";

        tvNgayDangKy.setText(dateRegister);
        tvTenTaiKhoan.setText(accountName);
        tvTaiKhoan.setText(userName);
        tvCMND.setText(indentityCard);
        tvDienThoai.setText(phone);
        tvEmail.setText(email);
        tvDiaChi.setText(address);
        tvSoTaiKhoan.setText(numberAccount);
        String balanceText = Common.convertLongToMoney(balance);
        tvSoDuKhaDung.setText(balanceText);
        tvLoaiTaiKhoan.setText(Common.TYPE_ACCOUNT.findCodeMessage(typeAccount).getTypeString());
    }

    @Override
    public Context getContextView() {
        return getActivity();
    }
    //endregion

    private void showDialogLogoutUserInfo() {
        dialogLogout = new Dialog(this.getActivity());
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.setContentView(R.layout.dialog_logout);
        dialogLogout.setCanceledOnTouchOutside(true);
        dialogLogout.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialogLogout.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialogLogout.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        unbinder.unbind();
        unbinder = ButterKnife.bind(this, dialogLogout);
        this.showStatusProgressLogout(Common.STATUS_PROGRESS.FINISH);
        dialogLogout.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        dialogLogout.show();
    }

    @Override
    public void showStatusProgressLogout(Common.STATUS_PROGRESS statusProgress) {
        rvLogoutProcess.setVisibility(View.VISIBLE);
        tvLogoutResponse.setVisibility(View.VISIBLE);
        pbarLogout.setVisibility(View.VISIBLE);

        btnLogoutCancel.setEnabled(true);
        btnLogoutOK.setEnabled(true);

        if (statusProgress == Common.STATUS_PROGRESS.BEGIN) {
            tvLogoutResponse.setVisibility(View.GONE);
            btnLogoutOK.setEnabled(false);
        }

        if (statusProgress == Common.STATUS_PROGRESS.ERROR) {
            tvLogoutResponse.setVisibility(View.VISIBLE);
            btnLogoutOK.setEnabled(true);
        }

        if (statusProgress == Common.STATUS_PROGRESS.SUCCESS) {
            tvLogoutResponse.setVisibility(View.VISIBLE);
            btnLogoutOK.setEnabled(false);
            btnLogoutCancel.setEnabled(false);
        }

        if (statusProgress == Common.STATUS_PROGRESS.FINISH) {
            rvLogoutProcess.setVisibility(View.INVISIBLE);
            btnLogoutOK.setEnabled(true);
        }
    }

    //region ILogoutView
    @Override
    public void showMessageLogout(String textMessage) {
        boolean fail = TextUtils.isEmpty(textMessage) || isHasNullViewLogoutDialog();
        if (fail)
            return;

        tvLogoutResponse.setText(textMessage);
    }

    public boolean isHasNullViewLogoutDialog() {
        return rvLogoutProcess == null || tvLogoutResponse == null || pbarLogout == null || btnLogoutOK == null || btnLogoutCancel == null;
    }

//    @Override
//    public void doClickOK() {
//
//    }
//
//    @Override
//    public void doClickCancel() {
//
//    }

    @Override
    public void showLoginForm() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    //endregion

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this.getContext(), "CODE: " + code + "\n DESCRIPTION: " + description, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
