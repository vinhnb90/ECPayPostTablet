package views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

/**
 * Created by tima on 6/19/17.
 */

public class PostBillResponse extends Respone {
    @SerializedName("header")
    @Expose
    private HeaderRespone headerPostBillReponse;
    @SerializedName("body")
    @Expose
    private BodyPostBillReponse bodyPostBillReponse;
    @SerializedName("footer")
    @Expose
    private FooterRespone footerPostBillReponse;


    @Override
    public HeaderRespone getHeader() {
        return this.headerPostBillReponse;
    }

    @Override
    public void setHeader(HeaderRespone header) {
        this.headerPostBillReponse = header;
    }

    @Override
    public BodyRespone getBody() {
        return this.bodyPostBillReponse;
    }

    @Override
    public void setBody(BodyRespone body) {
        this.bodyPostBillReponse = (BodyPostBillReponse)body;
    }

    @Override
    public FooterRespone getFooter() {
        return this.footerPostBillReponse;
    }

    @Override
    public void setFooter(FooterRespone footer) {
        this.footerPostBillReponse = footer;
    }
}
