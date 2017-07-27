package views.ecpay.com.postabletecpay.view.TrangChu;

import android.app.ActionBar;
import android.app.Activity;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import views.ecpay.com.postabletecpay.presenter.IMainPagePresenter;
import views.ecpay.com.postabletecpay.presenter.LogoutPresenter;
import views.ecpay.com.postabletecpay.presenter.MainPagePresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.MenuItem;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.PopupMenu;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.ChangePassActivity;
import views.ecpay.com.postabletecpay.view.IBackHandler;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TEXT_EMPTY;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;

/**
 * Created by macbook on 4/28/17.
 */

public class MainPageFragment extends Fragment implements
        IMainPageView,
        View.OnClickListener, PopupMenu.OnItemSelectedListener {

    private OnFragmentInteractionListener listener;
    private String mEdong;

    //main page fragment
    @Nullable
    @BindView(R.id.iv_frag_main_avatar)
    ImageView ivAvatar;
    @Nullable
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @Nullable
    @BindView(R.id.tvSoDuKhaDung)
    TextView tvSoDuKhaDung;
    @Nullable
    @BindView(R.id.tvSoHoaDon)
    TextView tvSoHoaDon;
    @Nullable
    @BindView(R.id.tv_fragment_thanh_toan_total_bills_money)
    TextView tvTongTien;
    @Nullable
    @BindView(R.id.ibtn_frag_main_exit)
    ImageButton ibtnExit;
    @Nullable
    @BindView(R.id.ibTroGiup)
    ImageButton ibTroGiup;
    @Nullable
    @BindView(R.id.btXoaDuLieu)
    Button btXoaDuLieu;
    @Nullable
    @BindView(R.id.btThanhToan)
    Button btThanhToan;
    @Nullable
    @BindView(R.id.btBaoCao)
    Button btBaoCao;
    @Nullable
    @BindView(R.id.btnChuyenViTong)
    Button btnChuyenViTong;
    @Nullable
    @BindView(R.id.btnDinhDanhThe)
    Button btnDinhDanhThe;

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


    private String[] arrPopupMenu = {"Thông tin tài khoản", "Đổi mật khẩu", "Hướng dẫn", "Phiên bản", "Đóng"};

    private final static int INFO_USER = 0;
    private final static int CHANGE_PASS = 1;
    private final static int HELP = 2;
    private final static int VERSION = 3;
    private final static int EXIT = 4;


    private IMainPagePresenter mIMainPagePresenter;
    private ILogoutPresenter mILogoutPresenter;
    private Unbinder unbinder;
    private Dialog dialogLogout;
    View curentFragment;

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
        onSaveInstanceState(savedInstanceState);
        setRetainInstance(true);
        mIMainPagePresenter = new MainPagePresenter(this);
        mILogoutPresenter = new LogoutPresenter(this);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        curentFragment = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        unbinder = ButterKnife.bind(this, curentFragment);

        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);

        ibTroGiup.setOnClickListener(this);
        btXoaDuLieu.setOnClickListener(this);
        btThanhToan.setOnClickListener(this);
        btBaoCao.setOnClickListener(this);
        btnChuyenViTong.setOnClickListener(this);
        btnDinhDanhThe.setOnClickListener(this);
        ((MainActivity) this.getContextView()).switchNavigationBottomMenu(MainActivity.ID_MENU_BOTTOM.HOME);
        this.refreshInfoMain();
        return curentFragment;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //region onClick fragment
    @Optional
    @OnClick(R.id.ibtn_frag_main_exit)
    public void onClickExit(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogLogoutMain();
            }
        }, TIME_DELAY_ANIM);
    }

    @Optional
    @OnClick(R.id.iv_frag_main_avatar)
    public void onClickAvatar(View view) {
        final PopupMenu menu = new PopupMenu(MainPageFragment.this.getActivity());
        menu.setOnItemSelectedListener(MainPageFragment.this);
        menu.add(INFO_USER, arrPopupMenu[0]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(CHANGE_PASS, arrPopupMenu[1]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(HELP, arrPopupMenu[2]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        menu.add(VERSION, arrPopupMenu[3]);
        menu.add(EXIT, arrPopupMenu[4]);

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
                        startActivity(new Intent(MainPageFragment.this.getActivity(), ChangePassActivity.class).putExtra(Common.KEY_EDONG, mEdong));
                        break;

                    case HELP:
                        showDialogHelp();
                        break;

                    case VERSION:
                        showDialogVesion();
                        break;

                    case EXIT:
                        menu.dismiss();
                        break;
                }
            }
        });

        menu.show(view);
    }

    @Optional
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.btXoaDuLieu:
                this.showDialogDeleteData();
                break;
            case R.id.btThanhToan:
                fragment = PayFragment.newInstance(mEdong);
                break;
            case R.id.btBaoCao:
                fragment = BaoCaoFragment.newInstance(mEdong);
                break;
            case R.id.ibTroGiup:
                showDialogHoTro();
                break;
            case R.id.btnChuyenViTong:
                fragment = CashTranferFragment.newInstance(mEdong, new IBackHandler() {
                    @Override
                    public void onBack(FragmentActivity activity, String eDong) {
                        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, MainPageFragment.newInstance(eDong));
                        fragmentTransaction.commit();
                    }
                });
                break;
            case R.id.btnDinhDanhThe:
                fragment = SearchCustomerFragment.newInstance(mEdong);
                break;
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = MainPageFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }
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
                try {
                    mILogoutPresenter.callLogout(mEdong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, TIME_DELAY_ANIM);
    }

    @Optional
    @OnClick(R.id.btn_dialog_logout_cancel)
    public void clickLogoutCancel(View view) {
        if (dialogLogout != null)
            dialogLogout.dismiss();
        unbinder = ButterKnife.bind(this, curentFragment);
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
        if (userName == null)
            userName = TEXT_EMPTY;
        try {

            tvUsername.setText(userName);
            tvSoDuKhaDung.setText(Common.convertLongToMoney(balance));
            tvSoHoaDon.setText(String.valueOf(totalBills));
            tvTongTien.setText(Common.convertLongToMoney(totalMoney));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showTextMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
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
            rvLogoutProcess.setVisibility(View.GONE);
            btnLogoutOK.setEnabled(true);
        }
    }

    @Override
    public void showMessageLogout(String textMessage) {
        boolean fail = TextUtils.isEmpty(textMessage) || isHasNullViewLogoutDialog();
        if (fail)
            return;

        tvLogoutResponse.setText(textMessage);

    }

    @Override
    public void showLoginForm() {
        MainPageFragment.this.getActivity().finish();
        startActivity(new Intent(MainPageFragment.this.getActivity(), LoginActivity.class));
    }

    @Override
    public Context getContextView() {
        return getContext();
    }

    public boolean isHasNullViewLogoutDialog() {
        return rvLogoutProcess == null || tvLogoutResponse == null || pbarLogout == null || btnLogoutOK == null || btnLogoutCancel == null;
    }

    public void refreshInfoMain() {
        mIMainPagePresenter.callInfoMain(mEdong);
    }

    //endregion

    public interface OnFragmentInteractionListener {
        void switchNavigationBottomMenu(MainActivity.ID_MENU_BOTTOM pay);
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
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogLogoutMain() {
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

    private void showDialogHelp() {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_help);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ibtn_dialog_help_close);

        Button btOK = (Button) dialog.findViewById(R.id.btn_dialog_help_ok);
        TextView tvHelp = (TextView) dialog.findViewById(R.id.tv_dialog_help_content);
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvHelp.setText(Html.fromHtml(Common.getDataFileHelp(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvHelp.setText(Html.fromHtml(Common.getDataFileHelp()));
        }

        dialog.show();
    }


    public void showDialogDeleteData() {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete_data);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button ibClose = (Button) dialog.findViewById(R.id.btn_dialog_logout_cancel);

        Button btOK = (Button) dialog.findViewById(R.id.btn_dialog_logout_ok);

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIMainPagePresenter.deleteData();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogVesion() {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_version);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ibtn_dialog_version_close);

        Button btOK = (Button) dialog.findViewById(R.id.btn_dialog_version_button_ok);
        ImageButton btDetailVersion = (ImageButton) dialog.findViewById(R.id.btn_dialog_version_content_update);
        final ListView list = (ListView) dialog.findViewById(R.id.tv_dialog_version_detail);
        TextView tvVersion = (TextView) dialog.findViewById(R.id.tv_dialog_version);

        tvVersion.setText("Phiên bản " + Common.getVersionApp(this.getActivity()));
        list.setVisibility(View.GONE);
        btDetailVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setVisibility(View.VISIBLE);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainPageFragment.this.getActivity(), R.layout.simple_spinner_dropdown_item_provider, Common.getDetailVersion());
                dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_provider);
                list.setAdapter(dataAdapter);
            }
        });
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
