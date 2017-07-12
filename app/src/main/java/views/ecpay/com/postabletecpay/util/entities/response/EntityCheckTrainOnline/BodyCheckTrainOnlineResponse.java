package views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

public class BodyCheckTrainOnlineResponse extends BodyRespone
{

@SerializedName("edong")
@Expose
private String edong;
@SerializedName("customer-code")
@Expose
private String customerCode;
@SerializedName("bill-id")
@Expose
private Long billId;
@SerializedName("amount")
@Expose
private Long amount;
@SerializedName("request-date")
@Expose
private String requestDate;

public String getEdong() {
return edong;
}

public void setEdong(String edong) {
this.edong = edong;
}

public String getCustomerCode() {
return customerCode;
}

public void setCustomerCode(String customerCode) {
this.customerCode = customerCode;
}

public Long getBillId() {
return billId;
}

public void setBillId(Long billId) {
this.billId = billId;
}

public Long getAmount() {
return amount;
}

public void setAmount(Long amount) {
this.amount = amount;
}

public String getRequestDate() {
return requestDate;
}

public void setRequestDate(String requestDate) {
this.requestDate = requestDate;
}

}