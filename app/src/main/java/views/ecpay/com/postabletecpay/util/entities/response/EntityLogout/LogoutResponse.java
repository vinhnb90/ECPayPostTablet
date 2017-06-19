package views.ecpay.com.postabletecpay.util.entities.response.EntityLogout;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutResponse implements Serializable
{

@SerializedName("header")
@Expose
private HeaderLogoutResponse header;
@SerializedName("body")
@Expose
private BodyLogoutResponse body;
@SerializedName("footer")
@Expose
private FooterLogoutResponse footer;
private final static long serialVersionUID = 6743274675011955698L;

    public HeaderLogoutResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderLogoutResponse header) {
        this.header = header;
    }

    public BodyLogoutResponse getBody() {
        return body;
    }

    public void setBody(BodyLogoutResponse body) {
        this.body = body;
    }

    public FooterLogoutResponse getFooter() {
        return footer;
    }

    public void setFooter(FooterLogoutResponse footer) {
        this.footer = footer;
    }
}