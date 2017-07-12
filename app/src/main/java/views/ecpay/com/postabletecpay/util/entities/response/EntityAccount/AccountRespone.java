package views.ecpay.com.postabletecpay.util.entities.response.EntityAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

/**
 * Created by duydatpham on 7/12/17.
 */

public class AccountRespone extends Respone {
    @SerializedName("header")
    @Expose
    private HeaderRespone header;
    @SerializedName("body")
    @Expose
    private BodyAccountRespone body;
    @SerializedName("footer")
    @Expose
    private FooterRespone footer;


    @Override
    public HeaderRespone getHeader() {
        return this.header;
    }

    @Override
    public void setHeader(HeaderRespone header) {
        this.header = header;
    }

    @Override
    public BodyRespone getBody() {
        return this.body;
    }

    @Override
    public void setBody(BodyRespone body) {
        this.body = (BodyAccountRespone) body;
    }

    @Override
    public FooterRespone getFooter() {
        return this.footer;
    }

    @Override
    public void setFooter(FooterRespone footer) {
        this.footer = footer;
    }
}
