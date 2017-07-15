package views.ecpay.com.postabletecpay.view.BaoCao;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by duydatpham on 7/15/17.
 */

public interface IReportLichSuThanhToanView extends ICommonView {
    public void fill(Account account, int hdGiao, long tienGiao, int dhThu, long tienThu, int hdVangLai, long tienVangLai, int hdTraKH, long tienTraKHH);
}
