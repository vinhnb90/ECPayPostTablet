package views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteBillOnlineRequest implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderDeleteBillOnlineRequest header;
    @SerializedName("body")
    @Expose
    private BodyDeleteBillOnlineRequest body;
    @SerializedName("footer")
    @Expose
    private FooterDeleteBillOnlineRequest footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderDeleteBillOnlineRequest getHeader() {
        return header;
    }

    public void setHeader(HeaderDeleteBillOnlineRequest header) {
        this.header = header;
    }

    public BodyDeleteBillOnlineRequest getBody() {
        return body;
    }

    public void setBody(BodyDeleteBillOnlineRequest body) {
        this.body = body;
    }

    public FooterDeleteBillOnlineRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterDeleteBillOnlineRequest footer) {
        this.footer = footer;
    }
}