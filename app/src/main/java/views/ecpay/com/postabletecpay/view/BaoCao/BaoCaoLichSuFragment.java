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
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.ReportLichSuThanhToanAdapter;
import views.ecpay.com.postabletecpay.presenter.IReportLichSuThanhToanPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportLichSuThanhToanPresenter;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoLichSuFragment extends Fragment implements View.OnClickListener, IReportLichSuThanhToanView {

    @BindView(R.id.rbMaKH) RadioButton rbMaKH;
    @BindView(R.id.rbTenKH) RadioButton rbTenKH;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.btTimKiem) Button btTimKiem;
    @BindView(R.id.rvDanhSach) RecyclerView rvDanhSach;


    IReportLichSuThanhToanPresenter reportLichSuThanhToanPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_lichsu, container, false);
        ButterKnife.bind(this, view);

        btTimKiem.setOnClickListener(this);

        reportLichSuThanhToanPresenter = new ReportLichSuThanhToanPresenter(this);

        return view;
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
}
