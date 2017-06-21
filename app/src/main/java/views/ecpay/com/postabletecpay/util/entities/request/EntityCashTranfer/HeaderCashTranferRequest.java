package views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duydatpham on 6/17/17.
 */

public class HeaderCashTranferRequest  implements Serializable
{
    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("command-id")
    @Expose
    private String commandId;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }
}
