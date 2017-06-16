package views.ecpay.com.postabletecpay.util.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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

import views.ecpay.com.postabletecpay.model.adapter.ChangePassRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.EvnRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.LoginRequestAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.BillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.BodyBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.FooterBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline.HeaderBillingOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.BodyChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.ChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.FooterChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.HeaderChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.BodyCheckTrainOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.CheckTrainOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.FooterCheckTrainOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline.HeaderCheckTrainOnlineRequest;
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
import views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline.FooterDeleteBillOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline.HeaderDeleteBillOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.BodyEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.EVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.FooterEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.HeaderEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline.BodySearchOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline.SearchOnlineRequest;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.CheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline.DeleteBillOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;

import static views.ecpay.com.postabletecpay.util.commons.Common.ENDPOINT_URL;

/**
 * Created by VinhNB on 5/12/2017.
 */

public class SoapAPI {

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

    public static String getJsonRequestChangePass(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver, String signatureEncrypted, String pinLogin, String session, String passNew, String passRetype, String accountId) {
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

    public static String getJsonRequestSearchOnline(String agent, String agentEncypted, String commandId, long auditNumber, String mac,
                                                    String diskDriver, String signatureEncrypted, String directEvn,
                                                    String customerCode,
                                                    String pcCode,
                                                    String accountId) {
        boolean fail =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(mac) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(directEvn) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(accountId);

        if (fail)
            return null;

        HeaderLoginRequest headerLoginRequest = new HeaderLoginRequest();
        headerLoginRequest.setAgent(agent);
        headerLoginRequest.setPassword(agentEncypted);
        headerLoginRequest.setCommandId(commandId);

        BodySearchOnlineRequest bodySearchOnlineRequest = new BodySearchOnlineRequest();
        bodySearchOnlineRequest.setAuditNumber(auditNumber);
        bodySearchOnlineRequest.setMac(mac);
        bodySearchOnlineRequest.setDiskDrive(diskDriver);
        bodySearchOnlineRequest.setSignature(signatureEncrypted);
        bodySearchOnlineRequest.setDirectEvn(directEvn);
        bodySearchOnlineRequest.setCustomerCode(customerCode);
        bodySearchOnlineRequest.setPcCode(pcCode);

        FooterLoginRequest footerLoginRequest = new FooterLoginRequest();
        footerLoginRequest.setAccountIdt(accountId);

        final SearchOnlineRequest searchOnlineRequest = new SearchOnlineRequest();
        searchOnlineRequest.setHeader(headerLoginRequest);
        searchOnlineRequest.setBody(bodySearchOnlineRequest);
        searchOnlineRequest.setFooter(footerLoginRequest);

//        Type type = new TypeToken<SearchOnlineRequest>() {
//        }.getType();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        String jsonResult = new GsonBuilder().create().toJson(searchOnlineRequest, type);
        String jsonResult = new GsonBuilder().setPrettyPrinting().create().toJson(searchOnlineRequest);
        return jsonResult;
    }

    public static String getJsonRequestSynchronizePC() {
        return null;
    }


    public static String getJsonRequestBillOnline(String agent, String agentEncypted, String commandId, long auditNumber,
                                                  String macAdressHexValue, String diskDriver, String signatureEncrypted,
                                                  String code, String session, int billId, Long amount, String phone,
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

    public static String getJsonRequestCheckTrainOnline(String agent, String agentEncypted, String commandId,
                                                        long auditNumber, String macAdressHexValue, String diskDriver,
                                                        String signatureEncrypted, String edong, Long amount, String customerCode,
                                                        Long billId, String requestDate, String accountId) {

        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(requestDate) ||
                        TextUtils.isEmpty(accountId);

        if (hasNull)
            return null;

        HeaderCheckTrainOnlineRequest headerCheckTrainOnlineRequest = new HeaderCheckTrainOnlineRequest();
        headerCheckTrainOnlineRequest.setAgent(agent);
        headerCheckTrainOnlineRequest.setPassword(agentEncypted);
        headerCheckTrainOnlineRequest.setCommandId(commandId);

        BodyCheckTrainOnlineRequest bodyCheckTrainOnlineRequest = new BodyCheckTrainOnlineRequest();
        bodyCheckTrainOnlineRequest.setAuditNumber(auditNumber);
        bodyCheckTrainOnlineRequest.setMac(macAdressHexValue);
        bodyCheckTrainOnlineRequest.setDiskDrive(diskDriver);
        bodyCheckTrainOnlineRequest.setSignature(signatureEncrypted);
        bodyCheckTrainOnlineRequest.setEdong(edong);

        long moneyBill = Double.valueOf(amount).longValue();
        bodyCheckTrainOnlineRequest.setAmount(moneyBill);

        bodyCheckTrainOnlineRequest.setCustomerCode(customerCode);
        bodyCheckTrainOnlineRequest.setBillId(billId);
        bodyCheckTrainOnlineRequest.setRequestDate(requestDate);

        FooterCheckTrainOnlineRequest footerCheckTrainOnlineRequest = new FooterCheckTrainOnlineRequest();
        footerCheckTrainOnlineRequest.setAccountIdt(accountId);

        final CheckTrainOnlineRequest checkTrainOnlineRequest = new CheckTrainOnlineRequest();
        checkTrainOnlineRequest.setHeader(headerCheckTrainOnlineRequest);
        checkTrainOnlineRequest.setBody(bodyCheckTrainOnlineRequest);
        checkTrainOnlineRequest.setFooter(footerCheckTrainOnlineRequest);


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
                                                              String requestDate, Long traceNumber, String reasonDeleteBill, String accountId) {
        boolean hasNull =
                TextUtils.isEmpty(agent) ||
                        TextUtils.isEmpty(agentEncypted) ||
                        TextUtils.isEmpty(commandId) ||
                        TextUtils.isEmpty(macAdressHexValue) ||
                        TextUtils.isEmpty(diskDriver) ||
                        TextUtils.isEmpty(signatureEncrypted) ||
                        TextUtils.isEmpty(code) ||
                        TextUtils.isEmpty(requestDate) ||
                        TextUtils.isEmpty(reasonDeleteBill) ||
                        TextUtils.isEmpty(accountId);

        if (hasNull)
            return null;


        HeaderDeleteBillOnlineRequest headerDeleteBillOnlineRequest = new HeaderDeleteBillOnlineRequest();
        headerDeleteBillOnlineRequest.setAgent(agent);
        headerDeleteBillOnlineRequest.setPassword(agentEncypted);
        headerDeleteBillOnlineRequest.setCommandId(commandId);

        BodyDeleteBillOnlineRequest bodyDeleteBillOnlineRequest = new BodyDeleteBillOnlineRequest();
        bodyDeleteBillOnlineRequest.setAuditNumber(auditNumber);
        bodyDeleteBillOnlineRequest.setMac(macAdressHexValue);
        bodyDeleteBillOnlineRequest.setDiskDrive(diskDriver);
        bodyDeleteBillOnlineRequest.setSignature(signatureEncrypted);

        bodyDeleteBillOnlineRequest.setAmount(amount);
        bodyDeleteBillOnlineRequest.setCustomerCode(code);
        bodyDeleteBillOnlineRequest.setBillId(billId);
        bodyDeleteBillOnlineRequest.setBillingDate(requestDate);
        bodyDeleteBillOnlineRequest.setTraceNumber(traceNumber);
        bodyDeleteBillOnlineRequest.setReason(reasonDeleteBill);

        FooterDeleteBillOnlineRequest footerDeleteBillOnlineRequest = new FooterDeleteBillOnlineRequest();
        footerDeleteBillOnlineRequest.setAccountIdt(accountId);

        final DeleteBillOnlineRequest deleteBillOnlineRequest = new DeleteBillOnlineRequest();
        deleteBillOnlineRequest.setHeader(headerDeleteBillOnlineRequest);
        deleteBillOnlineRequest.setBody(bodyDeleteBillOnlineRequest);
        deleteBillOnlineRequest.setFooter(footerDeleteBillOnlineRequest);

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
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            LoginResponseReponse loginResponseReponse = new Gson().fromJson(data, LoginResponseReponse.class);
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

    public static class AsyncSoapBillOnline extends AsyncTask<String, String, BillingOnlineRespone> {

        //request action to eStore
        private String edong;
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

        public AsyncSoapBillOnline(String edong, AsyncSoapBillOnlineCallBack callBack, Handler handler, int positionIndexListAsyncBillOnline) throws Exception {
            this.callBack = callBack;
            this.edong = edong;
            this.handlerDelay = handler;
            this.positionIndexListAsyncBillOnline = positionIndexListAsyncBillOnline;
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
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ListEVNReponse listEVNReponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
//            gsonBuilder.registerTypeAdapter(ListEVNReponse.class, new EVNRe());
//            gsonBuilder.setPrettyPrinting();
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

    //region đồng bộ
    public static class AsyncSoapSynchronizeData extends AsyncTask<String, String, ListDataResponse> {

        //request action to eStore
        private static final String METHOD_NAME = "execute";
        private static final String NAMESPACE = "http://services.ecpay.org/";
        private static final String URL = ENDPOINT_URL;
        private static final String SOAP_ACTION = "request action to eStore";
        private static final String METHOD_PARAM = "message";
        private AsyncSoapSynchronizeDataCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapSynchronizeData(AsyncSoapSynchronizeDataCallBack callBack, Context contextView) throws Exception {
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected ListDataResponse doInBackground(String... jsons) {
            String json = jsons[0];
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
            if (data.isEmpty()) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return null;
            }

            ListDataResponse listDataResponse = null;
            final GsonBuilder gsonBuilder = new GsonBuilder();
//            gsonBuilder.registerTypeAdapter(ListEVNReponse.class, new LoginResponseAdapter());
//            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            listDataResponse = gson.fromJson(data, ListDataResponse.class);

            return listDataResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
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

    //region đồng bộ
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
        private ProgressDialog progressDialog;

        public AsyncSoapSynchronizeDataZip(AsyncSoapSynchronizeDataZipCallBack callBack, Context context) throws Exception {
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
        protected ListDataZipResponse doInBackground(String... jsons) {
            String json = jsons[0];
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
            progressDialog.dismiss();
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
}
