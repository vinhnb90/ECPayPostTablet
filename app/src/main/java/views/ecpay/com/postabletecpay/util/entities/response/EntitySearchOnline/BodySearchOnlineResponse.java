package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

public class BodySearchOnlineResponse extends BodyRespone
{
    @SerializedName("pin-login")
    @Expose
    private String pinLogin;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("customer")
    @Expose
    private String customer;

    public String getPinLogin() {
        return pinLogin;
    }

    public void setPinLogin(String pinLogin) {
        this.pinLogin = pinLogin;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}