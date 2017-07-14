package views.ecpay.com.postabletecpay.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sewoo.port.android.BluetoothPort;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonNo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBalance.BalanceRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBalance.BodyBalanceRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BodyBillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.BodyCheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.CheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline.DeleteBillOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BodySearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS;
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
    private int totalPage;
    private Common.TYPE_SEARCH mTypeSearch;

    SoapAPI.AsyncSoapIncludeTimout<SearchOnlineResponse> currentAsyncSearchOnline;

    //bill paying online
    List<SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>> billOnlineAsyncList = new ArrayList<>();
    private int countBillPayedSuccess, totalBillsChooseDialogTemp;
    private long totalAmountBillPayedSuccess;


    //delay
    private Handler handlerDelay = new Handler();

    //list bills in fragment


    //info billDeleteOnline to delete
    PayAdapter.BillEntityAdapter billDeleteOnline;


    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());

        mIPayView.showCountBillsAndTotalMoneyFragment(0, 0);

    }

    @Override
    public PayModel getPayModel() {
        return mPayModel;
    }

    @Override
    public IPayView getIPayView()
    {
        return mIPayView;
    }

    @Override
    public void addSelectBillToPay(PayAdapter.BillEntityAdapter bill, boolean isSelect)
    {
        mPayModel.selectBill(bill, isSelect);

        mIPayView.updateBillSelectToPay(mPayModel.getListBillSelected());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callPayRecycler(final String edong, final int pageIndex, Common.TYPE_SEARCH typeSearch, final String infoSearch, final boolean isSeachOnline) {
        if (edong == null)
            return;
        if (infoSearch == null)
            return;
        if (typeSearch == null)
            return;

        mIPayView.showRecyclerFragment();

        this.mTypeSearch = typeSearch;


        if(!isSeachOnline)
        {

            try {
                PayModel.AsyncSearchOffline asyncSearchOffline = new PayModel.AsyncSearchOffline(mPayModel, new PayModel.AsyncSoapCallBack() {
                    @Override
                    public void onPost(List<PayAdapter.DataAdapter> result) {
                        List<PayAdapter.DataAdapter> fitter = new ArrayList<>();
                        int indexBegin = 0;
                        int indexEnd = 0;
                        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
                            int index = 0;


                            totalPage = result.size() / ROWS_ON_PAGE + PAGE_INCREMENT;

                            indexEnd = pageIndex * ROWS_ON_PAGE;

                            if (indexEnd > result.size())
                                indexEnd = result.size();

                            fitter = result.subList(indexBegin, indexEnd);

                        } else {
                            if (pageIndex > totalPage)
                                return;

                            indexBegin = ROWS_ON_PAGE * (pageIndex - PAGE_INCREMENT);
                            indexEnd = ROWS_ON_PAGE * (pageIndex);
                            if (indexEnd > result.size())
                                indexEnd = result.size();

                            int index = indexBegin;
                            int maxIndex = indexEnd;

                            fitter = result.subList(index, maxIndex);

                        }
                        final List<PayAdapter.DataAdapter> finalFitter = fitter;
                        final int finalIndexBegin = indexBegin;
                        final int finalIndexEnd = indexEnd;
                        Activity activity = ((Activity)(mIPayView.getContextView()));
                        if(activity!= null)
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);

                                    try
                                    {
                                        mIPayView.showPayRecyclerPage(finalFitter, finalIndexBegin, finalIndexEnd, pageIndex, totalPage, infoSearch, isSeachOnline);
                                    }catch (Exception e)
                                    {

                                    }
                                }
                            });
                    }
                }) ;
                asyncSearchOffline.execute(new Pair<Common.TYPE_SEARCH, String>(typeSearch, infoSearch));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else
        {
            callSearchOnline(MainActivity.mEdong, infoSearch, false);
//            List<PayAdapter.DataAdapter> fitter = new ArrayList<>();
//            int indexBegin = 0;
//            int indexEnd = 0;
//
//            mIPayView.showPayRecyclerPage(fitter, indexBegin, indexEnd, pageIndex, totalPage, infoSearch, isSeachOnline);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callSearchOnline(String mEdong, String infoSearch, boolean isReseach) {
        this.cancelSeachOnline();

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
//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIPayView.showMessageNotifySearchOnline(textMessage, Common.TYPE_DIALOG.LOI);
            return;
        }

        //setup info login
        ConfigInfo configInfo;
        String versionApp = Common.getVersionApp(context);

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.CUSTOMER_BILL.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(textMessage, Common.TYPE_DIALOG.LOI);
            return;
        }

        //create request to server
        String jsonRequestSearchOnline = SoapAPI.getJsonRequestSearchOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                infoSearch,
                mIPayView.getProviderCodeSelected().getCode(),
                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
        {
            finishSearchOnline(null);
            return;
        }

        try {
            currentAsyncSearchOnline = new SoapAPI.AsyncSoapIncludeTimout<SearchOnlineResponse>(handlerDelay, SearchOnlineResponse.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                }

                @Override
                public void onUpdate(final String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;
                    try
                    {
                        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIPayView.showMessageNotifySearchOnline(message, Common.TYPE_DIALOG.LOI);
                                finishSearchOnline(null);
                            }
                        });
                    }catch (Exception e)
                    {

                    }
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                    if(response != null)
                    {

                        Common.CODE_REPONSE_SEARCH_ONLINE codeResponse = Common.CODE_REPONSE_SEARCH_ONLINE.findCodeMessage(response.getFooter().getResponseCode());
                        if (codeResponse != Common.CODE_REPONSE_SEARCH_ONLINE.e000) {
                            mIPayView.showMessageNotifySearchOnline(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                            finishSearchOnline(null);
                            return;
                        }

                        finishSearchOnline((BodySearchOnlineResponse) response.getBody());

                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    try
                    {
                        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                                finishSearchOnline(null);
                            }
                        });
                    }catch (Exception e)
                    {

                    }
                }
            });
            currentAsyncSearchOnline.setUserData(infoSearch);
            currentAsyncSearchOnline.execute(jsonRequestSearchOnline);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void finishSearchOnline(BodySearchOnlineResponse response)
    {

        if(response != null)
        {
            CustomerInsideBody customerResponse = null;
            try {
                customerResponse = new Gson().fromJson(response.getCustomer(), CustomerInsideBody.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if(customerResponse != null)
            {
                EntityKhachHang khachHang = new EntityKhachHang();
                khachHang.setE_DONG(MainActivity.mEdong);
                khachHang.setMA_KHANG(customerResponse.getCode());
                khachHang.setMA_THE(customerResponse.getCardNo());
                khachHang.setTEN_KHANG(customerResponse.getName());
                khachHang.setDIA_CHI(customerResponse.getAddress());
                khachHang.setPHIEN_TTOAN(customerResponse.getInning());
                khachHang.setLO_TRINH(customerResponse.getRoad());
                khachHang.setSO_GCS(customerResponse.getBookCmis());
                khachHang.setDIEN_LUC(customerResponse.getPcCode());
                khachHang.setSO_HO("");
                khachHang.setSDT_ECPAY(customerResponse.getPhoneByecp());
                khachHang.setSDT_EVN(customerResponse.getPhoneByevn());
                khachHang.setGIAO_THU(Common.TRANG_THAI_GIAO_THU.VANG_LAI.getCode());
                khachHang.setNGAY_GIAO_THU(Calendar.getInstance().getTime());

                mPayModel.writeSQLiteCustomerTableFromSearchOnline(MainActivity.mEdong, khachHang, customerResponse.getListBill());

                PayAdapter.DataAdapter dataAdapter = new PayAdapter.DataAdapter(khachHang, new ArrayList<PayAdapter.BillEntityAdapter>(), 0);

                long totalMoney = 0;
                for (int i = 0, n = customerResponse.getListBill().size(); i < n; i ++)
                {
                    BillInsideCustomer billInsideCustomer = customerResponse.getListBill().get(i);


                    totalMoney += billInsideCustomer.getAmount();

                    PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
                    bill.setBillId(billInsideCustomer.getBillId());
                    bill.setVI_TTOAN("");
                    bill.setTIEN_THANH_TOAN(billInsideCustomer.getAmount());
                    bill.setTHANG_THANH_TOAN(Common.convertDateToDate(billInsideCustomer.getTerm(), yyyyMMddHHmmssSSS, Common.DATE_TIME_TYPE.MMyyyy));
                    bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
                    bill.setMA_DIEN_LUC(billInsideCustomer.getPcCode());
                    bill.setChecked(false);
                    bill.setMA_KHACH_HANG(billInsideCustomer.getCustomerCode());

                    bill.setCheckEnable(true);

                    bill.setPrintEnable(true);

                    bill.setRequestDate(Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));

                    dataAdapter.getBillKH().add(bill);

                }
                dataAdapter.setTotalMoney(totalMoney);

                List<PayAdapter.DataAdapter> adapters = new ArrayList<PayAdapter.DataAdapter>();
                adapters.add(dataAdapter);

                try {
                    mIPayView.showPayRecyclerPage(adapters, 0, 0, 1, 1, "", false);
                } catch (Exception e) {
                    e.printStackTrace();
                    mIPayView.hideSearchOnlineProcess();
                }

            }
        }

        mIPayView.hideSearchOnlineProcess();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callPay()
    {
        boolean isPayOnline = Common.isNetworkConnected(mIPayView.getContextView());

        mIPayView.showPayingRViewDialogStart();

        List<PayAdapter.BillEntityAdapter> bills = mPayModel.getListBillSelected();



        if(bills.size() == 0)
        {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage(), false, Common.TYPE_DIALOG.LOI, true);

            mIPayView.showPayingRviewDialogFinish();
            return;
        }

        long totalMoney = 0;
        totalBillsChooseDialogTemp = 0;
        for (int i = 0, n = bills.size(); i < n; i++)
        {
            PayAdapter.BillEntityAdapter bill = bills.get(i);
            if(bill.isChecked() && bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode())) {
                totalMoney += bill.getTIEN_THANH_TOAN();
                totalBillsChooseDialogTemp ++;
            }
        }


        if(totalBillsChooseDialogTemp == 0)
        {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage(), false, Common.TYPE_DIALOG.LOI, true);

            mIPayView.showPayingRviewDialogFinish();
            return;
        }


        Account account = mPayModel.getAccountInfo(MainActivity.mEdong);
        if(totalMoney > account.getBalance())
        {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_OFFLINE.e03.getMessage(), false, Common.TYPE_DIALOG.LOI, true);

            mIPayView.showPayingRviewDialogFinish();
            return;
        }

        if(!isPayOnline) { //Thanh Toan Offline
            //Kiểm tra địa bàn thanh toán

            List<String> pcCodes = mPayModel.getPcCodes();
            for (int i = 0, n = pcCodes.size(); i < n; i ++)
            {
                String pc = pcCodes.get(i).substring(0, 2).toUpperCase();
                if (pc.equals("PD") || pc.equals("PE")) {
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_OFFLINE.e04.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
                    mIPayView.showPayingRviewDialogFinish();
                    return;
                }
            }

            countBillPayedSuccess = 0;
            for (int i = 0, n = bills.size(); i < n; i ++)
            {
                if(bills.get(i).isChecked() && bills.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                {
                    try
                    {
                        payOffline(bills.get(i));
                        countBillPayedSuccess ++;
                        mIPayView.refreshAdapterPayRecyclerListBills(true);

                        this.refreshTextCountBillPayedSuccess();

                    }catch (Exception e)
                    {

                    }
                }
            }
            billOnlineAsyncList.clear();
            this.checkFinishPayOnline(false);
        }else //Thanh Toan Online
        {
            countBillPayedSuccess = 0;
            for (int i = 0, n = bills.size(); i < n; i ++)
            {
                if(bills.get(i).isChecked() && bills.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                {
                    try
                    {
                        payOnline(bills.get(i));
                    }catch (Exception e)
                    {
                        this.checkFinishPayOnline(true);
                    }
                }
            }
            this.checkFinishPayOnline(true);
        }






    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void payOnline(final PayAdapter.BillEntityAdapter bill) throws Exception {

        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(mIPayView.getContextView(), MainActivity.mEdong, Common.COMMAND_ID.BILLING.toString(), Common.getVersionApp(mIPayView.getContextView()));
        } catch (Exception e) {
            bill.setMessageError(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());

            this.checkFinishPayOnline(true);
            return;
        }


        //Số ví edong thực hiện thanh toán
        String phone = MainActivity.mEdong;
        Long amount = bill.getTIEN_THANH_TOAN();

        //Phiên làm việc của TNV đang thực hiện
        String session = mPayModel.getSession(MainActivity.mEdong);

        /**
         * Luồng quầy thu đang năng : DT0813
         * Luồng tài khoản tiền điện : DT0807
         * Còn lại sử dụng DT0605
         *
         * ECPAY mặc định sử dụng DT0605
         */

        String partnerCode = Common.PARTNER_CODE_DEFAULT;

        String providerCode = Common.PROVIDER_DEFAULT;

        //create request to server
        String jsonRequestBillOnline = SoapAPI.getJsonRequestBillOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                bill.getMA_KHACH_HANG(),
                session,
                bill.getBillId(),
                amount,
                phone,
                providerCode,
                partnerCode,
                configInfo.getAccountId());

        if (jsonRequestBillOnline == null) {
            bill.setMessageError(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_REQUEST.toString());

            this.checkFinishPayOnline(true);
            return;
        }


        SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone> billingOnlineResponeAsyncSoap = new SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>(handlerDelay, BillingOnlineRespone.class,
                new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                    @Override
                    public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                    }

                    @Override
                    public void onUpdate(String message) {

                    }

                    @Override
                    public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {

                        if (response == null) {
                            return;
                        }

                        //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu không thành công
                        final Common.CODE_REPONSE_BILL_ONLINE codeResponse = Common.CODE_REPONSE_BILL_ONLINE.findCodeMessage(response.getFooter().getResponseCode());
                        BodyBillingOnlineRespone body = response.getBodyByType();

                        if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e825.getCode())) {
                            /**
                             * Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonDaThanhToanBoiNguonKhac(body);
                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode());
                            bill.setVI_TTOAN("");
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10006.getMessage());

                            countBillPayedSuccess ++;
                            mIPayView.refreshAdapterPayRecyclerListBills(true);

                        }else if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e814.getCode())) {
                            /**
                             * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonDaThanhToanBoiViKhac(body);
                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
                            bill.setVI_TTOAN(body.getPaymentEdong());
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10007.getMessage());
                            countBillPayedSuccess ++;
                            mIPayView.refreshAdapterPayRecyclerListBills(true);
                        }else if (!codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode())
                                &&
                                !codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e095.getCode())) {
                            /**
                             * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonChamNoLoi(body);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10009.getMessage());
                            mIPayView.refreshAdapterPayRecyclerListBills(true);
                        }else
                        {
                            String gateEVN = body.getBillingType();
                            if(codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode()) && gateEVN.equalsIgnoreCase("ON"))
                            {//Thanh Toan THanh Cong Trong Gio Mo Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioMoKetNoi(body);
                            }else
                            {//Thanh Toan Thanh Cong Trong Gio Dong Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioDongKetNoi(body);
                            }

                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
                            bill.setVI_TTOAN(MainActivity.mEdong);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage());
                            mIPayView.refreshAdapterPayRecyclerListBills(true);

                            countBillPayedSuccess ++;
                        }



                        refreshTextCountBillPayedSuccess();

                        billOnlineAsyncList.remove(soap);
                        checkFinishPayOnline(true);
                    }

                    @Override
                    public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                        billOnlineAsyncList.remove(soap);
                        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkFinishPayOnline(true);
                            }
                        });
                    }
                });

        billOnlineAsyncList.add(billingOnlineResponeAsyncSoap);
        billingOnlineResponeAsyncSoap.execute(jsonRequestBillOnline);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void checkFinishPayOnline(boolean isOnline)
    {

        if(isOnline && countBillPayedSuccess > 0)
        {
            if(billOnlineAsyncList.size() == 0) //call cap nhat vi
            {
                //Update tien trong vi
                long tienTT_THanhCong = this.refreshTotalBillsAndTotalMoneyInDialogWhenChecked(Common.STATUS_BILLING.DA_THANH_TOAN);
                mPayModel.truTienTrongVi(MainActivity.mEdong, tienTT_THanhCong);

                ConfigInfo configInfo = null;
                try {
                    configInfo = Common.setupInfoRequest(mIPayView.getContextView(), MainActivity.mEdong, Common.COMMAND_ID.BALANCE.toString(), Common.getVersionApp(mIPayView.getContextView()));
                } catch (Exception e) {
                    return;
                }

                //create request to server
                String jsonRequestAccount = SoapAPI.getJsonRequestBalance(
                        configInfo.getAGENT(),
                        configInfo.getAgentEncypted(),
                        configInfo.getCommandId(),
                        configInfo.getAuditNumber(),
                        configInfo.getMacAdressHexValue(),
                        configInfo.getDiskDriver(),
                        configInfo.getSignatureEncrypted(),
                        MainActivity.mEdong,
                        mPayModel.getSession(MainActivity.mEdong),
                        Common.PARTNER_CODE_DEFAULT,
                        configInfo.getAccountId());

                if (jsonRequestAccount == null) {
                    return;
                }

                try {
                    SoapAPI.AsyncSoapIncludeTimout<BalanceRespone> accountResponeAsyncSoap = new SoapAPI.AsyncSoapIncludeTimout<BalanceRespone>(handlerDelay, BalanceRespone.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                        @Override
                        public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                        }

                        @Override
                        public void onUpdate(String message) {

                        }

                        @Override
                        public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {

                            if(response != null && response.getFooter().getResponseCode().equalsIgnoreCase("000"))
                            {
                                final GsonBuilder gsonBuilder = new GsonBuilder();
                                final Gson gson = gsonBuilder.create();

                                BodyBalanceRespone.ResponseRequestBalance resBalance = gson.fromJson(((BodyBalanceRespone)response.getBody()).getResponseRequestBalance(), BodyBalanceRespone.ResponseRequestBalance.class);
                                mPayModel.updateSoDuKhaDung(resBalance.geteDong(), resBalance.getBalance(), resBalance.getLockMoney());
                            }

                            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                            mIPayView.showPayingRviewDialogFinish();
                            mIPayView.refreshAdapterPayRecyclerListBills(true);
                        }

                        @Override
                        public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                                    mIPayView.showPayingRviewDialogFinish();
                                    mIPayView.refreshAdapterPayRecyclerListBills(true);
                                }
                            });
                        }
                    });
                    accountResponeAsyncSoap.execute(jsonRequestAccount);
                } catch (Exception e) {
                    e.printStackTrace();
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                    mIPayView.showPayingRviewDialogFinish();
                    mIPayView.refreshAdapterPayRecyclerListBills(true);
                }
            }
        }else
        {
            if(billOnlineAsyncList.size() == 0)
            {
                //Update tien trong vi
                long tienTT_THanhCong = this.refreshTotalBillsAndTotalMoneyInDialogWhenChecked(Common.STATUS_BILLING.DA_THANH_TOAN);
                mPayModel.truTienTrongVi(MainActivity.mEdong, tienTT_THanhCong);


                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                mIPayView.showPayingRviewDialogFinish();
                mIPayView.refreshAdapterPayRecyclerListBills(true);
            }
        }

    }


    void payOffline(PayAdapter.BillEntityAdapter bill)
    {
        bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
        bill.setVI_TTOAN(MainActivity.mEdong);

        mPayModel.updateHoaDonNo(bill.getBillId(),Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);
        EntityHoaDonNo entityHoaDonNo = mPayModel.getHoaDonNo(bill.getBillId());

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(entityHoaDonNo);

        entityHoaDonThu.setVI_TTOAN(MainActivity.mEdong);
        entityHoaDonThu.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.OFFLINE.getCode());
        entityHoaDonThu.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
        entityHoaDonThu.setTRANG_THAI_CHAM_NO(Common.TRANG_THAI_CHAM_NO.CHUA_CHAM.getCode());
        entityHoaDonThu.setTRANG_THAI_HUY("");
        entityHoaDonThu.setTRANG_THAI_HOAN_TRA("");
        entityHoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO("");
        entityHoaDonThu.setTRANG_THAI_DAY_CHAM_NO(Common.TRANG_THAI_DAY_CHAM_NO.CHUA_DAY.getCode());
        entityHoaDonThu.setSO_LAN_IN_BIEN_NHAN(0);
        entityHoaDonThu.setIN_THONG_BAO_DIEN("");

        mPayModel.insertHoaDonThu(entityHoaDonThu);

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.TTOAN_OFFLINE.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);
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
    public void callProcessDeleteBillOnline(String edong, PayAdapter.BillEntityAdapter bill, PayAdapter.BillInsidePayAdapter adapter) {
        boolean fail = TextUtils.isEmpty(edong)|| bill == null;
        if (fail)
            return;

        mIPayView.showDialogDeleteBillOnline(edong, bill, adapter);
    }

    @Override
    public void callFillInfoBillDeleteDialog(String edong, PayAdapter.BillEntityAdapter bill) {
        boolean fail = TextUtils.isEmpty(edong) || bill == null;
        if (fail)
            return;

        this.billDeleteOnline = bill;

        mIPayView.showInfoBillDeleteDialog(bill.getMA_KHACH_HANG(), bill.getTEN_KHACH_HANG(), bill.getTHANG_THANH_TOAN(), bill.getTIEN_THANH_TOAN());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callDeleteOnlineSoap(String edong, String reasonDeleteBill) {
        boolean fail = this.billDeleteOnline == null;
        if (fail)
            return;

        if (TextUtils.isEmpty(reasonDeleteBill)) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10001.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
        // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
        // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi
        callAPICheckTransAndRequestCancelBill(edong, reasonDeleteBill);
    }

    @Override
    public void callShowDialogBarcode() {
        mIPayView.showDialogBarcode();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPICheckTransAndRequestCancelBill(final String edong, final String reasonDeleteBill) {

        Context context = mIPayView.getContextView();
        //check wifi
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        ConfigInfo configInfo = null;
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.CHECK_TRANS.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        Long amount = billDeleteOnline.getTIEN_THANH_TOAN();
        String customerCode = billDeleteOnline.getMA_KHACH_HANG();
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

        if (jsonRequestCheckTrainOnline == null) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10002.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            SoapAPI.AsyncSoapIncludeTimout<CheckTrainOnlineResponse> async = new SoapAPI.AsyncSoapIncludeTimout<CheckTrainOnlineResponse>(handlerDelay, CheckTrainOnlineResponse.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {
                    mIPayView.showDeleteBillOnlineProcess();
                    mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);
                }

                @Override
                public void onUpdate(final String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;

                    ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                mIPayView.showMessageNotifyDeleteOnlineDialog(message, Common.TYPE_DIALOG.LOI);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                    if (response == null) {
                        return;
                    }

                    // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
                    // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
                    // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

                    Common.CODE_REPONSE_API_CHECK_TRAINS codeResponse = Common.CODE_REPONSE_API_CHECK_TRAINS.findCodeMessage(response.getFooter().getResponseCode());
                    if (codeResponse != Common.CODE_REPONSE_API_CHECK_TRAINS.eBILLING) {
                        try
                        {
                            mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                            mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                            return;
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    callAPITransationCancellation(edong, (CheckTrainOnlineResponse) response, reasonDeleteBill);
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try{
                                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
            async.execute(jsonRequestCheckTrainOnline);
        } catch (Exception e) {
            try{
                mIPayView.showMessageNotifyDeleteOnlineDialog(e.getMessage(), Common.TYPE_DIALOG.LOI);
            }catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }


    }

    @Override
    public void cancelSeachOnline() {
        if(currentAsyncSearchOnline != null)
        {
            if(!currentAsyncSearchOnline.isEndCallSoap())
                currentAsyncSearchOnline.cancel(true);

        }
        this.finishSearchOnline(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void reseachOnline(String edong) {
        cancelSeachOnline();
        if (currentAsyncSearchOnline != null) {
            mIPayView.showEditTextSearch((String)currentAsyncSearchOnline.getUserData());
            callSearchOnline(edong, (String)currentAsyncSearchOnline.getUserData(), true);
        } else {
            mIPayView.showMessageNotifySearchOnline(Common.CODE_REPONSE_SEARCH_ONLINE.e9999.getMessage(), Common.TYPE_DIALOG.LOI);
            Log.e(TAG, "reseachOnline: soapSearchOnline không được khởi tạo");
        }
    }

    @Override
    public void callProcessDataBillFragmentChecked(String edong, String code, int posCustomer, PayAdapter.BillEntityAdapter bill, int posBillInside, int indexBegin, int indexEnd) {
//        if (TextUtils.isEmpty(edong))
//            return;
//        if (TextUtils.isEmpty(code))
//            return;
//        if (bill == null)
//            return;
//        if (indexEnd > mAdapterList.size())
//            return;
//
//        mPayModel.updateBillIsChecked(edong, code, (int) bill.getBillId(), bill.isChecked());
//
//        //show billDeleteOnline or not show billDeleteOnline
//        mPayModel.updateCustomerIsShowBill(edong, code, bill.isChecked());
//
//        mAdapterList.get(indexBegin + posCustomer).setShowBill(bill.isChecked());
//
//        //show total bills and total money of all bills is checked
//        refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    @Override
    public void callPayRecyclerDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

//        listBillDialog = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(Common.STATUS_BILLING.CHUA_THANH_TOAN);
//
        mIPayView.showPayRecyclerListBills(getPayModel().getListBillSelected());
    }

    @Override
    public void callProcessDataBillDialogChecked(String edong, int pos, boolean isChecked) {

//        listBillDialog.get(pos).setChecked(isChecked);
//        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    private long refreshTotalBillsAndTotalMoneyInDialogWhenChecked(Common.STATUS_BILLING statusBilling) {
        int totalBillsChooseDialog = 0;
        long totalMoneyBillChooseDialog = 0;

        for (int i = 0, n = mPayModel.getListBillSelected().size(); i < n; i ++)
        {
            PayAdapter.BillEntityAdapter bill = mPayModel.getListBillSelected().get(i);
            if(bill.isChecked() && bill.getTRANG_THAI_TT().equalsIgnoreCase(statusBilling.getCode()))
            {
                totalBillsChooseDialog ++;
                totalMoneyBillChooseDialog += bill.getTIEN_THANH_TOAN();
            }
        }

        mIPayView.showCountBillsAndTotalMoneyInDialog(totalBillsChooseDialog, totalMoneyBillChooseDialog);

        return totalMoneyBillChooseDialog;
    }


    private SoapAPI.AsyncSoapCheckTrainPayingBill.AsyncSoapCheckTrainPayingBillCallBack soapCheckTrainPayingBillCallBack = new SoapAPI.AsyncSoapCheckTrainPayingBill.AsyncSoapCheckTrainPayingBillCallBack() {
        private String edong;
        private PayAdapter.BillEntityAdapter entity;

        @Override

        public void onPre(final SoapAPI.AsyncSoapCheckTrainPayingBill soapCheckTrainPayingBill) {
//            edong = soapCheckTrainPayingBill.getEdong();
//            entity = soapCheckTrainPayingBill.getEntity();
//
//            mIPayView.showPayingRViewDialogStart();
//
//            //check wifi
//            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
//            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());
//
////            if (!isHasWifi) {
////                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
////                soapSearchOnline.setEndCallSoap(true);
////                soapSearchOnline.cancel(true);
////            }
//            if (!isHasNetwork) {
//                mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), false, Common.TYPE_DIALOG.LOI, false);
//
//                soapCheckTrainPayingBill.setEndCallSoap(true);
//                soapCheckTrainPayingBill.cancel(true);
//            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyBillOnlineDialog(message, false, Common.TYPE_DIALOG.LOI, false);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(CheckTrainOnlineResponse response) {
//            if (response == null) {
//                return;
//            }
//
//            //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu không thành công
//            Common.CODE_REPONSE_API_CHECK_TRAINS codeResponse = Common.CODE_REPONSE_API_CHECK_TRAINS.findCodeMessage(response.getFooter().getResponseCode());
//            long billId = response.getBody().getBillId();
//            String date = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);
//
//            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eSOURCE_OTHER.getCode()) {
//                /**
//                 * Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
//                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
//                 * Ví thanh toán edong = null
//                 * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
//                 */
//                int rowAffect = mPayModel.updateBillWith(
//                        edong, (int) billId, //where
//                        Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), edong);
//                Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);
//
//                /**
//                 * Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
//                 * Ví thanh toán edong = null
//                 * Hình thức thanh toán payments = null
//                 * Trạng thái thanh toán payStatus = 03_Đã thanh toán bởi nguồn khác(trong database payStatus = 2)
//                 * Trạng thái chấm nợ stateOfDebt = null
//                 * Trạng thái hủy stateOfCancel = null
//                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
//                 * Trạng thái hoàn trả stateOfReturn = null
//                 * Số lần in biên nhận countPrintReceipt = 0
//                 * In thông báo điện printInfo = null
//                 * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
//                 * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
//                 *
//                 * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
//                 */
//
//                rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
//                        edong, (int) billId, null, //where
//                        Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
//                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
//                        date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());
//
//                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);
//
//                return;
//            }
//
//            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eEDONG_OTHER.getCode()) {
//                /**
//                 * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
//                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
//                 * Ví thanh toán edong = Ví thanh toán do server trả về
//                 * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác (Trong database = 3)
//                 */
//                int rowAffect = mPayModel.updateBillWith(
//                        edong, (int) billId, //where
//                        Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), response.getBody().getEdong());
//                Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);
//
//
//                /**
//                 * Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
//                 * Ví thanh toán edong = Ví thanh toán do server trả về
//                 * Hình thức thanh toán payments = null
//                 * Trạng thái thanh toán payStatus = 04_Đã thanh toán bởi ví khác(trong database payStatus = 3)
//                 * Trạng thái chấm nợ stateOfDebt = null
//                 * Trạng thái hủy stateOfCancel = null
//                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
//                 * Trạng thái hoàn trả stateOfReturn = null
//                 * Số lần in biên nhận countPrintReceipt = 0
//                 * In thông báo điện printInfo = null
//                 * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
//                 * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
//                 *
//                 * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
//                 */
//
//                rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
//                        edong, (int) billId,//where
//                        response.getBody().getEdong(),
//                        Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
//                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
//                        date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());
//
//                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);
//
//                return;
//            }
//
//            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eERROR.getCode()) {
//                /**
//                 * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
//                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
//                 * Ví thanh toán edong = Ví đăng nhập
//                 * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
//                 */
//                int rowAffect = mPayModel.updateBillWithWithThanhToanError(
//                        edong, (int) billId, //where
//                        edong);
//                Log.d(TAG, "rowAffect updateBillWithWithThanhToanError = " + rowAffect);
//
//
//                /**
//                 * Lưu thông tin hóa đơn vào Danh sách thu (Bảng Debt): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
//                 * Ví thanh toán edong = Ví đăng nhập
//                 * Hình thức thanh toán payments = 01_Online
//                 * Trạng thái thanh toán payStatus = 02_Đã thanh toán(trong database payStatus = 1 tương đương STATUS_BILLING)
//                 * Trạng thái chấm nợ stateOfDebt = 04_Chấm lỗi
//                 * Trạng thái hủy stateOfCancel = null
//                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
//                 * Trạng thái hoàn trả stateOfReturn = null
//                 * Số lần in biên nhận countPrintReceipt = 0
//                 * In thông báo điện printInfo = null
//                 */
//
//                rowAffect = mPayModel.updateBillDebtWithThanhToanErrorOrSuccess(
//                        edong, (int) billId,//where
//                        edong,
//                        Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
//                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
//                        ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());
//
//                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanErrorOrSuccess = " + rowAffect);
//
//
//                /**
//                 * Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin như Danh sách thu, chỉ khác bổ sung thêm các thông tin
//                 * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
//                 * Mã giao dịch tradeCode = 05_Thanh toán Online_Chấm nợ lỗi
//                 */
//
//                rowAffect = mPayModel.updateBillHistoryWithThanhToanErrorOrSuccess(
//                        edong, (int) billId,//where
//                        edong,
//                        Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
//                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
//                        ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), date, Common.TRADING_CODE.THANH_TOAN_ONLINE_CHAM_NO_LOI.getCode());
//
//                Log.d(TAG, "rowAffect updateBillHistoryWithThanhToanErrorOrSuccess = " + rowAffect);
//                return;
//            }
//
//            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
//            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
//            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi
//
//            if (codeResponse != Common.CODE_REPONSE_API_CHECK_TRAINS.eBILLING) {
//                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
//                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
//                return;
//            }

//            payOnlineTheBill(getVersionApp(mIPayView.getContextView()), edong, entity);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapCheckTrainPayingBill soapCheckTrainPayingBill) {
            soapCheckTrainPayingBill.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapCheckTrainPayingBill.isEndCallSoap()) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), false, Common.TYPE_DIALOG.LOI, false);
                    }
                }
            });
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPITransationCancellation(String edong, CheckTrainOnlineResponse response, final String reasonDeleteBill) {


        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();
        mIPayView.enableReasonEditText();
        mIPayView.visibleButtonDeleteDialog(SHOW_ALL);

        //check wifi
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());


        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);

            return;
        }

        ConfigInfo configInfo = null;
        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.TRANSACTION_CANCELLATION.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }


        BodyCheckTrainOnlineResponse body = (BodyCheckTrainOnlineResponse)response.getBody();

        Long amount = body.getAmount();
        String code = body.getCustomerCode();
        final Long billId = body.getBillId();
        String requestDate = body.getRequestDate();

