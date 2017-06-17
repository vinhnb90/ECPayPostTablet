package views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteBillOnlineRespone implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderDeleteBillOnlineRespone header;
    @SerializedName("body")
    @Expose
    private BodyDeleteBillOnlineRespone body;
    @SerializedName("footer")
    @Expose
    private FooterDeleteBillOnlineRespone footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderDeleteBillOnlineRespone getHeader() {
        return header;
    }

    public void setHeader(HeaderDeleteBillOnlineRespone header) {
        this.header = header;
    }

    public BodyDeleteBillOnlineRespone getBody() {
        return body;
    }

    public void setBody(BodyDeleteBillOnlineRespone body) {
        this.body = body;
    }

    public FooterDeleteBillOnlineRespone getFooter() {
        return footer;
    }

    public void setFooter(FooterDeleteBillOnlineRespone footer) {
        this.footer = footer;
    }
}