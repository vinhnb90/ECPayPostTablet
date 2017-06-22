package views.ecpay.com.postabletecpay.util.entities.request.EntityMapCustomerCard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;
import views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomerBill.BodySearchCustomerBillRequest;

/**
 * Created by duydatpham on 6/23/17.
 */

public class MapCustomerCardRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodyMapCustomerCardRequest body;
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
        this.body = (BodyMapCustomerCardRequest)body;
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
