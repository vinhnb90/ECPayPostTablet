package views.ecpay.com.postabletecpay.presenter;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public interface IMainPresenter {
    void callPutTransactionOffBill(String edong);

    void synchronizePC();

    void postBill();

    void refreshDataPayAdapter();

    List<PayAdapter.DataAdapter> getDataPayAdapter();
}
