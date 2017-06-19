package views.ecpay.com.postabletecpay.util.entities.request.EntityLogout;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutRequest implements Serializable
{

@SerializedName("header")
@Expose
private HeaderLogoutRequest header;
@SerializedName("body")
@Expose
private BodyLogoutRequest body;
@SerializedName("footer")
@Expose
private FooterLogoutRequest footer;
private final static long serialVersionUID = 6743274675011955698L;

    public HeaderLogoutRequest getHeader() {
        return header;
    }

    public void setHeader(HeaderLogoutRequest header) {
        this.header = header;
    }

    public BodyLogoutRequest getBody() {
        return body;
    }

    public void setBody(BodyLogoutRequest body) {
        this.body = body;
    }

    public FooterLogoutRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterLogoutRequest footer) {
        this.footer = footer;
    }
}