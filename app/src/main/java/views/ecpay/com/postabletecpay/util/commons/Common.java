package views.ecpay.com.postabletecpay.util.commons;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.ecpay.client.Partner;
import org.ecpay.client.Utils;
import org.ecpay.client.jce.Crypto;
import org.ecpay.client.jce.TripleDesCBC;
import org.ecpay.client.test.SecurityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.AlgorithmRSA.AsymmetricCryptography;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesNoDialog;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;

/**
 * Created by macbook on 5/5/17.
 */

public class Common {

    //region param account
    public enum TYPE_ACCOUNT {
        ADMIN_IT(1, "Admin IT"),
        ADMIN_KD(0, "Admin Kinh Doanh"),
        TNV_THUONG(0, "Thu nhân viên thường"),
        TNV_DA_NANG(0, "Thu nhân viên đa năng"),
        NV_QLY(0, "Nhân viên quản lý");

        private final int type;
        private String typeString;

        TYPE_ACCOUNT(int type, String typeString) {
            this.type = type;
            this.typeString = typeString;
        }

        public int getType() {
            return type;
        }

        public String getTypeString() {
            return typeString;
        }

        public static TYPE_ACCOUNT findCodeMessage(int type) {
            for (TYPE_ACCOUNT v : values()) {
                if (v.getType() == type) {
                    return v;
                }
            }
            return null;
        }
    }
    //endregion

    //region param search
    //condification search online
    public static final int LENGTH_MIN = 13;
    public static final String SYMBOL_FIRST = "P";
    public static final int LENGTH_MAX = 16;

    public static final String DIRECT_EVN = "0";

    public static enum TYPE_SEARCH {
        ALL(0, "Tất cả"),
        MA_KH_SO_THE(1, "Mã KH/Số thẻ"),
        TEN_KH(2, "Tên KH"),
        PHONE(3, "Số điện thoại"),
        ADDRESS(4, "Địa chỉ"),
        GCS(5, "Sổ Ghi chỉ số"),
        LO_TRINH(6, "Lộ trình");

        private int position;
        private String type;

        TYPE_SEARCH(int position, String type) {
            this.position = position;
            this.type = type;
        }

        public int getPosition() {
            return position;
        }

        public String getType() {
            return type;
        }

        public static TYPE_SEARCH findMessage(int position) {
            for (TYPE_SEARCH v : values()) {
                if (v.getPosition() == position) {
                    return v;
                }
            }
            return null;
        }

        public static TYPE_SEARCH findCode(String message) {
            for (TYPE_SEARCH v : values()) {
                if (v.getType().equals(message)) {
                    return v;
                }
            }
            return null;
        }

    }
    //endregion

    //region parrams billing
    public enum PARTNER_CODE {
        DT0813("DT0813", "Luồng quầy thu đang năng"),
        DT0807("DT0807", "Luồng tài khoản tiền điện"),
        DT0605("DT0605", "Các trường hợp khác");

        PARTNER_CODE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static PARTNER_CODE findCodeMessage(String code) {
            for (PARTNER_CODE v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return PARTNER_CODE.DT0605;
        }
    }

    public enum PROVIDER_CODE {
        NCC0498("NCC0498", "Tổng Công Ty Điện Lực Miền Nam"),
        NCC0499("NCC0499", "Tổng Công Ty Điện Lực Miền Trung"),
        NCC0483("NCC0483", "Tổng Công ty điện lực miền Bắc"),
        NCC0466("NCC0466", "Tổng Công Ty Điện Lực Hồ Chí Minh"),
        NCC0468("NCC0468", "Tổng Công Ty Điện Lực Hà Nội"),
        NCC0500("NCC0500", "Công ty điện lực Hải Phòng"),
        NCC0502("NCC0502", "Công ty điện lực Khánh Hòa"),
        NCC0501("NCC0501", "Tổng Công Ty Truyền Hình Cáp Việt Nam"),
        NCC0469("NCC0469", "Tổng Công Ty Viễn Thông Quân Đội Viettel"),
        NCC0470("NCC0470", "Công Ty Thông Tin Di Động MobiFone"),
        NCC0471("NCC0471", "Công Ty Dịch Vụ Viễn Thông VinaPhone");

        PROVIDER_CODE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static PROVIDER_CODE findCodeMessage(String code) {
            for (PROVIDER_CODE v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return null;
        }
    }

    public static final String PARTNER_CODE_DEFAULT = PARTNER_CODE.DT0605.getCode();
    public static final String PROVIDER_DEFAULT = PROVIDER_CODE.NCC0468.getCode();

    public enum STATUS_BILLING {
        CHUA_THANH_TOAN(0, "Chưa thanh toán"),
        DA_THANH_TOAN(1, "Đã thanh toán"),
        HUY_HOA_DON(2, "Đã bị hủy hóa đơn");

        STATUS_BILLING(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final int code;
        private String message;

        public static STATUS_BILLING findCodeMessage(int code) {
            for (STATUS_BILLING v : values()) {
                if (v.getCode() == code) {
                    return v;
                }
            }
            return null;
        }
    }
    //endregion

    //region Description key
    public static final String TAG = "TAG";
    public static final String KEY_EDONG = "EDONG";
    //endregion

    //region info error message android
    public enum MESSAGE_NOTIFY {
        ERR_WIFI,
        ERR_NETWORK,

