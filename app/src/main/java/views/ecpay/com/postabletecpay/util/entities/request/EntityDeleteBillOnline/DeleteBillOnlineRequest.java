package views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

public class DeleteBillOnlineRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodyDeleteBillOnlineRequest body;
    @SerializedName("footer")
    @Expose
    private FooterRequest footer;

    public HeaderRequest getHeader() {
        return header;
    }

    public void setHeader(HeaderRequest header) {
        this.header = header;
    }

    public BodyRequest getBody() {
        return body;
    }

    public void setBody(BodyRequest body) {
        this.body = (BodyDeleteBillOnlineRequest)body;
    }

    public FooterRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterRequest footer) {
        this.footer = footer;
    }
}