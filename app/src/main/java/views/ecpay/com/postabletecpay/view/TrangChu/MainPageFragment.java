package views.ecpay.com.postabletecpay.view.TrangChu;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IMainPagePresenter;
import views.ecpay.com.postabletecpay.presenter.MainPagePresenter;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesNoDialog;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.commons.Common.TEXT_DIALOG;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.MenuItem;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.PopupMenu;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.ChangePassActivity;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.TEXT_EMPTY;
import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by macbook on 4/28/17.
 */

public class MainPageFragment extends Fragment implements IMainPageView, View.OnClickListener, PopupMenu.OnItemSelectedListener {

    private OnFragmentInteractionListener listener;
    private String mEdong;
    @BindView(R.id.iv_frag_main_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvSoDuKhaDung)
    TextView tvSoDuKhaDung;
    @BindView(R.id.tvSoHoaDon)
    TextView tvSoHoaDon;
    @BindView(R.id.tvTongTien)
    TextView tvTongTien;
    @BindView(R.id.ibtn_frag_main_exit)
    ImageButton ibtnExit;
    @BindView(R.id.ibTroGiup)
    ImageButton ibTroGiup;
    @BindView(R.id.btXoaDuLieu)
    Button btXoaDuLieu;
    @BindView(R.id.btThanhToan)
    Button btThanhToan;
    @BindView(R.id.btBaoCao)
    Button btBaoCao;

    private String[] arrPopupMenu = {"Thông tin tài khoản", "Đổi mật khẩu", "Hướng dẫn", "Đóng"};

    private final static int INFO_USER = 0;
    private final static int CHANGE_PASS = 1;
    private final static int HELP = 2;
    private final static int EXIT = 3;

    private IMainPagePresenter iMainPagePresenter;

    public static MainPageFragment newInstance(String edong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, edong);

        MainPageFragment mainPageFragment = new MainPageFragment();
        mainPageFragment.setArguments(bundle);

        return mainPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iMainPagePresenter = new MainPagePresenter(this);
        iMainPagePresenter.synchronizePC();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        ButterKnife.bind(this, view);

        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);



        ibTroGiup.setOnClickListener(this);
        btXoaDuLieu.setOnClickListener(this);
        btThanhToan.setOnClickListener(this);
        btBaoCao.setOnClickListener(this);

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

    //region onClick
    @OnClick(R.id.ibtn_frag_main_exit)
    public void onClickExit(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IActionClickYesNoDialog clickYesNoDialog = new IActionClickYesNoDialog() {
                    @Override
                    public void doClickNo() {
                        MainPageFragment.this.getActivity().finish();
                        startActivity(new Intent(MainPageFragment.this.getActivity(), LoginActivity.class));
                    }

                    @Override
                    public void doClickYes() {

                    }
                };

                Common.showDialog(getActivity(), clickYesNoDialog, TEXT_DIALOG.TITLE_DEFAULT.toString(), TEXT_DIALOG.MESSAGE_EXIT.toString());
            }
        }, TIME_DELAY_ANIM);
    }

    @OnClick(R.id.iv_frag_main_avatar)
    public void onClickAvatar(View view) {
        final PopupMenu menu = new PopupMenu(MainPageFragment.this.getActivity());
        menu.setOnItemSelectedListener(MainPageFragment.this);
        menu.add(INFO_USER, arrPopupMenu[0]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(CHANGE_PASS, arrPopupMenu[1]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(HELP, arrPopupMenu[2]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(EXIT, arrPopupMenu[3]);

        menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case INFO_USER:
//                                MainActivity.updateNavigationBarState(R.id.navigation_accout);
                        Fragment fragment = null;
                        fragment = UserInfoFragment.newInstance(mEdong);
                        if (fragment != null) {
                            FragmentTransaction fragmentTransaction = MainPageFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout, fragment);
                            fragmentTransaction.commit();
                        }
                        break;

                    case CHANGE_PASS:
                        startActivity(new Intent(MainPageFragment.this.getActivity(), ChangePassActivity.class));
                        break;

                    case HELP:
                        break;

                    case EXIT:
                        menu.dismiss();
                        break;
                }
            }
        });

        menu.show(view);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.btXoaDuLieu:
                break;
            case R.id.btThanhToan:
                fragment = PayFragment.newInstance(mEdong);
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = MainPageFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.btBaoCao:
                fragment = BaoCaoFragment.newInstance();
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = MainPageFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.ibTroGiup:
                showDialogHoTro();
                break;
        }
    }
    //endregion

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemSelected(MenuItem item) {

    }

    //region IMainPageView
    @Override
    public void showMainPageInfo(String userName, long balance, int totalBills, int totalMoney) {
        if(userName == null)
            userName = TEXT_EMPTY;

        tvUsername.setText(userName);
        tvSoDuKhaDung.setText(String.valueOf(balance));
        tvSoHoaDon.setText(String.valueOf(totalBills));
        tvTongTien.setText(String.valueOf(totalMoney));

    }

    @Override
    public void showTextMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContextView() {
        return getContext();
    }
    //endregion

    public interface OnFragmentInteractionListener {
    }

    private void showDialogHoTro() {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_hotro);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ibClose);
        Button btOK = (Button) dialog.findViewById(R.id.btOK);

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
