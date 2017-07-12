package views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

public class BillingOnlineRespone extends Respone {
    @SerializedName("header")
    @Expose
    private HeaderBillingOnlineRespone header;
    @SerializedName("body")
    @Expose
    private BodyBillingOnlineRespone body;
    @SerializedName("footer")
    @Expose
    private FooterBillingOnlineRespone footer;
    private final static long serialVersionUID = 1637014148722407909L;


    @Override
    public HeaderRespone getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderRespone header) {
        this.header = (HeaderBillingOnlineRespone) header;
    }

    @Override
    public BodyRespone getBody() {
        return this.body;
    }

    @Override
    public void setBody(BodyRespone body) {
        this.body = (BodyBillingOnlineRespone)body;
    }

    @Override
    public FooterRespone getFooter() {
        return this.footer;
    }

    @Override
    public void setFooter(FooterRespone footer) {
        this.footer = (FooterBillingOnlineRespone)footer;
    }
}