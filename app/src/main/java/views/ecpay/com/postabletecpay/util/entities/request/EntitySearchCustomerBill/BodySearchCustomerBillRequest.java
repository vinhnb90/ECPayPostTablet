package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomerBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by MyPC on 22/06/2017.
 */

public class BodySearchCustomerBillRequest extends BodyRequest {

    @SerializedName("customer-code")
    @Expose
    private String customerCode;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
