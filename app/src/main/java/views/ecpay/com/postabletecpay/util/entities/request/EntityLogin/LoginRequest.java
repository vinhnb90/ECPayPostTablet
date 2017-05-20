package views.ecpay.com.postabletecpay.util.entities.request.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

@JsonAdapter(LoginRequest.class)
public class LoginRequest {

    @SerializedName("header")
    @Expose
    private HeaderLogin headerLogin;
    @SerializedName("body")
    @Expose
    private BodyLogin bodyLogin;
    @SerializedName("footer")
    @Expose
    private FooterLogin footerLogin;

    public HeaderLogin getHeaderLogin() {
        return headerLogin;
    }

    public void setHeaderLogin(HeaderLogin headerLogin) {
        this.headerLogin = headerLogin;
    }

    public BodyLogin getBodyLogin() {
        return bodyLogin;
    }

    public void setBodyLogin(BodyLogin bodyLogin) {
        this.bodyLogin = bodyLogin;
    }

    public FooterLogin getFooterLogin() {
        return footerLogin;
    }

    public void setFooterLogin(FooterLogin footerLogin) {
        this.footerLogin = footerLogin;
    }
}
