package views.ecpay.com.postabletecpay.view.BaoCao;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by duydatpham on 6/25/17.
 */

public interface IReportChiTietView extends ICommonView {
    public void fill(List<EntityHoaDonThu> lst);
    public void showMessage(String message);
}
