package views.ecpay.com.postabletecpay.view.ThanhToan;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.presenter.MainPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayView extends ICommonView {
//    void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage, String infoSearch, boolean isSeachOnline);

    void showPayRecyclerPage(List<PayAdapter.DataAdapter> adapterList, int indexBegin, int indexEnd, int pageIndexNew, int totalPage, String infoSearch, boolean isSeachOnline) throws Exception;

    Common.PROVIDER_CODE getProviderCodeSelected();

    void showSearchOnlineProcess();

    void hideSearchOnlineProcess();

    void showMessageNotifySearchOnline(String message, Common.TYPE_DIALOG typeDialog);

    void showEditTextSearch(String value);

    void showCountBillsAndTotalMoneyFragment(int size, long totalMoneyAllBills);

    void showPayRecyclerListBills(List<PayAdapter.BillEntityAdapter> listBillChecked);

    void refreshAdapterPayRecyclerListBills(boolean disableCheckBoxAll);

    void showCountBillsAndTotalMoneyInDialog(int totalBillsInList, long totalMoneyInList);

    void showMessageNotifyBillOnlineDialog(String message, boolean isMutilMessage, Common.TYPE_DIALOG typeDialog, boolean isShowDialog);

    void showPayingRViewDialogStart();

    void showPayingRviewDialogFinish();

    void showPayingRviewDialogMessage();

    void hidePayingRViewDialog();

    void showTextCountBillsPayed(int countBillPayedSuccess, int totalBillsDialog);

    void showTextCountBillsPayedSuccess(int countBillPayedSuccess, int totalBillsChooseDialog);

    void showDialogPayingOnline();

    void showDialogDeleteBillOnline(String edong, PayAdapter.BillEntityAdapter bill, PayAdapter.BillInsidePayAdapter adapter);

    void showInfoBillDeleteDialog(String customerPayCode, String tenKH, String monthBill, long moneyBill);

    void showDeleteBillOnlineProcess();

    void showPbarDeleteBillOnline();

    void hideAllProcessDeleteBillOnline();

    void showMessageNotifyDeleteOnlineDialog(String message, Common.TYPE_DIALOG typeDialog);

    void visibleButtonDeleteDialog(PayFragment.VISIBLE_BUTTON_DELETE_DIALOG type);

    void enableReasonEditText();

    void showRecyclerFragment();

    void showTextNoData();

    void showDialogBarcode();

    void showMessageNotifyPayfrag(String message);

    void processDialogDeleteBillOnline(String edong, PayAdapter.BillEntityAdapter bill, PayAdapter.BillInsidePayAdapter adapter);

    void updateBillSelectToPay(List<PayAdapter.BillEntityAdapter> lst);


    void processCheckedBillsDialog(String edong, int pos, boolean isChecked);

    void processClickMessageErrorBillDialog(String messageError);

    void processUnCheckedBillDialog(String message, Common.TYPE_DIALOG typeDialog);

//    void disableAllBillCheckboxWhenBillingOnline(boolean isDisableCheckbox);
}
