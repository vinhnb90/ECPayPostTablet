package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerResponse implements Serializable {

    @SerializedName("customer")
    @Expose
    private CustomerInsideBody customer;
    private final static long serialVersionUID = -1664811034473454612L;

    public CustomerInsideBody getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInsideBody customer) {
        this.customer = customer;
    }
}