package views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;


@JsonAdapter(ChangePassRequest.class)
public class ChangePassRequest {

    @SerializedName("header")
    @Expose
    private HeaderChangePassRequest headerChangePassRequest;
    @SerializedName("body")
    @Expose
    private BodyChangePassRequest bodyChangePassRequest;
    @SerializedName("footer")
    @Expose
    private FooterChangePassRequest footerChangePassRequest;

    public HeaderChangePassRequest getHeaderChangePassRequest() {
        return headerChangePassRequest;
    }

    public void setHeaderChangePassRequest(HeaderChangePassRequest HeaderChangePassRequest) {
        this.headerChangePassRequest = HeaderChangePassRequest;
    }

    public BodyChangePassRequest getBodyChangePassRequest() {
        return bodyChangePassRequest;
    }

    public void setBodyChangePassRequest(BodyChangePassRequest BodyChangePassRequest) {
        this.bodyChangePassRequest = BodyChangePassRequest;
    }

    public FooterChangePassRequest getFooterChangePassRequest() {
        return footerChangePassRequest;
    }

    public void setFooterChangePassRequest(FooterChangePassRequest footerChangePassRequest) {
        this.footerChangePassRequest = footerChangePassRequest;
    }
}
