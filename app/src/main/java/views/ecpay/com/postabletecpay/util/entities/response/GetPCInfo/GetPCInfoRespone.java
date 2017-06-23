package views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;

/**
 * Created by MyPC on 21/06/2017.
 */

public class GetPCInfoRespone extends Respone {

    @SerializedName("header")
    @Expose
    private HeaderGetPCInfoRespone header;
    @SerializedName("body")
    @Expose
    private BodyGetPCInfoRespone body;
    @SerializedName("footer")
    @Expose
    private FooterGetPCInfoRespone footer;


    @Override
    public HeaderRespone getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderRespone header) {
        this.header = (HeaderGetPCInfoRespone) header;
    }

    @Override
    public BodyRespone getBody() {
        return body;
    }

    @Override
    public void setBody(BodyRespone body) {
        this.body = (BodyGetPCInfoRespone)body;
    }

    @Override
    public FooterRespone getFooter() {
        return this.footer;
    }

    @Override
    public void setFooter(FooterRespone footer) {
        this.footer = (FooterGetPCInfoRespone)footer;
    }
}
