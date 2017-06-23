package views.ecpay.com.postabletecpay.util.entities.request.GetPCInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by MyPC on 21/06/2017.
 */

public class BodyGetPCInfoRequest extends BodyRequest {

    @SerializedName("pc-name")
    @Expose
    private String pcName = "";
    @SerializedName("pc-code")
    @Expose
    private String pcCode = "";
    @SerializedName("pc-tax")
    @Expose
    private String pcTax = "";
    @SerializedName("pc-address")
    @Expose
    private String pcAddress = "";
    @SerializedName("pc-phone-number")
    @Expose
    private String pcPhoneNumber;

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getPcTax() {
        return pcTax;
    }

    public void setPcTax(String pcTax) {
        this.pcTax = pcTax;
    }

    public String getPcAddress() {
        return pcAddress;
    }

    public void setPcAddress(String pcAddress) {
        this.pcAddress = pcAddress;
    }

    public String getPcPhoneNumber() {
        return pcPhoneNumber;
    }

    public void setPcPhoneNumber(String pcPhoneNumber) {
        this.pcPhoneNumber = pcPhoneNumber;
    }
}
