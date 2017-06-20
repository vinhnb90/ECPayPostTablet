package views.ecpay.com.postabletecpay.util.entities.response.EntityCashTranfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by duydatpham on 6/17/17.
 */

public class CashTranferRespone {
    @SerializedName("header")
    @Expose
    private HeaderCashTranferRespone header;

    @SerializedName("body")
    @Expose
    private  BodyCashTranferRespone body;

    @SerializedName("footer")
    @Expose
    private  FooterCashTranferRespone footer;

    public HeaderCashTranferRespone getHeader() {
        return header;
    }

    public void setHeader(HeaderCashTranferRespone header) {
        this.header = header;
    }

    public BodyCashTranferRespone getBody() {
        return body;
    }

    public void setBody(BodyCashTranferRespone body) {
        this.body = body;
    }

    public FooterCashTranferRespone getFooter() {
        return footer;
    }

    public void setFooter(FooterCashTranferRespone footer) {
        this.footer = footer;
    }
}
