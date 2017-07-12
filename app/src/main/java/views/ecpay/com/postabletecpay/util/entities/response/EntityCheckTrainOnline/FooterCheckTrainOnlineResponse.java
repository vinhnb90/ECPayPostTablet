package views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.FooterRespone;

public class FooterCheckTrainOnlineResponse extends FooterRespone
{

@SerializedName("source-address")
@Expose
private String sourceAddress;
public String getSourceAddress() {
return sourceAddress;
}

public void setSourceAddress(String sourceAddress) {
this.sourceAddress = sourceAddress;
}

}