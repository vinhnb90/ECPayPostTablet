package views.ecpay.com.postabletecpay.util.entities.request.EntityUpdateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.BodyPostBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.FooterPostBillRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.HeaderPostBillRequest;

/**
 * Created by tima on 6/19/17.
 */

public class UpdateAccountRequest {
    @SerializedName("header")
    @Expose
    private HeaderUpdateAccountRequest headerUpdateAccountRequest;
    @SerializedName("body")
    @Expose
    private BodyUpdateAccountRequest bodyUpdateAccountRequest;
    @SerializedName("footer")
    @Expose
    private FooterUpdateAccountRequest footerUpdateAccountRequest;

    public HeaderUpdateAccountRequest getHeaderUpdateAccountRequest() {
        return headerUpdateAccountRequest;
    }

    public void setHeaderUpdateAccountRequest(HeaderUpdateAccountRequest headerUpdateAccountRequest) {
        this.headerUpdateAccountRequest = headerUpdateAccountRequest;
    }

    public BodyUpdateAccountRequest getBodyUpdateAccountRequest() {
        return bodyUpdateAccountRequest;
    }

    public void setBodyUpdateAccountRequest(BodyUpdateAccountRequest bodyUpdateAccountRequest) {
        this.bodyUpdateAccountRequest = bodyUpdateAccountRequest;
    }

    public FooterUpdateAccountRequest getFooterUpdateAccountRequest() {
        return footerUpdateAccountRequest;
    }

    public void setFooterUpdateAccountRequest(FooterUpdateAccountRequest footerUpdateAccountRequest) {
        this.footerUpdateAccountRequest = footerUpdateAccountRequest;
    }
}
