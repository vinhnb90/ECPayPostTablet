package views.ecpay.com.postabletecpay.view.TrangChu;

import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by MyPC on 20/06/2017.
 */

public interface ICashTranferView extends ICommonView {
    public void showError(String message);
    public void showText(String message);
    public void setVisibleBar(boolean visible);
    public void onBack();
}
