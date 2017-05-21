package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLoginResponse {

@SerializedName("account")
@Expose
private AccountLoginResponse accountLoginResponse;
@SerializedName("listEvnPC")
@Expose
private List<ListEvnPCLoginResponse> listEvnPCLoginResponse = null;
@SerializedName("reponseCode")
@Expose
private String reponseCode;
@SerializedName("description")
@Expose
private String description;
@SerializedName("response")
@Expose
private String response;

public AccountLoginResponse getAccountLoginResponse() {
return accountLoginResponse;
}

public void setAccountLoginResponse(AccountLoginResponse accountLoginResponse) {
this.accountLoginResponse = accountLoginResponse;
}

public List<ListEvnPCLoginResponse> getListEvnPCLoginResponse() {
return listEvnPCLoginResponse;
}

public void setListEvnPCLoginResponse(List<ListEvnPCLoginResponse> listEvnPCLoginResponse) {
this.listEvnPCLoginResponse = listEvnPCLoginResponse;
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