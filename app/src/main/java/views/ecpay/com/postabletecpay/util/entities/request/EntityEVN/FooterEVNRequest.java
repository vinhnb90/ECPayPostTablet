package views.ecpay.com.postabletecpay.util.entities.request.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterEVNRequest {

@SerializedName("account-idt")
@Expose
private String accountIdt;

public String getAccountIdt() {
return accountIdt;
}

public void setAccountIdt(String accountIdt) {
this.accountIdt = accountIdt;
}

}