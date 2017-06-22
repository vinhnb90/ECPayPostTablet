package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;

/**
 * Created by MyPC on 22/06/2017.
 */

public class SearchCustomerRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodySearchCustomerRequest body;
    @SerializedName("footer")
    @Expose
    private FooterRequest footer;

    @Override
    public HeaderRequest getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderRequest header) {
        this.header = header;
    }

    @Override
    public BodyRequest getBody() {
        return body;
    }

    @Override
    public void setBody(BodyRequest body) {
        this.body = (BodySearchCustomerRequest)body;
    }

    @Override
    public FooterRequest getFooter() {
        return this.footer ;
    }

    @Override
    public void setFooter(FooterRequest footer) {
        this.footer = footer;
    }
}
