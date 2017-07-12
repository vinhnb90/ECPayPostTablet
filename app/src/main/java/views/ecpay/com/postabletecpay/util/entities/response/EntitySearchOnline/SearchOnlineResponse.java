package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.HeaderLoginResponse;

public class SearchOnlineResponse extends Respone {

    @SerializedName("header")
    @Expose
    private HeaderRespone header;
    @SerializedName("body")
    @Expose
    private BodySearchOnlineResponse bodySearchOnlineResponse;
    @SerializedName("footer")
    @Expose
    private FooterRespone footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderRespone getHeader() {
        return header;
    }

    public void setHeader(HeaderRespone header) {
        this.header = header;
    }

    public BodyRespone getBody() {
        return bodySearchOnlineResponse;
    }

    public void setBody(BodyRespone bodySearchOnlineResponse) {
        this.bodySearchOnlineResponse = (BodySearchOnlineResponse)bodySearchOnlineResponse;
    }

    public FooterRespone getFooter() {
        return footer;
    }

    public void setFooter(FooterRespone footerSearchOnlineResponse) {
        this.footer = footerSearchOnlineResponse;
    }
}