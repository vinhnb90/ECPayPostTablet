package views.ecpay.com.postabletecpay.util.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;

import views.ecpay.com.postabletecpay.model.adapter.ChangePassRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.EvnRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.LoginRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;
import views.ecpay.com.postabletecpay.util.entities.request.EntityAccount.AccountRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityAccount.BodyAccountRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBalance.BalanceRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBalance.BodyBalanceRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.BillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.BodyBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.FooterBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.HeaderBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer.BodyCashTranferRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer.CashTranferRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer.FooterCashTranferRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer.HeaderCashTranferRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.BodyChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.ChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.FooterChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.HeaderChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.BodyCheckTrainOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.CheckTrainOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityData.BodyListDataRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityData.FooterListDataRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityData.HeaderListDataRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityData.ListDataRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip.BodyListDataZipRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip.FooterListDataZipRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip.HeaderListDataZipRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip.ListDataZipRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline.BodyDeleteBillOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline.DeleteBillOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.BodyEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.EVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.FooterEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.HeaderEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogout.BodyLogoutRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogout.FooterLogoutRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogout.HeaderLogoutRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogout.LogoutRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityMapCustomerCard.BodyMapCustomerCardRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityMapCustomerCard.MapCustomerCardRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.BodyPostBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.PostBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomer.BodySearchCustomerRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomer.SearchCustomerRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomerBill.BodySearchCustomerBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomerBill.SearchCustomerBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline.BodySearchOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline.SearchOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo.BodyGetPCInfoRequest;
import views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo.FooterGetPCInfoRequest;
import views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo.GetPCInfoRequest;
import views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo.HeaderGetPCInfoRequest;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCashTranfer.CashTranferRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.CheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline.DeleteBillOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogout.LogoutResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.PostBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomer.SearchCustomerRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo.GetPCInfoRespone;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.ENDPOINT_URL;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT_KSOAP;

/**
 * Created by VinhNB on 5/12/2017.
 */

public class SoapAPI {


    static boolean TEST_REQUEST = true;

    //region create JSON Request service
    public static String getJsonRequestLogin(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver, String signatureEncrypted, String pinLogin, String accountId, String versionApp) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (pinLogin == null || pinLogin.isEmpty() || pinLogin.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;
        if (versionApp == null || versionApp.isEmpty() || versionApp.trim().equals(""))
            return null;

        HeaderLoginRequest headerLoginRequest = new HeaderLoginRequest();
        headerLoginRequest.setAgent(agent);
        headerLoginRequest.setPassword(agentEncypted);
        headerLoginRequest.setCommandId(commandId);

        BodyLoginRequest bodyLoginRequest = new BodyLoginRequest();
        bodyLoginRequest.setAuditNumber(auditNumber);
        bodyLoginRequest.setMac(mac);
        bodyLoginRequest.setDiskDrive(diskDriver);
        bodyLoginRequest.setSignature(signatureEncrypted);
        bodyLoginRequest.setPinLogin(pinLogin);
        bodyLoginRequest.setVersionApp(versionApp);

        FooterLoginRequest footerLoginRequest = new FooterLoginRequest();
        footerLoginRequest.setAccountIdt(accountId);

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setHeaderLoginRequest(headerLoginRequest);
        loginRequest.setBodyLoginRequest(bodyLoginRequest);
        loginRequest.setFooterLoginRequest(footerLoginRequest);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoginRequest.class, new LoginRequestAdapter());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(loginRequest);

        //Test Deserialised
        final LoginRequest parsedRequest = gson.fromJson(jsonResult, LoginRequest.class);

