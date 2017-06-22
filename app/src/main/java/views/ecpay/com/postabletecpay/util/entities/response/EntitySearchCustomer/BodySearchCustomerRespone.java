package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by MyPC on 22/06/2017.
 */

public class BodySearchCustomerRespone extends BodyRespone {
    @SerializedName("pin-login")
    @Expose
    private String pinLogin;
    @SerializedName("list-customer")
    @Expose
    private List<Customer> listCustomer;

    public String getPinLogin() {
        return pinLogin;
    }

    public void setPinLogin(String pinLogin) {
        this.pinLogin = pinLogin;
    }

    public List<Customer> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(List<Customer> listCustomer) {
        this.listCustomer = listCustomer;
    }
}
