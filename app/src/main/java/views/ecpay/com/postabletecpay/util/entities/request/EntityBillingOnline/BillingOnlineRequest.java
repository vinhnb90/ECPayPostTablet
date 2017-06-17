package views.ecpay.com.postabletecpay.util.entities.request.EntityBillingOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingOnlineRequest implements Serializable
{

@SerializedName("header")
@Expose
private HeaderBillingOnlineRequest headerBillingOnlineRequest;
@SerializedName("body")
@Expose
private BodyBillingOnlineRequest bodyBillingOnlineRequest;
@SerializedName("footer")
@Expose
private FooterBillingOnlineRequest footerBillingOnlineRequest;
private final static long serialVersionUID = -6765800300080082794L;

public HeaderBillingOnlineRequest getHeaderBillingOnlineRequest() {
return headerBillingOnlineRequest;
}

public void setHeaderBillingOnlineRequest(HeaderBillingOnlineRequest headerBillingOnlineRequest) {
this.headerBillingOnlineRequest = headerBillingOnlineRequest;
}

public BodyBillingOnlineRequest getBodyBillingOnlineRequest() {
return bodyBillingOnlineRequest;
}

public void setBodyBillingOnlineRequest(BodyBillingOnlineRequest bodyBillingOnlineRequest) {
this.bodyBillingOnlineRequest = bodyBillingOnlineRequest;
}

public FooterBillingOnlineRequest getFooterBillingOnlineRequest() {
return footerBillingOnlineRequest;
}

public void setFooterBillingOnlineRequest(FooterBillingOnlineRequest footerBillingOnlineRequest) {
this.footerBillingOnlineRequest = footerBillingOnlineRequest;
}

}