//        Long traceNumber = mPayModel.getTraceNumberBill(edong, code, billId);

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
                reasonDeleteBill,

                configInfo.getAccountId());

        if (jsonRequestDeleteBillOnline == null) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_TRANSACTION_CANCELLATION.ex10002.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            SoapAPI.AsyncSoapIncludeTimout<DeleteBillOnlineRespone> async = new SoapAPI.AsyncSoapIncludeTimout<DeleteBillOnlineRespone>(handlerDelay, DeleteBillOnlineRespone.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {

                @Override
                public void onPre(SoapAPI.AsyncSoapIncludeTimout soapDeleteBillOnline) {

                }

                @Override
                public void onUpdate(final String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;

                    ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIPayView.showMessageNotifyDeleteOnlineDialog(message, Common.TYPE_DIALOG.LOI);
                        }
                    });
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                    if (response == null) {
                        return;
                    }

                    // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
                    // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
                    // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

                    Common.CODE_REPONSE_TRANSACTION_CANCELLATION codeResponse = Common.CODE_REPONSE_TRANSACTION_CANCELLATION.findCodeMessage(response.getFooter().getResponseCode());
                    if (codeResponse != Common.CODE_REPONSE_TRANSACTION_CANCELLATION.e000) {
                        mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                        mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                        return;
                    }

                    //Cập nhật thông tin hủy hóa đơn trên danh sách hóa đơn

                    mPayModel.updateBillHuy(billId, Common.TRANG_THAI_HUY.CHO_HUY, reasonDeleteBill);

                    mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.THANH_CONG);
                    mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    try {
                        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                            }
                        });
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            async.execute(jsonRequestDeleteBillOnline);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(e.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

    }
    private void hoaDonThanhCongTrongGioDongKetNoi(BodyBillingOnlineRespone respone) {
        //TODO mark
        /**
         * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = ON)
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */

        mPayModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(mPayModel.getHoaDonNo(respone.getBillId()));

        entityHoaDonThu.setVI_TTOAN(MainActivity.mEdong);
        entityHoaDonThu.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.ONLINE.getCode());
        entityHoaDonThu.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
        entityHoaDonThu.setTRANG_THAI_CHAM_NO(Common.TRANG_THAI_CHAM_NO.DANG_CHO_XU_LY.getCode());
        entityHoaDonThu.setTRANG_THAI_HUY("");
        entityHoaDonThu.setTRANG_THAI_HOAN_TRA("");
        entityHoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO("");
        entityHoaDonThu.setTRANG_THAI_DAY_CHAM_NO("");
        entityHoaDonThu.setSO_LAN_IN_BIEN_NHAN(0);
        entityHoaDonThu.setIN_THONG_BAO_DIEN("");

        mPayModel.insertHoaDonThu(entityHoaDonThu);

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.TT_ONLINE_CHAM_NO_OFFLINE.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);
    }



    private void hoaDonThanhCongTrongGioMoKetNoi(BodyBillingOnlineRespone respone) {
        //TODO mark
        /**
         * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = ON)
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */

        mPayModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(mPayModel.getHoaDonNo(respone.getBillId()));

        entityHoaDonThu.setVI_TTOAN(MainActivity.mEdong);
        entityHoaDonThu.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.ONLINE.getCode());
        entityHoaDonThu.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
        entityHoaDonThu.setTRANG_THAI_CHAM_NO(Common.TRANG_THAI_CHAM_NO.DA_CHAM.getCode());
        entityHoaDonThu.setTRANG_THAI_HUY("");
        entityHoaDonThu.setTRANG_THAI_HOAN_TRA("");
        entityHoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO("");
        entityHoaDonThu.setTRANG_THAI_DAY_CHAM_NO("");
        entityHoaDonThu.setSO_LAN_IN_BIEN_NHAN(0);
        entityHoaDonThu.setIN_THONG_BAO_DIEN("");

        mPayModel.insertHoaDonThu(entityHoaDonThu);

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.TT_ONLINE_CHAM_NO_ONLINE.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);
    }

    private void hoaDonChamNoLoi(BodyBillingOnlineRespone respone) {
        /**
         * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
         */

        mPayModel.updateHoaDonNo(respone.getBillId(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(mPayModel.getHoaDonNo(respone.getBillId()));

        entityHoaDonThu.setVI_TTOAN(MainActivity.mEdong);
        entityHoaDonThu.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.ONLINE.getCode());
        entityHoaDonThu.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
        entityHoaDonThu.setTRANG_THAI_CHAM_NO(Common.TRANG_THAI_CHAM_NO.CHAM_LOI.getCode());
        entityHoaDonThu.setTRANG_THAI_HUY("");
        entityHoaDonThu.setTRANG_THAI_HOAN_TRA("");
        entityHoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO("");
        entityHoaDonThu.setTRANG_THAI_DAY_CHAM_NO("");
        entityHoaDonThu.setSO_LAN_IN_BIEN_NHAN(0);
        entityHoaDonThu.setIN_THONG_BAO_DIEN("");

        mPayModel.insertHoaDonThu(entityHoaDonThu);

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.TT_ONLINE_CHAM_NO_LOI.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);

    }

    private void hoaDonDaThanhToanBoiViKhac(BodyBillingOnlineRespone response) {
        /**
         * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví thanh toán do server trả về
         * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác
         */

        mPayModel.updateHoaDonNo(response.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode(), response.getPaymentEdong());

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mPayModel.getHoaDonNo(response.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN(response.getPaymentEdong());
        entityLichSuThanhToan.setHINH_THUC_TT("");
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.CAP_NHAT_TTHAI_NO.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }

    private void hoaDonDaThanhToanBoiNguonKhac(BodyBillingOnlineRespone respone) {
        /** Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = null
         * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
         */

        mPayModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode(), "");

        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mPayModel.getHoaDonNo(respone.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN("");
        entityLichSuThanhToan.setHINH_THUC_TT("");
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.CAP_NHAT_TTHAI_NO.getCode());
        mPayModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }
    //endregion
}
