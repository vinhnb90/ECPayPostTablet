package views.ecpay.com.postabletecpay.view.BaoCao;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.ReportChiTietAdapter;
import views.ecpay.com.postabletecpay.model.adapter.ReportHoanTraAdapter;
import views.ecpay.com.postabletecpay.presenter.IReportChiTietPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportChiTietPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportHoanTraPresenter;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoHoanTraFragment extends Fragment implements View.OnClickListener, IReportHoanTraView {

    @BindView(R.id.rbMaKH) RadioButton rbMaKH;
    @BindView(R.id.rbTenKH) RadioButton rbTenKH;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.etTuNgay) EditText etTuNgay;
    @BindView(R.id.etDenNgay) EditText etDenNgay;
    @BindView(R.id.btTimKiem) Button btTimKiem;
    @BindView(R.id.rvDanhSach) RecyclerView rvDanhSach;



    @Nullable
    @BindView(R.id.txtSoHD)
    TextView txtSoHD;
    @Nullable
    @BindView(R.id.txtTongTien)
    TextView txtTongTien;
    @Nullable
    @BindView(R.id.btnExport)
    ImageButton btnExport;

    Unbinder unbinder;

    private Calendar tuNgayDate, denNgayDate;


    IReportChiTietPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_hoantra, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new ReportHoanTraPresenter(this);


        tuNgayDate = Calendar.getInstance();
        denNgayDate = Calendar.getInstance();


        btnExport.setOnClickListener(this);
        btTimKiem.setOnClickListener(this);
        etTuNgay.setOnClickListener(this);
        etDenNgay.setOnClickListener(this);
        etTuNgay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showChooseDate(tuNgayDate, etTuNgay, false);
                }
            }
        });
        etDenNgay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showChooseDate(tuNgayDate, etDenNgay, true);
                }
            }
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDanhSach.setLayoutManager(layoutManager);


        rvDanhSach.setAdapter(new ReportHoanTraAdapter());
        rvDanhSach.invalidate();


        txtSoHD.setText("0");
        txtTongTien.setText("0");


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.etTuNgay)
        {
            this.showChooseDate(tuNgayDate, etTuNgay, false);
            return;
        }
        if(v.getId() == R.id.etDenNgay)
        {
            this.showChooseDate(tuNgayDate, etDenNgay, true);
            return;
        }
        if(v.getId() == R.id.btTimKiem)
        {
            presenter.search(rbMaKH.isChecked(), etSearch.getText().toString(), etTuNgay.getText().length() == 0 ? null : tuNgayDate, etDenNgay.getText().length() == 0 ? null : denNgayDate);
            return;
        }
    }

    void showChooseDate(final Calendar from, final EditText view, boolean hasMin) {


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                from.set(i, i1, i2);
                view.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }
        }, from.get(Calendar.YEAR), from.get(Calendar.MONTH), from.get(Calendar.DAY_OF_MONTH));
        if(hasMin)
            dialog.getDatePicker().setMinDate(from.getTimeInMillis());
        dialog.show();
    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }

    @Override
    public void fill(List<Bill> lst) {
        ReportHoanTraAdapter adapter = (ReportHoanTraAdapter)rvDanhSach.getAdapter();
        adapter.setmBills(lst);
        adapter.notifyDataSetChanged();


        txtSoHD.setText(String.valueOf(lst.size()));

        if (lst.size() == 0)
        {
            showMessage("Không Tồn Tại Hoá Đơn Thoả Mãn Điều Kiện Tìm Kiếm");
            return;
        }
    }

    @Override
    public void showMessage(String message) {
        try{
            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }
}
