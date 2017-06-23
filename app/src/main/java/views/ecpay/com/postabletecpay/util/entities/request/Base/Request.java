package views.ecpay.com.postabletecpay.util.entities.request.Base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer.FooterCashTranferRequest;

/**
 * Created by duydatpham on 6/17/17.
 */

public abstract class Request implements Serializable {
    public abstract HeaderRequest getHeader();

    public abstract void setHeader(HeaderRequest header);

    public abstract BodyRequest getBody();

    public abstract void setBody(BodyRequest body);

    public abstract FooterRequest getFooter();

    public abstract void setFooter(FooterRequest footer);
}
