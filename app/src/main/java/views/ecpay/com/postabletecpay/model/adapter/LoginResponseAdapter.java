package views.ecpay.com.postabletecpay.model.adapter;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.AccountLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.BodyLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.FooterLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.HeaderLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ResponseLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;

import static android.content.ContentValues.TAG;

/**
 * Created by VinhNB on 5/19/2017.
 */

public class LoginResponseAdapter extends TypeAdapter<LoginResponseReponse> {
    @Override
    public void write(JsonWriter out, LoginResponseReponse value) throws IOException {
        HeaderLoginResponse headerLoginResponse = value.getHeaderLoginResponse();
        BodyLoginResponse bodyLoginResponse = value.getBodyLoginResponse();
        FooterLoginResponse footerLoginResponse = value.getFooterLoginResponse();

        out.beginObject();
        //write headerLoginResponse
        if (headerLoginResponse != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerLoginResponse.getAgent());
            out.name("password").value(headerLoginResponse.getPassword());
            out.name("command-id").value(headerLoginResponse.getCommandId());
            out.endObject();
        }

        //write bodyLoginResponse
        if (bodyLoginResponse != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyLoginResponse.getAuditNumber());
            out.name("mac").value(bodyLoginResponse.getMac());
            out.name("disk-drive").value(bodyLoginResponse.getDiskDrive());
            out.name("signature").value(bodyLoginResponse.getSignature());
            out.name("pin-login").value(bodyLoginResponse.getPinLogin());
            out.name("version-app").value(bodyLoginResponse.getVersionApp());
            out.name("response-login").value(bodyLoginResponse.getResponseLoginResponse());

