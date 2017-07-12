package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayPresenter {
    void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline) throws Exception;

    void callSearchOnline(String mEdong, String infoSearch, boolean isReseach) throws Exception;

    void cancelSeachOnline();

    void reseachOnline(String edong);

    void callProcessDataBillFragmentChecked(String edong, String code,  int posCustomer, PayAdapter.BillEntityAdapter bill, int posBillInside, int indexBegin, int indexEnd);

    void callPayRecyclerDialog(String mEdong);

    void callProcessDataBillDialogChecked(String edong, int pos, boolean isChecked);

    void callPayingBillOnline(String edong);

    void callPayingBillOffline(String edong);

    void refreshTextCountBillPayedSuccess();

    void callShowDialogPay();

    void callProcessDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

    void callFillInfoBillDeleteDialog(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

    void callDeleteOnlineSoap(String edong, String reasonDeleteBill);

    void callShowDialogBarcode();

}
