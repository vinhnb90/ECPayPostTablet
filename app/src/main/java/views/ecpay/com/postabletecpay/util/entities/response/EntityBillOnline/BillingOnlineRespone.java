package views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingOnlineRespone implements Serializable
{
@SerializedName("header")
@Expose
private HeaderBillingOnlineRespone headerBillingOnlineRespone;
@SerializedName("body")
@Expose
private BodyBillingOnlineRespone bodyBillingOnlineRespone;
@SerializedName("footer")
@Expose
private FooterBillingOnlineRespone footerBillingOnlineRespone;
private final static long serialVersionUID = 1637014148722407909L;

public HeaderBillingOnlineRespone getHeaderBillingOnlineRespone() {
return headerBillingOnlineRespone;
}

public void setHeaderBillingOnlineRespone(HeaderBillingOnlineRespone headerBillingOnlineRespone) {
this.headerBillingOnlineRespone = headerBillingOnlineRespone;
}

public BodyBillingOnlineRespone getBodyBillingOnlineRespone() {
return bodyBillingOnlineRespone;
}

public void setBodyBillingOnlineRespone(BodyBillingOnlineRespone bodyBillingOnlineRespone) {
this.bodyBillingOnlineRespone = bodyBillingOnlineRespone;
}

public FooterBillingOnlineRespone getFooterBillingOnlineRespone() {
return footerBillingOnlineRespone;
}

public void setFooterBillingOnlineRespone(FooterBillingOnlineRespone footerBillingOnlineRespone) {
this.footerBillingOnlineRespone = footerBillingOnlineRespone;
}

}