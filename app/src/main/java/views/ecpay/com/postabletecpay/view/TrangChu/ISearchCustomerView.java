package views.ecpay.com.postabletecpay.view.TrangChu;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by MyPC on 21/06/2017.
 */

public interface ISearchCustomerView extends ICommonView {
    public void refreshView(List<Customer> lst);
    public void showDialogBarcode();
    public void showCustomerInfo(Customer customer);
}
