package views.ecpay.com.postabletecpay.presenter;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisReponse;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public interface IMainPresenter {
    void callPutTransactionOffBill(String edong);

    ListBookCmisReponse syncBookCmis() throws Exception;

    boolean checkAndPostBill();

    void refreshDataPayAdapter();

    List<PayAdapter.DataAdapter> getDataPayAdapter();

    void sync();
}
