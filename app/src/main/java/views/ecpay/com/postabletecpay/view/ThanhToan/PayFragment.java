package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController;
import com.sewoo.jpos.command.ESCPOSConst;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.Optional;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.presenter.PayPresenter;
import views.ecpay.com.postabletecpay.util.DialogHelper.Inteface.IActionClickYesNoDialog;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.Printer.ESCPOSSample;
import views.ecpay.com.postabletecpay.view.Printer.Printer;
import views.ecpay.com.postabletecpay.view.Printer.ReceiptUtility;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;
import views.ecpay.com.postabletecpay.view.Util.BarcodeScannerDialog;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_ANIM;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;

/**
 * Created by macbook on 4/28/17.
 */

public class PayFragment extends Fragment implements
        IPayView, PayBillsDialogAdapter.OnInteractionBillDialogRecycler {
    public static final int FIRST_PAGE_INDEX = 1;
    public static final int PAGE_INCREMENT = 1;
    public static final int ROWS_ON_PAGE = 10;
    @Nullable
    @BindView(R.id.et_frag_thanh_toan_search)
    EditText etSearch;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_qrcode)
    ImageButton btnBarcode;
    @Nullable
    @BindView(R.id.ibtn_frag_thanhtoan_back)
    ImageButton btnBack;
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
    @Nullable
    @BindView(R.id.spine_provider)
    Spinner spine_provider;
    @Nullable
    @BindView(R.id.ibtn_frag_thanh_toan_add)
    ImageButton ibtn_frag_thanh_toan_add;

    //Search online
    @Nullable
    @BindView(R.id.rv_frag_thanhtoan_search_online_progress)
    RelativeLayout rvProgressSearchOnline;
    @Nullable
    @BindView(R.id.tv_search)
    TextView tvTitleSearch;
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
    RecyclerView rvListBillDialog;
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

    private PayAdapter payAdapter;
    private IPayPresenter mIPayPresenter;
    private String mEdong;
    private int mPageIndex, mPageTotal;
    private Unbinder unbinder;
    private Common.TYPE_SEARCH typeSearch;
    private PayBillsDialogAdapter payBillsDialogAdapter;
    private View rootView;
    private Dialog dialogPayingOnline, dialogDeleteBillOnline;
    public static final int REQUEST_BARCODE = 999;
    public static final int RESPONSE_BARCODE = 1000;


    private Common.PROVIDER_CODE ProviderSelect = Common.PROVIDER_CODE.NCCNONE ;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Common.PROVIDER_CODE getProviderCodeSelected() {
        return  this.ProviderSelect;
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
        ((MainActivity)this.getContextView()).switchNavigationBottomMenu(MainActivity.ID_MENU_BOTTOM.PAY);
        setUpRecyclerFragment(rootView);
        //first page
        typeSearch = Common.TYPE_SEARCH.ALL;
        mPageIndex = FIRST_PAGE_INDEX;
        mPageTotal = mPageIndex;
        try {
            mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
        } catch (Exception e) {

        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < Common.PROVIDER_CODE.values().length; i ++)
        {
            list.add(Common.PROVIDER_CODE.values()[i].getMessage());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_provider);
        spine_provider.setAdapter(dataAdapter);
        spine_provider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProviderSelect = Common.PROVIDER_CODE.values()[position];

                if(ProviderSelect.getCode().equalsIgnoreCase(Common.PROVIDER_CODE.NCCNONE.getCode()))
                    return;

                if(checkUserNeedSearchOnline(etSearch.getText().toString()))
                {
                    showSearchOnlineProcess();
                    mIPayPresenter.reseachOnline(mEdong);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void updateBillSelectToPay(List<PayAdapter.BillEntityAdapter> lst)
    {
        long total = 0;
        for(int i = 0, n = lst.size(); i < n; i ++)
        {
            total += lst.get(i).getTIEN_THANH_TOAN();
        }
        this.showCountBillsAndTotalMoneyFragment(lst.size(), total);
    }

    //region onClick fragment
    @Optional
    @OnClick(R.id.btn_frag_thanh_toan_next)
    public void clickNext(View view) {
        if (payAdapter == null)
            return;

        mPageIndex += PAGE_INCREMENT;

        if(mPageIndex >= mPageTotal)
            mPageIndex = mPageTotal;

        try {
            mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Optional
    @OnClick(R.id.btn_frag_thanh_toan_pre)
    public void clickPre(View view) {
        if (payAdapter == null)
            return;

        mPageIndex -= PAGE_INCREMENT;
        if(mPageIndex < FIRST_PAGE_INDEX)
            mPageIndex = FIRST_PAGE_INDEX;
        try {
            mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        mIPayPresenter.callShowDialogPay();
    }
    @Optional
    @OnClick(R.id.ibtn_frag_thanh_toan_add)
    public void clickPaying2(View view) {
        hideSearchOnlineProcess();
//        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        mIPayPresenter.callShowDialogPay();
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_qrcode)
    public void clickBarcode(View view) {
        ((MainActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIPayPresenter.callShowDialogBarcode();
            }
        });
    }

    @Optional
    @OnClick(R.id.ibtn_frag_thanhtoan_back)
    public void clickBack(View view) {
        Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Fragment fragment = MainPageFragment.newInstance(mEdong);
                    if (fragment != null) {
                        FragmentTransaction fragmentTransaction = PayFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, fragment);
                        fragmentTransaction.commit();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, Common.TIME_DELAY_ANIM);
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
        mIPayPresenter.callPay();

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
    public void clickContinuedDeleteBillDialog(View view) {
        mIPayPresenter.callDeleteOnlineSoap(mEdong, etReasonDeleteBillDeleteDialog.getText().toString().trim());
    }
    //endregion


    //region listener tablayout
    @Optional
    @OnPageChange(R.id.vpage_frag_thanh_toan)
    public void onPageSelected(int position) {
        mPageIndex = FIRST_PAGE_INDEX;
        mPageTotal = mPageIndex;
        typeSearch = Common.TYPE_SEARCH.findMessage(position);
        boolean isSeachOnline = checkUserNeedSearchOnline(etSearch.getText().toString());
        if (isSeachOnline)
            return;
        try {
            mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString().trim(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }


    @Override
    public void onDestroyView() {
        mIPayPresenter.cancelSeachOnline();
        super.onDestroyView();
    }

    @Optional
    @OnTextChanged(R.id.et_frag_thanh_toan_search)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //first page
        mPageIndex = FIRST_PAGE_INDEX;
        mPageTotal = mPageIndex;
        final boolean isSeachOnline = checkUserNeedSearchOnline(etSearch.getText().toString());

        if (isSeachOnline) {
            typeSearch = Common.TYPE_SEARCH.MA_KH_SO_THE;
            try {
                mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), isSeachOnline);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(typeSearch.getPosition());
                    tabLayout.setupWithViewPager(viewPager);
                }
            });

        } else {
            if (typeSearch != Common.TYPE_SEARCH.ALL)
            {
                try {
                    mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), isSeachOnline);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hideSearchOnlineProcess();
        }
    }

    //endregion

    //region listerner et reason delete bill
    @Optional
    @OnTextChanged(R.id.et_dialog_delete_bill_online_reason)
    public void onTextChangedReasonDeleteBill(CharSequence s, int start, int before, int count) {
        hideAllProcessDeleteBillOnline();
    }
    //endregion

    //region IPayView
    @Override
    public Context getContextView() {
        return getContext();
    }

    //TODO mark Trang
    @Override
    public void showPayRecyclerPage(List<PayAdapter.DataAdapter> adapterList, int indexBegin, int totalPage, String infoSearch, boolean isSeachOnline) {
        try
        {
            mPageTotal = totalPage;

            btnPre.setEnabled(true);
            btnNext.setEnabled(true);
            tvPage.setText(String.valueOf(indexBegin).concat(Common.TEXT_SLASH).concat(String.valueOf(totalPage)));

            //enable disable button pre next
            if (indexBegin == FIRST_PAGE_INDEX) {
                setEnablePreNext(1);
                if (FIRST_PAGE_INDEX == totalPage)
                    setEnablePreNext(0);
                else
                    setEnablePreNext(1);
            } else if (indexBegin == totalPage) {
                if (totalPage == FIRST_PAGE_INDEX)
                    setEnablePreNext(0);
                else
                    setEnablePreNext(2);
            } else
                setEnablePreNext(3);
            if(payAdapter != null)
            {
                rvKH.removeAllViews();
                rvKH.invalidate();
            }

            payAdapter = new PayAdapter(this.getContext(), mIPayPresenter, adapterList, indexBegin);
            rvKH.setAdapter(payAdapter);
            rvKH.setLayoutManager(new LinearLayoutManager(getContext()));
            rvKH.setHasFixedSize(true);
            rvKH.invalidate();

            if (adapterList.size() == ZERO) {
                showTextNoData();
            }
        }catch (Exception e)
        {
            showTextNoData();
        }

//        //if isSeachOnline
//        if (isSeachOnline == false || infoSearch == null)
//            return;
//        mIPayPresenter.callSearchOnline(mEdong, infoSearch, true);
    }

    @Override
    public void showSearchOnlineProcess() {
        try
        {
            if (rvProgressSearchOnline.getVisibility() == View.GONE)
                rvProgressSearchOnline.setVisibility(View.VISIBLE);
            tvTitleSearch.setVisibility(View.VISIBLE);
            pbarSearchOnline.setVisibility(View.VISIBLE);
            pbarSearchOnline.setIndeterminate(true);
            ibtnResearchOnline.setVisibility(View.GONE);
            ibtnCancelSearchOnline.setVisibility(View.VISIBLE);
            tvMessageNotifySearchOnlne.setVisibility(View.GONE);
        }catch (Exception e)
        {

        }
    }

    @Override
    public void hideSearchOnlineProcess() {
        try {
            if (rvProgressSearchOnline.getVisibility() == View.VISIBLE)
                rvProgressSearchOnline.setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }

    @Override
    public void showMessageNotifySearchOnline(String message, Common.TYPE_DIALOG typeDialog) {
        if (message == null || message.isEmpty() || message.trim().equals(Common.TEXT_EMPTY))
            return;

        try
        {
            tvTitleSearch.setVisibility(View.VISIBLE);
            pbarSearchOnline.setVisibility(View.GONE);
            ibtnResearchOnline.setVisibility(View.VISIBLE);
            ibtnCancelSearchOnline.setVisibility(View.GONE);
            Common.runAnimationClickViewScale(tvMessageNotifySearchOnlne, R.anim.scale_view_pull, TIME_DELAY_ANIM);
            tvMessageNotifySearchOnlne.setVisibility(View.VISIBLE);
            tvMessageNotifySearchOnlne.setText(message);

            IActionClickYesNoDialog yesNoDialog = new IActionClickYesNoDialog() {
                @Override
                public void doClickNo() {

                }

                @Override
                public void doClickYes() {
                    //dismiss
                }
            };
            Common.showDialog(getContext(), yesNoDialog, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, false, typeDialog);
        }catch (Exception e)
        {

        }
    }

    /**
     * Show any message without search online on pay fragment
     *
     * @param message
     */
    @Override
    public void showMessageNotifyPayfrag(String message) {
        if (TextUtils.isEmpty(message))
            return;

        tvTitleSearch.setVisibility(View.VISIBLE);
        rvProgressSearchOnline.setVisibility(View.VISIBLE);
        pbarSearchOnline.setVisibility(View.GONE);
        ibtnCancelSearchOnline.setVisibility(View.GONE);
        ibtnResearchOnline.setVisibility(View.GONE);
        Common.runAnimationClickViewScale(tvMessageNotifySearchOnlne, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        tvMessageNotifySearchOnlne.setVisibility(View.VISIBLE);
        tvMessageNotifySearchOnlne.setText(message);

        IActionClickYesNoDialog yesNoDialog = new IActionClickYesNoDialog() {
            @Override
            public void doClickNo() {

            }

            @Override
            public void doClickYes() {
                //dismiss
            }
        };
        Common.showDialog(getContext(), yesNoDialog, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, false, Common.TYPE_DIALOG.LOI);
    }

    @Override
    public void showEditTextSearch(String value) {
        if (value == null)
            return;

        etSearch.setText(value);
    }

    @Override
    public void showCountBillsAndTotalMoneyFragment(int size, long totalMoneyAllBills) {
       try
       {
           tvTotalBills.setText(String.valueOf(size));
           tvTotalBillsMoney.setText(Common.convertLongToMoney(totalMoneyAllBills));
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    @Override
    public void refreshAdapterPayRecyclerListBills(boolean disableCheckBoxAll) {
        try
        {
            payBillsDialogAdapter.setDisableCheckbox(disableCheckBoxAll);
            payBillsDialogAdapter.notifyDataSetChanged();
            rvListBillDialog.invalidate();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void showPayRecyclerListBills(List<PayAdapter.BillEntityAdapter> listBillChecked) {
        if (listBillChecked == null)
            return;

        try
        {
            setUpRecyclerDialog();

            payBillsDialogAdapter = new PayBillsDialogAdapter(this, listBillChecked, false);
            rvListBillDialog.setAdapter(payBillsDialogAdapter);

            rvListBillDialog.invalidate();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerDialog() {
        rvListBillDialog.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void processClickPrinteBillDialog(PayAdapter.BillEntityAdapter billEntityAdapter) {
    }

    @Override
    public void processUnCheckedBillDialog(String message, Common.TYPE_DIALOG typeDialog) {
        if (message == null)
            return;
        IActionClickYesNoDialog yesNoDialog = new IActionClickYesNoDialog() {
            @Override
            public void doClickNo() {

            }

            @Override
            public void doClickYes() {
                //dismiss
            }
        };
        Common.showDialog(getContext(), yesNoDialog, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, false, typeDialog);
    }

    @Override
    public void PrintThongBaoDien(PayAdapter.DataAdapter data) {
        ArrayList<PayAdapter.BillEntityAdapter> billEntityAdapterArrayList = new ArrayList<PayAdapter.BillEntityAdapter>();
        for (int i = 0, n = data.getBillKH().size(); i < n; i ++) {
            if(data.getBillKH().get(i).isChecked()) {
                billEntityAdapterArrayList.add(data.getBillKH().get(i));
                Printer printer = new Printer(getActivity(),Printer.THONG_BAO,billEntityAdapterArrayList);
                printer.Action();
            }
            if (billEntityAdapterArrayList.size() == 0){
                Toast.makeText(getContext(),"Không có hóa đơn nào được chọn!",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void PrintHoaDon(PayAdapter.BillEntityAdapter bill) {
        Printer printer = new Printer(getActivity(),Printer.BIEN_NHAN,bill);
        printer.Action();
    }

    @Override
    public void showMessageNotifyBillOnlineDialog(String message, boolean isMutilMessage, Common.TYPE_DIALOG typeDialog, boolean isShowDialog) {
        if (message == null)
            return;
        if (isHasNullViewPayingOnlineDialog())
            return;

        showPayingRViewDialogStart();
        showPayingRviewDialogFinish();

        Common.runAnimationClickViewScale(tvMessageDialog, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        tvMessageDialog.setVisibility(View.VISIBLE);
        tvMessageDialog.setText(isMutilMessage ? tvMessageDialog.getText().toString() + Common.TEXT_ENTER + message : message);


        Log.d(TAG, "showMessageNotifyBillOnlineDialog: " + message);
        if (
                !isMutilMessage &&
                        isShowDialog) {
            IActionClickYesNoDialog yesNoDialog = new IActionClickYesNoDialog() {
                @Override
                public void doClickNo() {

                }

                @Override
                public void doClickYes() {
                    //dismiss
                }
            };
            Common.showDialog(PayFragment.this.getContextView(), yesNoDialog, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), tvMessageDialog.getText().toString(), false, typeDialog);
        }
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

                    for (int i = mIPayPresenter.getPayModel().getListBillSelected().size() - 1; i >= 0; i --)
                    {
                        PayAdapter.BillEntityAdapter bill = mIPayPresenter.getPayModel().getListBillSelected().get(i);

                        if(!bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                        {
                            payAdapter.UpdateBill(bill);
                        }

                        if(!bill.isChecked() || !bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                        {
                            mIPayPresenter.getPayModel().removeBillSelect(bill.getBillId());
                        }
                    }

                    try
                    {
                        bindViewAgain();
                        payAdapter.notifyDataSetChanged();
                        rvKH.invalidate();

                        updateBillSelectToPay(mIPayPresenter.getPayModel().getListBillSelected());
                    }catch (Exception e)
                    {

                    }
                }
            });
            dialogPayingOnline.show();
    }

    @Override
    public void showDialogDeleteBillOnline(String edong, PayAdapter.BillEntityAdapter bill, final PayAdapter.BillInsidePayAdapter adapter) {
        boolean fail = TextUtils.isEmpty(edong) ||  bill == null;
        if (fail)
            return;
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
            mIPayPresenter.callFillInfoBillDeleteDialog(edong, bill);

            dialogDeleteBillOnline.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    try
                    {
                        bindViewAgain();
                        if(adapter != null)
                        {
                            adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            dialogDeleteBillOnline.show();
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
        try {
            cardMessage.setVisibility(View.VISIBLE);
            tvMessageBillDeleteDialog.setVisibility(View.GONE);
            pbarBillDeleteDialog.setVisibility(View.VISIBLE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void hideAllProcessDeleteBillOnline() {
        try {
            cardMessage.setVisibility(View.INVISIBLE);
            tvMessageBillDeleteDialog.setVisibility(View.INVISIBLE);
            pbarBillDeleteDialog.setVisibility(View.GONE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void showMessageNotifyDeleteOnlineDialog(String message, Common.TYPE_DIALOG typeDialog) {
        if (TextUtils.isEmpty(message))
            return;
        cardMessage.setVisibility(View.VISIBLE);
        pbarBillDeleteDialog.setVisibility(View.GONE);
        Common.runAnimationClickViewScale(tvMessageBillDeleteDialog, R.anim.scale_view_pull, TIME_DELAY_ANIM);
        tvMessageBillDeleteDialog.setVisibility(View.VISIBLE);
        tvMessageBillDeleteDialog.setText(message);

        IActionClickYesNoDialog yesNoDialog = new IActionClickYesNoDialog() {
            @Override
            public void doClickNo() {

            }

            @Override
            public void doClickYes() {
                //dismiss
            }
        };
        Common.showDialog(getContext(), yesNoDialog, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, false, typeDialog);
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
        if(rvKH.getVisibility() != View.VISIBLE)
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
        BarcodeScannerDialog dialog = new BarcodeScannerDialog((MainActivity) this.getActivity(), new BarcodeScannerDialog.OnResultListener() {
            @Override
            public void onResult(String text) {
                fillResultToSearchText(text);
            }
        });
        dialog.show();
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

    public void showBillCheckedFragment(String edong, String code, int posCustomer, PayAdapter.BillEntityAdapter bill, int posBillInside, int indexBegin, int indexEnd) {
        mIPayPresenter.callProcessDataBillFragmentChecked(edong, code, posCustomer, bill, posBillInside, indexBegin, indexEnd);
    }

    @Override
    public void processDialogDeleteBillOnline(String edong, PayAdapter.BillEntityAdapter bill, PayAdapter.BillInsidePayAdapter adapter) {
        mIPayPresenter.callProcessDeleteBillOnline(edong,  bill, adapter);
    }

    @Override
    public void processCheckedBillsDialog(String edong, int pos, boolean isChecked) {
        mIPayPresenter.callProcessDataBillDialogChecked(edong, pos, isChecked);
    }

    @Override
    public  void processClickMessageErrorBillDialog(String messageError)
    {
        try
        {
            Toast.makeText(getContext(), messageError, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
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
            btnCancelDialog.setEnabled(false);
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

    private void setUpRecyclerFragment(final View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (rvKH != null)
            rvKH.setLayoutManager(linearLayoutManager);
    }
    //endregion

    public void fillResultToSearchText(String textBarcode) {
        if (TextUtils.isEmpty(textBarcode) || etSearch == null)
            return;
        etSearch.setText(textBarcode);
    }
    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this.getContext(), "CODE: " + code + "\nDESCRIPTION: " + description, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
