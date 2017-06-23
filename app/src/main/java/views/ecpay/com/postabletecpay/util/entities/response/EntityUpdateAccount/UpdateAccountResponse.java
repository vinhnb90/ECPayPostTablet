package views.ecpay.com.postabletecpay.util.entities.response.EntityUpdateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.BodyPostBillReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.FooterPostBillReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.HeaderPostBillReponse;

/**
 * Created by tima on 6/19/17.
 */

public class UpdateAccountResponse {
    @SerializedName("header")
    @Expose
    private HeaderUpdateAccountResponse headerUpdateAccountResponse;
    @SerializedName("body")
    @Expose
    private BodyUpdateAccountResponse bodyUpdateAccountResponse;
    @SerializedName("footer")
    @Expose
    private FooterUpdateAccountResponse footerUpdateAccountResponse;

    public HeaderUpdateAccountResponse getHeaderUpdateAccountResponse() {
        return headerUpdateAccountResponse;
    }

    public void setHeaderUpdateAccountResponse(HeaderUpdateAccountResponse headerUpdateAccountResponse) {
        this.headerUpdateAccountResponse = headerUpdateAccountResponse;
    }

    public BodyUpdateAccountResponse getBodyUpdateAccountResponse() {
        return bodyUpdateAccountResponse;
    }

    public void setBodyUpdateAccountResponse(BodyUpdateAccountResponse bodyUpdateAccountResponse) {
        this.bodyUpdateAccountResponse = bodyUpdateAccountResponse;
    }

    public FooterUpdateAccountResponse getFooterUpdateAccountResponse() {
        return footerUpdateAccountResponse;
    }

    public void setFooterUpdateAccountResponse(FooterUpdateAccountResponse footerUpdateAccountResponse) {
        this.footerUpdateAccountResponse = footerUpdateAccountResponse;
    }
}
