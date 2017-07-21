package views.ecpay.com.postabletecpay.Config;

/**
 * Created by MyPC on 21/07/2017.
 */

public class Config {
    private  static  boolean RELEASE = false;


    private  static boolean SHOW_RESPONE = true;

    public static boolean isShowRespone() {
        if(RELEASE)
            return  false;
        return SHOW_RESPONE;
    }
}
