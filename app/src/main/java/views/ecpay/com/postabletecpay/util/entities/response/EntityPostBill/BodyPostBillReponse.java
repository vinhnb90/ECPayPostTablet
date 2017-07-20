package views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

/**
 * Created by tima on 6/19/17.
 */

public class BodyPostBillReponse extends BodyRespone {
    @SerializedName("List")
    @Expose
    private String listPostBill;

    public String getListPostBill() {
        return listPostBill;
    }

    public void setListPostBill(String listPostBill) {
        this.listPostBill = listPostBill;
    }
}