        LOGIN_ERR_USER,
        LOGIN_ERR_PASS,
        LOGIN_ERR_EDONG,

        ERR_CREATE_FOLDER,
        ERR_ENCRYPT_AGENT,
        ERR_ENCRYPT_PASS,

        ERR_CALL_SOAP_EMPTY,
        ERR_CALL_SOAP_TIME_OUT,
        ERR_CALL_SOAP_LOGIN,

        CHANGE_PASS_ERR_PASS_OLD,
        CHANGE_PASS_ERR_PASS_NEW,
        CHANGE_PASS_ERR_PASS_NEW_NOT_EQUAL_PASS_OLD,
        CHANGE_PASS_ERR_PASS_RETYPE,
        CHANGE_PASS_ERR_PASS_RETYPE_NOT_EQUAL_PASS_RETYPE;

        @Override
        public String toString() {
            if (ERR_WIFI == this)
                return "Kiểm tra kết nối wifi!";

            if (ERR_NETWORK == this)
                return "Kiểm tra kết nối internet của wifi!";

            if (LOGIN_ERR_USER == this)
                return "Tên đăng nhập là chữ thường, chữ hoa, các kí tự đặc biệt, tối đa 20 kí tự và không để trống!";

            if (LOGIN_ERR_PASS == this)
                return "Mật khẩu là chữ thường, chữ hoa, các kí tự đặc biệt, tối đa 8 kí tự và không để trống!";

            if (ERR_CREATE_FOLDER == this)
                return "Xảy ra vấn đề khi tạo thư mục chứa tài nguyên trên SDCard!";

            if (ERR_ENCRYPT_AGENT == this)
                return "Xảy ra vấn đề khi tạo dữ liệu mã hóa, kiểm tra lại đầy đủ thông tin file config!";

            if (ERR_ENCRYPT_PASS == this)
                return "Xảy ra vấn đề khi mã hóa dữ liệu!";

            if (LOGIN_ERR_EDONG == this)
                return "Xảy ra vấn đề nhận dữ liệu về không xác định được thông tin ví edong!";

            if (ERR_CALL_SOAP_LOGIN == this)
                return "Xảy ra vấn đề với chức năng đăng nhập khi kết nối tới máy chủ!";

            if (ERR_CALL_SOAP_EMPTY == this)
                return "Không nhận được dữ liệu khi kết nối tới máy chủ!";

            if (ERR_CALL_SOAP_TIME_OUT == this)
                return "Quá thời gian kết nối cho phép tới máy chủ " + TIME_OUT_CONNECT / 1000 + " s";

            if (CHANGE_PASS_ERR_PASS_OLD == this)
                return "Mật khẩu cũ không được để trống, tối đa 8 kí tự!";

            if (CHANGE_PASS_ERR_PASS_NEW == this)
                return "Mật khẩu mới không được để trống, tối đa 8 kí tự!";

            if (CHANGE_PASS_ERR_PASS_RETYPE == this)
                return "Mật khẩu mới nhập lại không được để trống, tối đa 8 kí tự!";

            if (CHANGE_PASS_ERR_PASS_NEW_NOT_EQUAL_PASS_OLD == this)
                return "Mật khẩu mới không trùng với mật khẩu cũ!";

            if (CHANGE_PASS_ERR_PASS_RETYPE_NOT_EQUAL_PASS_RETYPE == this)
                return "Mật khẩu mới nhập lại không chính xác!";

            return super.toString();
        }
    }

    public enum MESSAGE {
        CHANGE_PASS_SUCSSES("Đổi mật khẩu thành công"),;


        MESSAGE(String message) {
            this.message = message;
        }

        private String message;

        public String getMessage() {
            return message;
        }
    }

