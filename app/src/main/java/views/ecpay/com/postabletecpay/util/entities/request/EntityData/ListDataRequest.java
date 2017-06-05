package views.ecpay.com.postabletecpay.util.entities.request.EntityData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class ListDataRequest {
    @SerializedName("header")
    @Expose
    private HeaderListDataRequest headerListDataRequest;
    @SerializedName("body")
    @Expose
    private BodyListDataRequest bodyListDataRequest;
    @SerializedName("footer")
    @Expose
    private FooterListDataRequest footerListDataRequest;

    public HeaderListDataRequest getHeaderListDataRequest() {
        return headerListDataRequest;
    }

    public void setHeaderListDataRequest(HeaderListDataRequest headerListDataRequest) {
        this.headerListDataRequest = headerListDataRequest;
    }

    public BodyListDataRequest getBodyListDataRequest() {
        return bodyListDataRequest;
    }

    public void setBodyListDataRequest(BodyListDataRequest bodyListDataRequest) {
        this.bodyListDataRequest = bodyListDataRequest;
    }

    public FooterListDataRequest getFooterListDataRequest() {
        return footerListDataRequest;
    }

    public void setFooterListDataRequest(FooterListDataRequest footerListDataRequest) {
        this.footerListDataRequest = footerListDataRequest;
    }
}
