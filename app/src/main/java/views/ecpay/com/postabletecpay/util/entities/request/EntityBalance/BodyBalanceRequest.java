package views.ecpay.com.postabletecpay.util.entities.request.EntityBalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by duydatpham on 7/12/17.
 */

public class BodyBalanceRequest extends BodyRequest {
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("partner-code")
    @Expose
    private String partnerCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
}
