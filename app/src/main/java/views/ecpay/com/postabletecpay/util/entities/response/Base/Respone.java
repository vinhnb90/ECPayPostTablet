package views.ecpay.com.postabletecpay.util.entities.response.Base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by duydatpham on 6/17/17.
 */

public abstract class Respone {
    public abstract HeaderRespone getHeader();

    public abstract void setHeader(HeaderRespone header);

    public abstract BodyRespone getBody();

    public abstract void setBody(BodyRespone body);

    public abstract FooterRespone getFooter();

    public abstract void setFooter(FooterRespone footer);
}
