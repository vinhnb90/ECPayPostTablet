package views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;

public class CheckTrainOnlineRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderRequest header;
    @SerializedName("body")
    @Expose
    private BodyCheckTrainOnlineRequest body;
    @SerializedName("footer")
    @Expose
    private FooterRequest footer;
    private final static long serialVersionUID = 6743274675011955698L;

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
        this.body = (BodyCheckTrainOnlineRequest) body;
    }

    public FooterRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterRequest footer) {
        this.footer = footer;
    }
}