        return jsonResult;
    }

    public static String getJsonRequestChangePass(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                                  String signatureEncrypted, String pinLogin, String session, String passNew, String passRetype,
                                                  String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (pinLogin == null || pinLogin.isEmpty() || pinLogin.trim().equals(""))
            return null;
        if (session == null || session.isEmpty() || session.trim().equals(""))
            return null;
        if (passNew == null || passNew.isEmpty() || passNew.trim().equals(""))
            return null;
        if (passRetype == null || passRetype.isEmpty() || passRetype.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;

        HeaderChangePassRequest headerChangePassRequest = new HeaderChangePassRequest();
        headerChangePassRequest.setAgent(agent);
        headerChangePassRequest.setPassword(agentEncypted);
        headerChangePassRequest.setCommandId(commandId);

        BodyChangePassRequest bodyChangePassRequest = new BodyChangePassRequest();
        bodyChangePassRequest.setAuditNumber(auditNumber);
        bodyChangePassRequest.setMac(mac);
        bodyChangePassRequest.setDiskDrive(diskDriver);
        bodyChangePassRequest.setSignature(signatureEncrypted);
        bodyChangePassRequest.setPinLogin(pinLogin);
        bodyChangePassRequest.setSession(session);
        bodyChangePassRequest.setNewPin(passNew);
        bodyChangePassRequest.setRetryPin(passRetype);

        FooterChangePassRequest footerChangePassRequest = new FooterChangePassRequest();
        footerChangePassRequest.setAccountIdt(accountId);

        final ChangePassRequest changePassRequest = new ChangePassRequest();
        changePassRequest.setHeaderChangePassRequest(headerChangePassRequest);
        changePassRequest.setBodyChangePassRequest(bodyChangePassRequest);
        changePassRequest.setFooterChangePassRequest(footerChangePassRequest);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChangePassRequest.class, new ChangePassRequestAdapter());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(changePassRequest);


//        String jsonResult = new GsonBuilder().setPrettyPrinting().create().toJson(changePassRequest);

        return jsonResult;

    }

    //region create JSON Request service
    public static String getJsonCashTranfer(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                            String signatureEncrypted, String session, String sendPhone, String receivedPhone, Long amount,
                                            String description, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;
        if (session == null || session.isEmpty() || session.trim().equals(""))
            return null;
        if (sendPhone == null || sendPhone.isEmpty() || sendPhone.trim().equals(""))
            return null;
        if (receivedPhone == null || receivedPhone.isEmpty() || receivedPhone.trim().equals(""))
            return null;

        HeaderCashTranferRequest header = new HeaderCashTranferRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyCashTranferRequest body = new BodyCashTranferRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setSession(session);
        ;
        body.setSendPhone(sendPhone);
        body.setOtp("");
        body.setReceivedPhone(receivedPhone);
        body.setAmount(amount);
        body.setDescription(description);

        FooterCashTranferRequest footer = new FooterCashTranferRequest();
        footer.setAccountIdt(accountId);

        final CashTranferRequest request = new CashTranferRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<CashTranferRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        if (TEST_REQUEST) {
            //Test Deserialised
            final CashTranferRequest parsedRequest = gson.fromJson(jsonResult, CashTranferRequest.class);
        }

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonGetPCInfo(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                          String signatureEncrypted, String pcName, String pcCode, String pcTax, String pcAddress,
                                          String pcPhoneNumber, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;


        HeaderGetPCInfoRequest header = new HeaderGetPCInfoRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyGetPCInfoRequest body = new BodyGetPCInfoRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setPcName(pcName);
        body.setPcCode(pcCode);
        body.setPcTax(pcTax);
        body.setPcAddress(pcAddress);
        body.setPcPhoneNumber(pcPhoneNumber);

        FooterGetPCInfoRequest footer = new FooterGetPCInfoRequest();
        footer.setAccountIdt(accountId);

        final Request request = new GetPCInfoRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<GetPCInfoRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        if (TEST_REQUEST) {
            //Test Deserialised
            final GetPCInfoRequest parsedRequest = gson.fromJson(jsonResult, GetPCInfoRequest.class);
            Log.d("LOG", "jsonResult = " + jsonResult);
        }

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonSearchCustomer(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                               String signatureEncrypted, String code, String name, String phone, String address,
                                               String gcs, String pcCode, int directEvn, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;


        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodySearchCustomerRequest body = new BodySearchCustomerRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setCustomerName(name);
        body.setPcCode(pcCode);
        body.setCustomerPhone(phone);
        body.setCustomerAddress(address);
        body.setBookCmis(gcs);
        body.setDirectEvn(directEvn);
        body.setCustomerCode(code);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final Request request = new SearchCustomerRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<SearchCustomerRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        if (TEST_REQUEST) {
            //Test Deserialised
            final SearchCustomerRequest parsedRequest = gson.fromJson(jsonResult, SearchCustomerRequest.class);
            Log.d("LOG", "jsonResult = " + jsonResult);
        }

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonSearchCustomerBill(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                                   String signatureEncrypted, String code, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;


        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodySearchCustomerBillRequest body = new BodySearchCustomerBillRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setCustomerCode(code);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final Request request = new SearchCustomerBillRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<SearchCustomerBillRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        if (TEST_REQUEST) {
            //Test Deserialised
            final SearchCustomerBillRequest parsedRequest = gson.fromJson(jsonResult, SearchCustomerBillRequest.class);
            Log.d("LOG", "jsonResult = " + jsonResult);
        }

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonMapCustomerCard(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver,
                                                String signatureEncrypted, String eCard, String cusCode, Long status, String phoneEcpay, String bankAcc,
                                                String idNumer, String bankName, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;


        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyMapCustomerCardRequest body = new BodyMapCustomerCardRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setCustomerCode(cusCode);
        body.setBankAccountNumber(bankAcc);
        body.setEcard(eCard);
        body.setStatus(status);
        body.setCustomerPhoneEcpay(phoneEcpay);
        body.setBankName(bankName);
        body.setIdNumber(idNumer);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final Request request = new MapCustomerCardRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<MapCustomerCardRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        if (TEST_REQUEST) {
            //Test Deserialised
            final MapCustomerCardRequest parsedRequest = gson.fromJson(jsonResult, MapCustomerCardRequest.class);
            Log.d("LOG", "jsonResult = " + jsonResult);
        }

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonSyncPC(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver, String signatureEncrypted, String edong, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (edong == null || edong.isEmpty() || edong.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;

        HeaderEVNRequest headerEVNRequest = new HeaderEVNRequest();
        headerEVNRequest.setAgent(agent);
        headerEVNRequest.setPassword(agentEncypted);
        headerEVNRequest.setCommandId(commandId);

        BodyEVNRequest bodyEVNRequest = new BodyEVNRequest();
        bodyEVNRequest.setAuditNumber(auditNumber);
        bodyEVNRequest.setMac(mac);
        bodyEVNRequest.setDiskDrive(diskDriver);
        bodyEVNRequest.setSignature(signatureEncrypted);
        bodyEVNRequest.setEdong(edong);

        FooterEVNRequest footerEVNRequest = new FooterEVNRequest();
        footerEVNRequest.setAccountIdt(accountId);

        final EVNRequest evnRequest = new EVNRequest();
        evnRequest.setHeaderEVNRequest(headerEVNRequest);
        evnRequest.setBodyEVNRequest(bodyEVNRequest);
        evnRequest.setFooterEVNRequest(footerEVNRequest);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EVNRequest.class, new EvnRequestAdapter());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(evnRequest);

        //Test Deserialised
        final EVNRequest parsedRequest = gson.fromJson(jsonResult, EVNRequest.class);

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonRequestSynData(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                               String diskDriver, String signatureEncrypted, String pcCode, String bookCmis,
                                               long fromIdChanged, String fromDateChanged, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
//        if (pcCode == null || pcCode.isEmpty() || pcCode.trim().equals(""))
//            return null;
        if (bookCmis == null || bookCmis.isEmpty() || bookCmis.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;

        HeaderListDataRequest headerListDataRequest = new HeaderListDataRequest();
        headerListDataRequest.setAgent(agent);
        headerListDataRequest.setPassword(agentEncypted);
        headerListDataRequest.setCommandID(commandId);

        BodyListDataRequest bodyListDataRequest = new BodyListDataRequest();
        bodyListDataRequest.setAuditNumber(auditNumber);
        bodyListDataRequest.setMac(mac);
        bodyListDataRequest.setDiskDrive(diskDriver);
        bodyListDataRequest.setSignature(signatureEncrypted);
        bodyListDataRequest.setPcCode(pcCode);
        bodyListDataRequest.setBookCmis(bookCmis);
        bodyListDataRequest.setFromIdChanged(fromIdChanged);
        bodyListDataRequest.setFromDateChanged(fromDateChanged);

        FooterListDataRequest footerListDataRequest = new FooterListDataRequest();
        footerListDataRequest.setAccountIdt(accountId);

        final ListDataRequest listDataRequest = new ListDataRequest();
        listDataRequest.setHeaderListDataRequest(headerListDataRequest);
        listDataRequest.setBodyListDataRequest(bodyListDataRequest);
        listDataRequest.setFooterListDataRequest(footerListDataRequest);

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(listDataRequest);

        //Test Deserialised
        final ListDataRequest parsedRequest = gson.fromJson(jsonResult, ListDataRequest.class);

        return jsonResult;
    }

    //region create JSON Request service
    public static String getJsonRequestSynDataZip(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                                  String diskDriver, String signatureEncrypted, String pcCode, String bookCmis,
                                                  String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
//        if (pcCode == null || pcCode.isEmpty() || pcCode.trim().equals(""))
//            return null;
//        if (bookCmis == null || bookCmis.isEmpty() || bookCmis.trim().equals(""))
//            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;

        HeaderListDataZipRequest headerListDataZipRequest = new HeaderListDataZipRequest();
        headerListDataZipRequest.setAgent(agent);
        headerListDataZipRequest.setPassword(agentEncypted);
        headerListDataZipRequest.setCommandID(commandId);

        BodyListDataZipRequest bodyListDataZipRequest = new BodyListDataZipRequest();
        bodyListDataZipRequest.setAuditNumber(auditNumber);
        bodyListDataZipRequest.setMac(mac);
        bodyListDataZipRequest.setDiskDrive(diskDriver);
        bodyListDataZipRequest.setSignature(signatureEncrypted);
        bodyListDataZipRequest.setPcCode(pcCode);
        bodyListDataZipRequest.setBookCmis(bookCmis);

        FooterListDataZipRequest footerListDataZipRequest = new FooterListDataZipRequest();
        footerListDataZipRequest.setAccountIdt(accountId);

        final ListDataZipRequest listDataZipRequest = new ListDataZipRequest();
        listDataZipRequest.setHeaderListDataZipRequest(headerListDataZipRequest);
        listDataZipRequest.setBodyListDataZipRequest(bodyListDataZipRequest);
        listDataZipRequest.setFooterListDataZipRequest(footerListDataZipRequest);

        final GsonBuilder gsonBuilder = new GsonBuilder();

        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(listDataZipRequest);

        //Test Deserialised
        final ListDataZipRequest parsedRequest = gson.fromJson(jsonResult, ListDataZipRequest.class);

        return jsonResult;
    }

    public static String getJsonRequestPostBill(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                                String diskDriver, String signatureEncrypted, ArrayList<TransactionOffItem> listTransactionOff,
                                                String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (agentEncypted == null || agentEncypted.isEmpty() || agentEncypted.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (signatureEncrypted == null || signatureEncrypted.isEmpty() || signatureEncrypted.trim().equals(""))
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;

        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyPostBillRequest bodyPostBillRequest = new BodyPostBillRequest();
        bodyPostBillRequest.setAuditNumber(auditNumber);
        bodyPostBillRequest.setMac(mac);
        bodyPostBillRequest.setDiskDrive(diskDriver);
        bodyPostBillRequest.setSignature(signatureEncrypted);
        bodyPostBillRequest.setList_transaction_off(listTransactionOff);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final PostBillRequest postBillRequest = new PostBillRequest();
        postBillRequest.setHeader(header);
        postBillRequest.setBody(bodyPostBillRequest);
        postBillRequest.setFooter(footer);

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        //Serialised
        final String jsonResult = gson.toJson(postBillRequest);

        return jsonResult;
    }

    public static String getJsonRequestSearchOnline(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                                    String diskDriver, String signatureEncrypted,
                                                    String customerCode,
                                                    String providerCode,
                                                    String accountId) {
        boolean fail =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(mac) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(accountId);

        if (fail)
            return null;

        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodySearchOnlineRequest bodySearchOnlineRequest = new BodySearchOnlineRequest();
        bodySearchOnlineRequest.setAuditNumber(auditNumber);
        bodySearchOnlineRequest.setMac(mac);
        bodySearchOnlineRequest.setDiskDrive(diskDriver);
        bodySearchOnlineRequest.setSignature(signatureEncrypted);
        bodySearchOnlineRequest.setCustomerCode(customerCode);
//        bodySearchOnlineRequest.setProviderCode(providerCode);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final SearchOnlineRequest searchOnlineRequest = new SearchOnlineRequest();
        searchOnlineRequest.setHeader(header);
        searchOnlineRequest.setBody(bodySearchOnlineRequest);
        searchOnlineRequest.setFooter(footer);

//        Type type = new TypeToken<SearchOnlineRequest>() {
//        }.getType();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        String jsonResult = new GsonBuilder().create().toJson(searchOnlineRequest, type);
        String jsonResult = new GsonBuilder().setPrettyPrinting().create().toJson(searchOnlineRequest);
        return jsonResult;
    }

    public static String getJsonRequestLogout(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                              String diskDriver, String signatureEncrypted, String session, String accountId) {
        boolean fail =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(mac) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(session) ||
                        TextUtils.isEmpty(accountId);

        if (fail)
            return null;

        HeaderLogoutRequest headerLogoutRequest = new HeaderLogoutRequest();
        headerLogoutRequest.setAgent(agent);
        headerLogoutRequest.setPassword(agentEncypted);
        headerLogoutRequest.setCommandId(commandId);

        BodyLogoutRequest bodyLogoutRequest = new BodyLogoutRequest();
        bodyLogoutRequest.setAuditNumber(auditNumber);
        bodyLogoutRequest.setMac(mac);
        bodyLogoutRequest.setDiskDrive(diskDriver);
        bodyLogoutRequest.setSignature(signatureEncrypted);
        bodyLogoutRequest.setSession(session);

        FooterLogoutRequest footerLogoutRequest = new FooterLogoutRequest();
        footerLogoutRequest.setAccountIdt(accountId);

        final LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setHeader(headerLogoutRequest);
        logoutRequest.setBody(bodyLogoutRequest);
        logoutRequest.setFooter(footerLogoutRequest);

//        Type type = new TypeToken<SearchOnlineRequest>() {
//        }.getType();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        String jsonResult = new GsonBuilder().create().toJson(searchOnlineRequest, type);
        String jsonResult = new GsonBuilder().setPrettyPrinting().create().toJson(logoutRequest);
        return jsonResult;
    }

    public static String getJsonRequestSynchronizePC() {
        return null;
    }


    public static String getJsonRequestBillOnline(String agent, String agentEncypted, String commandId, long auditNumber,
                                                  String macAdressHexValue, String diskDriver, String signatureEncrypted,
                                                  String code, String session, Long billId, Long amount, String phone,
                                                  String providerCode, String partnerCode, String accountId) {
        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(code) ||
                        TextUtils.isEmpty(session) ||
                        TextUtils.isEmpty(phone) ||
                        TextUtils.isEmpty(providerCode) ||
                        TextUtils.isEmpty(partnerCode) ||
                        TextUtils.isEmpty(accountId);

        if (hasNull)
            return null;

        HeaderBillingOnlineRequest headerBillingOnlineRequest = new HeaderBillingOnlineRequest();
        headerBillingOnlineRequest.setAgent(agent);
        headerBillingOnlineRequest.setPassword(agentEncypted);
        headerBillingOnlineRequest.setCommandId(commandId);

        BodyBillingOnlineRequest bodyBillingOnlineRequest = new BodyBillingOnlineRequest();
        bodyBillingOnlineRequest.setAuditNumber(auditNumber);
        bodyBillingOnlineRequest.setMac(macAdressHexValue);
        bodyBillingOnlineRequest.setDiskDrive(diskDriver);
        bodyBillingOnlineRequest.setSignature(signatureEncrypted);
        bodyBillingOnlineRequest.setCustomerCode(code);
        bodyBillingOnlineRequest.setSession(session);
        bodyBillingOnlineRequest.setBillId(billId);
        bodyBillingOnlineRequest.setAmount(amount);
        bodyBillingOnlineRequest.setPhone(phone);
        bodyBillingOnlineRequest.setProviderCode(providerCode);
        bodyBillingOnlineRequest.setPartnerCode(partnerCode);

        FooterBillingOnlineRequest footerBillingOnlineRequest = new FooterBillingOnlineRequest();
        footerBillingOnlineRequest.setAccountIdt(accountId);

        final BillingOnlineRequest billingOnlineRequest = new BillingOnlineRequest();
        billingOnlineRequest.setHeaderBillingOnlineRequest(headerBillingOnlineRequest);
        billingOnlineRequest.setBodyBillingOnlineRequest(bodyBillingOnlineRequest);
        billingOnlineRequest.setFooterBillingOnlineRequest(footerBillingOnlineRequest);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<BillingOnlineRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(billingOnlineRequest, type);

        return jsonResult;
    }

    public static String getJsonRequestAccount(String agent, String agentEncypted, String commandId, long auditNumber,
                                               String macAdressHexValue, String diskDriver, String signatureEncrypted,
                                               String phone, String accountId) {
        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(accountId) ||
                        TextUtils.isEmpty(phone);

        if (hasNull)
            return null;

        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyAccountRequest body = new BodyAccountRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(macAdressHexValue);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setPhone(phone);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final AccountRequest request = new AccountRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<AccountRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        return jsonResult;
    }

    public static String getJsonRequestBalance(String agent, String agentEncypted, String commandId, long auditNumber,
                                               String macAdressHexValue, String diskDriver, String signatureEncrypted,
                                               String phone, String session, String parnerCode, String accountId) {
        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(accountId) ||
                        TextUtils.isEmpty(phone);

        if (hasNull)
            return null;

        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyBalanceRequest body = new BodyBalanceRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(macAdressHexValue);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setPhone(phone);
        body.setSession(session);
        body.setPartnerCode(parnerCode);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final BalanceRequest request = new BalanceRequest();
        request.setHeader(header);
        request.setBody(body);
        request.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<BalanceRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(request, type);

        return jsonResult;
    }

    public static String getJsonRequestCheckTrainOnline(String agent, String agentEncypted, String commandId,
                                                        long auditNumber, String macAdressHexValue, String diskDriver,
                                                        String signatureEncrypted, String edong, Long amount, String customerCode,
                                                        Long billId,
                                                        @Nullable String requestDate,
                                                        String accountId) {

        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(accountId);

        if (hasNull)
            return null;

        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyCheckTrainOnlineRequest body = new BodyCheckTrainOnlineRequest();
        body.setAuditNumber(auditNumber);
        body.setMac(macAdressHexValue);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);

        long moneyBill = Double.valueOf(amount).longValue();
        body.setAmount(moneyBill);

        body.setCustomerCode(customerCode);
        body.setBillId(billId);
        body.setRequestDate(requestDate);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final CheckTrainOnlineRequest checkTrainOnlineRequest = new CheckTrainOnlineRequest();
        checkTrainOnlineRequest.setHeader(header);
        checkTrainOnlineRequest.setBody(body);
        checkTrainOnlineRequest.setFooter(footer);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<CheckTrainOnlineRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(checkTrainOnlineRequest, type);

        return jsonResult;
    }

    public static String getJsonRequestTransationCancellation(String agent, String agentEncypted, String commandId,
                                                              long auditNumber, String macAdressHexValue, String diskDriver,
                                                              String signatureEncrypted, Long amount, String code, Long billId,
                                                              @Nullable String requestDate,
                                                               String reasonDeleteBill, String accountId) {
        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(code) ||
                        TextUtils.isEmpty(reasonDeleteBill) ||
                        TextUtils.isEmpty(accountId);

        if (hasNull)
            return null;


        HeaderRequest header = new HeaderRequest();
        header.setAgent(agent);
        header.setPassword(agentEncypted);
        header.setCommandId(commandId);

        BodyDeleteBillOnlineRequest bodyDeleteBillOnlineRequest = new BodyDeleteBillOnlineRequest();
        bodyDeleteBillOnlineRequest.setAuditNumber(auditNumber);
        bodyDeleteBillOnlineRequest.setMac(macAdressHexValue);
        bodyDeleteBillOnlineRequest.setDiskDrive(diskDriver);
        bodyDeleteBillOnlineRequest.setSignature(signatureEncrypted);

        bodyDeleteBillOnlineRequest.setAmount(amount);
        bodyDeleteBillOnlineRequest.setCustomerCode(code);
        bodyDeleteBillOnlineRequest.setBillId(billId);
        bodyDeleteBillOnlineRequest.setBillingDate(requestDate);
        bodyDeleteBillOnlineRequest.setReason(reasonDeleteBill);

        FooterRequest footer = new FooterRequest();
        footer.setAccountIdt(accountId);

        final DeleteBillOnlineRequest deleteBillOnlineRequest = new DeleteBillOnlineRequest();
        deleteBillOnlineRequest.setHeader(header);
        deleteBillOnlineRequest.setBody(bodyDeleteBillOnlineRequest);
        deleteBillOnlineRequest.setFooter(footer);

        final GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<DeleteBillOnlineRequest>() {
        }.getType();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(deleteBillOnlineRequest, type);

        return jsonResult;
    }

    //endregion

    //region class api soap
    public static class AsyncSoapLogin extends AsyncTask<String, String, LoginResponseReponse> {
        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapLoginCallBack callBack;
        private boolean isEndCallSoap = false;
        private LoginResponseReponse loginResponseReponse;

        public AsyncSoapLogin(AsyncSoapLoginCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected LoginResponseReponse doInBackground(String... jsons) {
            String json = jsons[0];
            Log.d("here", "doInBackground: " + json);

            try {
                Common.writeLog(json, Common.COMMAND_ID.LOGIN.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL, TIME_OUT_CONNECT_KSOAP);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.LOGIN.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            loginResponseReponse = new Gson().fromJson(data, LoginResponseReponse.class);
            return loginResponseReponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(LoginResponseReponse loginResponseReponse) {
            super.onPostExecute(loginResponseReponse);
            isEndCallSoap = true;
            callBack.onPost(loginResponseReponse);
        }

        public static abstract class AsyncSoapLoginCallBack {
            public abstract void onPre(final AsyncSoapLogin soapLogin);

            public abstract void onUpdate(String message);

            public abstract void onPost(LoginResponseReponse response);

            public abstract void onTimeOut(final AsyncSoapLogin soapLogin);
        }

        public void callCountdown(final AsyncSoapLogin soapLogin) {
            if (soapLogin == null)
                return;

            callBack.onTimeOut(soapLogin);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public LoginResponseReponse getLoginResponseReponse() {
            return loginResponseReponse;
        }
    }

    public static class AsyncSoapChangePass extends AsyncTask<String, String, ChangePassResponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapChangePassCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapChangePass(AsyncSoapChangePassCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected ChangePassResponse doInBackground(String... jsons) {
            String json = jsons[0];
            try {
                Common.writeLog(json, Common.COMMAND_ID.CHANGE_PIN.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();
            try {
                Common.writeLog(data, Common.COMMAND_ID.CHANGE_PIN.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ChangePassResponse changePassResponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            changePassResponse = gson.fromJson(data, ChangePassResponse.class);

            return changePassResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(ChangePassResponse changePassResponse) {
            super.onPostExecute(changePassResponse);
            if (changePassResponse == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(changePassResponse);
        }

        public static abstract class AsyncSoapChangePassCallBack {
            public abstract void onPre(final AsyncSoapChangePass soapChangePass);

            public abstract void onUpdate(String message);

            public abstract void onPost(ChangePassResponse response);

            public abstract void onTimeOut(final AsyncSoapChangePass soapChangePass);
        }

        public void callCountdown(final AsyncSoapChangePass soapChangePass) {
            if (soapChangePass == null)
                return;

            callBack.onTimeOut(soapChangePass);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapCashTranfer extends AsyncTask<String, String, CashTranferRespone> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCashTranferCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapCashTranfer(AsyncSoapCashTranferCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected CashTranferRespone doInBackground(String... jsons) {
            String json = jsons[0];
            try {
                Common.writeLog(json, Common.COMMAND_ID.CASH_TRANSFER.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.CASH_TRANSFER.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            CashTranferRespone respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, CashTranferRespone.class);

            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(CashTranferRespone respone) {
            super.onPostExecute(respone);
            if (respone == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(respone);
        }

        public static abstract class AsyncSoapCashTranferCallBack {
            public abstract void onPre(final AsyncSoapCashTranfer soapChangePass);

            public abstract void onUpdate(String message);

            public abstract void onPost(CashTranferRespone response);

            public abstract void onTimeOut(final AsyncSoapCashTranfer soapChangePass);
        }

        public void callCountdown(final AsyncSoapCashTranfer soapChangePass) {
            if (soapChangePass == null)
                return;

            callBack.onTimeOut(soapChangePass);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapGetPCInfo extends AsyncTask<String, String, GetPCInfoRespone> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapGetPCInfoCallBack callBack;
        private boolean isEndCallSoap = false;
        private String mPhoneName;

        public AsyncSoapGetPCInfo(String _phoneName, AsyncSoapGetPCInfoCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.mPhoneName = _phoneName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected GetPCInfoRespone doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.GET_PC_INFO.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.GET_PC_INFO.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            GetPCInfoRespone respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, GetPCInfoRespone.class);


            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(GetPCInfoRespone respone) {
            super.onPostExecute(respone);
            if (respone == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(respone, mPhoneName);
        }

        public static abstract class AsyncSoapGetPCInfoCallBack {
            public abstract void onPre(final AsyncSoapGetPCInfo soap);

            public abstract void onUpdate(String message);

            public abstract void onPost(GetPCInfoRespone response, String phone);

            public abstract void onTimeOut(final AsyncSoapGetPCInfo soap);
        }

        public void callCountdown(final AsyncSoapGetPCInfo soap) {
            if (soap == null)
                return;

            callBack.onTimeOut(soap);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapSearchCustomer extends AsyncTask<String, String, SearchCustomerRespone> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapSearchCustomer(AsyncSoapCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected SearchCustomerRespone doInBackground(String... jsons) {
            String json = jsons[0];

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            SearchCustomerRespone respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, SearchCustomerRespone.class);


            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(SearchCustomerRespone respone) {
            super.onPostExecute(respone);
            if (respone == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(respone);
        }

        public static abstract class AsyncSoapCallBack {
            public abstract void onPre(final AsyncSoapSearchCustomer soap);

            public abstract void onUpdate(String message);

            public abstract void onPost(SearchCustomerRespone response);

            public abstract void onTimeOut(final AsyncSoapSearchCustomer soap);
        }

        public void callCountdown(final AsyncSoapSearchCustomer soap) {
            if (soap == null)
                return;

            callBack.onTimeOut(soap);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapSearchCustomerBill extends AsyncTask<String, String, SearchCustomerBillRespone> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapSearchCustomerBill(AsyncSoapCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected SearchCustomerBillRespone doInBackground(String... jsons) {
            String json = jsons[0];

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            SearchCustomerBillRespone respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, SearchCustomerBillRespone.class);


            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(SearchCustomerBillRespone respone) {
            super.onPostExecute(respone);
            if (respone == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(respone);
        }

        public static abstract class AsyncSoapCallBack {
            public abstract void onPre(final AsyncSoapSearchCustomerBill soap);

            public abstract void onUpdate(String message);

            public abstract void onPost(SearchCustomerBillRespone response);

            public abstract void onTimeOut(final AsyncSoapSearchCustomerBill soap);
        }

        public void callCountdown(final AsyncSoapSearchCustomerBill soap) {
            if (soap == null)
                return;

            callBack.onTimeOut(soap);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoap<T extends Respone> extends AsyncTask<String, String, T> {

        Class<T> classType;

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoap(Class<T> type, AsyncSoapCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.classType = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected T doInBackground(String... jsons) {
            String json = jsons[0];
            try {
                Common.writeLog(json, "Chưa rõ---Xem thông số header", true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, "Chưa rõ---Xem thông số header", false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            T respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, classType);


            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(T respone) {
            super.onPostExecute(respone);
            if (respone == null)
                return;
            isEndCallSoap = true;
            callBack.onPost(respone);
        }

        public static abstract class AsyncSoapCallBack<T extends Respone> {
            public abstract void onPre(final AsyncSoap soap);

            public abstract void onUpdate(String message);

            public abstract void onPost(T response);

            public abstract void onTimeOut(final AsyncSoap soap);
        }

        public void callCountdown(final AsyncSoap soap) {
            if (soap == null)
                return;

            callBack.onTimeOut(soap);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapIncludeTimout<T extends Respone> extends AsyncTask<String, String, T> {

        Class<T> classType;

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCallBack callBack;
        private boolean isEndCallSoap = false;
        private Handler mHandler;
        private Runnable handerTimeOut;


        private Object userData;

        public AsyncSoapIncludeTimout(Handler handler, Class<T> type, AsyncSoapCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.classType = type;
            mHandler = handler;
            handerTimeOut = new Runnable() {
                @Override
                public void run() {
                    cancel(true);
                    AsyncSoapIncludeTimout.this.callBack.onTimeOut(null);
                }
            };
        }

        public Object getUserData() {
            return userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);


            mHandler.postDelayed(handerTimeOut, Common.TIME_OUT_CONNECT);

        }

        @Override
        protected T doInBackground(String... jsons) {
            String json = jsons[0];

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "Không nhận được dữ liệu");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            T respone = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            respone = gson.fromJson(data, classType);


            return respone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            mHandler.removeCallbacks(handerTimeOut);
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(T respone) {
            super.onPostExecute(respone);

            mHandler.removeCallbacks(handerTimeOut);

            isEndCallSoap = true;
            if (respone == null)
                return;
            callBack.onPost(this, respone);
        }


        @Override
        protected void onCancelled(T t) {
            mHandler.removeCallbacks(handerTimeOut);
            super.onCancelled(t);
        }

        @Override
        protected void onCancelled() {
            mHandler.removeCallbacks(handerTimeOut);
            super.onCancelled();
        }

        public static abstract class AsyncSoapCallBack<T extends Respone> {
            public abstract void onPre(final AsyncSoapIncludeTimout soap);

            public abstract void onUpdate(String message);

            public abstract void onPost(AsyncSoapIncludeTimout soap, T response);

            public abstract void onTimeOut(final AsyncSoapIncludeTimout soap);
        }

        public void callCountdown(final AsyncSoapIncludeTimout soap) {
            if (soap == null)
                return;

            callBack.onTimeOut(soap);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }

    public static class AsyncSoapSearchOnline extends AsyncTask<String, String, SearchOnlineResponse> {

        //request action to eStore
        private Common.TYPE_SEARCH typeSearch;
        private String edong;
        private String infoSearch;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapSearchOnlineCallBack callBack;
        private boolean isEndCallSoap = false;
        private SearchOnlineResponse searchOnlineResponse;

        public AsyncSoapSearchOnline(Common.TYPE_SEARCH typeSearch, String edong, String infoSearch, AsyncSoapSearchOnlineCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.typeSearch = typeSearch;
            this.edong = edong;
            this.infoSearch = infoSearch;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchOnlineResponse = null;
            callBack.onPre(this);
        }

        @Override
        protected SearchOnlineResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.CUSTOMER_BILL.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            Log.d("here", "doInBackground: " + json);
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.CUSTOMER_BILL.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            searchOnlineResponse = new Gson().fromJson(data, SearchOnlineResponse.class);
           /* final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LoginResponseReponse.class, new LoginResponseAdapter());
//            gsonBuilder.registerTypeAdapter(ResponseLoginResponse.class, new )
            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            billingOnlineRespone = gson.fromJson(data, LoginResponseReponse.class);*/

            return searchOnlineResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(SearchOnlineResponse searchOnlineResponse) {
            super.onPostExecute(searchOnlineResponse);
            isEndCallSoap = true;
            callBack.onPost(searchOnlineResponse);
        }

        public static abstract class AsyncSoapSearchOnlineCallBack {
            public abstract void onPre(final AsyncSoapSearchOnline soapSearchOnline);

            public abstract void onUpdate(String message);

            public abstract void onPost(SearchOnlineResponse response);

            public abstract void onTimeOut(final AsyncSoapSearchOnline soapSearchOnline);
        }

        public void callCountdown(final AsyncSoapSearchOnline soapSearchOnline) {
            if (soapSearchOnline == null)
                return;

            callBack.onTimeOut(soapSearchOnline);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public Common.TYPE_SEARCH getTypeSearch() {
            return typeSearch;
        }

        public String getEdong() {
            return edong;
        }

        public String getInfoSearch() {
            return infoSearch;
        }

        public SearchOnlineResponse getSearchOnlineResponse() {
            return searchOnlineResponse;
        }
    }

    public static class AsyncSoapLogout extends AsyncTask<String, String, LogoutResponse> {

        //request action to eStore
        private Common.TYPE_SEARCH typeSearch;
        private String edong;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapLogoutCallBack callBack;
        private boolean isEndCallSoap = false;
        private LogoutResponse logoutResponse;

        public AsyncSoapLogout(String edong, AsyncSoapLogoutCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logoutResponse = null;
            callBack.onPre(this);
        }

        @Override
        protected LogoutResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.LOGOUT.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.LOGOUT.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            logoutResponse = new Gson().fromJson(data, LogoutResponse.class);
           /* final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LoginResponseReponse.class, new LoginResponseAdapter());
//            gsonBuilder.registerTypeAdapter(ResponseLoginResponse.class, new )
            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            billingOnlineRespone = gson.fromJson(data, LoginResponseReponse.class);*/

            return logoutResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(LogoutResponse logoutResponse) {
            super.onPostExecute(logoutResponse);
            isEndCallSoap = true;
            callBack.onPost(logoutResponse);
        }

        public static abstract class AsyncSoapLogoutCallBack {
            public abstract void onPre(final AsyncSoapLogout asyncSoapLogout);

            public abstract void onUpdate(String message);

            public abstract void onPost(LogoutResponse response);

            public abstract void onTimeOut(final AsyncSoapLogout asyncSoapLogout);
        }

        public void callCountdown(final AsyncSoapLogout soapSearchOnline) {
            if (soapSearchOnline == null)
                return;

            callBack.onTimeOut(soapSearchOnline);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public Common.TYPE_SEARCH getTypeSearch() {
            return typeSearch;
        }

        public String getEdong() {
            return edong;
        }

        public LogoutResponse getLogoutResponse() {
            return logoutResponse;
        }
    }

    public static class AsyncSoapBillOnline extends AsyncTask<String, String, BillingOnlineRespone> {

        //request action to eStore
        private String edong;
        PayAdapter.BillEntityAdapter entity;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapBillOnlineCallBack callBack;
        private boolean isEndCallSoap = false;
        private BillingOnlineRespone billingOnlineRespone;
        private Handler handlerDelay;
        int positionIndexListAsyncBillOnline;

        public AsyncSoapBillOnline(String edong, AsyncSoapBillOnlineCallBack callBack, Handler handler, int positionIndexListAsyncBillOnline, PayAdapter.BillEntityAdapter entity) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
            this.handlerDelay = handler;
            this.positionIndexListAsyncBillOnline = positionIndexListAsyncBillOnline;
            this.entity = entity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            billingOnlineRespone = null;
            callBack.onPre(this);
        }

        @Override
        protected BillingOnlineRespone doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.BILLING.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.BILLING.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            billingOnlineRespone = new Gson().fromJson(data, BillingOnlineRespone.class);

            return billingOnlineRespone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message, positionIndexListAsyncBillOnline);
        }

        @Override
        protected void onPostExecute(BillingOnlineRespone billingOnlineRespone) {
            super.onPostExecute(billingOnlineRespone);
            isEndCallSoap = true;
            callBack.onPost(billingOnlineRespone, positionIndexListAsyncBillOnline);
        }

        public static abstract class AsyncSoapBillOnlineCallBack {
            public abstract void onPre(final AsyncSoapBillOnline soapBillOnline);

            public abstract void onUpdate(String message, final int positionIndex);

            public abstract void onPost(BillingOnlineRespone response, final int positionIndex);

            public abstract void onTimeOut(final AsyncSoapBillOnline soapBillOnline);
        }

        public void callCountdown(final AsyncSoapBillOnline soapBillOnline) {
            if (soapBillOnline == null)
                return;

            callBack.onTimeOut(soapBillOnline);
        }

        public PayAdapter.BillEntityAdapter getEntity() {
            return entity;
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public String getEdong() {
            return edong;
        }

        public BillingOnlineRespone getBillingOnlineRespone() {
            return billingOnlineRespone;
        }

        public Handler getHandlerDelay() {
            return handlerDelay;
        }

        public int getPositionIndexListAsyncBillOnline() {
            return positionIndexListAsyncBillOnline;
        }
    }

    public static class AsyncSoapCheckTrainOnline extends AsyncTask<String, String, CheckTrainOnlineResponse> {

        //request action to eStore
        private String edong;
        private String reasonDeleteBill;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCheckTrainOnlineCallBack callBack;
        private boolean isEndCallSoap = false;
        private CheckTrainOnlineResponse checkTrainOnlineResponse;

        public AsyncSoapCheckTrainOnline(String edong, AsyncSoapCheckTrainOnlineCallBack callBack, String reasonDeleteBill) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
            this.reasonDeleteBill = reasonDeleteBill;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkTrainOnlineResponse = null;
            callBack.onPre(this);
        }

        @Override
        protected CheckTrainOnlineResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.CHECK_TRANS.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.CHECK_TRANS.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            checkTrainOnlineResponse = new Gson().fromJson(data, CheckTrainOnlineResponse.class);
            return checkTrainOnlineResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(CheckTrainOnlineResponse checkTrainOnlineResponse) {
            super.onPostExecute(checkTrainOnlineResponse);
            isEndCallSoap = true;
            callBack.onPost(checkTrainOnlineResponse);
        }

        public static abstract class AsyncSoapCheckTrainOnlineCallBack {
            public abstract void onPre(final AsyncSoapCheckTrainOnline searchOnlineResponse);

            public abstract void onUpdate(String message);

            public abstract void onPost(CheckTrainOnlineResponse response);

            public abstract void onTimeOut(final AsyncSoapCheckTrainOnline searchOnlineResponse);
        }

        public void callCountdown(final AsyncSoapCheckTrainOnline searchOnlineResponse) {
            if (searchOnlineResponse == null)
                return;

            callBack.onTimeOut(searchOnlineResponse);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public String getEdong() {
            return edong;
        }

        public String getReasonDeleteBill() {
            return reasonDeleteBill;
        }

        public CheckTrainOnlineResponse getCheckTrainOnlineResponse() {
            return checkTrainOnlineResponse;
        }
    }

    public static class AsyncSoapCheckTrainPayingBill extends AsyncTask<String, String, CheckTrainOnlineResponse> {

        //request action to eStore
        private String edong;
        private PayAdapter.BillEntityAdapter entity;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapCheckTrainPayingBillCallBack callBack;
        private boolean isEndCallSoap = false;
        private CheckTrainOnlineResponse checkTrainOnlineResponse;

        public AsyncSoapCheckTrainPayingBill(String edong, AsyncSoapCheckTrainPayingBillCallBack callBack, PayAdapter.BillEntityAdapter entity) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
            this.entity = entity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkTrainOnlineResponse = null;
            callBack.onPre(this);
        }

        @Override
        protected CheckTrainOnlineResponse doInBackground(String... jsons) {
            String json = jsons[0];
            Log.d("here", "doInBackground: " + json);

            try {
                Common.writeLog(json, Common.COMMAND_ID.CHECK_TRANS.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.CHECK_TRANS.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            checkTrainOnlineResponse = new Gson().fromJson(data, CheckTrainOnlineResponse.class);
            return checkTrainOnlineResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(CheckTrainOnlineResponse checkTrainOnlineResponse) {
            super.onPostExecute(checkTrainOnlineResponse);
            isEndCallSoap = true;
            callBack.onPost(checkTrainOnlineResponse);
        }

        public static abstract class AsyncSoapCheckTrainPayingBillCallBack {
            public abstract void onPre(final AsyncSoapCheckTrainPayingBill checkTrainPayingBill);

            public abstract void onUpdate(String message);

            public abstract void onPost(CheckTrainOnlineResponse response);

            public abstract void onTimeOut(final AsyncSoapCheckTrainPayingBill checkTrainPayingBill);
        }

        public void callCountdown(final AsyncSoapCheckTrainPayingBill checkTrainPayingBill) {
            if (checkTrainPayingBill == null)
                return;

            callBack.onTimeOut(checkTrainPayingBill);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public String getEdong() {
            return edong;
        }

        public PayAdapter.BillEntityAdapter getEntity() {
            return entity;
        }

        public CheckTrainOnlineResponse getCheckTrainOnlineResponse() {
            return checkTrainOnlineResponse;
        }
    }

    public static class AsyncSoapDeleteBillOnline extends AsyncTask<String, String, DeleteBillOnlineRespone> {

        //request action to eStore
        private String edong;
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapDeleteBillOnlineCallBack callBack;
        private boolean isEndCallSoap = false;
        private DeleteBillOnlineRespone deleteBillOnlineRespone;
        private String causeDeleteBill;
        private String code;
        private Long billId;

        public AsyncSoapDeleteBillOnline(String edong, String causeDeleteBill, String code, Long billId, AsyncSoapDeleteBillOnlineCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
            this.causeDeleteBill = causeDeleteBill;
            this.code = code;
            this.billId = billId;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deleteBillOnlineRespone = null;
            callBack.onPre(this);
        }

        @Override
        protected DeleteBillOnlineRespone doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.TRANSACTION_CANCELLATION.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.TRANSACTION_CANCELLATION.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            deleteBillOnlineRespone = new Gson().fromJson(data, DeleteBillOnlineRespone.class);
            return deleteBillOnlineRespone;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(DeleteBillOnlineRespone deleteBillOnlineRespone) {
            super.onPostExecute(deleteBillOnlineRespone);
            isEndCallSoap = true;
            callBack.onPost(deleteBillOnlineRespone);
        }

        public static abstract class AsyncSoapDeleteBillOnlineCallBack {
            public abstract void onPre(final AsyncSoapDeleteBillOnline soapDeleteBillOnline);

            public abstract void onUpdate(String message);

            public abstract void onPost(DeleteBillOnlineRespone response);

            public abstract void onTimeOut(final AsyncSoapDeleteBillOnline soapDeleteBillOnline);
        }

        public void callCountdown(final AsyncSoapDeleteBillOnline soapDeleteBillOnline) {
            if (soapDeleteBillOnline == null)
                return;

            callBack.onTimeOut(soapDeleteBillOnline);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }

        public String getEdong() {
            return edong;
        }

        public String getCauseDeleteBill() {
            return causeDeleteBill;
        }

        public String getCode() {
            return code;
        }

        public Long getBillId() {
            return billId;
        }

        public DeleteBillOnlineRespone getDeleteBillOnlineRespone() {
            return deleteBillOnlineRespone;
        }
    }
    //endregion

    //region đồng bộ
    public static class AsyncSoapSynchronizePC extends AsyncTask<String, String, ListEVNReponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapSynchronizePCCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapSynchronizePC(AsyncSoapSynchronizePCCallBack callBack) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected ListEVNReponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ListEVNReponse listEVNReponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();

            listEVNReponse = gson.fromJson(data, ListEVNReponse.class);

            return listEVNReponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(ListEVNReponse listEVNReponse) {
            super.onPostExecute(listEVNReponse);
            if (!isEndCallSoap)
                callBack.onPost(listEVNReponse);
        }

        public static abstract class AsyncSoapSynchronizePCCallBack {
            public abstract void onPre(final AsyncSoapSynchronizePC soapSynchronizePC);

            public abstract void onUpdate(String message);

            public abstract void onPost(ListEVNReponse response);

            public abstract void onTimeOut(final AsyncSoapSynchronizePC soapSynchronizePC);
        }

        public void callCountdown(final AsyncSoapSynchronizePC soapSynchronizePC) {
            if (soapSynchronizePC == null)
                return;

            callBack.onTimeOut(soapSynchronizePC);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }
    //endregion

    //region đồng bộ hoá đơn thay đổi
    public static class AsyncSoapSynchronizeData extends AsyncTask<String, String, ListDataResponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapSynchronizeDataCallBack callBack;
        private boolean isEndCallSoap = false;
        private Context context;

        public AsyncSoapSynchronizeData(AsyncSoapSynchronizeDataCallBack callBack, Context contextView) throws Exception {
            this.callBack = callBack;
            this.context = contextView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected ListDataResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.SYNC_DATA.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.SYNC_DATA.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ListDataResponse listDataResponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();

            listDataResponse = gson.fromJson(data, ListDataResponse.class);

            return listDataResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(ListDataResponse listDataResponse) {
            super.onPostExecute(listDataResponse);
            isEndCallSoap = true;
            callBack.onPost(listDataResponse);
        }

        public static abstract class AsyncSoapSynchronizeDataCallBack {
            public abstract void onPre(final AsyncSoapSynchronizeData soapSynchronizeData);

            public abstract void onUpdate(String message);

            public abstract void onPost(ListDataResponse response);

            public abstract void onTimeOut(final AsyncSoapSynchronizeData soapSynchronizeData);
        }

        public void callCountdown(final AsyncSoapSynchronizeData soapSynchronizeData) {
            if (soapSynchronizeData == null)
                return;

            callBack.onTimeOut(soapSynchronizeData);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }
    //endregion

    //region đồng bộ file
    public static class AsyncSoapSynchronizeDataZip extends AsyncTask<String, String, ListDataZipResponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapSynchronizeDataZipCallBack callBack;
        private boolean isEndCallSoap = false;
        private Context context;

        public AsyncSoapSynchronizeDataZip(final AsyncSoapSynchronizeDataZipCallBack callBack, Context context) throws Exception {
            this.callBack = callBack;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected ListDataZipResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.GET_FILE_GEN.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.GET_FILE_GEN.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ListDataZipResponse listDataZipResponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
//            gsonBuilder.registerTypeAdapter(ListEVNReponse.class, new LoginResponseAdapter());
//            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            listDataZipResponse = gson.fromJson(data, ListDataZipResponse.class);

            return listDataZipResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            isEndCallSoap = true;
            callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(ListDataZipResponse listDataZipResponse) {
            super.onPostExecute(listDataZipResponse);
            isEndCallSoap = true;
            callBack.onPost(listDataZipResponse);
        }

        public static abstract class AsyncSoapSynchronizeDataZipCallBack {
            public abstract void onPre(final AsyncSoapSynchronizeDataZip soapSynchronizeDataZip);

            public abstract void onUpdate(String message);

            public abstract void onPost(ListDataZipResponse response);

            public abstract void onTimeOut(final AsyncSoapSynchronizeDataZip soapSynchronizeDataZip);
        }

        public void callCountdown(final AsyncSoapSynchronizeDataZip soapSynchronizeDataZip) {
            if (soapSynchronizeDataZip == null)
                return;

            callBack.onTimeOut(soapSynchronizeDataZip);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }
    //endregion

    //region đồng bộ hoá đơn chấm offline lên server
    public static class AsyncSoapPostBill extends AsyncTask<String, String, PostBillResponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapPostBillCallBack callBack;
        private boolean isEndCallSoap = false;
        private Context context;
        private ProgressDialog progressDialog;

        public AsyncSoapPostBill(AsyncSoapPostBillCallBack callBack, Context context) throws Exception {
            this.callBack = callBack;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading file ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            callBack.onPre(this);
        }

        @Override
        protected PostBillResponse doInBackground(String... jsons) {
            String json = jsons[0];

            try {
                Common.writeLog(json, Common.COMMAND_ID.PUT_TRANSACTION_OFF.toString(), true);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(METHOD_PARAM, json);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht;
            SoapPrimitive response = null;

            try {
                ht = new HttpTransportSE(URL);
                ht.call(SOAP_ACTION, envelope);
                response = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
                return null;
            }

            String data = response.toString();

            try {
                Common.writeLog(data, Common.COMMAND_ID.PUT_TRANSACTION_OFF.toString(), false);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
            }

            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            PostBillResponse postBillResponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();

            postBillResponse = gson.fromJson(data, PostBillResponse.class);

            return postBillResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(PostBillResponse postBillResponse) {
            super.onPostExecute(postBillResponse);
            if (!isEndCallSoap)
                callBack.onPost(postBillResponse);
            progressDialog.dismiss();
        }

        public static abstract class AsyncSoapPostBillCallBack {
            public abstract void onPre(final AsyncSoapPostBill soapPostBill);

            public abstract void onUpdate(String message);

            public abstract void onPost(PostBillResponse response);

            public abstract void onTimeOut(final AsyncSoapPostBill soapPostBill);
        }

        public void callCountdown(final AsyncSoapPostBill soapPostBill) {
            if (soapPostBill == null)
                return;

            callBack.onTimeOut(soapPostBill);
        }

        public boolean isEndCallSoap() {
            return isEndCallSoap;
        }

        public void setEndCallSoap(boolean endCallSoap) {
            isEndCallSoap = endCallSoap;
        }
    }
    //endregion
}
