package views.ecpay.com.postabletecpay.util.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import views.ecpay.com.postabletecpay.model.adapter.LoginRequestAdapter;
import views.ecpay.com.postabletecpay.model.adapter.LoginResponseAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLogin;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLogin;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLogin;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.LoginResponseLogin;

import static views.ecpay.com.postabletecpay.util.commons.Common.ENDPOINT_URL;

/**
 * Created by VinhNB on 5/12/2017.
 */

public class SoapAPI {

    //region create JSON Request service
    public static String getJsonRequestLogin(String agent, String agentEncypted, String commandId, long auditNumber, String mac, String diskDriver, String signatureEncrypted, String pinLogin, String accountId) {
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

        HeaderLogin headerLogin = new HeaderLogin();
        headerLogin.setAgent(agent);
        headerLogin.setPassword(agentEncypted);
        headerLogin.setCommandId(commandId);

        BodyLogin bodyLogin = new BodyLogin();
        bodyLogin.setAuditNumber(auditNumber);
        bodyLogin.setMac(mac);
        bodyLogin.setDiskDrive(diskDriver);
        bodyLogin.setSignature(signatureEncrypted);
        bodyLogin.setPinLogin(pinLogin);

        FooterLogin footerLogin = new FooterLogin();
        footerLogin.setAccountIdt(accountId);

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setHeaderLogin(headerLogin);
        loginRequest.setBodyLogin(bodyLogin);
        loginRequest.setFooterLogin(footerLogin);


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

        HeaderLogin headerLogin = new HeaderLogin();
        headerLogin.setAgent(agent);
        headerLogin.setPassword(agentEncypted);
        headerLogin.setCommandId(commandId);

        BodyLogin bodyLogin = new BodyLogin();
        bodyLogin.setAuditNumber(auditNumber);
        bodyLogin.setMac(mac);
        bodyLogin.setDiskDrive(diskDriver);
        bodyLogin.setSignature(signatureEncrypted);
        bodyLogin.setPinLogin(pinLogin);

        FooterLogin footerLogin = new FooterLogin();
        footerLogin.setAccountIdt(accountId);

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setHeaderLogin(headerLogin);
        loginRequest.setBodyLogin(bodyLogin);
        loginRequest.setFooterLogin(footerLogin);


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
    //endregion

    //region class api soap
    public static class AsyncSoapLogin extends AsyncTask<String, String, LoginResponseLogin> {

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
        protected LoginResponseLogin doInBackground(String... jsons) {
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
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
            }

            String data = response.toString();
            if (data.isEmpty())
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());

            LoginResponseLogin loginResponseLogin = null;

            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LoginRequest.class, new LoginResponseAdapter());
            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

//            test
            String dataTest = "{\n" +
                    "\t\"header\": {\n" +
                    "\t\t\"agent\": \"EPOS\",\n" +
                    "\t\t\"password\": \"gWt+ybGFHpIIc5wIXwh2KTKLm4gbwLbvM3friK89Jv4aSXTwZZ/SjLewqUO2nudGqIA6tspYzgEaIbINarzqNZKQ1fq9w84533hVVtglctf2jYMdHdHbSV38ItvnH+zK+zj6romUip3D2TtHt6AQvT/SiqaEK11y6zfIy6mIKA0=\",\n" +
                    "\t\t\"command-id\": \"LOGIN\"\n" +
                    "\t},\n" +
                    "\t\"body\": {\n" +
                    "\t\t\"audit-number\": 80906783874,\n" +
                    "\t\t\"mac\": \"F48E38AC06E300000000000000E0\",\n" +
                    "\t\t\"disk-drive\": \"BFEBFBFF000506E3\",\n" +
                    "\t\t\"signature\": \"Ks/5QN2v/GjT1w+uvYKJqT0YYz3Tk4IlASoO3WQ/I+F0TbVAtKID+wcoUmSDN9gKfaLmFJv3TNgbkkl10pZYBpnF9iNO8zsJgTuF57Ehf38JB+XVtUxltTC9s2X6LCvkvqA3UG5+ew3zFvzkd00uLrAcpzPkYbRrMzeSsm0S26U=\",\n" +
                    "\t\t\"pin-login\": \"5DF1037BAACA9DF7\",\n" +
                    "\t\t\"response-login\":\n" +
                    "\t\t{\n" +
                    "\t\t\t\"account\": {\n" +
                    "\t\t\t\t\"status\": \"1\",\n" +
                    "\t\t\t\t\"idNumber\": \"091503361\",\n" +
                    "\t\t\t\t\"idNumberDate\": \"12-07-2012\",\n" +
                    "\t\t\t\t\"idNumberPlace\": \"CA Thai nguyen\",\n" +
                    "\t\t\t\t\"name\": \"Nguyễn Thị Thủy\",\n" +
                    "\t\t\t\t\"address\": \"Thai Nguyen\",\n" +
                    "\t\t\t\t\"email\": \"ntthuy07@ecpay.vn\",\n" +
                    "\t\t\t\t\"birthday\": \"20-08-1989\",\n" +
                    "\t\t\t\t\"idAccount\": 7782,\n" +
                    "\t\t\t\t\"edong\": \"0917932663\",\n" +
                    "\t\t\t\t\"parentId\": 2675,\n" +
                    "\t\t\t\t\"parentEdong\": \"0973968290\",\n" +
                    "\t\t\t\t\"pin\": null,\n" +
                    "\t\t\t\t\"type\": 1,\n" +
                    "\t\t\t\t\"balance\": 815941478,\n" +
                    "\t\t\t\t\"lockMoney\": 0,\n" +
                    "\t\t\t\t\"changedPIN\": true,\n" +
                    "\t\t\t\t\"session\": \"6dbabff2-67a1-4145-a8be-b9385e96ab3a\",\n" +
                    "\t\t\t\t\"verified\": 0,\n" +
                    "\t\t\t\t\"mac\": \"F48E38AC06E300000000000000E0\",\n" +
                    "\t\t\t\t\"ip\": \"192.168.120.170\",\n" +
                    "\t\t\t\t\"loginTime\": 1494464998473,\n" +
                    "\t\t\t\t\"logoutTime\": null,\n" +
                    "\t\t\t\t\"strLoginTime\": null,\n" +
                    "\t\t\t\t\"strLogoutTime\": null,\n" +
                    "\t\t\t\t\"strType\": null\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"listEvnPC\": [{\n" +
                    "\t\t\t\t\t\"parentId\": 0,\n" +
                    "\t\t\t\t\t\"pcId\": 1,\n" +
                    "\t\t\t\t\t\"code\": \"PD\",\n" +
                    "\t\t\t\t\t\"ext\": \"PD\",\n" +
                    "\t\t\t\t\t\"fullName\": \"Tổng Công Ty Điện Lực Thành Phố Hà Nội\",\n" +
                    "\t\t\t\t\t\"shortName\": \"HN-VietNam\",\n" +
                    "\t\t\t\t\t\"address\": \"35 Lê Đức Thọ-My Đình- Hà Nội\",\n" +
                    "\t\t\t\t\t\"taxCode\": null,\n" +
                    "\t\t\t\t\t\"phone1\": \"84422200898\",\n" +
                    "\t\t\t\t\t\"phone2\": null,\n" +
                    "\t\t\t\t\t\"fax\": \"84422200899\",\n" +
                    "\t\t\t\t\t\"level\": 1,\n" +
                    "\t\t\t\t\t\"mailTo\": null,\n" +
                    "\t\t\t\t\t\"mailCc\": null,\n" +
                    "\t\t\t\t\t\"status\": 1,\n" +
                    "\t\t\t\t\t\"dateCreated\": \"2016-11-18\",\n" +
                    "\t\t\t\t\t\"idChanged\": 26412281,\n" +
                    "\t\t\t\t\t\"dateChanged\": \"2017-04-25\",\n" +
                    "\t\t\t\t\t\"regionId\": 0\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"parentId\": 1,\n" +
                    "\t\t\t\t\t\"pcId\": 16,\n" +
                    "\t\t\t\t\t\"code\": \"PD1100\",\n" +
                    "\t\t\t\t\t\"ext\": \"PD11\",\n" +
                    "\t\t\t\t\t\"fullName\": \"Công Ty Điện Lực Thanh Xuân\",\n" +
                    "\t\t\t\t\t\"shortName\": \"Thanh Xuân\",\n" +
                    "\t\t\t\t\t\"address\": \"35 Lê Đức Thọ-My Đình- Hà Nội\",\n" +
                    "\t\t\t\t\t\"taxCode\": null,\n" +
                    "\t\t\t\t\t\"phone1\": \"84422200898\",\n" +
                    "\t\t\t\t\t\"phone2\": null,\n" +
                    "\t\t\t\t\t\"fax\": null,\n" +
                    "\t\t\t\t\t\"level\": 2,\n" +
                    "\t\t\t\t\t\"mailTo\": null,\n" +
                    "\t\t\t\t\t\"mailCc\": null,\n" +
                    "\t\t\t\t\t\"status\": 1,\n" +
                    "\t\t\t\t\t\"dateCreated\": \"2016-11-18\",\n" +
                    "\t\t\t\t\t\"idChanged\": 26412296,\n" +
                    "\t\t\t\t\t\"dateChanged\": \"2017-04-25\",\n" +
                    "\t\t\t\t\t\"regionId\": 0\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"parentId\": 1,\n" +
                    "\t\t\t\t\t\"pcId\": 24,\n" +
                    "\t\t\t\t\t\"code\": \"PD1700\",\n" +
                    "\t\t\t\t\t\"ext\": \"PD17\",\n" +
                    "\t\t\t\t\t\"fullName\": \"Công Ty Điện Lực Sơn Tây\",\n" +
                    "\t\t\t\t\t\"shortName\": \"Sơn Tây\",\n" +
                    "\t\t\t\t\t\"address\": null,\n" +
                    "\t\t\t\t\t\"taxCode\": null,\n" +
                    "\t\t\t\t\t\"phone1\": null,\n" +
                    "\t\t\t\t\t\"phone2\": null,\n" +
                    "\t\t\t\t\t\"fax\": null,\n" +
                    "\t\t\t\t\t\"level\": 2,\n" +
                    "\t\t\t\t\t\"mailTo\": null,\n" +
                    "\t\t\t\t\t\"mailCc\": null,\n" +
                    "\t\t\t\t\t\"status\": 1,\n" +
                    "\t\t\t\t\t\"dateCreated\": \"2016-11-18\",\n" +
                    "\t\t\t\t\t\"idChanged\": 26412302,\n" +
                    "\t\t\t\t\t\"dateChanged\": \"2017-04-25\",\n" +
                    "\t\t\t\t\t\"regionId\": 0\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t],\n" +
                    "\t\t\t\"reponseCode\": \"000\",\n" +
                    "\t\t\t\"description\": \"Xac thuc thanh cong\",\n" +
                    "\t\t\t\"response\": \"SUCCESS\"\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\t},\n" +
                    "\t\"footer\": {\n" +
                    "\t\t\"account-idt\": \"0917932663\",\n" +
                    "\t\t\"response-code\": \"000\",\n" +
                    "\t\t\"description\": \"Xac thuc thanh cong\",\n" +
                    "\t\t\"source-address\": \"192.168.120.170\"\n" +
                    "\t}\n" +
                    "}\n";

            data = dataTest;
            //end Tests

            loginResponseLogin = gson.fromJson(data, LoginResponseLogin.class);

           return loginResponseLogin;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(LoginResponseLogin loginResponseLogin) {
            super.onPostExecute(loginResponseLogin);
            if (!isEndCallSoap)
                callBack.onPost(loginResponseLogin);
        }

        public static abstract class AsyncSoapLoginCallBack {
            public abstract void onPre(final AsyncSoapLogin soapLogin);

            public abstract void onUpdate(String message);

            public abstract void onPost(LoginResponseLogin response);

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
    //endregion

}
