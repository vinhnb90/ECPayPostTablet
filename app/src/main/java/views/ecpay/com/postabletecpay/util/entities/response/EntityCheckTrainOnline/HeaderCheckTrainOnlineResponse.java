package views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeaderCheckTrainOnlineResponse implements Serializable
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
private final static long serialVersionUID = -2949421951748145887L;

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