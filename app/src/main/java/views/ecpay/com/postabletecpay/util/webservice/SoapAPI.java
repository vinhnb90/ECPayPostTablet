package views.ecpay.com.postabletecpay.util.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import views.ecpay.com.postabletecpay.model.adapter.LoginRequestAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.Body;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.Footer;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.Header;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponse;

/**
 * Created by VinhNB on 5/12/2017.
 */

public class SoapAPI {

    //region create JSON Request service
    public static String getJsonRequestLogin(String agent, String pass, String commandId, Long auditNumber, String mac, String diskDriver, String signatureEncrypted, String pinLogin, String accountId) {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (pass == null || pass.isEmpty() || pass.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (auditNumber == null)
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

        Header header = new Header();
        header.setAgent(agent);
        header.setPassword(pass);
        header.setCommandId(commandId);

        Body body = new Body();
        body.setAuditNumber(auditNumber);
        body.setMac(mac);
        body.setDiskDrive(diskDriver);
        body.setSignature(signatureEncrypted);
        body.setPinLogin(pinLogin);

        Footer footer = new Footer();
        footer.setAccountIdt(accountId);

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setHeader(header);
        loginRequest.setBody(body);
        loginRequest.setFooter(footer);


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
    public static class AsyncSoapLogin extends AsyncTask<String, String, LoginResponse> {
        private ExcuteAPI excuteAPI;
        private AsyncSoapLoginCallBack callBack;
        private boolean isEndCallSoap = false;

        public AsyncSoapLogin(AsyncSoapLoginCallBack callBack) throws Exception {
            this.callBack = callBack;

            try {
                excuteAPI = new Retrofit.Builder().baseUrl(Common.ENDPOINT_URL).
                        addConverterFactory(GsonConverterFactory.create()).
                        build().create(ExcuteAPI.class);
            } catch (Exception e) {
                Log.e(this.getClass().getName().toString(), "Error when config retrofit : " + e.getMessage());
                throw new Exception(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_LOGIN.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callBack.onPre(this);
        }

        @Override
        protected LoginResponse doInBackground(String... jsons) {
            String json = jsons[0];
            Call<LoginResponse> responseCall = excuteAPI.execute(json);
            Response<LoginResponse> response = null;

              /*  response = responseCall.execute();*/
            responseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    //end call
                    isEndCallSoap = true;

                    LoginResponse loginResponse = response.body();
                    onPostExecute(loginResponse);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    //end call
                    isEndCallSoap = true;

                    publishProgress(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_LOGIN.toString());
                }
            });

            return response.body();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            if (isEndCallSoap)
                callBack.onUpdate(message);
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            if (isEndCallSoap)
                callBack.onPost(loginResponse);
        }

        public static abstract class AsyncSoapLoginCallBack {
            public abstract void onPre(final AsyncSoapLogin soapLogin);

            public abstract void onUpdate(String message);

            public abstract void onPost(LoginResponse response);

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