    public enum CODE_REPONSE_LOGIN {
        e000("000", "Tài khoản ví chưa đăng ký dịch vụ"),
        e034("034", "Không giải mã được mã PIN"),
        e035("035", "Mã PIN không đúng"),
        e031("031", "Tài khoản ví đã bị hủy"),
        e030("030", "Tài khoản ví chưa đăng ký dịch vụ"),
        e032("032", "Tài khoản bị khóa"),
        e033("033", "Tài khoản ví chưa được duyệt"),
        e2038("2038", "Không thể login, sai địa chỉ MAC"),
        e2039("2039", "Không thể login, sai địa chỉ IP"),
        e2040("2040", "Không thể login, đã login ở 1 thiết bị khác"),
        e2042("2042", "Tài khoản chưa được xác định loại tài khoản"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ");

        CODE_REPONSE_LOGIN(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_LOGIN findCodeMessage(String code) {
            for (CODE_REPONSE_LOGIN v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_LOGIN.e9999;
        }
    }

    public enum CODE_REPONSE_CHANGE_PASS {
        e000("000", "Tài khoản ví chưa đăng ký dịch vụ"),
        e031("031", "Tài khoản ví đã bị hủy"),
        e030("030", "Tài khoản ví chưa đăng ký dịch vụ"),
        e032("032", "Tài khoản bị khóa"),
        e033("033", "Tài khoản ví chưa được duyệt"),
        e037("037", "Session không tồn tại"),
        e038("038", "Session không hợp lệ"),
        e039("039", "Session hết thời gian hiệu lực"),
        e999("999", "Có lỗi xảy ra trong code"),
        e024("024", "Mã pin xác nhận không đúng"),
        e034("034", "Không giải mã được pin"),
        e035("035", "Mã PIN không đúng"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ");

        CODE_REPONSE_CHANGE_PASS(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_CHANGE_PASS findCodeMessage(String code) {
            for (CODE_REPONSE_CHANGE_PASS v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_CHANGE_PASS.e9999;
        }
    }

    public enum CODE_REPONSE_SEARCH_ONLINE {
        e000("000", "Khách hàng còn nợ"),
        e2013("2013", "Mã KH không thuộc đơn vị nào đã khai báo trong hệ thống"),
        e0017("0017", "Không tồn tại KH nào thỏa mãn điều kiện tra cứu"),
        e0025("0025", "KH không còn nợ hóa đơn nào tại thời điểm hiện tại."),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ");

        CODE_REPONSE_SEARCH_ONLINE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static String getMessageServerNotify(String nameCustomer, String term, String message)
        {
            return "Gặp vấn đề với hóa đơn của khách hàng " + nameCustomer + " tại kỳ " + term
                    + "\nnhư sau: " + message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_SEARCH_ONLINE findCodeMessage(String code) {
            for (CODE_REPONSE_SEARCH_ONLINE v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_SEARCH_ONLINE.e9999;
        }
    }

    public enum CODE_REPONSE_BILL_ONLINE {
        e000("000", "Thực hiện nghiệp vụ thành công"),
        e040("040", "Tài khoản ví chưa được đăng ký"),
        e041("041", "Tài khoản ví đã bị hủy"),
        e042("042", "Tài khoản ví đã bị khóa"),
        e043("043", "Tài khoản ví chưa được duyệt"),
        e050("050", "Tài khoản bị từ chối dịch vụ"),
        e051("051", "Tài khoản bị chặn dịch vụ"),
        e2032("2032", "Giao dịch đang được hệ thống giữ chấm nợ bởi số ví khác"),
        e2038("2038", "Không thể login, sai địa chỉ MAC"),
        e2039("2039", "Không thể login, sai địa chỉ IP"),
        e2040("2039", "Không thể login, đã login ở 1 thiết bị khác"),
        e2042("2042", "Tài khoản chưa được xác định loại tài khoản"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ"),

        ex10000("10000", "Chưa có hóa đơn nào được chọn"),
        ex10001("10001", "Quá trình thanh toán kết thúc"),
        ex10002("10002", "Vui lòng kiểm tra sự tồn tại của database");

        CODE_REPONSE_BILL_ONLINE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_BILL_ONLINE findCodeMessage(String code) {
            for (CODE_REPONSE_BILL_ONLINE v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_BILL_ONLINE.e9999;
        }
    }

    public enum CODE_REPONSE_DELETE_BILL_ONLINE {
        e000("000", "Thành công"),
        e2006("2006", "Không tìm thấy giao dịch tương ứng"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ"),

        ex10000("10000", "Không tồn tại hóa đơn trong cơ sở dữ liệu"),
        ex10001("10001", "Vui lòng điền lý do hủy");

        CODE_REPONSE_DELETE_BILL_ONLINE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_DELETE_BILL_ONLINE findCodeMessage(String code) {
            for (CODE_REPONSE_DELETE_BILL_ONLINE v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_DELETE_BILL_ONLINE.e9999;
        }
    }

    public enum CODE_REPONSE_API_CHECK_TRAINS {
        eBILLING("BILLING", "Thanh toán thành công"),
        eOFF("OFF", "Giao dịch đang được giữ"),
        eEDONG_OTHER("EDONG_OTHER", "Hóa đơn được thanh toán bởi ví khác"),
        eSOURCE_OTHER("SOURCE_OTHER", "Hóa đơn được thanh toán bởi đối tác khác"),
        eTIMEOUT("TIMEOUT", "Timeout-Giao dịch nghi ngờ"),
        eERROR("ERROR", "Thanh toán lỗi"),
        eREVERT("REVERT", "Hủy hóa đơn"),
        e2006("2006", "Không tìm thấy giao dịch tương ứng"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ");

        CODE_REPONSE_API_CHECK_TRAINS(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_API_CHECK_TRAINS findCodeMessage(String code) {
            for (CODE_REPONSE_API_CHECK_TRAINS v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_API_CHECK_TRAINS.e9999;
        }
    }

    public enum CODE_REPONSE_TRANSACTION_CANCELLATION {
        e000("000", "Thành công"),
        e2006("2006", "Không tìm thấy giao dịch tương ứng"),
        e9999("9999", "Có lỗi xảy ra khi thực hiện nghiệp vụ");

        CODE_REPONSE_TRANSACTION_CANCELLATION(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private final String code;
        private String message;

        public static CODE_REPONSE_TRANSACTION_CANCELLATION findCodeMessage(String code) {
            for (CODE_REPONSE_TRANSACTION_CANCELLATION v : values()) {
                if (v.getCode().equals(code)) {
                    return v;
                }
            }
            return CODE_REPONSE_TRANSACTION_CANCELLATION.e9999;
        }
    }
    //endregion

    //region info communication server
    //unit: seconds
    public static long TIME_OUT_CONNECT = 50000;

    public enum COMMAND_ID {
        LOGIN,
        CHANGE_PIN,
        BILLING,
        GET_BOOK_CMIS_BY_CASHIER,
        SYNC_DATA,
        CUSTOMER_BILL,
        GET_FILE_GEN,
        CHECK_TRANS,
        TRANSACTION_CANCELLATION;

        @Override
        public String toString() {
            if (this == LOGIN)
                return "LOGIN";
            if (this == CHANGE_PIN)
                return "CHANGE-PIN";
            if (this == BILLING)
                return "BILLING";
            if (this == GET_BOOK_CMIS_BY_CASHIER)
                return "GET-BOOK-CMIS-BY-CASHIER";
            if (this == SYNC_DATA)
                return "SYNC-DATA";
            if (this == CUSTOMER_BILL)
                return "CUSTOMER-BILL";
            if (this == GET_FILE_GEN)
                return "GET-FILE-GEN";
            if (this == CHECK_TRANS)
                return "CHECK-TRANS";
            if (this == TRANSACTION_CANCELLATION)
                return "TRANSACTION-CANCELLATION";
            return super.toString();
        }
    }

    //define symbol
    public static final String TEXT_SPACE = " ";
    public static final String TEXT_EMPTY = "";
    public static final String TEXT_SLASH = "/";
    public static final String TEXT_BILL = "Hóa đơn";
    public static final String TEXT_MULTI_SPACE = TEXT_SPACE.concat(TEXT_SPACE).concat(TEXT_SPACE).concat(TEXT_SPACE).concat(TEXT_SPACE);
    public static final String TEXT_SEARCHING = "Searching online....";
    public static final int NEGATIVE_ONE = -1;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final boolean BOOL_DEFAULT = false;
    //endregion

    //region info path folder
    public static final String PATH_FOLDER_ROOT = Environment.getExternalStorageDirectory() + File.separator + "PosTablet" + File.separator;
    public static final String PATH_FOLDER_DB = PATH_FOLDER_ROOT + "DB" + File.separator;
    public static final String PATH_FOLDER_CONFIG = PATH_FOLDER_ROOT + "Config" + File.separator;
    public static final String PATH_FOLDER_DOWNLOAD = PATH_FOLDER_ROOT + "Download" + File.separator;
//    public static final String PATH_FOLDER_DATA = PATH_FOLDER_ROOT + "Data" + File.separator;
    //endregion

    //region info connect API SOAP
    public static String ENDPOINT_URL = "http://kiosktest.edong.vn:8989/eStoreCashier/EStoreCashierImpl?wsdl";
    //endregion

    //region config file and system
    private static ConfigInfo cfgInfo;
    public static final String[] CFG_COLUMN = {"PUBLIC_KEY", "PRIVATE_KEY", "AGENT", "PASS_WORD", "PC_CODE"};
    public static final String CONFIG_FILENAME = "config.cfg";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String getIMEIAddress(Context context) {
        if (context == null)
            return null;
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }

    public static String getMacAddress(Context context) throws Exception {
        if (context == null)
            return null;

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();

        if (macAddress == null)
            throw new Exception(MESSAGE_NOTIFY.ERR_WIFI.toString());

        return macAddress;
    }

    public static void makeRootFolderAndGetDataConfig(Context context) throws Exception {
        if (context == null)
            return;
        if (!isExternalStorageWritable())
            return;

        try {
            //create folder
            File folderRoot = new File(PATH_FOLDER_ROOT);
            if (!folderRoot.exists()) {
                folderRoot.mkdir();
            }

            File folderDB = new File(PATH_FOLDER_DB);
            if (!folderDB.exists()) {
                folderDB.mkdir();
            }

            File folderDownload = new File(PATH_FOLDER_DOWNLOAD);
            if (!folderDownload.exists()) {
                folderDownload.mkdir();
            }

//            File folderData = new File(PATH_FOLDER_DATA);
//            if (!folderData.exists()) {
//                folderData.mkdir();
//            }

            File folderConfig = new File(PATH_FOLDER_CONFIG);
            if (!folderConfig.exists()) {
                folderConfig.mkdir();
            }

            File fileConfig = new File(PATH_FOLDER_CONFIG + Common.CONFIG_FILENAME);
            if (fileConfig.exists()) {
                //get info config
                Common.cfgInfo = Common.getDataFileConfig();
                if (Common.cfgInfo == null) {
                    Common.cfgInfo = new ConfigInfo();
                }
            } else {
                //create file config
                Common.cfgInfo = new ConfigInfo();
                Common.createFileConfig(Common.cfgInfo, fileConfig);
            }

        } catch (Exception ex) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_CREATE_FOLDER.toString());
        }
    }

    public static void loadFolder(ContextWrapper ctx) {
        if (ctx == null)
            return;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // Load config folder
            File file_config = new File(Common.PATH_FOLDER_CONFIG);
            String[] allFilesConfig = file_config.list();
            for (int i = 0; i < allFilesConfig.length; i++) {
                allFilesConfig[i] = Common.PATH_FOLDER_CONFIG + allFilesConfig[i];
            }
            if (allFilesConfig != null)
                scanFile(ctx, allFilesConfig);

            // Load db folder
            File file_db = new File(Common.PATH_FOLDER_DB);
            String[] allFilesDb = file_db.list();

            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Common.PATH_FOLDER_DB + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            // Load download folder
            File file_download = new File(Common.PATH_FOLDER_DOWNLOAD);
            String[] allFilesDownload = file_db.list();

            for (int i = 0; i < allFilesDownload.length; i++) {
                allFilesDownload[i] = Common.PATH_FOLDER_DOWNLOAD + allFilesDownload[i];
            }
            if (allFilesDownload != null)
                scanFile(ctx, allFilesDownload);

        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Common.PATH_FOLDER_ROOT)));
        }
    }

    public static void scanFile(Context ctx, String[] allFiles) {
        MediaScannerConnection.scanFile(ctx, allFiles, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("ExternalStorage", "Scanned " + path + ":");
                        Log.d("ExternalStorage", "uri=" + uri);
                    }
                });
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (activity == null)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean createFileConfig(final ConfigInfo cfg, File cfgFile) {
        if (cfg == null || cfgFile == null)
            return false;
        FileOutputStream fos;
        XmlSerializer serializer = Xml.newSerializer();
        String[] tagValue = new String[]{cfg.getPUBLIC_KEY(), cfg.getPRIVATE_KEY(), cfg.getAGENT(), cfg.getPASS_WORD(), cfg.getPC_CODE()};
        try {
            cfgFile.createNewFile();
            cfgFile.setWritable(true);
            cfgFile.setReadable(true);
            fos = new FileOutputStream(cfgFile, false);

            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "ConfigData");

            for (int i = 0; i < Common.CFG_COLUMN.length; i++) {
                serializer.startTag(null, Common.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, Common.CFG_COLUMN[i]);
            }
            serializer.endTag(null, "ConfigData");
            serializer.endDocument();

            serializer.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ConfigInfo getDataFileConfig() {
        ConfigInfo cfgInfo = new ConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(PATH_FOLDER_CONFIG + Common.CONFIG_FILENAME));

            NodeList nodeList = doc.getElementsByTagName("ConfigData");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) node;
                    NodeList publicKey = elm.getElementsByTagName(Common.CFG_COLUMN[0]);
                    if (publicKey != null && publicKey.item(0) != null) {
                        Element sub_elm = (Element) publicKey.item(0);
                        cfgInfo.setPUBLIC_KEY(sub_elm.getTextContent());
                    }

                    NodeList privateKey = elm.getElementsByTagName(Common.CFG_COLUMN[1]);
                    if (privateKey != null && privateKey.item(0) != null) {
                        Element sub_elm = (Element) privateKey.item(0);
                        cfgInfo.setPRIVATE_KEY(sub_elm.getTextContent());
                    }

                    NodeList agent = elm.getElementsByTagName(Common.CFG_COLUMN[2]);
                    if (agent != null && agent.item(0) != null) {
                        Element sub_elm = (Element) agent.item(0);
                        cfgInfo.setAGENT(sub_elm.getTextContent());
                    }

                    NodeList password = elm.getElementsByTagName(Common.CFG_COLUMN[3]);
                    if (password != null && password.item(0) != null) {
                        Element sub_elm = (Element) password.item(0);
                        cfgInfo.setPASS_WORD(sub_elm.getTextContent());
                    }

                    NodeList pcCode = elm.getElementsByTagName(Common.CFG_COLUMN[4]);
                    if (pcCode != null && pcCode.item(0) != null) {
                        Element sub_elm = (Element) pcCode.item(0);
                        cfgInfo.setPC_CODE(sub_elm.getTextContent());
                    }
                }
            }
        } catch (SAXException e) {
            cfgInfo = null;
        } catch (IOException e) {
            cfgInfo = null;
        } catch (ParserConfigurationException e1) {
            cfgInfo = null;
        }
        return cfgInfo;
    }

    public static boolean isConnectingWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return false;
        } else return true;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null)
            return false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

//        ConnectivityManager cm =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    //endregion

    //region config share references
    public static String SHARE_REF_FILE_LOGIN = "LOGIN";
    public static String SHARE_REF_FILE_LOGIN_USER_NAME = "USERNAME";
    public static String SHARE_REF_FILE_LOGIN_PASS = "PASS";
    public static String SHARE_REF_FILE_LOGIN_IS_SAVE = "CHECKBOX";
    //endregion

    //region method process ecrypt and decrypt data
    public static int LENGTH_USER_NAME = 20;
    public static int LENGTH_PASS = 8;

    public static Long createAuditNumber(String dateTimeNow) {
        if (dateTimeNow == null || dateTimeNow.isEmpty() || dateTimeNow.trim().equals(""))
            return null;

        //random 100000 <= random <= 999999
        Random random = new Random();
        int randomNumber = random.nextInt(888888) + 100000;

        String auditNumber = dateTimeNow.concat(String.valueOf(randomNumber));
        return Long.parseLong(auditNumber);
    }

    /**
     * get signature by RSA with private key
     *
     * @param agent
     * @param commandId
     * @param auditNumber
     * @param mac
     * @param diskDriver    : instead of IMEI
     * @param pcCode
     * @param accountId
     * @param privateKeyRSA
     * @return
     */
    public static String getDataSignRSA(String agent, String commandId, Long auditNumber, String mac, String diskDriver, String pcCode, String accountId, String privateKeyRSA) throws Exception {
        if (agent == null || agent.isEmpty() || agent.trim().equals(""))
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().equals(""))
            return null;
        if (auditNumber == null)
            return null;
        if (mac == null || mac.isEmpty() || mac.trim().equals(""))
            return null;
        if (diskDriver == null || diskDriver.isEmpty() || diskDriver.trim().equals(""))
            return null;
        if (pcCode == null)
            return null;
        if (accountId == null || accountId.isEmpty() || accountId.trim().equals(""))
            return null;
        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().equals(""))
            return null;
        String dataSign = agent.concat(commandId).concat(String.format("%d", auditNumber)).concat(mac).concat(diskDriver).concat(pcCode).concat(accountId);

        return dataSign;
    }

