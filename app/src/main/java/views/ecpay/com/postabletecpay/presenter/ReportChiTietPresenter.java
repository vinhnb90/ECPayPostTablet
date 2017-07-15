package views.ecpay.com.postabletecpay.presenter;

import java.util.Calendar;
import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportChiTietView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by duydatpham on 6/25/17.
 */

public class ReportChiTietPresenter extends ReportPresenter implements IReportChiTietPresenter{

    IReportChiTietView reportChiTietView;

    public ReportChiTietPresenter(IReportChiTietView view) {
        super(view);
        reportChiTietView = view;
    }

    @Override
    public void fill() {

    }

    @Override
    public void search(boolean isMaKH, String code, Calendar from, Calendar to) {
            List<EntityHoaDonThu> lst = reportModel.getConnecttion().getBillThuByCodeAndDate(MainActivity.mEdong, isMaKH, code, from, to);
            reportChiTietView.fill(lst);
    }
}
