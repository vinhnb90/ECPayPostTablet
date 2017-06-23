package views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.FooterRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.HeaderRequest;
import views.ecpay.com.postabletecpay.util.entities.request.Base.Request;
import views.ecpay.com.postabletecpay.util.entities.response.Base.HeaderRespone;

/**
 * Created by MyPC on 21/06/2017.
 */

public class GetPCInfoRequest extends Request {

    @SerializedName("header")
    @Expose
    private HeaderGetPCInfoRequest header;
    @SerializedName("body")
    @Expose
    private BodyGetPCInfoRequest body;
    @SerializedName("footer")
    @Expose
    private FooterGetPCInfoRequest footer;


    @Override
    public HeaderRequest getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderRequest _header) {
        this.header = (HeaderGetPCInfoRequest)_header;
    }

    @Override
    public BodyRequest getBody() {
        return body;
    }

    @Override
    public void setBody(BodyRequest _body) {
        this.body = (BodyGetPCInfoRequest) _body;
    }

    @Override
    public FooterRequest getFooter() {
        return footer;
    }

    @Override
    public void setFooter(FooterRequest _footer) {
        this.footer = (FooterGetPCInfoRequest)_footer;
    }
}
