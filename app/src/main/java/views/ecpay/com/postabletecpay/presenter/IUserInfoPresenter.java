package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.view.Logout.ILogoutView;

public interface IUserInfoPresenter {
    void getInfoUser(String edong);

    void callLogout(String mEdong);
}
