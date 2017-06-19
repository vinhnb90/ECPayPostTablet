package views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class PostBillResponse {
    @SerializedName("header")
    @Expose
    private HeaderPostBillReponse headerPostBillReponse;
    @SerializedName("body")
    @Expose
    private BodyPostBillReponse bodyPostBillReponse;
    @SerializedName("footer")
    @Expose
    private FooterPostBillReponse footerPostBillReponse;

    public HeaderPostBillReponse getHeaderPostBillReponse() {
        return headerPostBillReponse;
    }

    public void setHeaderPostBillReponse(HeaderPostBillReponse headerPostBillReponse) {
        this.headerPostBillReponse = headerPostBillReponse;
    }

    public BodyPostBillReponse getBodyPostBillReponse() {
        return bodyPostBillReponse;
    }

    public void setBodyPostBillReponse(BodyPostBillReponse bodyPostBillReponse) {
        this.bodyPostBillReponse = bodyPostBillReponse;
    }

    public FooterPostBillReponse getFooterPostBillReponse() {
        return footerPostBillReponse;
    }

    public void setFooterPostBillReponse(FooterPostBillReponse footerPostBillReponse) {
        this.footerPostBillReponse = footerPostBillReponse;
    }
}
