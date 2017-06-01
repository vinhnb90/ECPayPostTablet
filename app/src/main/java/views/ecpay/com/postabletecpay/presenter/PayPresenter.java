package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
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

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());
    }

    @Override
    public void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline) {
        if (mEdong == null)
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
                mAdapterList = mPayModel.getInforRowCustomer(mEdong);
            else
                mAdapterList = mPayModel.getInforRowCustomerFitterBy(mEdong, typeSearch, infoSearch);

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

        mIPayView.showPayRecyclerPage(fitter, pageIndex, totalPage, infoSearch, isSeachOnline);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callSearchOnline(String mEdong, String infoSearch) {

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
        String versionApp = "";
        try {
            versionApp = mIPayView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.LOGIN.toString(), versionApp);
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
                bookCmis,

                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
            return;
        try {
            final SoapAPI.AsyncSoapSearchOnline soapSearchOnline;
            soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);

            if (soapSearchOnline.getStatus() != AsyncTask.Status.RUNNING) {
                soapSearchOnline.execute(jsonRequestSearchOnline);

                //thread time out
                Thread soapLoginThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginResponseReponse loginResponseReponse = null;
                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                        } finally {
                            if (loginResponseReponse == null) {
                                soapSearchOnline.callCountdown(soapSearchOnline);
                            }
                        }
                    }
                });

                soapLoginThread.start();
            }
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage());
            return;
        }
    }

    /*private void callPayRecyclerAll(String mEdong, int pageIndex) {
        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();

            totalPage = mAdapterList.size() / ROWS_ON_PAGE;
            if (totalPage * ROWS_ON_PAGE != mAdapterList.size() || (totalPage == 0))
                totalPage++;

            int index = 0;
            int maxIndex = pageIndex * ROWS_ON_PAGE;
            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> fitter = new ArrayList<>();
            for (; index < maxIndex; index++)
                fitter.add(mAdapterList.get(index));

            mIPayView.showPayRecyclerFirstPage(fitter, pageIndex, totalPage, , false);
        } else {
            if (pageIndex > totalPage)
                return;

            int index = ROWS_ON_PAGE * (pageIndex - FIRST_PAGE_INDEX);
            int maxIndex = ROWS_ON_PAGE * (pageIndex);

            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> adapterListSearch = new ArrayList<>();
            for (; index < maxIndex; index++) {
                adapterListSearch.add(mAdapterList.get(index));
            }

            mIPayView.showPayRecyclerPage(adapterListSearch, pageIndex, totalPage, infoSearch, false);
        }
    }*/

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
        public void onUpdate(String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            mIPayView.showMessageNotifySearchOnline(message);
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

            int rowEffect = mPayModel.writeSQLiteCustomerTable(edong,customerResponse);
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
}
