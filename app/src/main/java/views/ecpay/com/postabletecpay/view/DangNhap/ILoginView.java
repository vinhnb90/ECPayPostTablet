package views.ecpay.com.postabletecpay.view.DangNhap;

import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/11/2017.
 */

public interface ILoginView extends ICommonView{
    void showPbarLogin();
    void hidePbarLogin();

    void showTextMessage(String message);
    void hideTextMessage();

    void showMainScreen(String edong);

    void showTextUserPass(String userName, String pass);

    void showTickCheckbox(boolean isSaveLogin);
}
