package views.ecpay.com.postabletecpay.util.entities.request.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

@JsonAdapter(LoginRequest.class)
public class LoginRequest {

    @SerializedName("header")
    @Expose
    private HeaderLoginRequest headerLoginRequest;
    @SerializedName("body")
    @Expose
    private BodyLoginRequest bodyLoginRequest;
    @SerializedName("footer")
    @Expose
    private FooterLoginRequest footerLoginRequest;

    public HeaderLoginRequest getHeaderLoginRequest() {
        return headerLoginRequest;
    }

    public void setHeaderLoginRequest(HeaderLoginRequest headerLoginRequest) {
        this.headerLoginRequest = headerLoginRequest;
    }

    public BodyLoginRequest getBodyLoginRequest() {
        return bodyLoginRequest;
    }

    public void setBodyLoginRequest(BodyLoginRequest bodyLoginRequest) {
        this.bodyLoginRequest = bodyLoginRequest;
    }

    public FooterLoginRequest getFooterLoginRequest() {
        return footerLoginRequest;
    }

    public void setFooterLoginRequest(FooterLoginRequest footerLoginRequest) {
        this.footerLoginRequest = footerLoginRequest;
    }
}
