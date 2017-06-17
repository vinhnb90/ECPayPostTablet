package views.ecpay.com.postabletecpay.util.DialogHelper.Imp;


import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesDialog;

/**
 * Created by VinhNB on 2/28/2017.
 */

public abstract class DialogImp {

    protected DialogEntity mDialogEntity;
    protected IActionClickYesDialog iActionClickYesDialog;
    public abstract void show();
}
