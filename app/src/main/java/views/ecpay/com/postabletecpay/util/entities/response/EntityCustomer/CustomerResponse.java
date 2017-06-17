package views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/15/17.
 */

public class CustomerResponse {
    @SerializedName("header")
    @Expose
    private HeaderCustomerResponse headerCustomerResponse;
    @SerializedName("body")
    @Expose
    private BodyCustomerResponse bodyCustomerResponse;
    @SerializedName("footer")
    @Expose
    private FooterCustomerResponse footerCustomerResponse;

    public HeaderCustomerResponse getHeaderCustomerResponse() {
        return headerCustomerResponse;
    }

    public void setHeaderCustomerResponse(HeaderCustomerResponse headerCustomerResponse) {
        this.headerCustomerResponse = headerCustomerResponse;
    }

    public BodyCustomerResponse getBodyCustomerResponse() {
        return bodyCustomerResponse;
    }

    public void setBodyCustomerResponse(BodyCustomerResponse bodyCustomerResponse) {
        this.bodyCustomerResponse = bodyCustomerResponse;
    }

    public FooterCustomerResponse getFooterCustomerResponse() {
        return footerCustomerResponse;
    }

    public void setFooterCustomerResponse(FooterCustomerResponse footerCustomerResponse) {
        this.footerCustomerResponse = footerCustomerResponse;
    }
}
