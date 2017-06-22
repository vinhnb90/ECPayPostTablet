package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by MyPC on 22/06/2017.
 */

public class BodySearchCustomerBillRespone extends BodyRespone {
    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("customer")
    @Expose
    private String customer;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public class CustomerObject
    {
        @SerializedName("code")
        @Expose
        private Object code;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("address")
        @Expose
        private Object address;
        @SerializedName("pcCode")
        @Expose
        private Object pcCode;
        @SerializedName("pcCodeExt")
        @Expose
        private Object pcCodeExt;
        @SerializedName("phoneByevn")
        @Expose
        private Object phoneByevn;
        @SerializedName("phoneByecp")
        @Expose
        private Object phoneByecp;
        @SerializedName("bookCmis")
        @Expose
        private Object bookCmis;
        @SerializedName("electricityMeter")
        @Expose
        private Object electricityMeter;
        @SerializedName("inning")
        @Expose
        private Object inning;
        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("bankAccount")
        @Expose
        private Object bankAccount;
        @SerializedName("road")
        @Expose
        private Object road;
        @SerializedName("bankName")
        @Expose
        private Object bankName;
        @SerializedName("type")
        @Expose
        private Object type;
        @SerializedName("idChanged")
        @Expose
        private Object idChanged;
        @SerializedName("dateChanged")
        @Expose
        private Object dateChanged;
        @SerializedName("cardNo")
        @Expose
        private Object cardNo;

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getPcCode() {
            return pcCode;
        }

        public void setPcCode(Object pcCode) {
            this.pcCode = pcCode;
        }

        public Object getPcCodeExt() {
            return pcCodeExt;
        }

        public void setPcCodeExt(Object pcCodeExt) {
            this.pcCodeExt = pcCodeExt;
        }

        public Object getPhoneByevn() {
            return phoneByevn;
        }

        public void setPhoneByevn(Object phoneByevn) {
            this.phoneByevn = phoneByevn;
        }

        public Object getPhoneByecp() {
            return phoneByecp;
        }

        public void setPhoneByecp(Object phoneByecp) {
            this.phoneByecp = phoneByecp;
        }

        public Object getBookCmis() {
            return bookCmis;
        }

        public void setBookCmis(Object bookCmis) {
            this.bookCmis = bookCmis;
        }

        public Object getElectricityMeter() {
            return electricityMeter;
        }

        public void setElectricityMeter(Object electricityMeter) {
            this.electricityMeter = electricityMeter;
        }

        public Object getInning() {
            return inning;
        }

        public void setInning(Object inning) {
            this.inning = inning;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(Object bankAccount) {
            this.bankAccount = bankAccount;
        }

        public Object getRoad() {
            return road;
        }

        public void setRoad(Object road) {
            this.road = road;
        }

        public Object getBankName() {
            return bankName;
        }

        public void setBankName(Object bankName) {
            this.bankName = bankName;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public Object getIdChanged() {
            return idChanged;
        }

        public void setIdChanged(Object idChanged) {
            this.idChanged = idChanged;
        }

        public Object getDateChanged() {
            return dateChanged;
        }

        public void setDateChanged(Object dateChanged) {
            this.dateChanged = dateChanged;
        }

        public Object getCardNo() {
            return cardNo;
        }

        public void setCardNo(Object cardNo) {
            this.cardNo = cardNo;
        }
    }

}
