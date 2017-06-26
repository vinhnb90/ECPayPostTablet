package views.ecpay.com.postabletecpay.presenter;

import java.util.Calendar;

/**
 * Created by duydatpham on 6/25/17.
 */

public interface IReportChiTietPresenter extends IReportPrensenter {
    public void search(boolean isMaKH, String code, Calendar from, Calendar to);
}
