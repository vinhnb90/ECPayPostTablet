package views.ecpay.com.postabletecpay.view.Util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.Result;

import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by duydatpham on 6/23/17.
 */

public class BarcodeScannerDialog extends Dialog implements View.OnClickListener, ZXingScannerView.ResultHandler, MainActivity.IOnPauseScannerBarcodeListner {

    OnResultListener onResultListener;

    Button btnCancelTextBarcode;
    Button btnOKTextBarcode;
    TextView tvTextBarcode;
    LinearLayout llBarcode;

    private String textBarcode = Common.TEXT_EMPTY;
    private boolean isScannerBarcode = false;
    private boolean isOKText = false;

    ZXingScannerView mScannerView;

    public BarcodeScannerDialog(@NonNull MainActivity context, OnResultListener _onResultListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = context.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_barcode, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        context.setPauseScannerBarcodeListner(this);

        TextView textView = new TextView(context);
        mScannerView = new ZXingScannerView(context);
        mScannerView.setResultHandler(this);


        btnCancelTextBarcode = (Button)findViewById(R.id.btn_dialog_barcode_cancel);
        btnOKTextBarcode = (Button)findViewById(R.id.btn_dialog_barcode_ok);
        tvTextBarcode = (TextView)findViewById(R.id.tv_dialog_barcode_text);
        llBarcode = (LinearLayout)findViewById(R.id.ll_dialog_barcode);

        btnCancelTextBarcode.setOnClickListener(this);
        btnOKTextBarcode.setOnClickListener(this);

        LinearLayout ll = (LinearLayout)findViewById(R.id.ll_dialog_barcode_main);
        ViewGroup parent = (ViewGroup) ll.getParent();


        llBarcode.removeView(ll);
        llBarcode.addView(mScannerView);
        mScannerView.startCamera();

        this.onResultListener = _onResultListener;

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isScannerBarcode = false;

                mScannerView.stopCamera();
                if (isOKText && onResultListener != null)
                    onResultListener.onResult(textBarcode);

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_dialog_barcode_cancel)
        {
            if (mScannerView == null || tvTextBarcode == null)
                return;

            tvTextBarcode.setText(Common.TEXT_EMPTY);
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();
            mScannerView.resumeCameraPreview(this);
            return;
        }

        if(view.getId() == R.id.btn_dialog_barcode_ok)
        {
            if (tvTextBarcode == null)
                return;
            isOKText = true;
            textBarcode = tvTextBarcode.getText().toString().trim();
            this.dismiss();
            return;
        }
    }

    @Override
    public void handleResult(Result result) {
        tvTextBarcode.setText(result.getText());
    }

    @Override
    public void pause() {
        if (mScannerView == null || isScannerBarcode == false)
            return;

        mScannerView.stopCamera();
    }


    public interface OnResultListener {
        void onResult(String text);
    }
}
