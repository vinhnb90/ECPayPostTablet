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

import org.ecpay.client.test.SecurityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.CustomerSearchModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomer.SearchCustomerRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.BodySearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.BodyGetPCInfoRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.TrangChu.ISearchCustomerView;

import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

/**
 * Created by MyPC on 22/06/2017.
 */

public class SearchCustomerPresenter implements ISearchCustomerPresenter {

    private ISearchCustomerView searchCustomerView;
    private String mEDong;

    private CustomerSearchModel customerSearchModel;


    private  SoapAPI.AsyncSoap.AsyncSoapCallBack<SearchCustomerBillRespone> callBack = new SoapAPI.AsyncSoap.AsyncSoapCallBack<SearchCustomerBillRespone>() {
        @Override
        public void onPre(SoapAPI.AsyncSoap soap) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @Override
        public void onPost(SearchCustomerBillRespone response) {
            if (response == null)
                return;
            BodySearchCustomerBillRespone body = (BodySearchCustomerBillRespone) response.getBody();

            if (body.getCustomer() == null || body.getCustomer().length() == 0) {
                searchCustomerView.showMessage(response.getFooter().getDescription());
                return;
            }

            searchCustomerView.showMessage(response.getFooter().getDescription());

            String responseData = body.getCustomer();
            // định dạng kiểu Object JSON
            Type type = new TypeToken<BodySearchCustomerBillRespone.CustomerObject>() {
            }.getType();
            BodySearchCustomerBillRespone.CustomerObject customerObject = null;

            Customer customer = null;
            try {
                customerObject = new Gson().fromJson(responseData, type);


                customer = new Customer(
                        customerObject.getCode() == null ? null : customerObject.getCode().toString(),
                        customerObject.getName() == null ? null : customerObject.getName().toString(),
                        customerObject.getAddress() == null ? null : customerObject.getAddress().toString(),
                        customerObject.getPcCode() == null ? null : customerObject.getPcCode().toString(),
                        customerObject.getPcCodeExt() == null ? null : customerObject.getPcCodeExt().toString(),
                        customerObject.getPhoneByevn() == null ? null : customerObject.getPhoneByevn().toString(),
                        customerObject.getPhoneByecp() == null ? null : customerObject.getPhoneByecp().toString(),
                        customerObject.getBookCmis() == null ? null : customerObject.getBookCmis().toString(),
                        customerObject.getElectricityMeter() == null ? null : customerObject.getElectricityMeter().toString(),
                        customerObject.getInning() == null ? null : customerObject.getInning().toString(),
                        customerObject.getStatus() == null ? null : customerObject.getStatus().toString(),
                        customerObject.getBankAccount() == null ? null : customerObject.getBankAccount().toString(),
                        "",
                        customerObject.getBankName() == null ? null : customerObject.getBankName().toString(),
                        "",
                        customerObject.getIdChanged() == null ? null : customerObject.getIdChanged().toString(),
                        customerObject.getDateChanged() == null ? null : customerObject.getDateChanged().toString(),
                        false);
                customer.setCardNo(customerObject.getCardNo() == null ? null : customerObject.getCardNo().toString());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if(customer != null)
            {
                List<Customer> lst = new ArrayList<Customer>();
                lst.add(customer);
                searchCustomerView.refreshView(lst);
            }

        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoap soap) {

        }
    };
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
        List<Customer> lst = customerSearchModel.getListCustomer(maxKH, tenKH, dcKH, phoneKH, gtKH);
        searchCustomerView.refreshView(lst);

        if(lst.size() == 0)
        {
            searchCustomerView.showMessage("Không Tồn Tại Khách Hàng Thoả Mãn Điều Kiện Tìm Kiếm");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void searchOnline(String maKH)
    {
        Context context = searchCustomerView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.SEARCH_CUSTOMER_BILL.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


        String json = SoapAPI.getJsonSearchCustomerBill(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                maKH,
                configInfo.getAccountId()
        );



        if (json == null)
            return;




        try {
            final SoapAPI.AsyncSoap<SearchCustomerBillRespone> soapChangePass = new SoapAPI.AsyncSoap<SearchCustomerBillRespone>(SearchCustomerBillRespone.class, callBack);

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SearchCustomerBillRespone changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            searchCustomerView.showMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                        } finally {
                            if (changePassResponse == null) {
                                soapChangePass.callCountdown(soapChangePass);
                            }
                        }
                    }
                });

                soapChangePassThread.start();
            }
        } catch (Exception e) {
            searchCustomerView.showMessage(e.getMessage());
            return;
        }

    }
}
