package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.CheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline.DeleteBillOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.NEGATIVE_ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.PARTNER_CODE_DEFAULT;
import static views.ecpay.com.postabletecpay.util.commons.Common.PROVIDER_DEFAULT;
import static views.ecpay.com.postabletecpay.util.commons.Common.TEXT_SPACE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;
import static views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection.ERROR_OCCUR;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.PAGE_INCREMENT;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.ROWS_ON_PAGE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.VISIBLE_BUTTON_DELETE_DIALOG.HIDE_COUNTINUE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.VISIBLE_BUTTON_DELETE_DIALOG.SHOW_ALL;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter {
    private PayModel mPayModel;
    private IPayView mIPayView;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private int totalPage;
    private Common.TYPE_SEARCH mTypeSearch;

    //search online
    private SoapAPI.AsyncSoapSearchOnline soapSearchOnline;

    //bill paying online
    List<SoapAPI.AsyncSoapBillOnline> billOnlineAsyncList = new ArrayList<>();
    private SoapAPI.AsyncSoapBillOnline soapBillOnline;
    private int countBillPayedSuccess;

    //check Train Online
    private SoapAPI.AsyncSoapCheckTrainOnline soapCheckTrainOnline;

    //delete Bill Online
    private SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline;

    //delay
    private Handler handlerDelay = new Handler();

    //list bills in fragment
    private int totalBillsFragment = 0;
    private long totalMoneyFragment = 0;
    private List<PayBillsDialogAdapter.Entity> listBillCheckedFragment = new ArrayList<>();

    //list bills in dialog
    private int totalBillsChooseDialog = 0;
    private int totalBillsChooseDialogTemp;
    private long totalMoneyBillChooseDialog = 0;
    private List<PayBillsDialogAdapter.Entity> listBillDialog = new ArrayList<>();

    //info billDeleteOnline to delete
    PayAdapter.BillEntityAdapter billDeleteOnline;
    int posCustomerListDeleteOnline = NEGATIVE_ONE;

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());
    }

    @Override
    public void callPayRecycler(String edong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline) {
        if (edong == null)
            return;
        if (infoSearch == null)
            return;
        if (typeSearch == null)
            return;

        mIPayView.showRecyclerFragment();

        this.mTypeSearch = typeSearch;
        List<PayAdapter.PayEntityAdapter> fitter = new ArrayList<>();
        int indexBegin = 0;
        int indexEnd = 0;

        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            if (typeSearch == Common.TYPE_SEARCH.ALL)
                mAdapterList = mPayModel.getInforRowCustomer(edong);
            else
                mAdapterList = mPayModel.getInforRowCustomerFitterBy(edong, typeSearch, infoSearch);

            totalPage = mAdapterList.size() / ROWS_ON_PAGE + PAGE_INCREMENT;

            indexEnd = pageIndex * ROWS_ON_PAGE;
            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();
            for (; indexBegin < indexEnd; indexBegin++)
                fitter.add(mAdapterList.get(indexBegin));
        } else {
            if (pageIndex > totalPage)
                return;

            indexBegin = ROWS_ON_PAGE * (pageIndex - PAGE_INCREMENT);
            indexEnd = ROWS_ON_PAGE * (pageIndex);
            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();

            for (; indexBegin < indexEnd; indexBegin++) {
                fitter.add(mAdapterList.get(indexBegin));
            }
        }

        listBillCheckedFragment = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
        refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);

        mIPayView.showPayRecyclerPage(fitter, pageIndex, totalPage, infoSearch, isSeachOnline);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callSearchOnline(String mEdong, String infoSearch, boolean isReseach) {
        String textMessage = "";
        Context context = mIPayView.getContextView();
        Boolean isErr = false;

        if (mEdong == null || mEdong.isEmpty() || mEdong.trim().equals("") && !isErr) {
            return;
        }
        if (infoSearch == null && !isErr) {
            return;
        }

        mIPayView.showSearchOnlineProcess();

        //check wifi and network
        if (!Common.isConnectingWifi(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIPayView.showMessageNotifySearchOnline(textMessage);
            return;
        }

        //setup info login
        ConfigInfo configInfo;
        String versionApp = Common.getVersionApp(context);

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.CUSTOMER_BILL.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(textMessage);
            return;
        }

        //param is setup request by ECPAY, please contact ECPAY Server service if rechange.
        String directEvn = Common.DIRECT_EVN;
        String customerCode = infoSearch;
        String pcCode = configInfo.getPC_CODE().trim();
        String bookCmis = Common.TEXT_EMPTY;

        //create request to server
        String jsonRequestSearchOnline = SoapAPI.getJsonRequestSearchOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                directEvn,
                customerCode,
                pcCode,
                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
            return;

        try {
            if (soapSearchOnline == null) {
                //if null then create new
                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            } else if (soapSearchOnline.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapSearchOnline.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeSearchOnline);
                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeSearchOnline);

                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            }

            soapSearchOnline.execute(jsonRequestSearchOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeSearchOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage());
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callPayingBillOnline(String edong) {
        if (totalBillsChooseDialog == 0) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage());
            return;
        }

        //totalBillsChooseDialog will minus - 1 change value when every billDeleteOnline is payed online
        //totalBillsChooseDialogTemp to visible count billDeleteOnline paying succes and will be save value begin

        totalBillsChooseDialogTemp = totalBillsChooseDialog;

        mIPayView.showPayingRViewDialogStart();

        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);

        int index = 0;

        //stop all thread
        int maxIndex = billOnlineAsyncList.size();

        //start count billDeleteOnline pay
        countBillPayedSuccess = 0;

        for (; index < maxIndex; index++) {
            SoapAPI.AsyncSoapBillOnline billOnline = billOnlineAsyncList.get(index);
            billOnline.setEndCallSoap(true);
            billOnline.getHandlerDelay().removeCallbacks(runnableCountTimeSearchOnline);
            billOnline.cancel(true);
        }
        billOnlineAsyncList = new ArrayList<>();

        index = 0;
        maxIndex = listBillDialog.size();
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);

            if (entity.isChecked())
                payOnlineTheBill(context, versionApp, edong, entity);
        }
    }

    @Override
    public void refreshTextCountBillPayedSuccess() {
        mIPayView.showTextCountBillsPayedSuccess(countBillPayedSuccess, totalBillsChooseDialogTemp);
    }

    @Override
    public void callShowDialogPay() {
        mIPayView.showDialogPayingOnline();
    }

    @Override
    public void callProcessDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        mIPayView.showDialogDeleteBillOnline(edong, code, bill, posCustomerInside);
    }

    @Override
    public void callFillInfoBillDeleteDialog(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        this.billDeleteOnline = bill;
        this.posCustomerListDeleteOnline = posCustomerInside;

        PayAdapter.PayEntityAdapter infoCustomer = mAdapterList.get(posCustomerInside);
        mIPayView.showInfoBillDeleteDialog(code, infoCustomer.getTenKH(), bill.getMonthBill(), bill.getMoneyBill());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callDeleteOnlineSoap(String edong, String reasonDeleteBill) {
        boolean fail = this.billDeleteOnline == null || posCustomerListDeleteOnline == NEGATIVE_ONE || TextUtils.isEmpty(reasonDeleteBill);
        if (fail)
            return;

        if (TextUtils.isEmpty(reasonDeleteBill)) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_DELETE_BILL_ONLINE.ex10001.getMessage());
            return;
        }

        //TODO validate
        //check bill exist
        if (posCustomerListDeleteOnline > mAdapterList.size())
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_DELETE_BILL_ONLINE.ex10000.getMessage());

        // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
        // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
        // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi
        callAPICheckTrans(edong, reasonDeleteBill);
    }

    @Override
    public void callShowDialogBarcode() {
        mIPayView.showDialogBarcode();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPICheckTrans(String edong, String reasonDeleteBill) {

        Context context = mIPayView.getContextView();
        //check wifi
        boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

        if (!isHasWifi) {
            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
            return;
        }
        if (!isHasNetwork) {
            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
            return;
        }

        ConfigInfo configInfo = null;
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.CHECK_TRANS.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }

        PayAdapter.PayEntityAdapter infoCustomer = mAdapterList.get(posCustomerListDeleteOnline);

        Long amount = billDeleteOnline.getMoneyBill();
        String customerCode = infoCustomer.getMaKH();
        long billId = billDeleteOnline.getBillId();
        String requestDate = billDeleteOnline.getRequestDate();

        //create request to server
        String jsonRequestCheckTrainOnline = SoapAPI.getJsonRequestCheckTrainOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                edong,
                amount,
                customerCode,
                billId,
                requestDate,

                configInfo.getAccountId());

        if (jsonRequestCheckTrainOnline == null)
            return;

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            if (soapCheckTrainOnline == null) {
                //if null then create new
                soapCheckTrainOnline = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainOnlineCallBack, reasonDeleteBill);
            } else if (soapCheckTrainOnline.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapCheckTrainOnline.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapCheckTrainOnline.setEndCallSoap(true);
                soapCheckTrainOnline.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);
                soapCheckTrainOnline = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainOnlineCallBack, reasonDeleteBill);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);

                soapCheckTrainOnline = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainOnlineCallBack, reasonDeleteBill);
            }

            soapCheckTrainOnline.execute(jsonRequestCheckTrainOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeCheckTrainOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage());
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void payOnlineTheBill(Context context, String versionApp, String edong, PayBillsDialogAdapter.Entity entity) {
        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.BILLING.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }

        //Số ví edong thực hiện thanh toán
        String phone = edong;
        Long amount = entity.getAmount();

        //Phiên làm việc của TNV đang thực hiện
        String session = mPayModel.getSession(edong);

        /**
         * Luồng quầy thu đang năng : DT0813
         * Luồng tài khoản tiền điện : DT0807
         * Còn lại sử dụng DT0605
         *
         * ECPAY mặc định sử dụng DT0605
         */

        String partnerCode = PARTNER_CODE_DEFAULT;

        String providerCode = PROVIDER_DEFAULT;

        //create request to server
        String jsonRequestBillOnline = SoapAPI.getJsonRequestBillOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                entity.getCode(),
                session,
                entity.getBillId(),
                amount,
                phone,
                providerCode,
                partnerCode,
                configInfo.getAccountId());

        if (jsonRequestBillOnline == null)
            return;

        try {
            //get position stack of  List<SoapAPI.AsyncSoapBillOnline> asyntask object
            final int positionIndex = billOnlineAsyncList.size();

            Handler handlerDelayBillOnline = new Handler();
            soapBillOnline = new SoapAPI.AsyncSoapBillOnline(edong, soapBillOnlineCallBack, handlerDelayBillOnline, positionIndex);

            //add to last index and free soapBillOnline
            billOnlineAsyncList.add(soapBillOnline);

            //call set limit time out
            Runnable runnableCountTimeBillOnline = new Runnable() {
                @Override
                public void run() {
                    if (positionIndex >= billOnlineAsyncList.size())
                        return;
                    SoapAPI.AsyncSoapBillOnline soapBillOnline = billOnlineAsyncList.get(positionIndex);
                    if (soapBillOnline == null) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.e9999.getMessage());
                        Log.e(TAG, "run: at runnableCountTimeBillOnline has soapBillOnline is null!");
                        return;
                    }
                    if (soapBillOnline.isEndCallSoap())
                        return;

                    soapBillOnline.callCountdown(soapBillOnline);
                    //Do something after 100ms
                   /* BillingOnlineRespone billingOnlineRespone = soapBillOnline.getBillingOnlineRespone();

                    if (billingOnlineRespone == null) {
                        //call time out
                        soapBillOnline.callCountdown(soapBillOnline);
                    }*/
                }
            };

            //free param private common in class
            soapBillOnline = null;

            //count down
            billOnlineAsyncList.get(positionIndex).getHandlerDelay().postDelayed(runnableCountTimeBillOnline, TIME_OUT_CONNECT);

            //run
            billOnlineAsyncList.get(positionIndex).execute(jsonRequestBillOnline);
        } catch (Exception e) {
            mIPayView.showMessageNotifyBillOnlineDialog(e.getMessage());
            return;
        }
    }

    @Override
    public void cancelSeachOnline() {
        if (soapSearchOnline != null && !soapSearchOnline.isEndCallSoap()) {
            soapSearchOnline.setEndCallSoap(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void reseachOnline(String edong) {
        cancelSeachOnline();
        if (soapSearchOnline != null) {
            mIPayView.showEditTextSearch(soapSearchOnline.getInfoSearch());
            callSearchOnline(edong, soapSearchOnline.getInfoSearch(), true);
        } else {
            mIPayView.showMessageNotifySearchOnline(Common.CODE_REPONSE_SEARCH_ONLINE.e9999.getMessage());
            Log.e(TAG, "reseachOnline: soapSearchOnline không được khởi tạo");
        }
    }

    @Override
    public void callProcessDataBillFragmentChecked(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomer) {
        if (TextUtils.isEmpty(edong))
            return;
        if (TextUtils.isEmpty(code))
            return;
        if (bill == null)
            return;
        if (posCustomer >= mAdapterList.size())
            return;

        mPayModel.updateBillIsChecked(edong, code, (int) bill.getBillId(), bill.isChecked());

        //show billDeleteOnline or not show billDeleteOnline
        mPayModel.updateCustomerIsShowBill(edong, code, bill.isChecked());
        mAdapterList.get(posCustomer).setShowBill(bill.isChecked());

        //show total bills and total money of all bills is checked
        refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    @Override
    public void callPayRecyclerDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

        listBillDialog = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);

        mIPayView.showPayRecyclerListBills(listBillDialog);
    }

    @Override
    public void callProcessDataBillDialogChecked(String edong, int pos, boolean isChecked) {
        if (listBillCheckedFragment.size() <= pos)
            return;

        listBillDialog.get(pos).setChecked(isChecked);
        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    private void refreshTotalBillsAndTotalMoneyInFragment(String edong, Common.STATUS_BILLING statusBilling) {
        totalBillsFragment = 0;
        totalMoneyFragment = 0;
        listBillCheckedFragment.clear();

        listBillCheckedFragment = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, statusBilling);
        totalBillsFragment = mPayModel.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);
        totalMoneyFragment = mPayModel.countMoneyAllBillsIsCheckedWithStatusPay(edong, statusBilling);

        mIPayView.showCountBillsAndTotalMoneyFragment(totalBillsFragment, totalMoneyFragment);
    }

    private void refreshTotalBillsAndTotalMoneyInDialogWhenChecked(String edong, Common.STATUS_BILLING statusBilling) {
//        totalBillsChooseDialog = mPayModel.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);
//        totalMoneyBillChooseDialog = mPayModel.countMoneyAllBillsIsCheckedWithStatusPay(edong, statusBilling);

        totalBillsChooseDialog = 0;
        totalMoneyBillChooseDialog = 0;
        for (PayBillsDialogAdapter.Entity bill : listBillDialog) {
            if (bill.isChecked()) {
                totalBillsChooseDialog++;
                totalMoneyBillChooseDialog += bill.getAmount();
            }
        }
        mIPayView.showCountBillsAndTotalMoneyInDialog(totalBillsChooseDialog, totalMoneyBillChooseDialog);
    }

    //region search online
    private SoapAPI.AsyncSoapSearchOnline.AsyncSoapSearchOnlineCallBack soapSearchOnlineCallBack = new SoapAPI.AsyncSoapSearchOnline.AsyncSoapSearchOnlineCallBack() {
        private Common.TYPE_SEARCH typeSearch;
        private String edong;
        private String infoSearch;

        @Override
        public void onPre(final SoapAPI.AsyncSoapSearchOnline soapSearchOnline) {
            typeSearch = soapSearchOnline.getTypeSearch();
            edong = soapSearchOnline.getEdong();
            infoSearch = soapSearchOnline.getInfoSearch();

            mIPayView.showSearchOnlineProcess();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

            if (!isHasWifi) {
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());

                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifySearchOnline(message);
                }
            });
        }

        @Override
        public void onPost(SearchOnlineResponse response) {
            if (response == null) {
                return;
            }

            Common.CODE_REPONSE_SEARCH_ONLINE codeResponse = Common.CODE_REPONSE_SEARCH_ONLINE.findCodeMessage(response.getFooterSearchOnlineResponse().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_SEARCH_ONLINE.e000) {
                mIPayView.showMessageNotifySearchOnline(codeResponse.getMessage());
                return;
            }

            //get responseLoginResponse from body response
            //because server return string not object
            String customerData = response.getBodySearchOnlineResponse().getCustomer();

            // định dạng kiểu Object JSON
            CustomerInsideBody customerResponse = null;
            try {
                customerResponse = new Gson().fromJson(customerData, CustomerInsideBody.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if (customerResponse == null)
                return;

            int rowEffect = mPayModel.writeSQLiteCustomerTableFromSearchOnline(edong, customerResponse);
            if (rowEffect == ERROR_OCCUR) {
                Log.d(TAG, "Cannot insert customer to database.");
            }

            List<BillInsideCustomer> listBill = customerResponse.getListBill();
            if (listBill == null)
                return;
            int index = 0;
            int indexMax = listBill.size();
            boolean isOccurInsertBill = false;

            for (; index < indexMax; index++) {
                rowEffect = mPayModel.writeSQliteBillTableOfCustomer(edong, listBill.get(index));

                if (rowEffect == ERROR_OCCUR) {
                    isOccurInsertBill = true;
                }
            }

            if (isOccurInsertBill) {
                Log.d(TAG, "Has several billDeleteOnline of customer cannot insert to database.");
            }

            mIPayView.hideSearchOnlineProcess();
            callPayRecycler(edong, PayFragment.FIRST_PAGE_INDEX, mTypeSearch, infoSearch, false);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapSearchOnline soapSearchOnlineCallBack) {
            soapSearchOnlineCallBack.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapSearchOnlineCallBack.isEndCallSoap()) {
                        mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };

    private Runnable runnableCountTimeSearchOnline = new Runnable() {
        @Override
        public void run() {
            if (soapSearchOnline != null && soapSearchOnline.isEndCallSoap())
                return;
            //Do something after 100ms
            SearchOnlineResponse searchOnlineResponse = soapSearchOnline.getSearchOnlineResponse();

            if (searchOnlineResponse == null && !soapSearchOnline.isEndCallSoap()) {
                //call time out
                soapSearchOnline.callCountdown(soapSearchOnline);
            }
        }
    };
    //endregion

    //region check train online
    private SoapAPI.AsyncSoapCheckTrainOnline.AsyncSoapCheckTrainOnlineCallBack soapCheckTrainOnlineCallBack = new SoapAPI.AsyncSoapCheckTrainOnline.AsyncSoapCheckTrainOnlineCallBack() {
        private String edong;
        private String reasonDeleteBill;

        @Override
        public void onPre(final SoapAPI.AsyncSoapCheckTrainOnline soapSearchOnline) {
            edong = soapSearchOnline.getEdong();
            reasonDeleteBill = soapSearchOnline.getReasonDeleteBill();

            mIPayView.showDeleteBillOnlineProcess();
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

            if (!isHasWifi) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyDeleteOnlineDialog(message);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(CheckTrainOnlineResponse response) {
            if (response == null) {
                return;
            }

            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

            Common.CODE_REPONSE_API_CHECK_TRAINS codeResponse = Common.CODE_REPONSE_API_CHECK_TRAINS.findCodeMessage(response.getFooter().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_API_CHECK_TRAINS.eBILLING) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage());
                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                return;
            }

            callAPITransationCancellation(edong, response, reasonDeleteBill);

        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapCheckTrainOnline soapCheckTrainOnlineCallBack) {
            soapCheckTrainOnlineCallBack.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapCheckTrainOnlineCallBack.isEndCallSoap()) {
                        mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };

    private SoapAPI.AsyncSoapDeleteBillOnline.AsyncSoapDeleteBillOnlineCallBack soapDeleteBillOnlineCallBack = new SoapAPI.AsyncSoapDeleteBillOnline.AsyncSoapDeleteBillOnlineCallBack() {
        private String edong;
        private String reasonDeleteBill;
        private String code;
        private Long billId;

        @Override
        public void onPre(final SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline) {
            edong = soapDeleteBillOnline.getEdong();
            reasonDeleteBill = soapDeleteBillOnline.getCauseDeleteBill();
            code = soapDeleteBillOnline.getCode();
            billId = soapDeleteBillOnline.getBillId();

            mIPayView.showDeleteBillOnlineProcess();
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

            if (!isHasWifi) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
                soapDeleteBillOnline.setEndCallSoap(true);
                soapDeleteBillOnline.cancel(true);
            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapDeleteBillOnline.setEndCallSoap(true);
                soapDeleteBillOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyDeleteOnlineDialog(message);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(DeleteBillOnlineRespone response) {
            if (response == null) {
                return;
            }

            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

            Common.CODE_REPONSE_TRANSACTION_CANCELLATION codeResponse = Common.CODE_REPONSE_TRANSACTION_CANCELLATION.findCodeMessage(response.getFooter().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_TRANSACTION_CANCELLATION.e000) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage());
                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                return;
            }

            //Cập nhật thông tin hủy hóa đơn trên danh sách hóa đơn
            mPayModel.updateBillReasonDelete(edong, code, billId, reasonDeleteBill, Common.STATUS_BILLING.DANG_CHO_HUY);

//            mPayModel.updateBillStatusCancelBillOnline()
            mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage());
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline) {
            soapDeleteBillOnline.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapDeleteBillOnline.isEndCallSoap()) {
                        mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPITransationCancellation(String edong, CheckTrainOnlineResponse response, String reasonDeleteBill) {
        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();
        mIPayView.enableReasonEditText();
        mIPayView.visibleButtonDeleteDialog(SHOW_ALL);

        //check wifi
        boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

        if (!isHasWifi) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
            soapDeleteBillOnline.setEndCallSoap(true);
            soapDeleteBillOnline.cancel(true);
        }
        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

            soapDeleteBillOnline.setEndCallSoap(true);
            soapDeleteBillOnline.cancel(true);
        }

        ConfigInfo configInfo = null;
        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.TRANSACTION_CANCELLATION.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }

        Long amount = response.getBody().getAmount();
        String code = response.getBody().getCustomerCode();
        Long billId = response.getBody().getBillId();
        String requestDate = response.getBody().getRequestDate();

        Long traceNumber = mPayModel.getTraceNumberBill(edong, code, billId);

        //create request to server
        String jsonRequestDeleteBillOnline = SoapAPI.getJsonRequestTransationCancellation(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                amount,
                code,
                billId,
                requestDate,
                traceNumber,
                reasonDeleteBill,

                configInfo.getAccountId());

        if (jsonRequestDeleteBillOnline == null)
            return;

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            if (soapDeleteBillOnline == null) {
                //if null then create new
                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            } else if (soapDeleteBillOnline.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapDeleteBillOnline.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapDeleteBillOnline.setEndCallSoap(true);
                soapDeleteBillOnline.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeDeleteBillOnline);
                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeDeleteBillOnline);

                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            }

            soapDeleteBillOnline.execute(jsonRequestDeleteBillOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeDeleteBillOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(e.getMessage());
            return;
        }

    }

    private Runnable runnableCountTimeDeleteBillOnline = new Runnable() {
        @Override
        public void run() {
            if (soapDeleteBillOnline != null && soapDeleteBillOnline.isEndCallSoap())
                return;
            //Do something after 100ms
            DeleteBillOnlineRespone deleteBillOnlineRespone = soapDeleteBillOnline.getDeleteBillOnlineRespone();

            if (deleteBillOnlineRespone == null && !soapDeleteBillOnline.isEndCallSoap()) {
                //call time out
                soapDeleteBillOnline.callCountdown(soapDeleteBillOnline);
            }
        }
    };

    private Runnable runnableCountTimeCheckTrainOnline = new Runnable() {
        @Override
        public void run() {
            if (soapCheckTrainOnline != null && soapCheckTrainOnline.isEndCallSoap())
                return;
            //Do something after 100ms
            CheckTrainOnlineResponse checkTrainOnlineResponse = soapCheckTrainOnline.getCheckTrainOnlineResponse();

            if (checkTrainOnlineResponse == null && !soapCheckTrainOnline.isEndCallSoap()) {
                //call time out
                soapCheckTrainOnline.callCountdown(soapCheckTrainOnline);
            }
        }
    };
    //endregion

    //region billDeleteOnline online
    private SoapAPI.AsyncSoapBillOnline.AsyncSoapBillOnlineCallBack soapBillOnlineCallBack = new SoapAPI.AsyncSoapBillOnline.AsyncSoapBillOnlineCallBack() {
        private String edong;

        @Override
        public void onPre(final SoapAPI.AsyncSoapBillOnline soapBillOnline) {
            edong = soapBillOnline.getEdong();

            mIPayView.showPayingRViewDialogStart();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

            if (!isHasWifi) {
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());

                soapBillOnline.setEndCallSoap(true);
                soapBillOnline.cancel(true);
            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapBillOnline.setEndCallSoap(true);
                soapBillOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message, final int positionIndex) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
//                    Toast.makeText(mIPayView.getContextView(), "Erorr billDeleteOnline " + listBillDialog.get(positionIndex).getName() + " - " + listBillDialog.get(positionIndex).getTerm(), Toast.LENGTH_SHORT).show();
                }
            });
          /*  ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyBillOnlineDialog(message);
                }
            });*/
        }

        @Override
        public void onPost(BillingOnlineRespone response, final int positionIndex) {
            if (response == null) {
                return;
            }

            final Common.CODE_REPONSE_SEARCH_ONLINE codeResponse = Common.CODE_REPONSE_SEARCH_ONLINE.findCodeMessage(response.getFooterBillingOnlineRespone().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_SEARCH_ONLINE.e000) {
                ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PayBillsDialogAdapter.Entity entity = listBillDialog.get(positionIndex);
                        String term = Common.convertDateToDate(entity.getTerm(), Common.DATE_TIME_TYPE.yyyymmdd, Common.DATE_TIME_TYPE.mmyyyy);
                        Toast.makeText(mIPayView.getContextView(), Common.CODE_REPONSE_SEARCH_ONLINE.getMessageServerNotify(entity.getTerm(), term, codeResponse.getMessage()), Toast.LENGTH_SHORT).show();

                        //check if not has thread is running then hide process bar paying online
                        boolean isHasRunningYet = checkIsHasThreadRunning(positionIndex);
                        if (!isHasRunningYet)
                            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage());
                        refreshStatusPaySuccessDialog(edong);
                        Log.e(TAG, "run: billing online: " + Common.CODE_REPONSE_SEARCH_ONLINE.getMessageServerNotify(entity.getTerm(), term, codeResponse.getMessage()));
                    }
                });
                return;
            }

            //update text count billDeleteOnline payed success
            countBillPayedSuccess++;

            //check full billDeleteOnline is payed
            if (countBillPayedSuccess == totalBillsChooseDialogTemp)
                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage());

            //set database to
            int rowUpdate = mPayModel.updateBillStatus(edong, response.getBodyBillingOnlineRespone().getCustomerCode(), response.getBodyBillingOnlineRespone().getBillId(), Common.STATUS_BILLING.DA_THANH_TOAN);
            if (rowUpdate == ERROR_OCCUR) {
                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10002.getMessage());
                Log.e(TAG, "onPost: Lỗi thực hiện update dữ liệu trên máy tính bảng. ");
