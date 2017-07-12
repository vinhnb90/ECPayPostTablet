package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayPresenter {
    void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline);

    void callSearchOnline(String mEdong, String infoSearch, boolean isReseach);

    void cancelSeachOnline();

    void reseachOnline(String edong);

    void callProcessDataBillFragmentChecked(String edong, String code,  int posCustomer, PayAdapter.BillEntityAdapter bill, int posBillInside, int indexBegin, int indexEnd);

    void callPayRecyclerDialog(String mEdong);

    void callProcessDataBillDialogChecked(String edong, int pos, boolean isChecked);


    void callPay();

    void refreshTextCountBillPayedSuccess();

    void callShowDialogPay();

    void callProcessDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

    void callFillInfoBillDeleteDialog(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

    void callDeleteOnlineSoap(String edong, String reasonDeleteBill);

    void callShowDialogBarcode();

    PayModel getPayModel();

    IPayView getIPayView();

    void addSelectBillToPay(PayAdapter.BillEntityAdapter bill, boolean isSelect);
}
