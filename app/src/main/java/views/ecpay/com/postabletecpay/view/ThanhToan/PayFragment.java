package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.Optional;
import butterknife.Unbinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.presenter.PayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;

/**
 * Created by macbook on 4/28/17.
 */

public class PayFragment extends Fragment implements
        IPayView {
    public static final int FIRST_PAGE_INDEX = 1;
    public static final int PAGE_INCREMENT = 1;
    public static final int ROWS_ON_PAGE = 10;
    private static ZXingScannerView mScannerView;
    private String textBarcode = Common.TEXT_EMPTY;
    private boolean isScannerBarcode = false;
    private boolean isOKText = false;
    @Nullable
    @BindView(R.id.et_frag_thanh_toan_search)
    EditText etSearch;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_qrcode)
    ImageButton btnBarcode;
    @Nullable
    @BindView(R.id.ll_frag_thanh_toan_count)
    LinearLayout llCount;
    @Nullable
    @BindView(R.id.rv_frag_thanh_toan_customer)
    RecyclerView rvKH;
    @Nullable
    @BindView(R.id.tv_frag_thanh_toan_no_data)
    TextView tvNoData;
    @Nullable
    @BindView(R.id.tabs_frag_thanh_toan)
    TabLayout tabLayout;
    @Nullable
    @BindView(R.id.vpage_frag_thanh_toan)
    ViewPager viewPager;
    @Nullable
    @BindView(R.id.btn_frag_thanh_toan_pre)
    Button btnPre;
    @Nullable
    @BindView(R.id.btn_frag_thanh_toan_next)
    Button btnNext;
    @Nullable
    @BindView(R.id.tv_frag_thanh_toan_page)
    TextView tvPage;
    @Nullable
    @BindView(R.id.ll_frag_thanh_toan_page)
    LinearLayout llCountPage;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_clear_text_search)
    ImageButton ibtnClearSearch;
    @Nullable
    @BindView(R.id.tv_fragment_thanh_toan_total_bills_checked)
    TextView tvTotalBills;
    @Nullable
    @BindView(R.id.tv_fragment_thanh_toan_total_bills_money)
    TextView tvTotalBillsMoney;

    //Search online
    @Nullable
    @BindView(R.id.rv_frag_thanhtoan_search_online_progress)
    RelativeLayout rvProgressSearchOnline;
    @Nullable
    @BindView(R.id.tv_frag_thanh_toan_search_online_message)
    TextView tvMessageNotifySearchOnlne;
    @Nullable
    @BindView(R.id.pbar_frag_thanhtoan_search_online)
    ProgressBar pbarSearchOnline;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_action_research_online)
    ImageButton ibtnResearchOnline;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_action_cancel_search_online)
    ImageButton ibtnCancelSearchOnline;

    //Dialog pay list bills
    @Nullable
    @BindView(R.id.rv_dialog_thanhtoan_bills)
    RecyclerView rvListBill;
    @Nullable
    @BindView(R.id.tv_dialog_thanhtoan_total_money_bills)
    TextView tvTotalBillsMoneyDialog;
    @Nullable
    @BindView(R.id.tv_dialog_thanhtoan_bills_count)
    TextView tvTotalBillsDialog;
    @Nullable
    @BindView(R.id.btn_dialog_thanhtoan_cancel)
    Button btnCancelDialog;
    @Nullable
    @BindView(R.id.btn_dialog_thanhtoan_pay)
    Button btnPayDialog;
    @Nullable
    @BindView(R.id.tv_diaglog_thanhtoan_message)
    TextView tvMessageDialog;
    @Nullable
    @BindView(R.id.tv_diaglog_thanhtoan_count_bill_payed)
    TextView tvCountBillPayedSuccessDialog;
    @Nullable
    @BindView(R.id.pbar_diaglog_thanhtoan)
    ProgressBar pbarDialogBilling;
    @Nullable
    @BindView(R.id.rv_dialog_thanhtoan_request_pay)
    RelativeLayout rvBillOnline;

    //Dialog delete bill online
    @Nullable
    @BindView(R.id.tv_dialog_delete_bill_online_code_customer)
    TextView tvCodeCustomerDeleteDialog;
    @Nullable
    @BindView(R.id.tv_dialog_delete_bill_online_name_customer)
    TextView tvNameCustomerDeleteDialog;
    @Nullable
    @BindView(R.id.tv_dialog_delete_bill_online_term)
    TextView tvTermBillDeleteDialog;
    @Nullable
    @BindView(R.id.tv_dialog_delete_bill_online_amount)
    TextView tvAmountBillDeleteDialog;
    @Nullable
    @BindView(R.id.et_dialog_delete_bill_online_reason)
    EditText etReasonDeleteBillDeleteDialog;
    @Nullable
    @BindView(R.id.btn_dialog_delete_bill_ignore)
    Button btnIgnoreDeleteDialog;
    @Nullable
    @BindView(R.id.btn_dialog_delete_bill_continued)
    Button btnContinuedDeleteDialog;
    @Nullable
    @BindView(R.id.tv_dialog_delete_bill_message_online)
    TextView tvMessageBillDeleteDialog;
    @Nullable
    @BindView(R.id.pbar_dialog_delete_bill_online)
    ProgressBar pbarBillDeleteDialog;
    @Nullable
    @BindView(R.id.card_dialog_delete_bill_online_message)
    CardView cardMessage;

    //Dialog barcode
    @Nullable
    @BindView(R.id.btn_dialog_barcode_cancel)
    Button btnCancelTextBarcode;
    @Nullable
    @BindView(R.id.btn_dialog_barcode_ok)
    Button btnOKTextBarcode;
    @Nullable
    @BindView(R.id.tv_dialog_barcode_text)
    TextView tvTextBarcode;
    @Nullable
    @BindView(R.id.ll_dialog_barcode)
    LinearLayout llBarcode;

    private OnPayFragmentInteractionListener listener;
    private PayAdapter payAdapter;
    private IPayPresenter mIPayPresenter;
    private String mEdong;
    private int mPageIndex;
    private Unbinder unbinder;
    private Common.TYPE_SEARCH typeSearch;
    private PayBillsDialogAdapter payBillsDialogAdapter;
    private View rootView;
    private Dialog dialogPayingOnline, dialogDeleteBillOnline, dialogBarcode;
    public static final int REQUEST_BARCODE = 999;
    public static final int RESPONSE_BARCODE = 1000;

    public enum VISIBLE_BUTTON_DELETE_DIALOG {
        SHOW_ALL(0),
        HIDE_ALL(1),
        HIDE_CANCEL(2),
        HIDE_COUNTINUE(3);

        private final int value;

        VISIBLE_BUTTON_DELETE_DIALOG(int value) {
            this.value = value;
        }
    }

    public enum TYPE_PAYING_PROCESS {
        HIDE_ALL(1, "Hide all"),
        IS_PAYING(2, "Show pbar && is paying"),
        FINISH_PAYED(3, "Show pbar && finish payed"),
        SHOW_MESSAGE(4, "show pbar && show message");

        TYPE_PAYING_PROCESS(int code, String description) {
            this.code = code;
            this.description = description;
        }

        private int code;
        private String description;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static PayFragment newInstance(String edong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, edong);

        PayFragment payFragment = new PayFragment();
        payFragment.setArguments(bundle);

        return payFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnPayFragmentInteractionListener)
            listener = (OnPayFragmentInteractionListener) activity;
        else
            throw new ClassCastException("activity must be implement OnPayFragmentInteractionListener!");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        rootView = inflater.inflate(R.layout.fragment_thanh_toan, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        viewPager.setAdapter(new TimKiemAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        mIPayPresenter = new PayPresenter(this);
        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);
        setUpRecyclerFragment(rootView);
        //first page
        typeSearch = Common.TYPE_SEARCH.ALL;
        mPageIndex = FIRST_PAGE_INDEX;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPayFragmentInteractionListener) {
            listener = (OnPayFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPayFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    //region onClick fragment
    @Optional
    @OnClick(R.id.btn_frag_thanh_toan_next)
    public void clickNext(View view) {
        if (payAdapter == null)
            return;

        mPageIndex += PAGE_INCREMENT;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    @Optional
    @OnClick(R.id.btn_frag_thanh_toan_pre)
    public void clickPre(View view) {
        if (payAdapter == null)
            return;

        mPageIndex -= PAGE_INCREMENT;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_action_cancel_search_online)
    public void clickCancelSearchOnline(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        showSearchOnlineProcess();

        pbarSearchOnline.setIndeterminate(false);
        ibtnCancelSearchOnline.setVisibility(View.GONE);
        ibtnResearchOnline.setVisibility(View.VISIBLE);

        mIPayPresenter.cancelSeachOnline();
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_action_research_online)
    public void clickResearchOnline(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        showSearchOnlineProcess();
        mIPayPresenter.reseachOnline(mEdong);
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_clear_text_search)
    public void clickClearTextSearch(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        etSearch.setText("");
    }

    @Optional
    @OnClick(R.id.btn_frag_thanh_toan_paying)
    public void clickPaying(View view) {
        hideSearchOnlineProcess();
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        mIPayPresenter.callShowDialogPay();
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_qrcode)
    public void clickBarcode(View view) {
        isScannerBarcode = true;
        ((MainActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIPayPresenter.callShowDialogBarcode();
            }
        });
    }

    //endregion

    //region onClick dialog PayingOnline
    @Optional
    @OnClick(R.id.btn_dialog_thanhtoan_cancel)
    public void clickCancelPayingOnlineDialog(View view) {
        if (dialogPayingOnline.isShowing())
            dialogPayingOnline.dismiss();
    }

    @Optional
    @OnClick(R.id.btn_dialog_thanhtoan_pay)
    public void clickPayPayingOnlineDialog(View view) {

        mIPayPresenter.callPayingBillOnline(mEdong);
    }
    //endregion

    //region onClick dialogDeleteBillOnline
    @Optional
    @OnClick(R.id.btn_dialog_delete_bill_ignore)
    public void clickIgnoreDeleteBillDialog(View view) {
        if (dialogDeleteBillOnline.isShowing())
            dialogDeleteBillOnline.dismiss();
    }

    @Optional
    @OnClick(R.id.btn_dialog_delete_bill_continued)
    public void clickContinuedDialog(View view) {
        mIPayPresenter.callDeleteOnlineSoap(mEdong, etReasonDeleteBillDeleteDialog.getText().toString().trim());
    }
    //endregion

    //region onClick dialog Barcode
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
    //endregion

    //region listener etTextBarcode
    @Optional
    @OnTextChanged(R.id.et_frag_thanh_toan_search)
    public void onTextChangeBarcode(CharSequence s, int start, int before, int count) {
        if (dialogBarcode == null || tvTextBarcode == null)
            return;

        tvTextBarcode.setEnabled(!textBarcode.equals(Common.TEXT_EMPTY) ? true : false);
    }

    //endregion

    //region listener tablayout
    @Optional
    @OnPageChange(R.id.vpage_frag_thanh_toan)
    public void onPageSelected(int position) {
        mPageIndex = FIRST_PAGE_INDEX;
        typeSearch = Common.TYPE_SEARCH.findMessage(position);
//        boolean isSeachOnline = checkUserNeedSearchOnline(etSearch.getText().toString());
//        if (isSeachOnline)
//            return;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString().trim(), false);
    }

    private boolean checkUserNeedSearchOnline(String infoSearch) {
        if ((infoSearch.length() == Common.LENGTH_MIN && String.valueOf(infoSearch.charAt(Common.ZERO)).equalsIgnoreCase(Common.SYMBOL_FIRST)) || infoSearch.length() == Common.LENGTH_MAX) {
            return true;
        }
        return false;
    }
    //endregion

    //region listener et search
    @Optional
    @OnFocusChange(R.id.et_frag_thanh_toan_search)
    public void onFocusChange(boolean hasFocus) {
        if (hasFocus) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    @Optional
    @OnTextChanged(R.id.et_frag_thanh_toan_search)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //first page
        mPageIndex = FIRST_PAGE_INDEX;
        boolean isSeachOnline = checkUserNeedSearchOnline(etSearch.getText().toString());

        if (isSeachOnline) {
            typeSearch = Common.TYPE_SEARCH.MA_KH_SO_THE;
            mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), isSeachOnline);
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(typeSearch.getPosition());
                    tabLayout.setupWithViewPager(viewPager);
                }
            });

