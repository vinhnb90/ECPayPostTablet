package views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;

/**
 * Created by tima on 6/19/17.
 */

public class PostBillRequest extends Request {
    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodyPostBillRequest bodyPostBillRequest;
    @SerializedName("footer")
    @Expose
    private FooterRequest footerPostBillRequest;

    public HeaderRequest getHeader() {
        return this.header;
    }

    public void setHeader(HeaderRequest headerPostBillRequest) {
        this.header = headerPostBillRequest;
    }

    public BodyRequest getBody() {
        return bodyPostBillRequest;
    }

    public void setBody(BodyRequest bodyPostBillRequest) {
        this.bodyPostBillRequest = (BodyPostBillRequest) bodyPostBillRequest;
    }

    public FooterRequest getFooter() {
        return footerPostBillRequest;
    }

    public void setFooter(FooterRequest footerPostBillRequest) {
        this.footerPostBillRequest = footerPostBillRequest;
    }
}
