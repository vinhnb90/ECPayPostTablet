package views.ecpay.com.postabletecpay.util.webservice;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;

/**
 * Created by VinhNB on 5/16/2017.
 */

public interface ExcuteAPI {
    @POST("/execute")
    Call<LoginResponseReponse> execute(@Field("message") String message);
}
