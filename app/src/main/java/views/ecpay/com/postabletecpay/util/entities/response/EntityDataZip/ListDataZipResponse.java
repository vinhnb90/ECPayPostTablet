package views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class ListDataZipResponse {
    @SerializedName("header")
    @Expose
    private HeaderListDataZipResponse headerListDataZipResponse;
    @SerializedName("body")
    @Expose
    private BodyListDataZipResponse bodyListDataZipResponse;
    @SerializedName("footer")
    @Expose
    private FooterListDataZipResponse footerListDataZipResponse;

    public HeaderListDataZipResponse getHeaderListDataZipResponse() {
        return headerListDataZipResponse;
    }

    public void setHeaderListDataZipResponse(HeaderListDataZipResponse headerListDataZipResponse) {
        this.headerListDataZipResponse = headerListDataZipResponse;
    }

    public BodyListDataZipResponse getBodyListDataResponse() {
        return bodyListDataZipResponse;
    }

    public void setBodyListDataResponse(BodyListDataZipResponse bodyListDataZipResponse) {
        this.bodyListDataZipResponse = bodyListDataZipResponse;
    }

    public FooterListDataZipResponse getFooterListDataZipResponse() {
        return footerListDataZipResponse;
    }

    public void setFooterListDataZipResponse(FooterListDataZipResponse footerListDataZipResponse) {
        this.footerListDataZipResponse = footerListDataZipResponse;
    }
}
