package views.ecpay.com.postabletecpay.model;

import android.content.Context;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPageModel extends CommonModel {

    public MainPageModel(Context context) {
        super(context);
    }

    //region call SQLite
    public int getTotalBill(String edong) {
        sqLiteConnection.getReadableDatabase();
        return sqLiteConnection.countBill(edong);
    }

    public int getTotalMoney(String edong) {
        sqLiteConnection.getReadableDatabase();
        return sqLiteConnection.countMoneyAllBill(edong);
    }
    //endregion
}