    public static String encryptPasswordAgentByRSA(String passwordAgent, String privateKeyRSA) throws Exception {
        if (passwordAgent == null || passwordAgent.isEmpty() || passwordAgent.trim().equals(""))
            return null;
        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty())
            return null;

        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate(privateKeyRSA);
        String encryptPass = ac.encryptText(passwordAgent, privateKey);

        return encryptPass;
    }


    /*public static String createKeyTripleDsCbc(String publicKeyRSA, String privateKeyRSA) {
        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty())
            return null;
        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty())
            return null;

        int length2 = publicKeyRSA.length();
        String keyResult = privateKeyRSA.substring((privateKeyRSA.length() - 12) / 2, (privateKeyRSA.length() - 12) / 2 + 12) + publicKeyRSA.substring((length2 - 12) / 2, (length2 - 12) / 2 + 12);
        return keyResult;
    }

    public static String createInitialVector(String publicKeyRSA, String privateKeyRSA) {
        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty())
            return null;
        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty())
            return null;

        String vectorResult = privateKeyRSA.substring((privateKeyRSA.length() - 4) / 2, (privateKeyRSA.length() - 4) / 2 + 4) + publicKeyRSA.substring((publicKeyRSA.length() - 4) / 2, (publicKeyRSA.length() - 4) / 2 + 4);
        return vectorResult;
    }*/

