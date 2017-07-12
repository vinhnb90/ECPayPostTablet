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

public interface IPayView extends ICommonView, MainPresenter.InteractorMainPresenter {
//    void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage, String infoSearch, boolean isSeachOnline);

    void showPayRecyclerPage(List<PayAdapter.DataAdapter> adapterList, int indexBegin, int indexEnd, int pageIndexNew, int totalPage, String infoSearch, boolean isSeachOnline) throws Exception;

    void showSearchOnlineProcess();

    void hideSearchOnlineProcess();

    void showMessageNotifySearchOnline(String message, Common.TYPE_DIALOG typeDialog);

    void showEditTextSearch(String value);

    void showCountBillsAndTotalMoneyFragment(int size, long totalMoneyAllBills);

    void showPayRecyclerListBills(List<PayBillsDialogAdapter.Entity> listBillChecked);

    void showCountBillsAndTotalMoneyInDialog(int totalBillsInList, long totalMoneyInList);

    void showMessageNotifyBillOnlineDialog(String message, boolean isMutilMessage, Common.TYPE_DIALOG typeDialog, boolean isShowDialog);

    void showPayingRViewDialogStart();

    void showPayingRviewDialogFinish();

    void showPayingRviewDialogMessage();

    void hidePayingRViewDialog();

    void showTextCountBillsPayed(int countBillPayedSuccess, int totalBillsDialog);

    void showTextCountBillsPayedSuccess(int countBillPayedSuccess, int totalBillsChooseDialog);

    void showDialogPayingOnline();

    void showDialogDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

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

    void showPayRecyclerListBillsAndDisableCheckBox(List<PayBillsDialogAdapter.Entity> listBillChecked, boolean isDisableAllCheckbox);

//    void disableAllBillCheckboxWhenBillingOnline(boolean isDisableCheckbox);
}
