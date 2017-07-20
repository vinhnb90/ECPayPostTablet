package views.ecpay.com.postabletecpay.presenter;

import android.util.Log;

import views.ecpay.com.postabletecpay.model.ReportModel;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportTongHopView;
import views.ecpay.com.postabletecpay.view.ICommonView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by duydatpham on 6/24/17.
 */

public class ReportTongHopPresenter extends ReportPresenter implements IReportTongHopPresenter {

    private IReportTongHopView reportTongHopView;
    public Account account;


    public ReportTongHopPresenter(IReportTongHopView view) {
        super(view);

        reportTongHopView = view;
    }

    @Override
    public void fill() {
        try {
            ReportModel.BillInfo billDuocGiao = reportModel.getConnecttion().countBillDuocGiao(MainActivity.mEdong);
            ReportModel.BillInfo billDaThu = reportModel.getConnecttion().countBillDaThu(MainActivity.mEdong);
            ReportModel.BillInfo billVangLai = reportModel.getConnecttion().countBillVangLai(MainActivity.mEdong);
            ReportModel.BillInfo billHoanTra = reportModel.getConnecttion().countBillHoanTra(MainActivity.mEdong);
            reportTongHopView.fill(reportModel.getAccountInfo(MainActivity.mEdong),
                    billDuocGiao.getCount(), billDuocGiao.getAmount(),
                    billDaThu.getCount(), billDaThu.getAmount(),
                    billVangLai.getCount(), billVangLai.getAmount(),
                    billHoanTra.getCount(), billHoanTra.getAmount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
