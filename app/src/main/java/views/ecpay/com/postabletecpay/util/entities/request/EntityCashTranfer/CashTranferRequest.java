package views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duydatpham on 6/17/17.
 */

public class CashTranferRequest implements Serializable {
    @SerializedName("header")
    @Expose
    private HeaderCashTranferRequest header;
    @SerializedName("body")
    @Expose
    private BodyCashTranferRequest body;
    @SerializedName("footer")
    @Expose
    private FooterCashTranferRequest footer;



    public HeaderCashTranferRequest getHeader() {
        return header;
    }

    public BodyCashTranferRequest getBody() {
        return body;
    }

    public FooterCashTranferRequest getFooter() {
        return footer;
    }

    public void setHeader(HeaderCashTranferRequest header) {
        this.header = header;
    }

    public void setBody(BodyCashTranferRequest body) {
        this.body = body;
    }

    public void setFooter(FooterCashTranferRequest footer) {
        this.footer = footer;
    }
}
