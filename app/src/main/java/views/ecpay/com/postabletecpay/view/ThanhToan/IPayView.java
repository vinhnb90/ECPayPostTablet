package views.ecpay.com.postabletecpay.view.ThanhToan;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayView extends ICommonView {
//    void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage, String infoSearch, boolean isSeachOnline);

    void showPayRecyclerPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndexNew, int totalPage, String infoSearch, boolean isSeachOnline);

    void showSearchOnlineProcess();

    void hideSearchOnlineProcess();

    void showMessageNotifySearchOnline(String message);

    void showEditTextSearch(String value);

    void showCountBillsAndTotalMoneyFragment(int size, long totalMoneyAllBills);

    void showPayRecyclerListBills(List<PayBillsDialogAdapter.Entity> listBillChecked);

    void showCountBillsAndTotalMoneyInDialog(int totalBillsInList, long totalMoneyInList);

    void showMessageNotifyBillOnlineDialog(String message);

    void showPayingRViewDialogStart();

    void showPayingRviewDialogFinish();

    void showPayingRviewDialogMessage();

    void hidePayingRViewDialog();

    void showTextCountBillsPayed(int countBillPayedSuccess, int totalBillsDialog);

    void showTextCountBillsPayedSuccess(int countBillPayedSuccess, int totalBillsChooseDialog);

    void showDialogPayingOnline();

    void showDialogDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside);

    void showInfoBillDeleteDialog(String customerPayCode, String tenKH, String monthBill, double moneyBill);

    void showDeleteBillOnlineProcess();

    void showPbarDeleteBillOnline();

    void hideAllProcessDeleteBillOnline();

    void showMessageNotifyDeleteOnlineDialog(String message);

    void visibleButtonDeleteDialog(PayFragment.VISIBLE_BUTTON_DELETE_DIALOG type);

    void enableReasonEditText();

    void showRecyclerFragment();

    void showTextNoData();
}
