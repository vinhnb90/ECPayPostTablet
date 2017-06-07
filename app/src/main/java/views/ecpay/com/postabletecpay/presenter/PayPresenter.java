package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayListBillsAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.FIRST_PAGE_INDEX;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.ROWS_ON_PAGE;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter {
    private PayModel mPayModel;
    private IPayView mIPayView;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private int totalPage;
    private Common.TYPE_SEARCH mTypeSearch;
    private SoapAPI.AsyncSoapSearchOnline soapSearchOnline;
    private Handler handlerDelay = new Handler();

    //list bills in fragment
    int totalBillsFragment = 0;
    long totalMoneyFragment = 0;
    private List<PayListBillsAdapter.Entity> listBillCheckedFragment = new ArrayList<>();

    //list bills in dialog
    int totalBillsDialog = 0;
    long totalMoneyDialog = 0;
    private List<PayListBillsAdapter.Entity> listBillCheckedDialog = new ArrayList<>();

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

            totalPage = mAdapterList.size() / ROWS_ON_PAGE;
            if (totalPage * ROWS_ON_PAGE != mAdapterList.size() || (totalPage == 0))
                totalPage++;

            indexEnd = pageIndex * ROWS_ON_PAGE;
            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();
            for (; indexBegin < indexEnd; indexBegin++)
                fitter.add(mAdapterList.get(indexBegin));
        } else {
            if (pageIndex > totalPage)
                return;

            indexBegin = ROWS_ON_PAGE * (pageIndex - FIRST_PAGE_INDEX);
            indexEnd = ROWS_ON_PAGE * (pageIndex);
            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();

            for (; indexBegin < indexEnd; indexBegin++) {
                fitter.add(mAdapterList.get(indexBegin));
            }
        }

        listBillCheckedFragment = mPayModel.getAllBillOfCustomerChecked(edong);
        refreshTotalBillsAndTotalMoneyInFragment();

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
        String jsonRequestSearchOnline = "";
                /*SoapAPI.getJsonRequestSearchOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                directEvn,
                customerCode,
                configInfo.getAccountId());*/

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

                handlerDelay.removeCallbacks(runnableCountTime);
                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTime);

                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            }

            soapSearchOnline.execute(jsonRequestSearchOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTime, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage());
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callPayOnline(String edong) {
        if (totalBillsDialog == 0)
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage());

        mIPayView.showBillOnlineDialogPrecess();

        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);

        int index = 0;
        for (; index < totalBillsDialog; index++) {
            PayListBillsAdapter.Entity entity = listBillCheckedDialog.get(index);

            if (entity.isChecked())
                payOnlineTheBill(context, versionApp, edong, entity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void payOnlineTheBill(Context context, String versionApp, String edong, PayListBillsAdapter.Entity entity) {
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

        String partnerCode = Common.PARTNER_CODE.DT0605.getCode();

        String providerCode = "NCC0468";
        String account_type = "";

        //create request to server
        String jsonRequestSearchOnline = SoapAPI.getJsonRequestBillOnline(
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
                account_type,

//                edong,
//                partnerCode,
                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
            return;

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

        mPayModel.updateBillIsChecked(edong, code, bill.getBillId(), bill.isChecked());

        //show bill or not show bill
        mPayModel.updateCustomerIsShowBill(edong, code, bill.isChecked());
        mAdapterList.get(posCustomer).setShowBill(bill.isChecked());

        //show total bills and total money of all bills is checked
        int totalBills = mPayModel.countAllBillsIsChecked(edong);
        int totalMoneyAllBills = mPayModel.countMoneyAllBillsIsChecked(edong);

        mIPayView.showCountBillsAndCountTotalMoney(totalBills, totalMoneyAllBills);
    }

    @Override
    public void callPayRecyclerDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

        listBillCheckedDialog = mPayModel.getAllBillOfCustomerChecked(edong);
        refreshTotalBillsAndTotalMoneyInDialog();

        mIPayView.showPayRecyclerListBills(listBillCheckedDialog);
    }

    @Override
    public void callProcessDataBillDialogChecked(int pos, boolean isChecked) {
        if (listBillCheckedFragment.size() <= pos)
            return;
        //update again
        /*
        String edong = this.listBillCheckedFragment.get(pos).getEdong();
        String code = this.listBillCheckedFragment.get(pos).getCode();
        int bill = this.listBillCheckedFragment.get(pos).getBillId();

        mPayModel.updateBillIsChecked(edong, code, bill, isChecked);*/

        listBillCheckedDialog.clear();
        listBillCheckedDialog.addAll(listBillCheckedFragment);
        listBillCheckedDialog.get(pos).setChecked(isChecked);

        refreshTotalBillsAndTotalMoneyInDialog();
    }

    private void refreshTotalBillsAndTotalMoneyInFragment() {
        totalBillsFragment = 0;
        totalMoneyFragment = 0;

        int index = 0;
        int maxIndex = listBillCheckedFragment.size();
        for (; index < maxIndex; index++) {
            if (listBillCheckedFragment.get(index).isChecked()) {
                totalBillsFragment++;
                totalMoneyFragment += listBillCheckedFragment.get(index).getAmount();
            }
        }

        mIPayView.showCountBillsAndCountTotalMoney(totalBillsFragment, totalMoneyFragment);
    }

    private void refreshTotalBillsAndTotalMoneyInDialog() {
        totalBillsDialog = 0;
        totalMoneyDialog = 0;

        int index = 0;
        int maxIndex = listBillCheckedDialog.size();

        for (; index < maxIndex; index++) {
            if (listBillCheckedDialog.get(index).isChecked()) {
                totalBillsDialog++;
                totalMoneyDialog += listBillCheckedDialog.get(index).getAmount();
            }
        }

        mIPayView.showCountBillsAndCountTotalMoneyInDialog(totalBillsDialog, totalMoneyDialog);
    }

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
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
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

            int rowEffect = mPayModel.writeSQLiteCustomerTable(edong, customerResponse);
            if (rowEffect == SQLiteConnection.ERROR_OCCUR) {
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

                if (rowEffect == SQLiteConnection.ERROR_OCCUR) {
                    isOccurInsertBill = true;
                }
            }

            if (isOccurInsertBill) {
                Log.d(TAG, "Has several bill of customer cannot insert to database.");
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

    private Runnable runnableCountTime = new Runnable() {
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
}
