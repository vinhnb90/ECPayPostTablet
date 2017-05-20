package views.ecpay.com.postabletecpay.util.webservice;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.LoginResponseLogin;

/**
 * Created by VinhNB on 5/16/2017.
 */

public interface ExcuteAPI {
    @POST("/execute")
    Call<LoginResponseLogin> execute(@Field("message") String message);
}
