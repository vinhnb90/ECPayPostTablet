package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

public class BodySearchOnlineRequest extends BodyRequest {

    @SerializedName("provider-code")
    @Expose
    private String providerCode;
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("list-customer-code")
    @Expose
    private String[] listCustomerCode;

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String[] getListCustomerCode() {
        return listCustomerCode;
    }

    public void setListCustomerCode(String[] listCustomerCode) {
        this.listCustomerCode = listCustomerCode;
    }
}
