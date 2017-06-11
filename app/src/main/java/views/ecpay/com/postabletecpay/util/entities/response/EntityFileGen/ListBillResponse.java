package views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/10/17.
 */

public class ListBillResponse {
    @SerializedName("header")
    @Expose
    private HeaderBillResponse headerBillResponse;
    @SerializedName("body")
    @Expose
    private BodyBillResponse bodyBillResponse;
    @SerializedName("footer")
    @Expose
    private FooterBillResponse footerBillResponse;

    public HeaderBillResponse getHeaderBillResponse() {
        return headerBillResponse;
    }

    public void setHeaderBillResponse(HeaderBillResponse headerBillResponse) {
        this.headerBillResponse = headerBillResponse;
    }

    public BodyBillResponse getBodyBillResponse() {
        return bodyBillResponse;
    }

    public void setBodyBillResponse(BodyBillResponse bodyBillResponse) {
        this.bodyBillResponse = bodyBillResponse;
    }

    public FooterBillResponse getFooterBillResponse() {
        return footerBillResponse;
    }

    public void setFooterBillResponse(FooterBillResponse footerBillResponse) {
        this.footerBillResponse = footerBillResponse;
    }
}