            /*ResponseLoginResponse responseLoginResponse = value.getBodyLoginResponse().getResponseLoginResponse();
            out.name("response-login").beginObject();

            AccountLoginResponse accountLoginResponse = responseLoginResponse.getAccountLoginResponse();
            out.name("account").beginObject();
            out.name("status").name(accountLoginResponse.getStatus());
            out.name("idNumber").name(accountLoginResponse.getIdNumber());
            out.name("idNumberDate").name(accountLoginResponse.getIdNumberDate());
            out.name("idNumberPlace").name(accountLoginResponse.getIdNumberPlace());
            out.name("name").name(accountLoginResponse.getName());
            out.name("address").name(accountLoginResponse.getAddress());
            out.name("email").name(accountLoginResponse.getEmail());
            out.name("birthday").name(accountLoginResponse.getBirthday());
            out.name("idAccount").name(accountLoginResponse.getIdAccount() + "");
            out.name("edong").name(accountLoginResponse.getEdong());
            out.name("parentId").name(accountLoginResponse.getParentId() + "");
            out.name("parentEdong").name(accountLoginResponse.getParentEdong() + "");
            out.name("pin").name(accountLoginResponse.getPin() + "");
            out.name("type").name(accountLoginResponse.getType() + "");
            out.name("balance").name(accountLoginResponse.getBalance() + "");
            out.name("type").name(accountLoginResponse.getType() + "");
            out.name("lockMoney").name(accountLoginResponse.getLockMoney() + "");
            out.name("changedPIN").name(accountLoginResponse.getChangedPIN() + "");
            out.name("session").name(accountLoginResponse.getSession() + "");
            out.name("verified").name(accountLoginResponse.getVerified() + "");
            out.name("mac").name(accountLoginResponse.getMac());
            out.name("ip").name(accountLoginResponse.getIp());
            out.name("loginTime").name(accountLoginResponse.getLoginTime() + "");
            out.name("logoutTime").name(accountLoginResponse.getLogoutTime() + "");
            out.name("strLoginTime").name(accountLoginResponse.getStrLoginTime() + "");
            out.name("strLogoutTime").name(accountLoginResponse.getStrLogoutTime() + "");
            out.name("strType").name(accountLoginResponse.getStrType() + "");
            out.endObject(); // end account

            out.name("listEvnPC").beginArray();
            for (final ListEvnPCLoginResponse listEvnPCLoginResponse :
                    responseLoginResponse.getListEvnPCLoginResponse()) {
                writeListEvnPC(out, listEvnPCLoginResponse);
            }
            out.endArray();//end array evn

            out.name("reponseCode").name(responseLoginResponse.getReponseCode());
            out.name("description").name(responseLoginResponse.getDescription());
            out.name("response").name(responseLoginResponse.getResponse());

            out.endObject(); // end responseLogin*/
            out.endObject(); // end  body
        }

        //write footerLoginResponse
        if (footerLoginResponse != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerLoginResponse.getAccountIdt());
            out.name("response-code").value(footerLoginResponse.getResponseCode());
            out.name("description").value(footerLoginResponse.getDescription());
            out.endObject();
        }

        out.endObject();
    }


    private void writeListEvnPC(final JsonWriter out, final ListEvnPCLoginResponse listEvnPCLoginResponse) throws IOException {
        out.beginObject();
        out.name("parentId").value(listEvnPCLoginResponse.getParentId());
        out.name("pcId").value(listEvnPCLoginResponse.getPcId());
        out.name("code").value(listEvnPCLoginResponse.getCode());
        out.name("ext").value(listEvnPCLoginResponse.getExt());
        out.name("fullName").value(listEvnPCLoginResponse.getFullName());
        out.name("shortName").value(listEvnPCLoginResponse.getShortName());
        out.name("address").value(String.valueOf(listEvnPCLoginResponse.getAddress()));
        out.name("taxCode").value(String.valueOf(listEvnPCLoginResponse.getTaxCode()));
        out.name("phone1").value(String.valueOf(listEvnPCLoginResponse.getPhone1()));
        out.name("phone2").value(String.valueOf(listEvnPCLoginResponse.getPhone2()));
        out.name("fax").value(String.valueOf(listEvnPCLoginResponse.getFax()));
        out.name("level").value(listEvnPCLoginResponse.getLevel());
        out.name("mailTo").value(String.valueOf(listEvnPCLoginResponse.getMailTo()));
        out.name("mailCc").value(String.valueOf(listEvnPCLoginResponse.getMailCc()));
        out.name("status").value(listEvnPCLoginResponse.getStatus());
        out.name("dateCreated").value(listEvnPCLoginResponse.getDateCreated());
        out.name("idChanged").value(listEvnPCLoginResponse.getIdChanged());
        out.name("dateChanged").value(listEvnPCLoginResponse.getDateChanged());
        out.name("regionId").value(listEvnPCLoginResponse.getRegionId());
        out.endObject();
    }

    @Override
    public LoginResponseReponse read(JsonReader in) throws IOException {
        final LoginResponseReponse loginRequest = new LoginResponseReponse();

        in.beginObject();
        while (in.hasNext()) {
            String nextName = in.nextName();
            switch (nextName) {
                case "header":
                    final HeaderLoginResponse headerLoginResponse = new HeaderLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerLoginResponse.setAgent(in.nextString());
                                break;
                            case "password":
                                headerLoginResponse.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerLoginResponse.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setHeaderLoginResponse(headerLoginResponse);
                    break;

                case "body":
                    final BodyLoginResponse bodyLoginResponse = new BodyLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                bodyLoginResponse.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                bodyLoginResponse.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                bodyLoginResponse.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                bodyLoginResponse.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                bodyLoginResponse.setPinLogin(in.nextString());
                                break;
                            case "version-app":
                                bodyLoginResponse.setVersionApp(in.nextString());
                                break;
                            case "response-login":
                                bodyLoginResponse.setResponseLoginResponse(in.nextString());
//                                ResponseLoginResponse result = setBodyLoginResponse(bodyLoginResponse, in);
                                break;

                            default:
                                in.skipValue();
                        }
                    }
                    in.endObject();

                    loginRequest.setBodyLoginResponse(bodyLoginResponse);
                    break;

                case "footer":
                    final FooterLoginResponse footerLoginResponse = new FooterLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        nextName = in.nextName();
                        switch (nextName) {
                            case "account-idt":
                                footerLoginResponse.setAccountIdt(in.nextString());
                                break;
                            case "response-code":
                                footerLoginResponse.setResponseCode(in.nextString());
                                break;
                            case "description":
                                footerLoginResponse.setDescription(in.nextString());
                                break;
                            case "source-address":
                                footerLoginResponse.setSourceAddress(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setFooterLoginResponse(footerLoginResponse);
                    break;
            }
        }

        in.endObject();

        return loginRequest;
    }

    private ResponseLoginResponse setBodyLoginResponse(final BodyLoginResponse bodyLoginResponse, JsonReader in) throws IOException {
        AccountLoginResponse accountLoginResponse = null;
        List<ListEvnPCLoginResponse> listEvnPCLoginResponse = null;
        String reponseCode = null;
        String description = null;
        String response = null;
        in.beginObject();
        while (in.hasNext()) {
            String nextName = in.nextName();
            switch (in.nextName()) {
                case "account":
                    accountLoginResponse = setAccount(bodyLoginResponse, in);
                    break;
                case "listEvnPC":
                    listEvnPCLoginResponse = setListEvnPc(in);
                    break;
                case "reponseCode":
                    reponseCode = in.nextString();
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "response":
                    response = in.nextString();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        ResponseLoginResponse result = new ResponseLoginResponse( accountLoginResponse, listEvnPCLoginResponse,  reponseCode,  description,  response);
        return result;
    }

    private List<ListEvnPCLoginResponse> setListEvnPc(final JsonReader in) throws IOException {
        final List<ListEvnPCLoginResponse> listEvnPCLoginResponses = new ArrayList<>();
//                bodyLoginResponse.getResponseLoginResponse().getListEvnPCLoginResponse();
        in.beginArray();
        while (in.hasNext()) {
            listEvnPCLoginResponses.add(readEvnPCLoginResponse(in));
        }

        /*while (in.hasNext()) {
            final int parentId = in.nextInt();
            final int pcId = in.nextInt();
            final String code = in.nextString();
            final String ext = in.nextString();
            final String fullName = in.nextString();
            final String shortName = in.nextString();
            final String address = in.nextString();
            final String taxCode = in.nextString();
            final String phone1 = in.nextString();
            final String phone2 = in.nextString();
            final String fax = in.nextString();
            final int level = in.nextInt();
            final String mailTo = in.nextString();
            final String mailCc = in.nextString();
            final int status = in.nextInt();
            final String dateCreated = in.nextString();
            final int idChanged = in.nextInt();
            final String dateChanged = in.nextString();
            final int regionId = in.nextInt();

            listEvnPCLoginResponses.add(new ListEvnPCLoginResponse(parentId, pcId, code, ext, fullName, shortName, address, taxCode, phone1, phone2, fax, level, mailTo, mailCc, status, dateCreated, idChanged, dateChanged, regionId));
        }*/

        in.endArray();

        return listEvnPCLoginResponses;
    }

    private ListEvnPCLoginResponse readEvnPCLoginResponse(JsonReader in) throws IOException {

        Integer parentId = 0;
        Integer pcId = 0;
        String code = null;
        String ext = null;
        String fullName = null;
        String shortName = null;
        Object address = null;
        Object taxCode = null;
        Object phone1 = null;
        Object phone2 = null;
        Object fax = null;
        Integer level = 0;
        Object mailTo = null;
        Object mailCc = null;
        Integer status = 0;
        String dateCreated = null;
        Integer idChanged = 0;
        String dateChanged = null;
        Integer regionId = 0;

        in.beginObject();
        while (in.hasNext()) {
            String nameNext = in.nextName();
            switch (nameNext) {
                case "parentId":
                    parentId = in.nextInt();
                    break;
                case "pcId":
                    pcId = in.nextInt();
                    break;
                case "code":
                    code = in.nextString();
                    break;
                case "ext":
                    ext = in.nextString();
                    break;
                case "fullName":
                    fullName = in.nextString();
                    break;
                case "shortName":
                    shortName = in.nextString();
                    break;
                case "address":
                    address = in.nextInt();
                    break;
                case "taxCode":
                    taxCode = in.nextInt();
                    break;
                case "phone1":
                    phone1 = in.nextInt();
                    break;
                case "phone2":
                    phone2 = in.nextInt();
                    break;
                case "fax":
                    fax = in.nextInt();
                    break;
                case "level":
                    level = in.nextInt();
                    break;
                case "mailTo":
                    mailTo = in.nextInt();
                    break;
                case "mailCc":
                    mailCc = in.nextInt();
                    break;
                case "status":
                    status = in.nextInt();
                    break;
                case "dateCreated":
                    dateCreated = in.nextString();
                    break;
                case "idChanged":
                    idChanged = in.nextInt();
                    break;
                case "dateChanged":
                    dateChanged = in.nextString();
                    break;
                case "regionId":
                    regionId = in.nextInt();
                    break;
                default:
                    in.skipValue();

            }
        }
        in.endObject();

        ListEvnPCLoginResponse result = new ListEvnPCLoginResponse(parentId, pcId, code, ext, fullName, shortName, address, taxCode, phone1, phone2, fax, level, mailTo, mailCc, status, dateCreated, idChanged, dateChanged, regionId);
        return result;
    }


    private AccountLoginResponse setAccount(final BodyLoginResponse bodyLoginResponse, JsonReader in) throws IOException {

        String status = null;
        String idNumber = null;
        String idNumberDate = null;
        String idNumberPlace = null;
        String name = null;
        String address = null;
        String email = null;
        String birthday = null;
        Integer idAccount = 0;
        String edong = null;
        Integer parentId = 0;
        String parentEdong = null;
        Object pin = null;
        Integer type = 0;
        Long balance = 0l;
        Integer lockMoney = 0;
        Boolean changedPIN = false;
        String session = null;
        Integer verified = 0;
        String mac = null;
        String ip = null;
        Long loginTime = 0l;
        Object logoutTime = null;
        Object strLoginTime = null;
        Object strLogoutTime = null;
        Object strType = null;

        in.beginObject();
        while (in.hasNext()) {
            String nameNext = in.nextName();
            switch (nameNext) {
                case "status":
                    status = in.nextString();
                    break;
                case "idNumber":
                    idNumber = in.nextString();
                    break;
                case "idNumberDate":
                    idNumberDate = in.nextString();
                    break;
                case "idNumberPlace":
                    idNumberPlace = in.nextString();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "address":
                    address = in.nextString();
                    break;
                case "email":
                    email = in.nextString();
                    break;
                case "birthday":
                    birthday = in.nextString();
                    break;
                case "idAccount":
                    idAccount = in.nextInt();
                    break;
                case "edong":
                    status = in.nextString();
                    break;
                case "parentId":
                    parentId = in.nextInt();
                    break;
                case "parentEdong":
                    parentEdong = in.nextString();
                    break;
                case "pin":
                    pin = in.nextString();
                    break;
                case "type":
                    type = in.nextInt();
                    break;
                case "balance":
                    balance = in.nextLong();
                    break;
                case "lockMoney":
                    lockMoney = in.nextInt();
                    break;
                case "changedPIN":
                    changedPIN = in.nextBoolean();
                    break;
                case "session":
                    session = in.nextString();
                    break;
                case "verified":
                    verified = in.nextInt();
                    break;
                case "mac":
                    mac = in.nextString();
                    break;
                case "ip":
                    ip = in.nextString();
                    break;
                case "loginTime":
                    loginTime = in.nextLong();
                    break;
                case "logoutTime":
                    logoutTime = in.nextString();
                    break;
                case "strLoginTime":
                    strLoginTime = in.nextString();
                    break;
                case "strLogoutTime":
                    strLogoutTime = in.nextString();
                    break;
                case "strType":
                    strType = in.nextString();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        AccountLoginResponse account = new AccountLoginResponse(status, idNumber, idNumberDate, idNumberPlace, name, address, email, birthday, idAccount, edong, parentId, parentEdong, pin, type, balance, lockMoney, changedPIN, session, verified, mac, ip, loginTime, logoutTime, strLoginTime, strLogoutTime, strType);
        return account;
    }
}