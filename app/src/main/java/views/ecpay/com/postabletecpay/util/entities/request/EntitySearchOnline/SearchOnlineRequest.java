package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;

public class SearchOnlineRequest implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderLoginRequest header;
    @SerializedName("body")
    @Expose
    private BodySearchOnlineRequest body;
    @SerializedName("footer")
    @Expose
    private FooterLoginRequest footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderLoginRequest getHeader() {
        return header;
    }

    public void setHeader(HeaderLoginRequest header) {
        this.header = header;
    }

    public BodySearchOnlineRequest getBody() {
        return body;
    }

    public void setBody(BodySearchOnlineRequest body) {
        this.body = body;
    }

    public FooterLoginRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterLoginRequest footer) {
        this.footer = footer;
    }
}