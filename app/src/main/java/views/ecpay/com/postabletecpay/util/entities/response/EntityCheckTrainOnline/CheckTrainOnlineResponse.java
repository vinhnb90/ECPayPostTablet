package views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTrainOnlineResponse implements Serializable {

    @SerializedName("header")
    @Expose
    private HeaderCheckTrainOnlineResponse header;
    @SerializedName("body")
    @Expose
    private BodyCheckTrainOnlineResponse body;
    @SerializedName("footer")
    @Expose
    private FooterCheckTrainOnlineResponse footer;
    private final static long serialVersionUID = 6743274675011955698L;

    public HeaderCheckTrainOnlineResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderCheckTrainOnlineResponse header) {
        this.header = header;
    }

    public BodyCheckTrainOnlineResponse getBody() {
        return body;
    }

    public void setBody(BodyCheckTrainOnlineResponse body) {
        this.body = body;
    }

    public FooterCheckTrainOnlineResponse getFooter() {
        return footer;
    }

    public void setFooter(FooterCheckTrainOnlineResponse footer) {
        this.footer = footer;
    }
}