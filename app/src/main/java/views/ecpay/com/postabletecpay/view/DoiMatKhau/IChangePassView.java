package views.ecpay.com.postabletecpay.view.DoiMatKhau;

import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/19/2017.
 */

public interface IChangePassView extends ICommonView {
    void showPbar();
    void hidePbar();

    void showText(String message);
    void hideText();
}