//            etSearch.post(new Runnable() {
//                @Override
//                public void run() {
//                    etSearch.setHint(etSearch.getText().toString().concat(Common.TEXT_MULTI_SPACE).concat(Common.TEXT_SEARCHING));
//                }
//            });
        } else {
            if (typeSearch != Common.TYPE_SEARCH.ALL)
                mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), isSeachOnline);
            hideSearchOnlineProcess();
        }
    }

    /*@OnClick(R.id.etSearch)
    public void doClick(View view) {
        mPageIndex = FIRST_PAGE_INDEX;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString());
        //get size real tablayout
//        showRecyclerViewWidthTabLayout();
    }*/

    //endregion

    //region listerner et reason delete bill
    @Optional
    @OnTextChanged(R.id.et_dialog_delete_bill_online_reason)
    public void onTextChangedReasonDeleteBill(CharSequence s, int start, int before, int count) {
        hideAllProcessDeleteBillOnline();
    }
    //endregion

    //region barcode result
    public void fillResultToTextBarcodeDialog(String text) {
        if (TextUtils.isEmpty(text) || tvTextBarcode == null)
            return;
        tvTextBarcode.setText(text);
//        if (TextUtils.isEmpty(text) || etSearch == null)
//            return;
//        etSearch.setText(text);
    }

    //endregion

    //region IPayView
    @Override
    public Context getContextView() {
        return getContext();
    }

    @Override
    public void showPayRecyclerPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage, String infoSearch, boolean isSeachOnline) {
        btnPre.setEnabled(true);
        btnNext.setEnabled(true);
        tvPage.setText(String.valueOf(pageIndex).concat(Common.TEXT_SLASH).concat(String.valueOf(totalPage)));

        //enable disable button pre next
        if (pageIndex == FIRST_PAGE_INDEX) {
            setEnablePreNext(1);
            if (FIRST_PAGE_INDEX == totalPage)
                setEnablePreNext(0);
            else
                setEnablePreNext(1);
        } else if (pageIndex == totalPage) {
            if (totalPage == FIRST_PAGE_INDEX)
                setEnablePreNext(0);
            else
                setEnablePreNext(2);
        } else
            setEnablePreNext(3);


        payAdapter = new PayAdapter(this.getContext(), this, adapterList);
        rvKH.setAdapter(payAdapter);
        rvKH.invalidate();

        if (adapterList.size() == ZERO) {
            showTextNoData();
        }
        //if isSeachOnline
        if (isSeachOnline == false || infoSearch == null)
            return;
        mIPayPresenter.callSearchOnline(mEdong, infoSearch, true);
    }

    @Override
    public void showSearchOnlineProcess() {
        if (rvProgressSearchOnline.getVisibility() == View.GONE)
            rvProgressSearchOnline.setVisibility(View.VISIBLE);

        pbarSearchOnline.setVisibility(View.VISIBLE);
        pbarSearchOnline.setIndeterminate(true);
        ibtnResearchOnline.setVisibility(View.GONE);
        ibtnCancelSearchOnline.setVisibility(View.VISIBLE);
        tvMessageNotifySearchOnlne.setVisibility(View.GONE);
    }

    @Override
    public void hideSearchOnlineProcess() {
        if (rvProgressSearchOnline.getVisibility() == View.VISIBLE)
            rvProgressSearchOnline.setVisibility(View.GONE);
    }

    @Override
    public void showMessageNotifySearchOnline(String message) {
        if (message == null || message.isEmpty() || message.trim().equals(Common.TEXT_EMPTY))
            return;

        pbarSearchOnline.setVisibility(View.GONE);
        ibtnResearchOnline.setVisibility(View.VISIBLE);
        ibtnCancelSearchOnline.setVisibility(View.GONE);
        tvMessageNotifySearchOnlne.setVisibility(View.VISIBLE);
        tvMessageNotifySearchOnlne.setText(message);
    }

    @Override
    public void showEditTextSearch(String value) {
        if (value == null)
            return;

        etSearch.setText(value);
    }

    @Override
    public void showCountBillsAndTotalMoneyFragment(int size, long totalMoneyAllBills) {
        tvTotalBills.setText(String.valueOf(size));
        tvTotalBillsMoney.setText(Common.convertLongToMoney(totalMoneyAllBills));
    }

    @Override
    public void showPayRecyclerListBills(List<PayBillsDialogAdapter.Entity> listBillChecked) {
        if (listBillChecked == null)
            return;

        setUpRecyclerDialog();

//        if (payBillsDialogAdapter == null) {
        payBillsDialogAdapter = new PayBillsDialogAdapter(this.getContext(), listBillChecked);
        rvListBill.setAdapter(payBillsDialogAdapter);
//        } else
//            payBillsDialogAdapter.refreshData(listBillChecked);

        rvListBill.invalidate();
    }

    private void setUpRecyclerDialog() {
        rvListBill.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showCountBillsAndTotalMoneyInDialog(int totalBillsInDialog, long totalMoneyInDialog) {
        if (tvTotalBillsDialog == null)
            return;
        if (tvTotalBillsMoneyDialog == null)
            return;

        tvTotalBillsDialog.setText(String.valueOf(totalBillsInDialog));
        tvTotalBillsMoneyDialog.setText(Common.convertLongToMoney(totalMoneyInDialog));
    }

    @Override
    public void showMessageNotifyBillOnlineDialog(String message) {
        if (message == null)
            return;
        if (isHasNullViewPayingOnlineDialog())
            return;

        showPayingRViewDialogStart();
        showPayingRviewDialogFinish();

        tvMessageDialog.setVisibility(View.VISIBLE);
        tvMessageDialog.setText(message);
    }

    @Override
    public void showPayingRViewDialogStart() {
        if (isHasNullViewPayingOnlineDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(TYPE_PAYING_PROCESS.IS_PAYING);
    }

    @Override
    public void showPayingRviewDialogFinish() {
        if (isHasNullViewPayingOnlineDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(TYPE_PAYING_PROCESS.FINISH_PAYED);
    }

    @Override
    public void showPayingRviewDialogMessage() {
        if (isHasNullViewPayingOnlineDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(TYPE_PAYING_PROCESS.SHOW_MESSAGE);
    }

    @Override
    public void hidePayingRViewDialog() {
        if (isHasNullViewPayingOnlineDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(TYPE_PAYING_PROCESS.HIDE_ALL);
    }

    @Override
    public void showTextCountBillsPayed(int countBillPayedSuccess, int totalBillsDialog) {
        if (isHasNullViewPayingOnlineDialog())
            return;
        tvCountBillPayedSuccessDialog.setText(countBillPayedSuccess + Common.TEXT_SLASH + totalBillsDialog + Common.TEXT_BILL);
    }

    @Override
    public void showTextCountBillsPayedSuccess(int countBillPayedSuccess, int totalBillsChooseDialog) {
        if (isHasNullViewPayingOnlineDialog())
            return;

        tvCountBillPayedSuccessDialog.setText(countBillPayedSuccess + Common.TEXT_SLASH + totalBillsChooseDialog + Common.TEXT_SPACE + Common.TEXT_BILL);
    }

    @Override
    public void showDialogPayingOnline() {
        if (this.getActivity() instanceof CallbackPayingOnlineDialog) {
            final CallbackPayingOnlineDialog callbackPayingOnlineDialog = (CallbackPayingOnlineDialog) getContextView();

            dialogPayingOnline = new Dialog(this.getActivity());
            dialogPayingOnline.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogPayingOnline.setContentView(R.layout.dialog_thanhtoan);
            dialogPayingOnline.setCanceledOnTouchOutside(true);
            dialogPayingOnline.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialogPayingOnline.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialogPayingOnline.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialogPayingOnline.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            unbinder.unbind();
            unbinder = ButterKnife.bind(this, dialogPayingOnline);

            rvBillOnline.setVisibility(View.INVISIBLE);

            setUpRecyclerDialog();
            mIPayPresenter.callPayRecyclerDialog(mEdong);

            dialogPayingOnline.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    callbackPayingOnlineDialog.processOnDismissPayingOnlineDialog();
                }
            });
            dialogPayingOnline.show();
        } else
            Log.e(TAG, "showDialogPayingOnline: fragment cannot implement CallbackPayingOnlineDialog");
    }

    @Override
    public void showDialogDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        if (this.getActivity() instanceof CallbackDeleteBillOnlineDialog) {
            final CallbackDeleteBillOnlineDialog callbackDeleteBillOnlineDialog = (CallbackDeleteBillOnlineDialog) getContextView();

            dialogDeleteBillOnline = new Dialog(this.getActivity());
            dialogDeleteBillOnline.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDeleteBillOnline.setContentView(R.layout.dialog_delete_bill_online);
            dialogDeleteBillOnline.setCanceledOnTouchOutside(true);
            dialogDeleteBillOnline.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialogDeleteBillOnline.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialogDeleteBillOnline.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Window window = dialogDeleteBillOnline.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            unbinder.unbind();
            unbinder = ButterKnife.bind(this, dialogDeleteBillOnline);

            hideAllProcessDeleteBillOnline();
            mIPayPresenter.callFillInfoBillDeleteDialog(edong, code, bill, posCustomerInside);

            dialogDeleteBillOnline.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    callbackDeleteBillOnlineDialog.processOnDismissDeleteBillOnlineDialog();
                }
            });

            dialogDeleteBillOnline.show();
        } else
            Log.e(TAG, "showdialogDeleteBillOnline: fragment cannot implement callbackDeleteBillOnlineDialog");
    }

    @Override
    public void showInfoBillDeleteDialog(String customerPayCode, String tenKH, String monthBill, long moneyBill) {
        boolean fail = TextUtils.isEmpty(customerPayCode) ||
                TextUtils.isEmpty(tenKH) ||
                TextUtils.isEmpty(monthBill) ||
                isHasNullViewDeleteBillOnlineDialog();
        if (fail) return;

        tvCodeCustomerDeleteDialog.setText(customerPayCode);
        tvNameCustomerDeleteDialog.setText(tenKH);
        tvTermBillDeleteDialog.setText(monthBill);
        tvAmountBillDeleteDialog.setText(Common.convertLongToMoney(moneyBill));
    }


    @Override
    public void showDeleteBillOnlineProcess() {
        if (isHasNullViewDeleteBillOnlineDialog())
            return;
    }

    @Override
    public void showPbarDeleteBillOnline() {
        cardMessage.setVisibility(View.VISIBLE);
        tvMessageBillDeleteDialog.setVisibility(View.GONE);
        pbarBillDeleteDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAllProcessDeleteBillOnline() {
        cardMessage.setVisibility(View.INVISIBLE);
        tvMessageBillDeleteDialog.setVisibility(View.INVISIBLE);
        pbarBillDeleteDialog.setVisibility(View.GONE);
    }

    @Override
    public void showMessageNotifyDeleteOnlineDialog(String message) {
        if (TextUtils.isEmpty(message))
            return;
        cardMessage.setVisibility(View.VISIBLE);
        pbarBillDeleteDialog.setVisibility(View.GONE);
        tvMessageBillDeleteDialog.setVisibility(View.VISIBLE);
        tvMessageBillDeleteDialog.setText(message);
    }

    @Override
    public void visibleButtonDeleteDialog(PayFragment.VISIBLE_BUTTON_DELETE_DIALOG type) {
        if (isHasNullViewDeleteBillOnlineDialog())
            return;
        btnContinuedDeleteDialog.setEnabled(true);
//        btnIgnoreDeleteDialog.setEnabled(true);

        etReasonDeleteBillDeleteDialog.setEnabled(true);

        if (type == VISIBLE_BUTTON_DELETE_DIALOG.HIDE_ALL) {
            btnContinuedDeleteDialog.setEnabled(false);
            etReasonDeleteBillDeleteDialog.setEnabled(false);
        }

        if (type == VISIBLE_BUTTON_DELETE_DIALOG.HIDE_CANCEL) {
            btnContinuedDeleteDialog.setEnabled(false);
            etReasonDeleteBillDeleteDialog.setEnabled(false);
        }

        if (type == VISIBLE_BUTTON_DELETE_DIALOG.HIDE_COUNTINUE) {
            btnContinuedDeleteDialog.setEnabled(false);
            etReasonDeleteBillDeleteDialog.setEnabled(false);
        }

        if (type == VISIBLE_BUTTON_DELETE_DIALOG.SHOW_ALL) {
            btnContinuedDeleteDialog.setEnabled(true);
            etReasonDeleteBillDeleteDialog.setEnabled(true);
        }
    }

    @Override
    public void enableReasonEditText() {
        if (isHasNullViewDeleteBillOnlineDialog())
            return;

        etReasonDeleteBillDeleteDialog.setEnabled(true);
    }

    @Override
    public void showRecyclerFragment() {
        if (rvKH == null || tvNoData == null)
            return;
        rvKH.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void showTextNoData() {
        if (rvKH == null || tvNoData == null)
            return;
        Common.runAnimationClickViewScale(tvNoData, R.anim.twinking_view, Common.TIME_DELAY_ANIM);
        rvKH.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
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

    private boolean isHasNullViewDeleteBillOnlineDialog() {
        return tvCodeCustomerDeleteDialog == null || tvNameCustomerDeleteDialog == null || tvTermBillDeleteDialog == null
                || tvAmountBillDeleteDialog == null || etReasonDeleteBillDeleteDialog == null || btnContinuedDeleteDialog == null
                || btnIgnoreDeleteDialog == null || tvMessageBillDeleteDialog == null || pbarBillDeleteDialog == null || cardMessage == null;
    }

    private void setEnablePreNext(int i) {
        if (i == 0) {
            btnPre.setEnabled(false);
            btnNext.setEnabled(false);
        }
        if (i == 1) {
            btnPre.setEnabled(false);
            btnNext.setEnabled(true);
        }
        if (i == 2) {
            btnPre.setEnabled(true);
            btnNext.setEnabled(false);
        }
        if (i == 3) {
            btnPre.setEnabled(true);
            btnNext.setEnabled(true);
        }
    }

    public void showBillCheckedFragment(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomer) {
        mIPayPresenter.callProcessDataBillFragmentChecked(edong, code, bill, posCustomer);
    }

    public void processDialogDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        mIPayPresenter.callProcessDeleteBillOnline(edong, code, bill, posCustomerInside);
    }

    public void showBillCheckedDialog(String edong, int pos, boolean isChecked) {
        mIPayPresenter.callProcessDataBillDialogChecked(edong, pos, isChecked);
    }

    public void bindViewAgain() {
        if (unbinder != null)
            unbinder.unbind();
        unbinder = ButterKnife.bind(this, rootView);
    }

    private boolean isHasNullViewPayingOnlineDialog() {
        return rvBillOnline == null || pbarDialogBilling == null || tvMessageDialog == null;
    }

    private void setVisibleViewDialogBillOnlineProcess(TYPE_PAYING_PROCESS type) {
        if (isHasNullViewPayingOnlineDialog())
            return;
        tvCountBillPayedSuccessDialog.setVisibility(View.VISIBLE);
        //hide all
        if (type == TYPE_PAYING_PROCESS.HIDE_ALL) {
            rvBillOnline.setVisibility(View.INVISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && is paying
        if (type == TYPE_PAYING_PROCESS.IS_PAYING) {
            rvBillOnline.setVisibility(View.VISIBLE);
            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.VISIBLE);
            tvMessageDialog.setVisibility(View.GONE);

            btnPayDialog.setEnabled(false);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && finish payed
        if (type == TYPE_PAYING_PROCESS.FINISH_PAYED) {
            rvBillOnline.setVisibility(View.VISIBLE);
            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.GONE);
            tvMessageDialog.setVisibility(View.VISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && show message
        if (type == TYPE_PAYING_PROCESS.SHOW_MESSAGE) {
            rvBillOnline.setVisibility(View.VISIBLE);
//            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.GONE);
            tvMessageDialog.setVisibility(View.VISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }
    }

    public void refreshRecyclerListFragment() {
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    private void setUpRecyclerFragment(final View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (rvKH != null)
            rvKH.setLayoutManager(linearLayoutManager);
    }
    //endregion

    public void onPauseScannerBarcode() {
        if (mScannerView == null
                || isScannerBarcode == false
                )
            return;

        mScannerView.stopCamera();
    }

    public void fillResultToSearchText(String textBarcode) {
        if (TextUtils.isEmpty(textBarcode) || etSearch == null)
            return;
        etSearch.setText(textBarcode);
    }

    public interface OnPayFragmentInteractionListener {
        void fillToSearchText(String textBarcode);

        void setRootViewAgain();

        void refreshCamera(final ZXingScannerView mScannerView);

    }

    public interface CallbackPayingOnlineDialog {
        void processOnDismissPayingOnlineDialog();
    }

    public interface CallbackBarcodeDialog {
        void processOnDismissBarcodeDialog(String textBarcode);
    }

    public interface CallbackDeleteBillOnlineDialog {
        void processOnDismissDeleteBillOnlineDialog();
    }
}
