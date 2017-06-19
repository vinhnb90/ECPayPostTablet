package views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class PostBillRequest {
    @SerializedName("header")
    @Expose
    private HeaderPostBillRequest headerPostBillRequest;
    @SerializedName("body")
    @Expose
    private BodyPostBillRequest bodyPostBillRequest;
    @SerializedName("footer")
    @Expose
    private FooterPostBillRequest footerPostBillRequest;

    public HeaderPostBillRequest getHeaderPostBillRequest() {
        return headerPostBillRequest;
    }

    public void setHeaderPostBillRequest(HeaderPostBillRequest headerPostBillRequest) {
        this.headerPostBillRequest = headerPostBillRequest;
    }

    public BodyPostBillRequest getBodyPostBillRequest() {
        return bodyPostBillRequest;
    }

    public void setBodyPostBillRequest(BodyPostBillRequest bodyPostBillRequest) {
        this.bodyPostBillRequest = bodyPostBillRequest;
    }

    public FooterPostBillRequest getFooterPostBillRequest() {
        return footerPostBillRequest;
    }

    public void setFooterPostBillRequest(FooterPostBillRequest footerPostBillRequest) {
        this.footerPostBillRequest = footerPostBillRequest;
    }
}
