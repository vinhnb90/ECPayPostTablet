package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.HeaderLoginResponse;

public class SearchOnlineResponse implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderLoginResponse header;
    @SerializedName("bodySearchOnlineResponse")
    @Expose
    private BodySearchOnlineResponse bodySearchOnlineResponse;
    @SerializedName("footerSearchOnlineResponse")
    @Expose
    private FooterSearchOnlineResponse footerSearchOnlineResponse;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderLoginResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderLoginResponse header) {
        this.header = header;
    }

    public BodySearchOnlineResponse getBodySearchOnlineResponse() {
        return bodySearchOnlineResponse;
    }

    public void setBodySearchOnlineResponse(BodySearchOnlineResponse bodySearchOnlineResponse) {
        this.bodySearchOnlineResponse = bodySearchOnlineResponse;
    }

    public FooterSearchOnlineResponse getFooterSearchOnlineResponse() {
        return footerSearchOnlineResponse;
    }

    public void setFooterSearchOnlineResponse(FooterSearchOnlineResponse footerSearchOnlineResponse) {
        this.footerSearchOnlineResponse = footerSearchOnlineResponse;
    }
}