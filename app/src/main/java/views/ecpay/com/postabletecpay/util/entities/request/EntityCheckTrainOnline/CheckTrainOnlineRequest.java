package views.ecpay.com.postabletecpay.util.entities.request.EntityCheckTrainOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTrainOnlineRequest implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderCheckTrainOnlineRequest header;
    @SerializedName("body")
    @Expose
    private BodyCheckTrainOnlineRequest body;
    @SerializedName("footer")
    @Expose
    private FooterCheckTrainOnlineRequest footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderCheckTrainOnlineRequest getHeader() {
        return header;
    }

    public void setHeader(HeaderCheckTrainOnlineRequest header) {
        this.header = header;
    }

    public BodyCheckTrainOnlineRequest getBody() {
        return body;
    }

    public void setBody(BodyCheckTrainOnlineRequest body) {
        this.body = body;
    }

    public FooterCheckTrainOnlineRequest getFooter() {
        return footer;
    }

    public void setFooter(FooterCheckTrainOnlineRequest footer) {
        this.footer = footer;
    }
}