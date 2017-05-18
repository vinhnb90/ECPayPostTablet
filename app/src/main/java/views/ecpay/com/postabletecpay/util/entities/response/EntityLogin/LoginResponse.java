package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

@SerializedName("header")
@Expose
private Header header;
@SerializedName("body")
@Expose
private Body body;
@SerializedName("footer")
@Expose
private Footer footer;

public Header getHeader() {
return header;
}

public void setHeader(Header header) {
this.header = header;
}

public Body getBody() {
return body;
}

public void setBody(Body body) {
this.body = body;
}

public Footer getFooter() {
return footer;
}

public void setFooter(Footer footer) {
this.footer = footer;
}

}