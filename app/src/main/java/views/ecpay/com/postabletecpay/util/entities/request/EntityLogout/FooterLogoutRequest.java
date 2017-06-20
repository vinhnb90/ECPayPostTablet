package views.ecpay.com.postabletecpay.util.entities.request.EntityLogout;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterLogoutRequest implements Serializable
{

@SerializedName("account-idt")
@Expose
private String accountIdt;
private final static long serialVersionUID = 4654096509266101364L;

public String getAccountIdt() {
return accountIdt;
}

public void setAccountIdt(String accountIdt) {
this.accountIdt = accountIdt;
}

}