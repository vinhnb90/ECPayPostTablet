package views.ecpay.com.postabletecpay.view.TaiKhoan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IUserInfoPresenter;
import views.ecpay.com.postabletecpay.presenter.UserInfoPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.ChangePassActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by macbook on 4/28/17.
 */

public class UserInfoFragment extends Fragment implements IUserInfoView {

    private IUserInfoPresenter iUserInfoPresenter;
    private String mEdong;

    @BindView(R.id.ibtn_frag_user_info_back)
    ImageButton ibBack;
    @BindView(R.id.tvTaiKhoan)
    TextView tvTaiKhoan;
    @BindView(R.id.tvNgayDangKy)
    TextView tvNgayDangKy;
    @BindView(R.id.tvTenTaiKhoan)
    TextView tvTenTaiKhoan;
    @BindView(R.id.tvCMND)
    TextView tvCMND;
    @BindView(R.id.tvDienThoai)
    TextView tvDienThoai;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvDiaChi)
    TextView tvDiaChi;
    @BindView(R.id.tvSoTaiKhoan)
    TextView tvSoTaiKhoan;
    @BindView(R.id.tvSoDuKhaDung)
    TextView tvSoDuKhaDung;
    @BindView(R.id.tvLoaiTaiKhoan)
    TextView tvLoaiTaiKhoan;
    @BindView(R.id.btn_ac_frag_user_info_change_pass)
    Button btDoiMatKhau;
    @BindView(R.id.btn_frag_user_info_logout)
    Button btDangXuat;

    private OnFragmentInteractionListener listener;

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
        iUserInfoPresenter = new UserInfoPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tai_khoan, container, false);
        ButterKnife.bind(this, view);

        mEdong = getArguments().getString(KEY_EDONG, Common.EMPTY_TEXT);
        iUserInfoPresenter.getInfoUser(mEdong);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //region onCLick
    @OnClick(R.id.btn_ac_frag_user_info_change_pass)
    public void clickChangePass(View v) {
        Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(UserInfoFragment.this.getActivity(), ChangePassActivity.class));
            }
        }, Common.TIME_DELAY_ANIM);
    }

    @OnClick(R.id.btn_frag_user_info_logout)
    public void clickLogout(View v) {
        Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(UserInfoFragment.this.getActivity(), LoginActivity.class));
            }
        }, Common.TIME_DELAY_ANIM);
    }

    @OnClick(R.id.ibtn_frag_user_info_back)
    public void clickBack(View v)
    {
        Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = null;
                fragment = MainPageFragment.newInstance(mEdong);
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = UserInfoFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
            }
        }, Common.TIME_DELAY_ANIM);
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

        tvTaiKhoan.setText(userName);
        tvNgayDangKy.setText(dateRegister);
        tvTaiKhoan.setText(accountName);
        tvCMND.setText(indentityCard);
        tvDienThoai.setText(phone);
        tvEmail.setText(email);
        tvDiaChi.setText(address);
        tvSoTaiKhoan.setText(numberAccount);
        tvSoDuKhaDung.setText(String.valueOf(balance));
        tvLoaiTaiKhoan.setText(Common.TYPE_ACCOUNT.findCodeMessage(typeAccount).getTypeString());
    }

    @Override
    public Context getContextView() {
        return getActivity();
    }
    //endregion

    public interface OnFragmentInteractionListener {
    }
}
