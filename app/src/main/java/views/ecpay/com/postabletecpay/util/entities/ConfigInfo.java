package views.ecpay.com.postabletecpay.util.entities;

/**
 * Created by VinhNB on 5/12/2017.
 */

public class ConfigInfo {
    private String PUBLIC_KEY;
    private String PRIVATE_KEY;
    private String AGENT;
    private String PASS_WORD;
    private String PC_CODE;

    public ConfigInfo() {
        PRIVATE_KEY = "";
        PUBLIC_KEY = "";
        AGENT = "";
        PASS_WORD = "";
        PC_CODE = "";
    }

    public ConfigInfo(String PRIVATE_KEY, String PUBLIC_KEY, String AGENT, String PC_CODE) {
        this.PRIVATE_KEY = PRIVATE_KEY;
        this.PUBLIC_KEY = PUBLIC_KEY;
        this.AGENT = AGENT;
        this.PASS_WORD = PASS_WORD;
        this.PC_CODE = PC_CODE;
    }

    public String getPUBLIC_KEY() {
        return PUBLIC_KEY;
    }

    public void setPUBLIC_KEY(String PUBLIC_KEY) {
        this.PUBLIC_KEY = PUBLIC_KEY;
    }

    public String getPRIVATE_KEY() {
        return PRIVATE_KEY;
    }

    public void setPRIVATE_KEY(String PRIVATE_KEY) {
        this.PRIVATE_KEY = PRIVATE_KEY;
    }

    public String getAGENT() {
        return AGENT;
    }

    public void setAGENT(String AGENT) {
        this.AGENT = AGENT;
    }

    public String getPASS_WORD() {
        return PASS_WORD;
    }

    public void setPASS_WORD(String PASS_WORD) {
        this.PASS_WORD = PASS_WORD;
    }

    public String getPC_CODE() {
        return PC_CODE;
    }

    public void setPC_CODE(String PC_CODE) {
        this.PC_CODE = PC_CODE;
    }
}
