package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseReponse {

@SerializedName("header")
@Expose
private HeaderLoginResponse headerLoginResponse;
@SerializedName("body")
@Expose
private BodyLoginResponse bodyLoginResponse;
@SerializedName("footer")
@Expose
private FooterLoginResponse footerLoginResponse;

public HeaderLoginResponse getHeaderLoginResponse() {
return headerLoginResponse;
}

public void setHeaderLoginResponse(HeaderLoginResponse headerLoginResponse) {
this.headerLoginResponse = headerLoginResponse;
}

public BodyLoginResponse getBodyLoginResponse() {
return bodyLoginResponse;
}

public void setBodyLoginResponse(BodyLoginResponse bodyLoginResponse) {
this.bodyLoginResponse = bodyLoginResponse;
}

public FooterLoginResponse getFooterLoginResponse() {
return footerLoginResponse;
}

public void setFooterLoginResponse(FooterLoginResponse footerLoginResponse) {
this.footerLoginResponse = footerLoginResponse;
}

}