package views.ecpay.com.postabletecpay.util.entities.response.EntityBalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

/**
 * Created by duydatpham on 7/12/17.
 */

public class BodyBalanceRespone extends BodyRespone {

    @SerializedName("sum-off")
    @Expose
    private Long sumOff;


    @SerializedName("response-request-balance")
    @Expose
    private String responseRequestBalance;


    public Long getSumOff() {
        return sumOff;
    }

    public void setSumOff(Long sumOff) {
        this.sumOff = sumOff;
    }

    public String getResponseRequestBalance() {
        return responseRequestBalance;
    }

    public void setResponseRequestBalance(String responseRequestBalance) {
        this.responseRequestBalance = responseRequestBalance;
    }

    public class ResponseRequestBalance
    {
        @SerializedName("result")
        @Expose
        private boolean result;
        @SerializedName("responseCode")
        @Expose
        private String responseCode;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("balance")
        @Expose
        private Long balance;
        @SerializedName("eDong")
        @Expose
        private String eDong;
        @SerializedName("lockMoney")
        @Expose
        private Long lockMoney;


        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getBalance() {
            return balance;
        }

        public void setBalance(Long balance) {
            this.balance = balance;
        }

        public String geteDong() {
            return eDong;
        }

        public void seteDong(String eDong) {
            this.eDong = eDong;
        }

        public Long getLockMoney() {
            return lockMoney;
        }

        public void setLockMoney(Long lockMoney) {
            this.lockMoney = lockMoney;
        }
    }
}
