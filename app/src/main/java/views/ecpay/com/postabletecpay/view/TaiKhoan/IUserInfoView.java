package views.ecpay.com.postabletecpay.view.TaiKhoan;

import views.ecpay.com.postabletecpay.view.ICommonView;
import views.ecpay.com.postabletecpay.view.Logout.ILogoutView;

/**
 * Created by VinhNB on 5/23/2017.
 */

public interface IUserInfoView extends ICommonView, ILogoutView{
    void showInfoUser(String userName, String dateRegister, String accountName, String indentityCard, String phone, String email, String address, String numberAccount, long balance, int typeAccount);
}
