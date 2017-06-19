package views.ecpay.com.postabletecpay.view.Logout;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB_PC on 6/19/2017.
 */

public interface ILogoutView extends ICommonView {
    void showStatusProgressLogout(Common.STATUS_PROGRESS statusProgress);
    void showMessageLogout(String textMessage);
//    void doClickOK();
//    void doClickCancel();
    void showLoginForm();
}
