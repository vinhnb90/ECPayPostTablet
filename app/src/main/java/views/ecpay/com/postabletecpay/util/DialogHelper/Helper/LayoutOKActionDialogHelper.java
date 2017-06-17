package views.ecpay.com.postabletecpay.util.DialogHelper.Helper;


import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.DialogImp;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.LayoutOKActionImpDialog;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesDialog;


/**
 * Created by VinhNB on 3/6/2017.
 */

public class LayoutOKActionDialogHelper extends DialogHelper {
    private DialogEntity mDialogEntity;
    private IActionClickYesDialog mActionClickYesDialog;
    public LayoutOKActionDialogHelper(DialogEntity dialogEntity, IActionClickYesDialog actionClickYesDialog) {
        if (dialogEntity == null) {
            return;
        }
        if (actionClickYesDialog == null) {
            return;
        }
        this.mDialogEntity = dialogEntity;
        this.mActionClickYesDialog = actionClickYesDialog;
    }

    @Override
    public DialogImp build() {
        return new LayoutOKActionImpDialog(this.mDialogEntity, this.mActionClickYesDialog);
    }
}
