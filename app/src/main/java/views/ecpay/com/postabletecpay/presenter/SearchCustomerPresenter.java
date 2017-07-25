package views.ecpay.com.postabletecpay.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.ecpay.client.test.SecurityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import views.ecpay.com.postabletecpay.model.CustomerSearchModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomer.SearchCustomerRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.BodySearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BodySearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.BodyGetPCInfoRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.ISearchCustomerView;

import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

/**
 * Created by MyPC on 22/06/2017.
 */

public class SearchCustomerPresenter implements ISearchCustomerPresenter {

    private ISearchCustomerView searchCustomerView;
    private String mEDong;

    private CustomerSearchModel customerSearchModel;

    Handler handlerDelay = new Handler();

    SoapAPI.AsyncSoapIncludeTimout<SearchOnlineResponse> currentAsyncSearchOnline;
    public  SearchCustomerPresenter(ISearchCustomerView view, String eDong)
    {
        searchCustomerView = view;
        mEDong = eDong;

        customerSearchModel = new CustomerSearchModel(view.getContextView());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void search(boolean isSearchOnline, String maKH, String tenKH, String dcKH, String phoneKH, String gtKH) {
        if(isSearchOnline)
        {
            searchOnline(maKH);
        }else
        {
            this.searchOffline(maKH, tenKH, dcKH, phoneKH, gtKH);
        }
    }


    protected void searchOffline(String maxKH, String tenKH, String dcKH, String phoneKH, String gtKH)
    {
        List<EntityKhachHang> lst = customerSearchModel.getListCustomer(maxKH.trim(), tenKH.trim(), dcKH.trim(), phoneKH.trim(), gtKH.trim());
        searchCustomerView.refreshView(lst);

        if(lst.size() == 0)
        {
            searchCustomerView.showMessage("Khách hàng không tồn tại!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void searchOnline(String customerCode)
    {

        if(currentAsyncSearchOnline != null && currentAsyncSearchOnline.isEndCallSoap())
        {
            currentAsyncSearchOnline.cancel(true);
        }
        //setup info login
        ConfigInfo configInfo;
        try {
            configInfo = Common.setupInfoRequest(searchCustomerView.getContextView(), mEDong, Common.COMMAND_ID.CUSTOMER_BILL.toString(), Common.getVersionApp(searchCustomerView.getContextView()));
        } catch (Exception e) {
            searchCustomerView.showMessage(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
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
                customerCode,
                "",
                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
        {
            searchCustomerView.showMessage(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10002.getMessage());
            return;
        }

        try {
            final String maKH = customerCode;
            final String soTien = "";
            final String kyPhatSinh = "";

            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.CUSTOMER_BILL, true);

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
                        searchCustomerView.showMessage(message);
                    }catch (Exception e)
                    {

                    }
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {

                    if (response == null) {
                        try {
                            searchCustomerView.showMessage("Mã khách hàng không tồn tại!");
                        } catch (Exception e) {

                        }
                        try {
                            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.CUSTOMER_BILL, false);
                        } catch (Exception e) {
                            Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
                        }
                        return;
                    }

                    String maLoi = "";
                    String moTaLoi = "";
                    if (response.getFooter() != null) {
                        maLoi = response.getFooter().getResponseCode();
                        moTaLoi =  response.getFooter().getDescription();
                    }

                    try {
                        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, maLoi, moTaLoi, Common.COMMAND_ID.CUSTOMER_BILL, false);
                    } catch (Exception e) {
                        Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
                    }

                    searchCustomerView.showRespone(response.getFooter().getResponseCode(), response.getFooter().getDescription());

                    if (!response.getFooter().getResponseCode().equalsIgnoreCase("000")) {

                        Common.CODE_REPONSE_SEARCH_ONLINE codeResponse = Common.CODE_REPONSE_SEARCH_ONLINE.findCodeMessage(response.getFooter().getResponseCode());

                        searchCustomerView.showMessage(codeResponse.getMessage());
                        return;
                    }


                    BodySearchOnlineResponse body = (BodySearchOnlineResponse) response.getBody();

                    if (body.getCustomer() == null || body.getCustomer().length() == 0) {
                        searchCustomerView.showMessage(response.getFooter().getDescription());
                        return;
                    }


                    CustomerInsideBody customerResponse = null;
                    try {
                        customerResponse = new Gson().fromJson(body.getCustomer(), CustomerInsideBody.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }


                    if (customerResponse == null) {
                        searchCustomerView.showMessage(response.getFooter().getDescription());
                        return;
                    }
                    searchCustomerView.showMessage(response.getFooter().getDescription());

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


                    if (customerResponse.getCardNo() == null || customerResponse.getCardNo().length() == 0) {
                        if (customerResponse.getListBill() != null && customerResponse.getListBill().size() != 0) {
                            BillInsideCustomer billInsideCustomer = customerResponse.getListBill().get(0);
                            if (billInsideCustomer.getCardNo() == null || billInsideCustomer.getCardNo().length() == 0) {
                                khachHang.setMA_THE(billInsideCustomer.getCardNo());
                            }
                        }
                    }


                    if (khachHang != null) {
                        List<EntityKhachHang> lst = new ArrayList<>();
                        lst.add(khachHang);
                        searchCustomerView.refreshView(lst);
                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    try
                    {
                        searchCustomerView.showMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }catch (Exception e)
                    {

                    }
                }
            });
            currentAsyncSearchOnline.setUserData(jsonRequestSearchOnline);
            currentAsyncSearchOnline.execute(jsonRequestSearchOnline);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
