package views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class ListDataZipRequest {
    @SerializedName("header")
    @Expose
    private HeaderListDataZipRequest headerListDataZipRequest;
    @SerializedName("body")
    @Expose
    private BodyListDataZipRequest bodyListDataZipRequest;
    @SerializedName("footer")
    @Expose
    private FooterListDataZipRequest footerListDataZipRequest;

    public HeaderListDataZipRequest getHeaderListDataZipRequest() {
        return headerListDataZipRequest;
    }

    public void setHeaderListDataZipRequest(HeaderListDataZipRequest headerListDataZipRequest) {
        this.headerListDataZipRequest = headerListDataZipRequest;
    }

    public BodyListDataZipRequest getBodyListDataZipRequest() {
        return bodyListDataZipRequest;
    }

    public void setBodyListDataZipRequest(BodyListDataZipRequest bodyListDataZipRequest) {
        this.bodyListDataZipRequest = bodyListDataZipRequest;
    }

    public FooterListDataZipRequest getFooterListDataZipRequest() {
        return footerListDataZipRequest;
    }

    public void setFooterListDataZipRequest(FooterListDataZipRequest footerListDataZipRequest) {
        this.footerListDataZipRequest = footerListDataZipRequest;
    }
}
