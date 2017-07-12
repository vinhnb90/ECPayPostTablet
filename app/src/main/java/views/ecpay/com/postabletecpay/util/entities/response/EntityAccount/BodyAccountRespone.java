package views.ecpay.com.postabletecpay.util.entities.response.EntityAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

/**
 * Created by duydatpham on 7/12/17.
 */

public class BodyAccountRespone extends BodyRespone {

    @SerializedName("phone")
    @Expose
    private String phone;

}
