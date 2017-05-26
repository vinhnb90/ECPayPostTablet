package views.ecpay.com.postabletecpay.presenter;

import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter{
    private PayModel mPayModel;
    private IPayView mIPayView;

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());
    }

    @Override
    public void callPayRecycler(String mEdong, int pageIndex) {
        if(mEdong == null)
            return;

        if(pageIndex == PayFragment.FIRST_PAGE_INDEX)
        {
            List<PayEntityAdapter>  adapterList = mPayModel.getAllBill(mEdong);
            mIPayView.showPayRecyclerFirstPage(adapterList);
        }else {
            mIPayView.showPayRecyclerOtherPage(pageIndex + PayFragment.PAGE_INCREMENT);
        }


    }
}
