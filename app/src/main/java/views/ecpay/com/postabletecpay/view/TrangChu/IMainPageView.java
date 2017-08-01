package views.ecpay.com.postabletecpay.view.TrangChu;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ICommonView;
import views.ecpay.com.postabletecpay.view.Logout.ILogoutView;

/**
 * Created by VinhNB on 5/23/2017.
 */

public interface IMainPageView extends ICommonView, ILogoutView {
    void showMainPageInfo(String userName, long balance, int totalBills, long totalMoney);
    void showTextMessage(String message);

//    void showStatusProgressLogout(Common.STATUS_PROGRESS statusProgress);
//
//    void showMessageLogout(String textMessage);
//
//    void showLoginForm();
}
