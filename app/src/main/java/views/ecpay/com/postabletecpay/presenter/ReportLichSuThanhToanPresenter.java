package views.ecpay.com.postabletecpay.presenter;

import java.util.Calendar;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.ReportLichSuThanhToanAdapter;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportHoanTraView;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportLichSuThanhToanView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by duydatpham on 7/15/17.
 */

public class ReportLichSuThanhToanPresenter extends ReportPresenter implements IReportLichSuThanhToanPresenter {
    IReportLichSuThanhToanView reportLichSuThanhToanView;
    public ReportLichSuThanhToanPresenter(IReportLichSuThanhToanView view) {
        super(view);
        reportLichSuThanhToanView = view;
    }

    @Override
    public void fill() {

    }

    @Override
    public void search(boolean isMaKH, String code) {
        List<ReportLichSuThanhToanAdapter.LichSuThanhToanData> lst = reportModel.getConnecttion().getLichSuThanhToan(MainActivity.mEdong, isMaKH, code);
        reportLichSuThanhToanView.fill(lst);
    }
}
