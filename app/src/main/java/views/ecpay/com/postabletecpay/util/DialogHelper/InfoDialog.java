package views.ecpay.com.postabletecpay.util.DialogHelper;

import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickDialog;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesDialog;

/**
 * Created by VinhNB on 5/19/2017.
 */

public abstract class InfoDialog {
    protected int idLayout;
    protected IActionClickDialog actionClickDialog;

    public InfoDialog(int idLayout) {
        this.idLayout = idLayout;
    }

    public void setActionClickDialog(IActionClickDialog actionClickDialog) {
        this.actionClickDialog = actionClickDialog;
    }

    public int getIdLayout() {
        return idLayout;
    }
}
