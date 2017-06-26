package views.ecpay.com.postabletecpay.presenter;

import java.util.Calendar;
import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportHoanTraView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by duydatpham on 6/26/17.
 */

public class ReportHoanTraPresenter extends ReportPresenter implements IReportChiTietPresenter {
    IReportHoanTraView reportHoanTraView;
    public ReportHoanTraPresenter(IReportHoanTraView view) {
        super(view);
        reportHoanTraView = view;
    }

    @Override
    public void fill() {

    }

    @Override
    public void search(boolean isMaKH, String code, Calendar from, Calendar to) {
        List<Bill> lst = reportModel.getConnecttion().getBillHoanTraByCodeAndDate(MainActivity.mEdong, isMaKH, code, from, to);
        reportHoanTraView.fill(lst);
    }
}