    public static String encryptPassByTripleDsCbc(Context context, String pass, String publicKeyRSA, String privateKeyRSA) throws Exception {
        if (pass == null || pass.isEmpty() || pass.trim().isEmpty())
            return null;
        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty())
            return null;
        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty())
            return null;

        privateKeyRSA = context.getString(R.string.prvkey);
        publicKeyRSA = context.getString(R.string.pblKey);
        privateKeyRSA = privateKeyRSA.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").trim().replace("\n", "").replace("\r", "");
        publicKeyRSA = publicKeyRSA.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").trim().replace("\n", "").replace("\r", "");


        //check pass: if pass length < 8 insert space
        pass = pass.trim();
        while (pass.length() < 8) {
            pass = TEXT_SPACE + pass;
        }

        //Create partner with code is null because login not request partner code
        Partner partner;
        Crypto crypto;
        String passEncrypted;
        boolean padding = true;

        partner = new Partner(null, publicKeyRSA, privateKeyRSA);

        try {
            crypto = new TripleDesCBC(partner.getKEY().getBytes(), partner.getIV().getBytes(), padding);
        } catch (Exception e) {
            throw new Exception(MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());
        }
        if (crypto == null)
            throw new Exception(MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());

        byte[] passBytes = pass.getBytes();
        byte[] encryptBytes = crypto.encrypt(passBytes);
        passEncrypted = new String(Utils.encodeHex(encryptBytes));

