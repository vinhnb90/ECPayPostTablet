package views.ecpay.com.postabletecpay.presenter;

/**
 * Created by VinhNB on 5/11/2017.
 */

public interface ILoginPresenter {
    void validateInput(String userName, String pass);

    void writeSharedPrefLogin(String userName, String pass);

    void clearSharedPrefLogin();

    void showInfoSharePrefLogin();
}
