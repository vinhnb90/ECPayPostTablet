package views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

public class DeleteBillOnlineRespone extends Respone{

    @SerializedName("header")
    @Expose
    private HeaderRespone header;
    @SerializedName("body")
    @Expose
    private BodyRespone body;
    @SerializedName("footer")
    @Expose
    private FooterRespone footer;

    @Override
    public HeaderRespone getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderRespone header) {
        this.header = header;
    }

    @Override
    public BodyRespone getBody() {
        return body;
    }

    @Override
    public void setBody(BodyRespone body) {
        this.body = body;
    }

    @Override
    public FooterRespone getFooter() {
        return footer;
    }

    @Override
    public void setFooter(FooterRespone footer) {
        this.footer = footer;
    }
}