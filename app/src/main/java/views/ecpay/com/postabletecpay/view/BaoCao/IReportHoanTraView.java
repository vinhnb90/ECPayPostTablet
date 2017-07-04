package views.ecpay.com.postabletecpay.view.BaoCao;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by duydatpham on 6/26/17.
 */

public interface IReportHoanTraView extends ICommonView {
    public void fill(List<Bill> lst);
    public void showMessage(String message);
}