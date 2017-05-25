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
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.BodyChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.ChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.FooterChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.HeaderChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.BodyChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;

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

        FooterChangePassRequest footerChangePassRequest = new FooterChangePassRequest();
        footerChangePassRequest.setAccountIdt(accountId);

        final ChangePassRequest changePassRequest = new ChangePassRequest();
        changePassRequest.setHeaderChangePassRequest(headerChangePassRequest);
        changePassRequest.setBodyChangePassRequest(bodyChangePassRequest);
        changePassRequest.setFooterChangePassRequest(footerChangePassRequest);


        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoginRequest.class, new LoginRequestAdapter());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        //Serialised
        final String jsonResult = gson.toJson(changePassRequest);

        //Test Deserialised
        final ChangePassRequest parsedRequest = gson.fromJson(jsonResult, ChangePassRequest.class);

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

            LoginResponseReponse loginResponseReponse = null;

            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LoginRequest.class, new LoginResponseAdapter());
            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            loginResponseReponse = gson.fromJson(data, LoginResponseReponse.class);

            return loginResponseReponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(LoginResponseReponse loginResponseReponse) {
            super.onPostExecute(loginResponseReponse);
            if (!isEndCallSoap)
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
                e.printStackTrace();
            }

            if (response == null) {
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                Log.e(this.getClass().getName(), "doInBackground: Sai định dạng cấu trúc json response không chính xác.");
            }

            String data = response.toString();
            if (data.isEmpty())
                publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());

            ChangePassResponse changePassResponse = null;

            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChangePassRequest.class, new ChangePassResponse());
            gsonBuilder.setPrettyPrinting();
            final Gson gson = gsonBuilder.create();

            //end Tests

            changePassResponse = gson.fromJson(data, ChangePassResponse.class);

            return changePassResponse;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(ChangePassResponse changePassResponse) {
            super.onPostExecute(changePassResponse);
            if (!isEndCallSoap)
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
    //endregion

}
