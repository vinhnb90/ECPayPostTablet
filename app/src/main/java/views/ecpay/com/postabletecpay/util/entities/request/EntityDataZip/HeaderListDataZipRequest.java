package views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class HeaderListDataZipRequest {
    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("command-id")
    @Expose
    private String commandID;

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

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }
}
