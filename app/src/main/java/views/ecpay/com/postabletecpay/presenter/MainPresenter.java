package views.ecpay.com.postabletecpay.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Base64;
import org.ecpay.client.test.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import views.ecpay.com.postabletecpay.model.MainModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BodyBillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FileGenResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.GetPCInfoRespone;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.IMainView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG_SYNC;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG_SYNC_DATA;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG_SYNC_GET_FILE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_DOWNLOAD;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView mIMainView;
    private MainModel mainModel;


    private Handler mHandler = new Handler();
    private String edong;
    //    private String bookCmis;
    private static List<PayAdapter.DataAdapter> mDataPayAdapter = new ArrayList<>();
    //Insert BOOK CMIS
    List<ListBookCmisResponse> listBookCmis = new ArrayList<>();
    List<ListBookCmisResponse> listBookCmisExist = new ArrayList<>();
    List<ListBookCmisResponse> listBookCmisNeedDownload = new ArrayList<>();

    private int allProcessDownload;
    private List<SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>> CurrentPostBillAsync = new ArrayList<>();

    public MainPresenter(IMainView mIMainView, String edong) {
        this.mIMainView = mIMainView;
        this.mainModel = new MainModel(mIMainView.getContextView());
        this.edong = edong;

        mainModel.getManagerSharedPref().addSharePref(Common.SHARE_REF_CHANGED_GEN_FILE, MODE_PRIVATE);

    }


    @Override
    public void callPutTransactionOffBill(String edong) {
        boolean fail = TextUtils.isEmpty(edong);
        if (fail)
            return;

        //check network
        Context context = mIMainView.getContextView();
        boolean isHasNetwork = Common.isNetworkConnected(mIMainView.getContextView());

//        if (!isHasNetwork) {
//            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
//            return;
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ConfigInfo getConfig(Common.COMMAND_ID commandId) throws Exception {
        ConfigInfo configInfo = null;
        String versionApp = "";
        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(mIMainView.getContextView().getPackageName(), 0).versionName;

            configInfo = Common.setupInfoRequest(mIMainView.getContextView(), edong, commandId.toString(), versionApp);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            return configInfo;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ConfigInfo getConfigWithPcCode(Common.COMMAND_ID commandId, String pcCode) throws Exception {
        ConfigInfo configInfo = null;
        String versionApp = "";
        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(mIMainView.getContextView().getPackageName(), 0).versionName;

            configInfo = Common.setupInfoRequest(mIMainView.getContextView(), edong, commandId.toString(), versionApp, pcCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            return configInfo;
        }
    }

    //TODO syncBookCmis
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public ListBookCmisReponse syncBookCmis() throws Exception {
        if (!Common.isNetworkConnected(mIMainView.getContextView())) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
        }

        ConfigInfo configInfo;
        try {
            configInfo = getConfig(Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        String jsonRequestEVN = SoapAPI.getJsonSyncPC(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                configInfo.getAccountId(),
                configInfo.getAccountId());

        if (jsonRequestEVN == null) {
            return null;
        }

        final String maKH = "";
        final String soTien = "";
        final  String kyPhatSinh = "";

        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER, true);
        SoapAPI.SoapGetBookCMIS soapSynchronizePC = new SoapAPI.SoapGetBookCMIS(mIMainView);
        soapSynchronizePC.execute(jsonRequestEVN);

        try {
            return soapSynchronizePC.get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected GetPCInfoRespone syncPcEVN(List<ListEvnPCResponse> listEvnPc) throws Exception {


        String pcName = "", pcCode = "", pcCodeExt = "", pcTax = "", pcAddress = "", pcPhoneNumber = "";

        if (listEvnPc.size() > 0) {
            pcName = listEvnPc.get(0).getFullName();
            pcCode = listEvnPc.get(0).getCode();
            pcCodeExt = listEvnPc.get(0).getExt();
            pcTax = listEvnPc.get(0).getTaxCode();
            pcAddress = listEvnPc.get(0).getAddress();
            pcPhoneNumber = listEvnPc.get(0).getPhone1();
        }
/*
        Cursor cursorEvnPc = mainModel.getCursorEvnPc(edong);

        if (cursorEvnPc != null && cursorEvnPc.moveToFirst()) {
            pcName = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("fullName"));
            pcCode = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("code"));
            pcCodeExt = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("ext"));
            pcTax = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("taxCode"));
            pcAddress = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("address"));
            pcPhoneNumber = cursorEvnPc.getString(cursorEvnPc.getColumnIndex("phone1"));
        }
*/

        ConfigInfo configInfo = null;
        try {
            configInfo = getConfigWithPcCode(Common.COMMAND_ID.GET_PC_INFO, pcCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonGetPCInfo = SoapAPI.getJsonGetPCInfo(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                pcName,
                pcCode,
                pcTax,
                pcAddress,
                pcPhoneNumber,
                configInfo.getAccountId()
        );

        if (jsonGetPCInfo == null) {
            return null;
        }

        final String maKH = "";
        final String soTien = "";
        final String kyPhatSinh = "";

        try {
            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.GET_PC_INFO, true);
        } catch (Exception e) {
            Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
        }

        SoapAPI.AsyncSoapGetPCInfo soapGetBookCMIS = new SoapAPI.AsyncSoapGetPCInfo(mIMainView);
        soapGetBookCMIS.execute(jsonGetPCInfo);

        try {
            return soapGetBookCMIS.get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e;
        }
    }


    /*  private SoapAPI.SoapGetBookCMIS.AsyncSoapSynchronizePCCallBack callBackGetBookCMIS = new SoapAPI.SoapGetBookCMIS.AsyncSoapSynchronizePCCallBack() {

          @Override
          public void onPre(SoapAPI.SoapGetBookCMIS soapSynchronizeInvoices) {
              mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_START.getTitle(), 0);
          }

          @Override
          public void onUpdate(String message) {

          }

          @RequiresApi(api = Build.VERSION_CODES.KITKAT)
          @Override
          public void onPost(ListBookCmisReponse response) {
              if (response == null) {
                  mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_START.getTitle(), 100);
                  return;
              }

              try {
                  GsonBuilder gsonBuilder = new GsonBuilder();
                  Gson gson = gsonBuilder.create();
                  //Insert EVN PC
                  ListEvnPCResponse listEvnPCResponse;
                  String dataEvnPC = response.getBodyListEVNResponse().getListEvnPCLoginResponse();
                  JSONArray jaEvnPC = new JSONArray(dataEvnPC);
                  for (int i = 0; i < jaEvnPC.length(); i++) {
                      listEvnPCResponse = gson.fromJson(jaEvnPC.getString(i), ListEvnPCResponse.class);
                      if (mainModel.checkEvnPCExist(listEvnPCResponse.getPcId(), edong) == 0) {
                          mainModel.insertEvnPC(listEvnPCResponse, edong);
                      }
                  }

                  String dataBookCmis = response.getBodyListEVNResponse().getListBookCmissResponse();
                  JSONArray jaBookCmis = new JSONArray(dataBookCmis);

                  List<ListBookCmisResponse> listBook = new ArrayList<>();
                  for (int i = 0; i < jaBookCmis.length(); i++) {
                      ListBookCmisResponse listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);
                      listBook.add(listBookCmisResponse);
                  }

                  for (int i = 0; i < 100; i++) {
                      Thread.sleep(500);
                      mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_START.getTitle(), i);
                  }

              *//*    for (int i = 0; i < listBook.size(); i++) {
                    if (mainModel.checkBookCmisExist(listBook.get(i).getBookCmis()) == 0) {
                        mainModel.insertBookCmis(listBook.get(i));
                    }

                    File fileDownload = new File(Common.PATH_FOLDER_DOWNLOAD + listBook.get(i).getBookCmis() + "_" + edong + ".zip");
                    if (!fileDownload.exists()) {
                        listBookCmisNeedDownload.add(listBook.get(i));
                    } else {
                        listBookCmisExist.add(listBook.get(i));
                    }

                    final int statusProcess = i / listBook.size() * 100;

                    mIMainView.updateDelayPbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_START.getTitle(), statusProcess);
                }
*//*
//                mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_END.getTitle(), 100);
//
//                //delay 0.5s
                Thread.sleep(TIME_DELAY_DOWNLOAD);
                mIMainView.finishHidePbarDownload();

              *//*  //delay 0.5s
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIMainView.finishHidePbarDownload();
//                        mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.GET_FILE_GEN_START.getTitle(), 0);

                     *//**//*   allProcessDownload = listBookCmisNeedDownload.size() + listBookCmisExist.size();
                        syncFileGen(listBookCmisNeedDownload);
                        syncData(listBookCmisExist);*//**//*
                    }
                }, TIME_DELAY_DOWNLOAD);*//*

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
//                mIMainView.finishHidePbarDownload();
            }
        }
    };

*/

    //TODO syncFileGen
    //region đồng bộ hoá đơn
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ListDataZipResponse syncFileGen(ListBookCmisResponse bookCmisNeedDownload) throws Exception {

        if (!Common.isNetworkConnected(mIMainView.getContextView())) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
        }

        Context context = mIMainView.getContextView();

        ConfigInfo configInfo;
        String versionApp = "";
        String bookCmis = bookCmisNeedDownload.getBookCmis();
        String pcCodeExt = bookCmisNeedDownload.getPcCodeExt();

        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, pcCodeExt);
        } catch (PackageManager.NameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), pcCodeExt, configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
        } catch (Exception e) {
            throw e;
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        String jsonRequestZipData = SoapAPI.getJsonRequestSynDataZip(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                pcCodeExt,
                bookCmis,
                configInfo.getAccountId());


        if (jsonRequestZipData == null) {
            return null;
        }
        final String maKH ="";
        final String soTien = "";
        final  String kyPhatSinh = "";
        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.GET_FILE_GEN, true);
        SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeDataZip = new SoapAPI.AsyncSoapSynchronizeDataZip(mIMainView);
        soapSynchronizeDataZip.execute(jsonRequestZipData);

        try {
            return soapSynchronizeDataZip.get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e;
        }
    }


    //TODO syncData
    public ListDataResponse syncData(ListBookCmisResponse bookCmisResponse) throws Exception {

        if (!Common.isNetworkConnected(mIMainView.getContextView())) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
        }

        ConfigInfo configInfo;
        String versionApp = "";
        Context context = mIMainView.getContextView();
        String bookCmis = bookCmisResponse.getBookCmis();
        String pcCodeExt = bookCmisResponse.getPcCodeExt();

        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.SYNC_DATA.toString(), versionApp, pcCodeExt);
        } catch (PackageManager.NameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), pcCodeExt, configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
        } catch (Exception e) {
            throw e;
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);


        File fileBookCmis = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
        if (!fileBookCmis.exists()) {
            throw new Exception("File " + Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip không có trong sdcard");
        }

//            long maxIdChanged = mainModel.getMaxIdChanged(edong, bookCmis);
//            String maxDateChanged = mainModel.getMaxDateChanged(edong, bookCmis);
        //A Trường setup maxId
        long maxIdChanged = 0;
        String maxDateChanged = "";
        String jsonRequestData = SoapAPI.getJsonRequestSynData(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                pcCodeExt,
                bookCmis,
                maxIdChanged,
                maxDateChanged,
                configInfo.getAccountId());


        if (jsonRequestData == null) {
            return null;
        }

        final String maKH = "";
        final String soTien = "";
        final  String kyPhatSinh = "";

        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.SYNC_DATA, true);

        SoapAPI.AsyncSoapSynchronizeData soapSyncData = new SoapAPI.AsyncSoapSynchronizeData(mIMainView);
        soapSyncData.execute(jsonRequestData);

        try {
            return soapSyncData.get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkAndPostBill() {

        if (CurrentPostBillAsync != null && !CurrentPostBillAsync.isEmpty()) {
            return false;
        }

        List<PayAdapter.BillEntityAdapter> lstTransactionOff = mainModel.selectOfflineBill();
        if (lstTransactionOff.size() == 0)
            return true;


        for (int i = 0, n = lstTransactionOff.size(); i < n; i++) {
            try {
                payOnline(lstTransactionOff.get(i));
            } catch (Exception e) {
            }
        }

        return false;
    }


    @Override
    public void refreshDataPayAdapter() {
    }

    @Override
    public List<PayAdapter.DataAdapter> getDataPayAdapter() {
        return mDataPayAdapter;
    }

    @Override
    public void sync() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    //region Cài đặt các tham số
                    listBookCmis = new ArrayList<ListBookCmisResponse>();
                    listBookCmisExist = new ArrayList<ListBookCmisResponse>();
                    listBookCmisNeedDownload = new ArrayList<ListBookCmisResponse>();
                    ListBookCmisReponse listBookCmisReponse = null;
//                    GetPCInfoRespone getPCInfoRespone = null;
                    List<ListDataZipResponse> listGetFile = new ArrayList<>();
                    //endregion

                    //region Xử lý đồng bộ sổ
                    try {
                        listBookCmisReponse = syncBookCmis();
                    } catch (Exception e) {
                        //show lỗi;
                        throw e;
                    }

                    //nếu null thì tắt download
                    if (listBookCmisReponse == null) {
                        mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_ERROR.getTitle(), TIME_DELAY_DOWNLOAD);
                        try {
                            Thread.sleep(TIME_DELAY_DOWNLOAD);
                        } catch (InterruptedException e) {
                            throw e;
                        }
                    }

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    //Insert EVN PC
                    List<ListEvnPCResponse> listEvnPc = new ArrayList<>();
                    String dataEvnPC = listBookCmisReponse.getBodyListEVNResponse().getListEvnPCLoginResponse();
                    JSONArray jaEvnPC = null;

                    jaEvnPC = new JSONArray(dataEvnPC);
                    for (int i = 0; i < jaEvnPC.length(); i++) {
                        ListEvnPCResponse evnPCResponse = gson.fromJson(jaEvnPC.getString(i), ListEvnPCResponse.class);
                        listEvnPc.add(evnPCResponse);

                        if (mainModel.checkEvnPCExist(evnPCResponse.getPcId(), edong) == 0) {
                            mainModel.insertEvnPC(evnPCResponse, edong);
                        }
                    }

                    String dataBookCmis = listBookCmisReponse.getBodyListEVNResponse().getListBookCmissResponse();
                    JSONArray jaBookCmis = new JSONArray(dataBookCmis);

                    List<ListBookCmisResponse> listBook = new ArrayList<>();
                    for (int i = 0; i < jaBookCmis.length(); i++) {
                        ListBookCmisResponse listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);
                        listBook.add(listBookCmisResponse);
                    }

                    int timeDelayElement = 0;
                    if (listBook.size() * Common.TIME_DELAY_ELEMENT_DOWNLOAD < TIME_DELAY_DOWNLOAD)
                        timeDelayElement = TIME_DELAY_DOWNLOAD / listBook.size();
                    else timeDelayElement = Common.TIME_DELAY_ELEMENT_DOWNLOAD;

                    for (int i = 1; i <= listBook.size(); i++) {
                        if (mainModel.checkBookCmisExist(listBook.get(i - 1).getBookCmis()) == 0) {
                            mainModel.insertBookCmis(listBook.get(i - 1));
                        }

                        listBookCmis.add(listBook.get(i - 1));

                        File fileDownload = new File(Common.PATH_FOLDER_DOWNLOAD + listBook.get(i - 1).getBookCmis() + "_" + edong + ".zip");
                        if (!fileDownload.exists()) {
                            listBookCmisNeedDownload.add(listBook.get(i - 1));
                        } else {
                            listBookCmisExist.add(listBook.get(i - 1));
                        }

                        int statusProcess = (i * 100) / listBook.size();
                        mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_START.getTitle(), statusProcess);
                        Thread.sleep(timeDelayElement);
                    }

                    //Kết thúc đồng bộ sổ
                    mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.BOOK_CMIS_END.getTitle(), 100);
                    Thread.sleep(TIME_DELAY_DOWNLOAD);
                    //endregion

                    //region Xử lý đồng bộ thông tin điện lực
                    // getPCInfoRespone = syncPcEVN(listEvnPc);
                    //endregion

                    //region Xử lý đồng bộ file
                    mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.GET_FILE_GEN_START.getTitle(), 0);

                    //đảm bảo thời gian hiển thị process ít nhất TIME_DELAY_DOWNLOAD ms
                    if (listBook.size() * Common.TIME_DELAY_ELEMENT_DOWNLOAD < TIME_DELAY_DOWNLOAD)
                        timeDelayElement = TIME_DELAY_DOWNLOAD / listBook.size();
                    else timeDelayElement = Common.TIME_DELAY_ELEMENT_DOWNLOAD;

                    for (int i = 1; i <= listBookCmisNeedDownload.size(); i++) {
                        ListDataZipResponse dataFileGen = null;
                        try {
                            dataFileGen = syncFileGen(listBookCmisNeedDownload.get(i - 1));
                        } catch (Exception e) {
                            Log.e(TAG_SYNC_GET_FILE, "không download được file " + listBookCmisNeedDownload.get(i - 1).getBookCmis());
                            break;
                        }
                        listGetFile.add(dataFileGen);

                        //Thao tác với file
                        String bookCmis = listBookCmisNeedDownload.get(i - 1).getBookCmis();
                        String sData = dataFileGen.getBodyListDataResponse().getFile_data();
                        if (sData.isEmpty() || sData.isEmpty()) {
                            Log.e(TAG_SYNC_GET_FILE, "sync get file bookCmis hoặc sData rỗng.");
                            break;
                        }
                        byte[] zipByte = Base64.decodeBase64(sData.getBytes());

                        try {
                            File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
                            if (!file.exists()) {
                                Common.writeBytesToFile(file, zipByte);
                                if (file.exists()) {
                                    File fileText = new File(Common.PATH_FOLDER_DOWNLOAD, "full.txt");
                                    if (fileText.exists())
                                        fileText.delete();
                                    if (Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + "_" + edong + ".zip")) {
                                        StringBuilder text = new StringBuilder();
                                        try {
                                            BufferedReader br = new BufferedReader(new FileReader(fileText));
                                            String line;

                                            while ((line = br.readLine()) != null) {
                                                text.append(line);//tạo file
                                                text.append('\n');
                                            }
                                            br.close();

                                            JSONObject ja = new JSONObject(text.toString());
                                            gsonBuilder = new GsonBuilder();
                                            gson = gsonBuilder.create();
                                            FileGenResponse fileGenResponse = gson.fromJson(ja.toString(), FileGenResponse.class);

                                            //set các tham số id_change
                                            mainModel.setChangedGenFile(edong, bookCmis, fileGenResponse.getId_changed(), fileGenResponse.getDate_changed());

                                            for (ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                                if (listCustomerResponse.getBodyCustomerResponse() == null) {
                                                    Log.e(TAG_SYNC_GET_FILE, "BodyCustomerResponse() null");
                                                    break;
                                                }
                                                String customerCode = listCustomerResponse.getBodyCustomerResponse().getCustomerCode();
                                                if (customerCode.isEmpty()) {
                                                    Log.e(TAG_SYNC_GET_FILE, "customerCode rỗng");
                                                    break;
                                                }
                                                if (mainModel.checkCustomerExist(customerCode, edong) == 0) {
                                                    if (mainModel.insertCustomer(listCustomerResponse) == -1) {
                                                        Log.e(TAG_SYNC_GET_FILE, "insertCustomer " + customerCode + " fail ");
                                                        break;
                                                    }
                                                }
                                            }

                                            for (ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
                                                if (listBillResponse.getBodyBillResponse() == null) {
                                                    Log.e(TAG_SYNC_GET_FILE, "getBodyBillResponse null");
                                                    break;
                                                }
                                                listBillResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                                                String billID = listBillResponse.getBodyBillResponse().getBillId();
                                                if (billID.isEmpty()) {
                                                    Log.e(TAG_SYNC_GET_FILE, "billID rỗng");
                                                    break;
                                                }
                                                if (mainModel.checkBillExist(billID) == 0) {
                                                    if (mainModel.insertBill(listBillResponse) == -1) {
                                                        Log.e(TAG_SYNC_GET_FILE, "insert bill " + billID + " fail");
                                                        break;
                                                    }
                                                }
                                            }

                                        } catch (IOException e) {
                                            throw e;
                                        } catch (JSONException e) {
                                            throw e;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIMainView.refreshInfoMain();
                                }
                            });
                        }

                        int statusProcess = (i * 100) / listBook.size();
                        mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.GET_FILE_GEN_START.getTitle(), statusProcess);
                        Thread.sleep(timeDelayElement);
                    }

                    //Kết thúc đồng bộ file
                    mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.GET_FILE_GEN_END.getTitle(), 100);
                    Thread.sleep(TIME_DELAY_DOWNLOAD);
                    //endregion

                    //region Xử lý đồng bộ dữ liệu thời điểm hiện tại
                    //Gọi api SYNC-DATA tất cả các sổ với idChange = 0 và dateChange = "";
                    mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.SYNC_DATA_START.getTitle(), 0);

                    //đảm bảo thời gian hiển thị tiến trình download phải lớn hơn TIME_DELAY_DOWNLOAD
                    timeDelayElement = 0;
                    if (listBook.size() * Common.TIME_DELAY_ELEMENT_DOWNLOAD < TIME_DELAY_DOWNLOAD)
                        timeDelayElement = TIME_DELAY_DOWNLOAD / listBookCmis.size();
                    else timeDelayElement = Common.TIME_DELAY_ELEMENT_DOWNLOAD;

                    for (int index = 1; index <= listBookCmis.size(); index++) {
                        ListDataResponse dataSync = null;
                        try {
                            dataSync = syncData(listBookCmis.get(index - 1));
                        } catch (Exception e) {
                            Log.e(TAG_SYNC_GET_FILE, "không download được file " + listBookCmis.get(index - 1).getBookCmis());
                            break;
                        }

                        try {
                            //Thao tác với dữ liệu tải về
                            //check code response
                            String responseCode = dataSync.getFooterListDataResponse().getResponse_code();
                            if (!responseCode.equals(Common.CODE_REPONSE_SYNC_DATA.e000.getCode())) {
                                throw new Exception("Common.CODE_REPONSE_SYNC_DATA.findCodeMessage(responseCode).getMessage()");
                            }

                            String sDataCustomer = dataSync.getBodyListDataResponse().getCustomer();
                            if (sDataCustomer.isEmpty()) {
                                throw new Exception("sDataCustomer empty!");
                            }

                            byte[] zipByteCustomer = Base64.decodeBase64(sDataCustomer.getBytes());
                            String sCustomer = Common.decompress(zipByteCustomer);
                            JSONArray jsonArray = new JSONArray(sCustomer);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ja = jsonArray.getJSONObject(i);
                                gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                CustomerResponse customerResponse = gson.fromJson(ja.toString(), CustomerResponse.class);
                                String customerCode = customerResponse.getBodyCustomerResponse().getCustomerCode();

                                if (customerCode.isEmpty()) {
                                    Log.e(TAG_SYNC_DATA, "customerCode isEmpty");
                                    break;
                                }

                                if (mainModel.checkCustomerExist(customerCode, edong) == 0) {
                                    if (mainModel.insertCustomer(customerResponse) == -1) {
                                        Log.e(TAG_SYNC_DATA, "insertCustomer fail: " + customerCode);
                                    }
                                } else {
                                    if (mainModel.updateCustomer(customerResponse) == -1) {
                                        Log.e(TAG_SYNC_DATA, "updateCustomer fail: " + customerCode);
                                    }
                                }
                            }


                            String sDataBill = dataSync.getBodyListDataResponse().getBill();
                            if (sDataBill.isEmpty()) {
                                throw new Exception("sDataBill is isEmpty!");
                            }

                            byte[] zipByteBill = Base64.decodeBase64(sDataBill.getBytes());
                            String sBill = Common.decompress(zipByteBill);
                            jsonArray = new JSONArray(sBill);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ja = jsonArray.getJSONObject(i);
                                gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                BillResponse billResponse = gson.fromJson(ja.toString(), BillResponse.class);
                                billResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);

                                String billID = billResponse.getBodyBillResponse().getBillId();

                                if (billID.isEmpty()) {
                                    Log.e(TAG_SYNC_DATA, "billID.isEmpty()");
                                    break;
                                }

                                if (mainModel.checkBillExist(billID) == 0) {
                                    if (mainModel.insertBill(billResponse) == -1) {
                                        Log.e(TAG_SYNC_DATA, "insert bill fail");
                                    }
                                } else {
                                    if (mainModel.updateBill(billResponse) == -1) {
                                        Log.e(TAG_SYNC_DATA, "update bill fail");
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw e;
                        } catch (JSONException e) {
                            throw e;
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIMainView.refreshInfoMain();
                                }
                            });
                        }

                        int statusProcess = (index * 100) / listBook.size();
                        mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.SYNC_DATA_START.getTitle(), statusProcess);
                        Thread.sleep(timeDelayElement);
                    }

                    //Kết thúc đồng bộ đồng bộ dữ liệu mới nhất
                    mIMainView.updatePbarDownload(Common.STATUS_DOWNLOAD.SYNC_DATA_END.getTitle(), 100);
                    Thread.sleep(TIME_DELAY_DOWNLOAD);
                    //endregion

                    //region Xử lý đẩy chấm nợ
                    //
                    //endregion

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG_SYNC, "Lỗi trong quá trình đồng bộ và đẩy chấm nợ offline" + e.getMessage());
                } finally {
                    //kết thúc download
                    try {
                        Thread.sleep(TIME_DELAY_DOWNLOAD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mIMainView.finishHidePbarDownload();
                }
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void payOnline(final PayAdapter.BillEntityAdapter bill) throws Exception {

        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(mIMainView.getContextView(), MainActivity.mEdong, Common.COMMAND_ID.BILLING.toString(), Common.getVersionApp(mIMainView.getContextView()));
        } catch (Exception e) {
            bill.setMessageError(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());

            return;
        }


        //Số ví edong thực hiện thanh toán
        String phone = MainActivity.mEdong;
        Long amount = bill.getTIEN_THANH_TOAN();

        //Phiên làm việc của TNV đang thực hiện
        String session = mainModel.getSession(MainActivity.mEdong);

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

            return;
        }

        final String maKH = bill.getMA_KHACH_HANG();
        final String soTien = amount.toString();
        final String kyPhatSinh = Common.parse(bill.getTHANG_THANH_TOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString());

        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.BILLING, true);

        final SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone> billingOnlineResponeAsyncSoap = new SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>(mHandler, BillingOnlineRespone.class,
                new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                    @Override
                    public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                    }

                    @Override
                    public void onUpdate(final String message) {
                        CurrentPostBillAsync.remove(this);
                    }

                    @Override
                    public void onPost(final SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                        if (response == null) {
                            CurrentPostBillAsync.remove(this);
                            try {
                                Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.BILLING, false);
                            } catch (Exception e) {
                                Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
                            }
                            return;
                        }

                        String maLoi = "";
                        String moTaLoi = "";
                        if (response.getFooter() != null) {
                            maLoi = response.getFooter().getResponseCode();
                            moTaLoi = response.getFooter().getDescription();
                        }

                        try {
                            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, maLoi, moTaLoi, Common.COMMAND_ID.BILLING, false);
                        } catch (Exception e) {
                            Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
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

                        } else if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e814.getCode())) {
                            /**
                             * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonDaThanhToanBoiViKhac(body);
                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
                            bill.setVI_TTOAN(body.getPaymentEdong());
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10007.getMessage());
                        } else if (!codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode())
                                &&
                                !codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e095.getCode())) {
                            /**
                             * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonChamNoLoi(body);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10009.getMessage());
                        } else {
                            String gateEVN = body.getBillingType();
                            if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode()) && gateEVN.equalsIgnoreCase("ON")) {//Thanh Toan THanh Cong Trong Gio Mo Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioMoKetNoi(body);
                            } else {//Thanh Toan Thanh Cong Trong Gio Dong Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioDongKetNoi(body);
                            }

                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
                            bill.setVI_TTOAN(MainActivity.mEdong);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage());
                        }


                        CurrentPostBillAsync.remove(soap);
                    }

                    @Override
                    public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                        CurrentPostBillAsync.remove(soap);
                    }
                });

        CurrentPostBillAsync.add(billingOnlineResponeAsyncSoap);
        billingOnlineResponeAsyncSoap.execute(jsonRequestBillOnline);
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

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(mainModel.getHoaDonNo(respone.getBillId()));

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), MainActivity.mEdong, Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.DANG_CHO_XU_LY.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), "");


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);
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

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = mainModel.getHoaDonThu(respone.getBillId());


        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), MainActivity.mEdong, Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.DA_CHAM.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), "");


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);
    }

    private void hoaDonChamNoLoi(BodyBillingOnlineRespone respone) {
        /**
         * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode(), "");

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = mainModel.getHoaDonThu(respone.getBillId());

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), "", Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.CHAM_LOI.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);

    }

    private void hoaDonDaThanhToanBoiViKhac(BodyBillingOnlineRespone response) {
        /**
         * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví thanh toán do server trả về
         * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác
         */

        mainModel.updateHoaDonNo(response.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode(), response.getPaymentEdong());


        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(response.getBillId()), response.getPaymentEdong(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC,
                "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()),
                Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mainModel.getHoaDonNo(response.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN(response.getPaymentEdong());
        entityLichSuThanhToan.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.OFFLINE.getCode());
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }

    private void hoaDonDaThanhToanBoiNguonKhac(BodyBillingOnlineRespone respone) {
        /** Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = null
         * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode(), "");

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), "", Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC,
                "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mainModel.getHoaDonNo(respone.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN("");
        entityLichSuThanhToan.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.OFFLINE.getCode());
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }


    /* private SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack callBackFileGen = new SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack() {
         @Override
         public void onPre(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

         }

         @Override
         public void onUpdate(String message) {

         }

         @RequiresApi(api = Build.VERSION_CODES.KITKAT)
         @Override
         public void onPost(ListDataZipResponse response) {
             if (response == null)
                 return;


             String sData = response.getBodyListDataResponse().getFile_data();
             if (sData != null && !sData.isEmpty()) {
                 byte[] zipByte = org.apache.commons.codec.binary.Base64.decodeBase64(sData.getBytes());

                 try {
                     File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
                     if (!file.exists()) {
                         Common.writeBytesToFile(file, zipByte);
                         if (file.exists()) {
                             File fileText = new File(Common.PATH_FOLDER_DOWNLOAD, "full.txt");
                             if (fileText.exists())
                                 fileText.delete();
                             if (Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + "_" + edong + ".zip")) {
                                 StringBuilder text = new StringBuilder();
                                 try {
                                     BufferedReader br = new BufferedReader(new FileReader(fileText));
                                     String line;

                                     while ((line = br.readLine()) != null) {
                                         text.append(line);//chỗ tạo file đây
                                         text.append('\n');
                                     }
                                     br.close();

                                     JSONObject ja = new JSONObject(text.toString());
                                     GsonBuilder gsonBuilder = new GsonBuilder();
                                     Gson gson = gsonBuilder.create();
                                     FileGenResponse fileGenResponse = gson.fromJson(ja.toString(), FileGenResponse.class);

                                     mainModel.setChangedGenFile(edong, bookCmis, fileGenResponse.getId_changed(), fileGenResponse.getDate_changed());

                                     for (ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                         if (mainModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                             if (mainModel.insertCustomer(listCustomerResponse) != -1) {
                                                 Log.i("INFO", "TEST");
                                             }
                                         }
                                     }

                                     for (ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
                                         listBillResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                                         if (mainModel.checkBillExist(listBillResponse.getBodyBillResponse().getBillId()) == 0) {
                                             if (mainModel.insertBill(listBillResponse) != -1) {
                                                 Log.i("INFO", "TEST");
                                             }
                                         }
                                     }

                                     //update mDataPayAdapter
                                     MainPresenter.this.refreshDataPayAdapter();

                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 } finally {
                     ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             mIMainView.refreshInfoMain();
                         }
                     });
                 }

                 ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mIMainView.refreshInfoMain();
                     }
                 });
             }
         }

         @Override
         public void onTimeOut(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

         }
     };
 */
    /*private SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack callBackData = new SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(ListDataResponse response) {
            if (response == null)
                return;

            try {
                String sDataCustomer = response.getBodyListDataResponse().getCustomer();
                if (sDataCustomer != null && !sDataCustomer.isEmpty()) {
                    byte[] zipByteCustomer = org.apache.commons.codec.binary.Base64.decodeBase64(sDataCustomer.getBytes());

                    try {
                        String sCustomer = Common.decompress(zipByteCustomer);

                        JSONArray jsonArray = new JSONArray(sCustomer);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ja = jsonArray.getJSONObject(i);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            CustomerResponse customerResponse = gson.fromJson(ja.toString(), CustomerResponse.class);
                            if (mainModel.checkCustomerExist(customerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                if (mainModel.insertCustomer(customerResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            } else {
                                if (mainModel.updateCustomer(customerResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String sDataBill = response.getBodyListDataResponse().getBill();
                if (sDataBill != null && !sDataBill.isEmpty()) {
                    byte[] zipByteBill = org.apache.commons.codec.binary.Base64.decodeBase64(sDataBill.getBytes());

                    try {
                        String sCustomer = Common.decompress(zipByteBill);

                        JSONArray jsonArray = new JSONArray(sCustomer);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ja = jsonArray.getJSONObject(i);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            BillResponse billResponse = gson.fromJson(ja.toString(), BillResponse.class);
                            billResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                            if (mainModel.checkBillExist(billResponse.getBodyBillResponse().getBillId()) == 0) {
                                if (mainModel.insertBill(billResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            } else {
                                if (mainModel.updateBill(billResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            }
                        }

                        //update data
                        MainPresenter.this.refreshDataPayAdapter();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainView.refreshInfoMain();
                    }
                });
            }

            ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIMainView.refreshInfoMain();
                }
            });
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }
    };
*/
//endregion


    public interface InteractorMainPresenter {
        List<PayAdapter.DataAdapter> getData();

        void refreshData();
    }
}