//                callPayRecyclerDialog(edong);
            }

            //update date payed and tract number
            String dateNow = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddmmyyyy);
            Long traceNumber = response.getBodyBillingOnlineRespone().getTraceNumber();
            mPayModel.updateBillRequestDateBill(edong, response.getBodyBillingOnlineRespone().getCustomerCode(), response.getBodyBillingOnlineRespone().getBillId(), dateNow, traceNumber);

            //set new status for billDeleteOnline and refesh recycler bills
            listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN.getCode());

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(
                    new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            totalBillsChooseDialog--;
                            refreshStatusPaySuccessDialog(edong);
                        }
                    }
            );

        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapBillOnline soapBillOnline) {
            soapBillOnline.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapBillOnline.isEndCallSoap()) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };

    private boolean checkIsHasThreadRunning(int positionIndex) {

        boolean hasThreadRunning = false;
        for (int index = 0; index < billOnlineAsyncList.size(); index++) {
            if (index != positionIndex) {
                SoapAPI.AsyncSoapBillOnline soapBillOnline = billOnlineAsyncList.get(index);
                if (!soapBillOnline.isEndCallSoap()) {
                    hasThreadRunning = true;
                    break;
                }
            }
        }
        return hasThreadRunning;
    }

    private void refreshStatusPaySuccessDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

        //not refresh listBillDialog
        //but refresh
        refreshTextCountBillPayedSuccess();
        mIPayView.showPayRecyclerListBills(listBillDialog);
    }

    private void refreshTotalBillsAndTotalMoneyInDialogWhenCallPayingOnline() {
        mIPayView.showCountBillsAndTotalMoneyInDialog(countBillPayedSuccess, totalBillsChooseDialogTemp);
    }
    //endregion
}
