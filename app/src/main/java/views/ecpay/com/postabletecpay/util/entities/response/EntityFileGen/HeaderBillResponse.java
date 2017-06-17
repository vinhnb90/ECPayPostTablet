package views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/10/17.
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
