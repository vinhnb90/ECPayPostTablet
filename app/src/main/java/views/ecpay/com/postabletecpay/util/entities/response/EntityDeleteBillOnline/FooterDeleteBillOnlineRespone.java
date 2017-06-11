package views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterDeleteBillOnlineRespone implements Serializable
{

@SerializedName("response-code")
@Expose
private String responseCode;
@SerializedName("description")
@Expose
private String description;
@SerializedName("account-idt")
@Expose
private String accountIdt;
private final static long serialVersionUID = -5237039005785593727L;

public String getResponseCode() {
return responseCode;
}

public void setResponseCode(String responseCode) {
this.responseCode = responseCode;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getAccountIdt() {
return accountIdt;
}

public void setAccountIdt(String accountIdt) {
this.accountIdt = accountIdt;
}

}