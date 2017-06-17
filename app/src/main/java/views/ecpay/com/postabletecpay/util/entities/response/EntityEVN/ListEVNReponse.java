package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListEVNReponse {

@SerializedName("header")
@Expose
private HeaderListEVNResponse headerListEVNResponse;
@SerializedName("body")
@Expose
private BodyListEVNResponse bodyListEVNResponse;
@SerializedName("footer")
@Expose
private FooterListEVNResponse footerListEVNResponse;

    public HeaderListEVNResponse getHeaderListEVNResponse() {
        return headerListEVNResponse;
    }

    public void setHeaderListEVNResponse(HeaderListEVNResponse headerListEVNResponse) {
        this.headerListEVNResponse = headerListEVNResponse;
    }

    public BodyListEVNResponse getBodyListEVNResponse() {
        return bodyListEVNResponse;
    }

    public void setBodyListEVNResponse(BodyListEVNResponse bodyListEVNResponse) {
        this.bodyListEVNResponse = bodyListEVNResponse;
    }

    public FooterListEVNResponse getFooterListEVNResponse() {
        return footerListEVNResponse;
    }

    public void setFooterListEVNResponse(FooterListEVNResponse footerListEVNResponse) {
        this.footerListEVNResponse = footerListEVNResponse;
    }
}