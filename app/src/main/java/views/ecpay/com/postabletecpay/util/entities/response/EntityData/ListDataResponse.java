package views.ecpay.com.postabletecpay.util.entities.response.EntityData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.EntityData.BodyListDataRequest;

/**
 * Created by macbook on 6/1/17.
 */

public class ListDataResponse {
    @SerializedName("header")
    @Expose
    private HeaderListDataResponse headerListDataResponse;
    @SerializedName("body")
    @Expose
    private BodyListDataResponse bodyListDataResponse;
    @SerializedName("footer")
    @Expose
    private FooterListDataResponse footerListDataResponse;

    public HeaderListDataResponse getHeaderListDataResponse() {
        return headerListDataResponse;
    }

    public void setHeaderListDataResponse(HeaderListDataResponse headerListDataResponse) {
        this.headerListDataResponse = headerListDataResponse;
    }

    public BodyListDataResponse getBodyListDataResponse() {
        return bodyListDataResponse;
    }

    public void setBodyListDataResponse(BodyListDataResponse bodyListDataResponse) {
        this.bodyListDataResponse = bodyListDataResponse;
    }

    public FooterListDataResponse getFooterListDataResponse() {
        return footerListDataResponse;
    }

    public void setFooterListDataResponse(FooterListDataResponse footerListDataResponse) {
        this.footerListDataResponse = footerListDataResponse;
    }
}
