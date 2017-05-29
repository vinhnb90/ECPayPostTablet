package views.ecpay.com.postabletecpay.view.ThanhToan;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayView extends ICommonView {
    void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage);

    void showPayRecyclerOtherPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndexNew, int totalPage);
}
