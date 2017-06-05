package views.ecpay.com.postabletecpay.util.entities.response.EntityData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class BodyListDataResponse {
    @SerializedName("audit-number")
    @Expose
    private Long auditNumber;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("disk-drive")
    @Expose
    private String diskDrive;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("pin-login")
    @Expose
    private String pinLogin;
    @SerializedName("version-app")
    @Expose
    private String versionApp;
}
