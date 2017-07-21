package views.ecpay.com.postabletecpay.view.BaoCao;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/*
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
*/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.ReportChiTietAdapter;
import views.ecpay.com.postabletecpay.model.adapter.ReportHoanTraAdapter;
import views.ecpay.com.postabletecpay.presenter.IReportChiTietPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportChiTietPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportHoanTraPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
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
    public static ReportHoanTraAdapter adapter;

    IReportChiTietPresenter presenter;

    IBaoCaoView baoCaoView;

    public static BaoCaoHoanTraFragment newInstance(IBaoCaoView view)
    {
        BaoCaoHoanTraFragment fragment = new BaoCaoHoanTraFragment();
        fragment.baoCaoView = view;
        return fragment;
    }

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

        adapter = new ReportHoanTraAdapter();
        rvDanhSach.setAdapter(adapter);
        rvDanhSach.invalidate();


        txtSoHD.setText("0");
        txtTongTien.setText(Common.convertLongToMoney(0));


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
        }if (v.getId() == R.id.btnExport){
            if (adapter.getmBills().size() != 0) {
                Date date = new Date();
                String strDateFormat = "dd_MM_yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                saveExcelFile("Bao_cao_hoan_tra_" +sdf.format(date) +".xls");

            }else {
                Toast.makeText(getContext(),"Không có hóa đơn",Toast.LENGTH_LONG).show();
            }
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
    public void fill(List<EntityHoaDonThu> lst) {
        adapter = (ReportHoanTraAdapter)rvDanhSach.getAdapter();
        adapter.setmBills(lst);
        adapter.notifyDataSetChanged();

        txtSoHD.setText(String.valueOf(lst.size()));



        if (lst.size() == 0)
        {
            txtTongTien.setText(Common.convertLongToMoney(0));
            showMessage("Không Tồn Tại Hoá Đơn Thoả Mãn Điều Kiện Tìm Kiếm");
            return;
        }else
        {
            long total = 0;
            for (int i = 0, n = lst.size();  i < n; i ++){
                total += lst.get(i).getSO_TIEN_TTOAN();
            }


            txtTongTien.setText(Common.convertLongToMoney(total));

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

    @Override
    public void onStart() {
        super.onStart();
        this.baoCaoView.showBackBtn(false);
    }

    private boolean saveExcelFile(String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("a", "Storage not available or read only");
            return false;
        }

        boolean success = false;
        /*
        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        CellStyle cs1 = wb.createCellStyle();
        cs1.setFillForegroundColor(HSSFColor.WHITE.index);
        cs1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings
        Row row1 = sheet1.createRow(0);
        c = row1.createCell(2);
        c.setCellValue("BÁO CÁO THU CHI TIẾT");
        c.setCellStyle(cs);

        Row row = sheet1.createRow(3);

        c = row.createCell(0);
        c.setCellValue("Mã Khách Hàng");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Tên");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Địa Chỉ");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Kỳ");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Số Tiền");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Ngày Thu");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Lý Do Hoàn Trả");
        c.setCellStyle(cs);

        Bangexcel(c,cs1,sheet1);
        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 300));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 400));
        sheet1.setColumnWidth(6, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(Common.PATH_FOLDER_LOG , fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            success = true;
            Toast.makeText(getContext(),"Đã xuất ra file excel thành công",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        */
        return success;
    }
    /*
    private static void Bangexcel(Cell c, CellStyle cs, Sheet sheet1){
        if (adapter.getmBills().size() != 0) {

            for (int i = 0; i < adapter.getmBills().size(); i++) {
                Row row = sheet1.createRow(i + 4);

                c = row.createCell(0);
                c.setCellValue(adapter.getmBills().get(i).getMA_KHANG());
                c.setCellStyle(cs);

                c = row.createCell(1);
                c.setCellValue(adapter.getmBills().get(i).getTEN_KHANG());
                c.setCellStyle(cs);

                c = row.createCell(2);
                c.setCellValue(adapter.getmBills().get(i).getDIA_CHI());
                c.setCellStyle(cs);

                c = row.createCell(3);
                c.setCellValue(Common.parse(adapter.getmBills().get(i).getTHANG_TTOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString()));
                c.setCellStyle(cs);

                c = row.createCell(4);
                c.setCellValue(adapter.getmBills().get(i).getSO_TIEN_TTOAN()+"");
                c.setCellStyle(cs);

                c = row.createCell(5);
                c.setCellValue(Common.parse(adapter.getmBills().get(i).getNGAY_THU(), Common.DATE_TIME_TYPE.ddMMyyyy.toString()));
                c.setCellStyle(cs);

                c = row.createCell(6);
                c.setCellValue(adapter.getmBills().get(i).getTRANG_THAI_HOAN_TRA());
                c.setCellStyle(cs);
            }
        }

    }
    */

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
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
