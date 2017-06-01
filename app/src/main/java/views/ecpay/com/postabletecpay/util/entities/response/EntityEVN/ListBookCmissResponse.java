package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 5/30/17.
 */

public class ListBookCmissResponse {

    @SerializedName("bookCmis")
    @Expose
    private String bookCmis;
    @SerializedName("pcCodeExt")
    @Expose
    private String pcCodeExt;

    public ListBookCmissResponse(String bookCmis, String pcCodeExt) {
        this.bookCmis = bookCmis;
        this.pcCodeExt = pcCodeExt;
    }

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public String getPcCodeExt() {
        return pcCodeExt;
    }

    public void setPcCodeExt(String pcCodeExt) {
        this.pcCodeExt = pcCodeExt;
    }
}
