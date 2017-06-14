package views.ecpay.com.postabletecpay.view.Barcode;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import views.ecpay.com.postabletecpay.R;

public class BarcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    public void handleResult(Result result) {
        //Do anything with result here :D
        Log.w("handleResult",result.getText( ));
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Resume scanning uncomment below
        //mScannerView.resumeCameraPreview(this);
    }
}
