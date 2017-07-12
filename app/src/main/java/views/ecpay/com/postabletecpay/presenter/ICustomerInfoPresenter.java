package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by MyPC on 23/06/2017.
 */

public interface ICustomerInfoPresenter {
    public void register(EntityKhachHang customer, String mEDong, String eCard, String phoneEcpay, String bankAcc, String bankName);
    public void update(EntityKhachHang customer, String mEDong, String eCard, String phoneEcpay, String bankAcc, String bankName);
}
