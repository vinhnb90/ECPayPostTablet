package views.ecpay.com.postabletecpay.util.entities.response.EntityBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/15/17.
 */

public class HeaderBillResponse {
    @SerializedName("object")
    @Expose
    private String object;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
