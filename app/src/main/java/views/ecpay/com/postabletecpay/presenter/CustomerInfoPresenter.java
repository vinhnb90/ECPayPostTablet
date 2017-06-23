package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import views.ecpay.com.postabletecpay.model.CustomerInfoModel;
import views.ecpay.com.postabletecpay.model.CustomerSearchModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityMapCustomerCard.MapCustomerCardRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.TrangChu.ICustomerInfoView;

/**
 * Created by MyPC on 23/06/2017.
 */

public class CustomerInfoPresenter implements ICustomerInfoPresenter {

    ICustomerInfoView customerInfoView;
    CustomerInfoModel customerInfoModel;
    public CustomerInfoPresenter(ICustomerInfoView view)
    {
        customerInfoView = view;
        customerInfoModel = new CustomerInfoModel(view.getContextView());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void register(final Customer customer, final String mEDong, final String eCard, final String phoneEcpay, final String bankAcc,  final String bankName) {
        Context context = customerInfoView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.MAP_CUSTOMER_CARD.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


        String json = SoapAPI.getJsonMapCustomerCard(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                eCard,
                customer.getCode(),
                1L, //Update
                phoneEcpay,
                bankAcc,
                customer.getIdNumber(),
                bankName,
                configInfo.getAccountId()
        );



        if (json == null)
            return;




        try {
            final SoapAPI.AsyncSoap<MapCustomerCardRespone> soapChangePass = new SoapAPI.AsyncSoap<MapCustomerCardRespone>(MapCustomerCardRespone.class, new SoapAPI.AsyncSoap.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoap soap) {

                }

                @Override
                public void onUpdate(String message) {

                }

                @Override
                public void onPost(Respone response) {
                    customerInfoView.setLoading(false);
                    if(response == null)
                    {
                        return;
                    }

                    customerInfoView.showMessageText(response.getFooter().getDescription());
                    if(response.getFooter().getResponseCode().equals("000"))
                    {
                        customer.setCardNo(eCard);
                        customer.setPhoneByecp(phoneEcpay);
                        customer.setBankName(bankName);
                        customer.setBankAccount(bankAcc);
                        UpdateDataBase(customer);
                    }else
                    {

                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoap soap) {

                }
            });

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MapCustomerCardRespone changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            //iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
            //iCashTranferView.showText(e.getMessage());
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void update(final Customer customer, final String mEDong, final String eCard, final String phoneEcpay, final String bankAcc,  final String bankName) {
        Context context = customerInfoView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.MAP_CUSTOMER_CARD.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


        String json = SoapAPI.getJsonMapCustomerCard(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                eCard,
                customer.getCode(),
                2L, //Update
                phoneEcpay,
                bankAcc,
                customer.getIdNumber(),
                bankName,
                configInfo.getAccountId()
        );



        if (json == null)
            return;




        try {
            final SoapAPI.AsyncSoap<MapCustomerCardRespone> soapChangePass = new SoapAPI.AsyncSoap<MapCustomerCardRespone>(MapCustomerCardRespone.class, new SoapAPI.AsyncSoap.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoap soap) {

                }

                @Override
                public void onUpdate(String message) {

                }

                @Override
                public void onPost(Respone response) {
                    customerInfoView.setLoading(false);
                    if(response == null)
                    {
                        return;
                    }

                    customerInfoView.showMessageText(response.getFooter().getDescription());
                    if(response.getFooter().getResponseCode().equals("000"))
                    {
                        customer.setCardNo(eCard);
                        customer.setPhoneByecp(phoneEcpay);
                        customer.setBankName(bankName);
                        customer.setBankAccount(bankAcc);
                        UpdateDataBase(customer);
                    }else
                    {

                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoap soap) {

                }
            });

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MapCustomerCardRespone changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            //iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
            //iCashTranferView.showText(e.getMessage());
            return;
        }
    }

    protected void UpdateDataBase(Customer customer)
    {
        customerInfoModel.UpdateCustomer(customer);
        customerInfoView.refill(customer);
        customerInfoView.setLoading(false);
    }
}
