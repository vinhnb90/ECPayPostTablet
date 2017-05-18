package views.ecpay.com.postabletecpay.view.TrangChu;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.MenuItem;
import views.ecpay.com.postabletecpay.util.libs.PopupMenu.PopupMenu;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.DoiMatKhauActivity;
import views.ecpay.com.postabletecpay.view.TaiKhoan.TaiKhoanFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.ThanhToanFragment;

/**
 * Created by macbook on 4/28/17.
 */

public class TrangChuFragment extends Fragment implements View.OnClickListener, PopupMenu.OnItemSelectedListener {

    private OnFragmentInteractionListener listener;

    @BindView(R.id.ivAvatar) ImageView ivAvatar;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvSoDuKhaDung) TextView tvSoDuKhaDung;
    @BindView(R.id.tvSoHoaDon) TextView tvSoHoaDon;
    @BindView(R.id.tvTongTien) TextView tvTongTien;
    @BindView(R.id.ibThoat) ImageButton ibThoat;
    @BindView(R.id.ibTroGiup) ImageButton ibTroGiup;
    @BindView(R.id.btXoaDuLieu) Button btXoaDuLieu;
    @BindView(R.id.btThanhToan) Button btThanhToan;
    @BindView(R.id.btBaoCao) Button btBaoCao;

    private String[] arrPopupMenu = {"Thông tin tài khoản", "Đổi mật khẩu", "Hướng dẫn", "Đóng"};

    private final static int THONGTIN_TAIKHOAN = 0;
    private final static int DOI_MAT_KHAU = 1;
    private final static int HUONG_DAN = 2;
    private final static int DONG = 3;

    public static TrangChuFragment newInstance() {
        return new TrangChuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        ButterKnife.bind(this, view);

        ivAvatar.setOnClickListener(this);
        ibThoat.setOnClickListener(this);
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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.btXoaDuLieu:
                break;
            case R.id.btThanhToan:
                fragment = ThanhToanFragment.newInstance();
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = TrangChuFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.btBaoCao:
                fragment = BaoCaoFragment.newInstance();
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = TrangChuFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.ivAvatar:
                PopupMenu menu = new PopupMenu(TrangChuFragment.this.getActivity());
                menu.setOnItemSelectedListener(TrangChuFragment.this);
                menu.add(THONGTIN_TAIKHOAN, arrPopupMenu[0]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                menu.add(DOI_MAT_KHAU, arrPopupMenu[1]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                menu.add(HUONG_DAN, arrPopupMenu[2]).setIcon(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                menu.add(DONG, arrPopupMenu[3]);

                menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case THONGTIN_TAIKHOAN:
//                                MainActivity.updateNavigationBarState(R.id.navigation_accout);

                                Fragment fragment = null;
                                fragment = TaiKhoanFragment.newInstance();
                                if (fragment != null) {
                                    FragmentTransaction fragmentTransaction = TrangChuFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                                    fragmentTransaction.commit();
                                }
                                break;
                            case DOI_MAT_KHAU:
                                startActivity(new Intent(TrangChuFragment.this.getActivity(), DoiMatKhauActivity.class));
                                break;
                            case HUONG_DAN:
                                break;
                            case DONG:
                                TrangChuFragment.this.getActivity().finish();
                                break;
                        }
                    }
                });

                menu.show(v);
                break;
            case R.id.ibThoat:
                TrangChuFragment.this.getActivity().finish();
                startActivity(new Intent(TrangChuFragment.this.getActivity(), LoginActivity.class));
                break;
            case R.id.ibTroGiup:
                showDialogHoTro();
                break;
        }
    }

    @Override
    public void onItemSelected(MenuItem item) {

    }

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
