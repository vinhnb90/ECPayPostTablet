package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.database.Cursor;

import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPageModel extends CommonModel {

    public MainPageModel(Context context) {
        super(context);
    }

    //region call SQLite
    public int getTotalBill(String edong) {
        return sqLiteConnection.countBill(edong);
    }

    public int getTotalMoney(String edong) {
        return sqLiteConnection.countMoneyAllBill(edong);
    }

    public long insertEvnPC(ListEvnPCResponse listEvnPCResponse, String edong) {
        return sqLiteConnection.insertEvnPC(listEvnPCResponse, edong);
    }

    public long deleteAllPC(String edong) {
        return sqLiteConnection.deleteAllPC(edong);
    }

    public  long checkEvnPCExist(int pcId, String edong) {
        return sqLiteConnection.checkEvnPCExist(pcId, edong);
    }

    public long insertBookCmis(ListBookCmisResponse listBookCmisResponse) {
        return sqLiteConnection.insertBookCmis(listBookCmisResponse);
    }

    public String getPcCode(String edong){
        return sqLiteConnection.getPcCode(edong);
    }

    public long deleteAllBookCmis() {
        return sqLiteConnection.deleteAllBookCmis();
    }

    public  long checkBookCmisExist(String bookCmis) {
        return sqLiteConnection.checkBookCmisExist(bookCmis);
    }

    public Cursor getAllBookCmis() {
        return sqLiteConnection.getAllBookCmis();
    }


    public void deleteAllData()
    {
        sqLiteConnection.deleteAllData();
    }
    //endregion
}

