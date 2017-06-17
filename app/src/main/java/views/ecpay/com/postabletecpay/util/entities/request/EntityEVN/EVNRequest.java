package views.ecpay.com.postabletecpay.util.entities.request.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;

@JsonAdapter(EVNRequest.class)
public class EVNRequest {

    @SerializedName("header")
    @Expose
    private HeaderEVNRequest headerEVNRequest;
    @SerializedName("body")
    @Expose
    private BodyEVNRequest bodyEVNRequest;
    @SerializedName("footer")
    @Expose
    private FooterEVNRequest footerEVNRequest;

    public HeaderEVNRequest getHeaderEVNRequest() {
        return headerEVNRequest;
    }

    public void setHeaderEVNRequest(HeaderEVNRequest headerEVNRequest) {
        this.headerEVNRequest = headerEVNRequest;
    }

    public BodyEVNRequest getBodyEVNRequest() {
        return bodyEVNRequest;
    }

    public void setBodyEVNRequest(BodyEVNRequest bodyEVNRequest) {
        this.bodyEVNRequest = bodyEVNRequest;
    }

    public FooterEVNRequest getFooterEVNRequest() {
        return footerEVNRequest;
    }

    public void setFooterEVNRequest(FooterEVNRequest footerEVNRequest) {
        this.footerEVNRequest = footerEVNRequest;
    }
}
