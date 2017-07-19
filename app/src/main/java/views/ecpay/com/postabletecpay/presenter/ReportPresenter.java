package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.ReportModel;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by duydatpham on 6/24/17.
 */

public abstract class ReportPresenter implements IReportPrensenter {

    protected ReportModel reportModel;


    public ReportPresenter(ICommonView view)
    {
        reportModel = new ReportModel(view.getContextView());
    }

    @Override
    public ReportModel getModel() {
        return reportModel;
    }
}
