package views.ecpay.com.postabletecpay.view.TrangChu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.CustomerAdapter;
import views.ecpay.com.postabletecpay.presenter.ISearchCustomerPresenter;
import views.ecpay.com.postabletecpay.presenter.SearchCustomerPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by MyPC on 21/06/2017.
 */

public class SearchCustomerFragment extends Fragment implements ISearchCustomerView, View.OnClickListener {

    @Nullable
    @BindView(R.id.recycle_list_customer)
    RecyclerView rvKH;

    private String eDong;
    private Unbinder unbinder;

    private boolean isCurrentExpand;

    private ISearchCustomerPresenter searchCustomerPresenter;

    private PayFragment.OnPayFragmentInteractionListener listener;
    private Dialog dialogBarcode;
    private View rootView;

    private ZXingScannerView mScannerView;

    public static SearchCustomerFragment newInstance(String eDong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, eDong);


        SearchCustomerFragment fragment = new SearchCustomerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @BindView(R.id.layout_timkiem_nangcao)
    LinearLayout layout_timkiem_nangcao;
    @Nullable
    @BindView(R.id.btnExpand)
    ImageButton btnExpand;
    @Nullable
    @BindView(R.id.btnSearch)
    ImageButton btnSearch;
    @Nullable
    @BindView(R.id.btnSearch2)
    Button btnSearch2;


    //For dialog Barcode
    private String textBarcode = Common.TEXT_EMPTY;
    private boolean isScannerBarcode = false;
    private boolean isOKText = false;



    @Nullable
    @BindView(R.id.ll_dialog_barcode)
    LinearLayout llBarcode;
    @Nullable
    @BindView(R.id.tv_dialog_barcode_text)
    TextView tvTextBarcode;
    @Optional
    @OnClick(R.id.btn_dialog_barcode_cancel)
    public void clickRefreshBarcode(View view) {
        if (dialogBarcode == null || mScannerView == null || tvTextBarcode == null)
            return;

        tvTextBarcode.setText(Common.TEXT_EMPTY);
        mScannerView.setResultHandler((MainActivity) getContext()); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        listener.refreshCamera(mScannerView);
    }

    @Optional
    @OnClick(R.id.btn_dialog_barcode_ok)
    public void clickOKBarcode(View view) {
        if (dialogBarcode == null || tvTextBarcode == null)
            return;
        isOKText = true;
        textBarcode = tvTextBarcode.getText().toString().trim();
        dialogBarcode.dismiss();
    }
    //End dialog Barcode

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_customer, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        eDong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);


        searchCustomerPresenter = new SearchCustomerPresenter(this, eDong);



        btnExpand.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnSearch2.setOnClickListener(this);

        isCurrentExpand = false;
        btnExpand.setRotation(180F);

        layout_timkiem_nangcao.setVisibility(View.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKH.setLayoutManager(layoutManager);

        List<Customer> lst = new ArrayList<>();
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());

        rvKH.setAdapter(new CustomerAdapter(lst));
        rvKH.invalidate();


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PayFragment.OnPayFragmentInteractionListener)
            listener = (PayFragment.OnPayFragmentInteractionListener) activity;
        else
            throw new ClassCastException("activity must be implement OnPayFragmentInteractionListener!");

    }


    public void bindViewAgain()
    {
        if (unbinder != null)
            unbinder.unbind();
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExpand) {
            //layout_timkiem_nangcao.setVisibility(layout_timkiem_nangcao.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            float deg = btnExpand.getRotation() + 180F;
            btnExpand.animate().rotation(deg).setDuration(1);

            isCurrentExpand = !isCurrentExpand;
            layout_timkiem_nangcao.setVisibility(isCurrentExpand ? View.VISIBLE : View.GONE);
            return;
        }


        if(v.getId() == R.id.btnSearch2)
        {
            showDialogBarcode();
            //searchCustomerPresenter.search("", "Da", "", "", "", "PA0503", 2);
            return;
        }
    }

    @Override
    public void showDialogBarcode() {
//        if (this.getActivity() instanceof CallbackBarcodeDialog) {
//            final CallbackBarcodeDialog callbackBarcodeDialog = (CallbackBarcodeDialog) getContextView();

        dialogBarcode = new Dialog(this.getActivity());
        dialogBarcode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_barcode, null);
        dialogBarcode.setContentView(view);
        dialogBarcode.setCanceledOnTouchOutside(true);
        dialogBarcode.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialogBarcode.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogBarcode.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialogBarcode.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        unbinder.unbind();
        unbinder = ButterKnife.bind(this, dialogBarcode);


        if (llBarcode == null)
            return;
        TextView textView = new TextView(getContext());
        mScannerView = new ZXingScannerView(getContext());
        mScannerView.setResultHandler((MainActivity) getContext());

        LinearLayout ll = (LinearLayout) dialogBarcode.findViewById(R.id.ll_dialog_barcode_main);
        ViewGroup parent = (ViewGroup) ll.getParent();


        llBarcode.removeView(ll);
        llBarcode.addView(mScannerView);
        mScannerView.startCamera();

        dialogBarcode.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isScannerBarcode = false;

                mScannerView.stopCamera();
                listener.setRootViewAgain();
                if (isOKText)
                    listener.fillToSearchText(textBarcode);

            }
        });
        dialogBarcode.show();
//        } else
//            Log.e(TAG, "showDialogBarcode: fragment cannot implement CallbackBarcodeDialog ");
    }

    public void fillResultToSearchText(String textBarcode) {
        if (TextUtils.isEmpty(textBarcode))
            return;
    }
    public void fillResultToTextBarcodeDialog(String text) {
        if (TextUtils.isEmpty(text) || tvTextBarcode == null)
            return;
        tvTextBarcode.setText(text);
//        if (TextUtils.isEmpty(text) || etSearch == null)
//            return;
//        etSearch.setText(text);
    }


    public void onPauseScannerBarcode() {
        if (mScannerView == null
                || isScannerBarcode == false
                )
            return;

        mScannerView.stopCamera();
    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }
}
