package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomerBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;

/**
 * Created by MyPC on 22/06/2017.
 */

public class SearchCustomerBillRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodySearchCustomerBillRequest body;
    @SerializedName("footer")
    @Expose
    private FooterRequest footer;

    @Override
    public HeaderRequest getHeader() {
        return this.header;
    }

    @Override
    public void setHeader(HeaderRequest header) {
        this.header = header;
    }

    @Override
    public BodyRequest getBody() {
        return this.body;
    }

    @Override
    public void setBody(BodyRequest body) {
        this.body = (BodySearchCustomerBillRequest)body;
    }

    @Override
    public FooterRequest getFooter() {
        return this.footer;
    }

    @Override
    public void setFooter(FooterRequest footer) {
        this.footer = footer;
    }
}
