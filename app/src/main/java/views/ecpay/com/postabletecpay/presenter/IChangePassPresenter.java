package views.ecpay.com.postabletecpay.presenter;

public interface IChangePassPresenter {
    void validateInputChangePass(String edong, String passOld, String passNew, String passRetype);

    void callInfo(String mEdong);
}