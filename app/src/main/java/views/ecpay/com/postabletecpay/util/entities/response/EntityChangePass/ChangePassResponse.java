package views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassResponse {

    @SerializedName("header")
    @Expose
    private HeaderChangePassResponse headerChangePassResponse;
    @SerializedName("body")
    @Expose
    private BodyChangePassResponse bodyChangePassResponse;
    @SerializedName("footer")
    @Expose
    private FooterChangePassResponse footerChangePassResponse;

    public HeaderChangePassResponse getHeaderChangePassResponse() {
        return headerChangePassResponse;
    }

    public void setHeaderChangePassResponse(HeaderChangePassResponse headerChangePassResponse) {
        this.headerChangePassResponse = headerChangePassResponse;
    }

    public BodyChangePassResponse getBodyChangePassResponse() {
        return bodyChangePassResponse;
    }

    public void setBodyChangePassResponse(BodyChangePassResponse bodyChangePassResponse) {
        this.bodyChangePassResponse = bodyChangePassResponse;
    }

    public FooterChangePassResponse getFooterChangePassResponse() {
        return footerChangePassResponse;
    }

    public void setFooterChangePassResponse(FooterChangePassResponse footerChangePassResponse) {
        this.footerChangePassResponse = footerChangePassResponse;
    }
}