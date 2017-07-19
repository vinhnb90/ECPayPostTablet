package views.ecpay.com.postabletecpay.view.TrangChu;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by duydatpham on 6/23/17.
 */

public interface ICustomerInfoView extends ICommonView {
    public void setLoading(boolean pShow);

    public void showMessageText(String message, Common.TYPE_DIALOG typeLoi);

    public void refill(EntityKhachHang customer);

}
