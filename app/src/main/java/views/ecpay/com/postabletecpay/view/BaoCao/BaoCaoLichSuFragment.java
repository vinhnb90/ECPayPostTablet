package views.ecpay.com.postabletecpay.view.BaoCao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.ReportLichSuThanhToanAdapter;
import views.ecpay.com.postabletecpay.presenter.IReportLichSuThanhToanPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportLichSuThanhToanPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoLichSuFragment extends Fragment implements View.OnClickListener, IReportLichSuThanhToanView {

    IBaoCaoView baoCaoView;

    @BindView(R.id.rlChiTiet)
    LinearLayout rlChiTiet;
    @BindView(R.id.rlDanhSach)
    RelativeLayout rlDanhSach;


    @BindView(R.id.rbMaKH) RadioButton rbMaKH;
    @BindView(R.id.rbTenKH) RadioButton rbTenKH;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.btTimKiem) Button btTimKiem;
    @BindView(R.id.rvDanhSach) RecyclerView rvDanhSach;

    @BindView(R.id.tvTenKH) TextView tvTenKH;
    @BindView(R.id.tvMaKH) TextView tvMaKH;
    @BindView(R.id.tvKyThanhToan) TextView tvKyThanhToan;
    @BindView(R.id.tvNgayPhatSinh) TextView tvNgayPhatSinh;
    @BindView(R.id.tvSoTien) TextView tvSoTien;
    @BindView(R.id.tvTrangThai) TextView tvTrangThai;




    IReportLichSuThanhToanPresenter reportLichSuThanhToanPresenter;

    Unbinder unbinder;


    public static BaoCaoLichSuFragment newInstance(IBaoCaoView view)
    {
        BaoCaoLichSuFragment fragment = new BaoCaoLichSuFragment();
        fragment.baoCaoView = view;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_lichsu, container, false);
        unbinder = ButterKnife.bind(this, view);

        btTimKiem.setOnClickListener(this);

        reportLichSuThanhToanPresenter = new ReportLichSuThanhToanPresenter(this);

        rlDanhSach.setVisibility(View.VISIBLE);
        rlChiTiet.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btTimKiem)
        {
            reportLichSuThanhToanPresenter.search(rbMaKH.isChecked() , etSearch.getText().toString());
        }
    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }

    @Override
    public void fill(Account account, int hdGiao, long tienGiao, int dhThu, long tienThu, int hdVangLai, long tienVangLai, int hdTraKH, long tienTraKHH) {

    }

    @Override
    public void fill(List<ReportLichSuThanhToanAdapter.LichSuThanhToanData> lst) {
        ReportLichSuThanhToanAdapter adapter = new ReportLichSuThanhToanAdapter(lst, this);
        rvDanhSach.setAdapter(adapter);
        rvDanhSach.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDanhSach.setHasFixedSize(true);
        rvDanhSach.invalidate();
    }

    @Override
    public void showChiTiet(EntityLichSuThanhToan lichSuThanhToan) {

        tvTenKH.setText(lichSuThanhToan.getTEN_KHANG());
        tvMaKH.setText(lichSuThanhToan.getMA_KHANG());
        tvKyThanhToan.setText(Common.parse(lichSuThanhToan.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString()));
        tvNgayPhatSinh.setText(Common.parse(lichSuThanhToan.getNGAY_PHAT_SINH(), Common.DATE_TIME_TYPE.ddMMyyyy.toString()));
        tvSoTien.setText(Common.convertLongToMoney(lichSuThanhToan.getSO_TIEN_TTOAN()));
        tvTrangThai.setText(Common.MA_GIAO_DICH.findCode(lichSuThanhToan.getMA_GIAO_DICH()).getMessage());


        rlDanhSach.setVisibility(View.INVISIBLE);
        rlChiTiet.setVisibility(View.VISIBLE);

        baoCaoView.showBackBtn(true);
    }

    public void hideLichSu()
    {

        rlDanhSach.setVisibility(View.VISIBLE);
        rlChiTiet.setVisibility(View.INVISIBLE);

        baoCaoView.showBackBtn(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(rlChiTiet.getVisibility() == View.VISIBLE)
        {
            this.baoCaoView.showBackBtn(true);
        }else
        {
            this.baoCaoView.showBackBtn(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(rlChiTiet.getVisibility() == View.VISIBLE)
        {
            this.baoCaoView.showBackBtn(true);
        }else
        {
            this.baoCaoView.showBackBtn(false);
        }
    }

    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this.getContext(), "CODE: " + code, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
