package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.text.TextUtils;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.IMainView;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView mIMainView;

    public MainPresenter(IMainView mIMainView) {
        this.mIMainView = mIMainView;
    }


    @Override
    public void callPutTransactionOffBill(String edong) {
        boolean fail = TextUtils.isEmpty(edong);
        if (fail)
            return;

        //check network
        Context context = mIMainView.getContextView();
        boolean isHasNetwork = Common.isNetworkConnected(mIMainView.getContextView());

//        if (!isHasNetwork) {
//            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
//            return;
//        }
    }
}
