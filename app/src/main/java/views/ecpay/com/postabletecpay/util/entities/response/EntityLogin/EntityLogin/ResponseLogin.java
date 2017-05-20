package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

@SerializedName("account")
@Expose
private AccountLogin accountLogin;
@SerializedName("listEvnPC")
@Expose
private List<ListEvnPCLogin> listEvnPCLogin = null;
@SerializedName("reponseCode")
@Expose
private String reponseCode;
@SerializedName("description")
@Expose
private String description;
@SerializedName("response")
@Expose
private String response;

public AccountLogin getAccountLogin() {
return accountLogin;
}

public void setAccountLogin(AccountLogin accountLogin) {
this.accountLogin = accountLogin;
}

public List<ListEvnPCLogin> getListEvnPCLogin() {
return listEvnPCLogin;
}

public void setListEvnPCLogin(List<ListEvnPCLogin> listEvnPCLogin) {
this.listEvnPCLogin = listEvnPCLogin;
}

public String getReponseCode() {
return reponseCode;
}

public void setReponseCode(String reponseCode) {
this.reponseCode = reponseCode;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getResponse() {
return response;
}

public void setResponse(String response) {
this.response = response;
}

}