        return passEncrypted;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static ConfigInfo setupInfoRequest(Context context, String userName, String commandId, String versionApp) throws Exception {
//        if (context == null)
//            return null;
//        if (userName == null || userName.isEmpty() || userName.trim().isEmpty())
//            return null;
//        if (commandId == null || commandId.isEmpty() || commandId.trim().isEmpty())
//            return null;
//        if (versionApp == null || versionApp.isEmpty() || versionApp.trim().isEmpty())
//            return null;
//
//        //setup info login
//        ConfigInfo configInfo = Common.getDataFileConfig();
//
//        //create auditNumber
//        String timeNow = Common.getDateTimeNow6Digit();
//        Long auditNumber = Common.createAuditNumber(timeNow);
//        configInfo.setAuditNumber(auditNumber);
//
//        //create macAdressHexValue
//        String macAdressHexValue;
//        try {
//            //get and convert mac adress to hex
//            macAdressHexValue = Common.getMacAddress(context);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//        configInfo.setMacAdressHexValue(macAdressHexValue);
//
//        //create diskDriver
//        String diskDriver = Common.getIMEIAddress(context);
//        configInfo.setDiskDriver(diskDriver);
//
//        //create diskDriver
//        String accountId = userName.toString().trim();
//        configInfo.setAccountId(accountId);
//
//        //create agentEncypted by RSA
//        String passwordAgent, passwordAgentEcrypted;
//        String agent = configInfo.getAGENT();
//        passwordAgent = configInfo.getPASS_WORD();
//        String pcCode = configInfo.getPC_CODE();
//        String privateKeyRSA = configInfo.getPRIVATE_KEY();
//        String publicKeyRSA = configInfo.getPUBLIC_KEY();
//
//        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty()) {
//            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
//        }
//        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty()) {
//            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
//        }
//        if (passwordAgent == null || passwordAgent.isEmpty() || passwordAgent.trim().isEmpty()) {
//            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
//        }
//        try {
//            passwordAgentEcrypted = SecurityUtils.encryptRSAToString(passwordAgent, publicKeyRSA);
//        } catch (Exception e) {
//            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
//        }
//        configInfo.setAgentEncypted(passwordAgentEcrypted);
//
//        //set command id
//        //encrypt signature by RSA
//        String signatureEncrypted;
//        try {
//            String dataSign = Common.getDataSignRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);
//            Log.d(TAG, "setupInfoRequest: " + dataSign);
//            signatureEncrypted = SecurityUtils.sign(dataSign, privateKeyRSA);
//            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
//        } catch (Exception e) {
//            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
//        }
//        configInfo.setSignatureEncrypted(signatureEncrypted);
//
//        //set version app
//        configInfo.setVersionApp(versionApp);
//
//        //set command id
//        configInfo.setCommandId(commandId);
//
//        return configInfo;
//    }

    public static ConfigInfo setupInfoRequest(Context context, String userName, String commandId, String versionApp) throws Exception {
        if (context == null)
            return null;
        if (userName == null || userName.isEmpty() || userName.trim().isEmpty())
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().isEmpty())
            return null;
        if (versionApp == null || versionApp.isEmpty() || versionApp.trim().isEmpty())
            return null;

        //setup info login
        ConfigInfo configInfo = Common.getDataFileConfig();

        //create auditNumber
        String timeNow = Common.getDateTimeNow6Digit();
        Long auditNumber = Common.createAuditNumber(timeNow);
        configInfo.setAuditNumber(auditNumber);

        //create macAdressHexValue
        String macAdressHexValue;
        try {
            //get and convert mac adress to hex
            macAdressHexValue = Common.getMacAddress(context);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        configInfo.setMacAdressHexValue(macAdressHexValue);

        //create diskDriver
        String diskDriver = Common.getIMEIAddress(context);
        configInfo.setDiskDriver(diskDriver);

        //create diskDriver
        String accountId = userName.toString().trim();
        configInfo.setAccountId(accountId);

        //create agentEncypted by RSA
        String passwordAgent, passwordAgentEcrypted;
        String agent = configInfo.getAGENT();
        passwordAgent = configInfo.getPASS_WORD();
        String pcCode = configInfo.getPC_CODE();
        String privateKeyRSA = configInfo.getPRIVATE_KEY();
        String publicKeyRSA = configInfo.getPUBLIC_KEY();

        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        if (passwordAgent == null || passwordAgent.isEmpty() || passwordAgent.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }

        try {
            passwordAgentEcrypted = SecurityUtils.encryptRSAToString(passwordAgent, publicKeyRSA);
        } catch (Exception e) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        configInfo.setAgentEncypted(passwordAgentEcrypted);

        //set command id
        //encrypt signature by RSA
        String signatureEncrypted;
        try {
            String dataSign = Common.getDataSignRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, privateKeyRSA);
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        //set command id
        configInfo.setCommandId(commandId);

        //set version app
        configInfo.setVersionApp(versionApp);

        return configInfo;
    }


    public static ConfigInfo setupInfoRequest(Context context, String userName, String commandId, String versionApp, String pcCode) throws Exception {
        if (context == null)
            return null;
        if (userName == null || userName.isEmpty() || userName.trim().isEmpty())
            return null;
        if (commandId == null || commandId.isEmpty() || commandId.trim().isEmpty())
            return null;
        if (versionApp == null || versionApp.isEmpty() || versionApp.trim().isEmpty())
            return null;

        //setup info login
        ConfigInfo configInfo = Common.getDataFileConfig();

        //create auditNumber
        String timeNow = Common.getDateTimeNow6Digit();
        Long auditNumber = Common.createAuditNumber(timeNow);
        configInfo.setAuditNumber(auditNumber);

        //create macAdressHexValue
        String macAdressHexValue;
        try {
            //get and convert mac adress to hex
            macAdressHexValue = Common.getMacAddress(context);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        configInfo.setMacAdressHexValue(macAdressHexValue);

        //create diskDriver
        String diskDriver = Common.getIMEIAddress(context);
        configInfo.setDiskDriver(diskDriver);

        //create diskDriver
        String accountId = userName.toString().trim();
        configInfo.setAccountId(accountId);

        //create agentEncypted by RSA
        String passwordAgent, passwordAgentEcrypted;
        String agent = configInfo.getAGENT();
        passwordAgent = configInfo.getPASS_WORD();
//        String pcCode = configInfo.getPC_CODE();
        String privateKeyRSA = configInfo.getPRIVATE_KEY();
        String publicKeyRSA = configInfo.getPUBLIC_KEY();

        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        if (publicKeyRSA == null || publicKeyRSA.isEmpty() || publicKeyRSA.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        if (passwordAgent == null || passwordAgent.isEmpty() || passwordAgent.trim().isEmpty()) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }

        try {
            passwordAgentEcrypted = SecurityUtils.encryptRSAToString(passwordAgent, publicKeyRSA);
        } catch (Exception e) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        configInfo.setAgentEncypted(passwordAgentEcrypted);

        //set command id
        //encrypt signature by RSA
        String signatureEncrypted;
        try {
            String dataSign = Common.getDataSignRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, privateKeyRSA);
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
            throw new Exception(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        //set command id
        configInfo.setCommandId(commandId);

        //set version app
        configInfo.setVersionApp(versionApp);

        return configInfo;
    }

    //endregion

    //region method utils
    //delay animations when view is clicked
    public static final String UNIT_MONEY = "đ";

    public static final int TIME_DELAY_ANIM = 250;

    public enum TEXT_DIALOG {
        OK,
        CANCLE,
        TITLE_DEFAULT,
        MESSAGE_DEFAULT,
        MESSAGE_CLICK_YES_DEFAULT,
        MESSAGE_EXIT;

        @Override
        public String toString() {
            if (this == OK)
                return "Chấp nhận";
            if (this == CANCLE)
                return "Không";
            if (this == TITLE_DEFAULT)
                return "Thông báo";
            if (this == MESSAGE_DEFAULT)
                return "Chưa có nội dung";
            if (this == MESSAGE_CLICK_YES_DEFAULT)
                return "Bạn đã chấp nhận";
            if (this == MESSAGE_EXIT)
                return "Bạn có chắc chắn muốn thoát không?";
            return "";
        }
    }

    public enum DATE_TIME_TYPE {
        HHmmss,
        yyyymmdd,
        mmyyyy,
        ddmmyyyy,
        FULL;

        @Override
        public String toString() {
            if (this == HHmmss)
                return "HHmmss";
            if (this == yyyymmdd)
                return "yyyy-mm-dd";
            if (this == mmyyyy)
                return "mm/yyyy";
            if (this == ddmmyyyy)
                return "dd/MM/yyyy";
            if (this == FULL)
                return "yyyy-MM-dd'T'HH:mm:ss";
            return super.toString();
        }
    }

    public static String getDateTimeNow(Common.DATE_TIME_TYPE formatDate) {
        SimpleDateFormat df = new SimpleDateFormat(formatDate.toString());
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(Calendar.getInstance().getTime());
    }

    public static String getDateTimeNow6Digit() {
        Calendar c = Calendar.getInstance();

        int s = c.get(Calendar.SECOND);
        String second = (s < 10) ? "0" + s : String.valueOf(s);
        int m = c.get(Calendar.MINUTE);
        String minute = (m < 10) ? "0" + m : String.valueOf(m);
        int h = c.get(Calendar.HOUR);
        String hour = (h < 10) ? "0" + h : String.valueOf(h);

        return hour.concat(minute).concat(second);
    }

    public static String convertLongToDate(long time, Common.DATE_TIME_TYPE format) {
        if (time < 0)
            return null;
        if (format == null)
            return null;

        SimpleDateFormat df2 = new SimpleDateFormat(format.toString());
        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(time);
        return df2.format(date);
    }

    // decode data from base 64
    public static byte[] decodeBase64(String dataToDecode) {
        if (dataToDecode == null || dataToDecode.isEmpty() || dataToDecode.trim().equals(""))
            return null;

        byte[] dataDecoded = Base64.decode(dataToDecode, Base64.DEFAULT);
        return dataDecoded;
    }

    //enconde data in base 64
    public static byte[] encodeBase64(byte[] dataToEncode) {
        if (dataToEncode == null)
            return null;

        byte[] dataEncoded = Base64.encode(dataToEncode, Base64.DEFAULT);
        return dataEncoded;
    }

    public static void runAnimationClickViewScale(final View view, int idAnimation, int timeDelayAnim) {
        if (view == null)
            return;
        if (idAnimation <= 0)
            return;

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), idAnimation);
        if (timeDelayAnim > 0)
            animation.setDuration(timeDelayAnim);

        view.startAnimation(animation);
    }

    public static void showDialog(Context context, final IActionClickYesNoDialog clickYesNoDialog, String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.diaglog_layout_button_ok, null);
        final TextView titleView = (TextView) view.findViewById(R.id.tv_dialog_layout_title);
        final TextView messageView = (TextView) view.findViewById(R.id.tv_dialog_layout_message);
        final Button buttonOK = (Button) view.findViewById(R.id.btn_dialog_layout_button_ok);
        final Button buttonCancle = (Button) view.findViewById(R.id.btn_dialog_layout_button_cancel);

        builder.setView(view);

        titleView.setText(title);
        messageView.setText(message);

        final AlertDialog alertDialog = builder.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickYesNoDialog.doClickYes();
                alertDialog.dismiss();
            }
        });

        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickYesNoDialog.doClickNo();
                alertDialog.dismiss();
            }
        });
    }

    public static int getScreenHeight(Context context) {
        int screenWidth = 0;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        return screenWidth;
    }

    public static String convertDateToDate(String time, DATE_TIME_TYPE typeDefault, DATE_TIME_TYPE typeConvert) {
        if (time == null || time.trim().isEmpty())
            return "";

        long longDate = Common.convertDateToLong(time, typeDefault);

        String dateByDefaultType = Common.convertLongToDate(longDate, typeConvert);
        return dateByDefaultType;
    }

    public static long convertDateToLong(String date, DATE_TIME_TYPE typeDefault) {
        SimpleDateFormat formatter = new SimpleDateFormat(typeDefault.toString());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateParse;
        try {
            dateParse = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return dateParse.getTime();
    }

    public static String getVersionApp(Context context) {
        String versionApp = "";
        try {
            versionApp = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionApp;
    }

    //endregion

    //region handle file
    public static void writeBytesToFile(File theFile, byte[] bytes) throws IOException {
        if (bytes != null) {
            BufferedOutputStream bos = null;

            try {
                FileOutputStream fos = new FileOutputStream(theFile);
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
            } finally {
                if(bos != null) {
                    try  {
                        bos.flush();
                        bos.close();
                    } catch(Exception e){}
                }
            }
        }
    }

    public static boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    //endregion

    public static String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

}
