package views.ecpay.com.postabletecpay.util.entities.response.GetPCInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;

/**
 * Created by MyPC on 21/06/2017.
 */

public class BodyGetPCInfoRespone extends BodyRespone {


    public List<ListEvnPCLoginResponse> getListEvnPCLoginResponse() {
        return listEvnPCLoginResponse;
    }

    public void setListEvnPCLoginResponse(List<ListEvnPCLoginResponse> listEvnPCLoginResponse) {
        this.listEvnPCLoginResponse = listEvnPCLoginResponse;
    }

    @SerializedName("listEvnPC")
    @Expose
    private List<ListEvnPCLoginResponse> listEvnPCLoginResponse = null;

}
