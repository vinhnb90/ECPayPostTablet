package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayListBillsAdapter;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.presenter.PayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by macbook on 4/28/17.
 */

public class PayFragment extends Fragment implements
        IPayView{
    public static final int FIRST_PAGE_INDEX = 1;
    public static final int PAGE_INCREMENT = 1;
    public static final int ROWS_ON_PAGE = 10;

    @Nullable
    @BindView(R.id.et_frag_thanh_toan_search)
    EditText etSearch;
    @Nullable
    @BindView(R.id.ll_frag_thanh_toan_count)
    LinearLayout llCount;
    @Nullable
    @BindView(R.id.rv_frag_thanh_toan_customer)
    RecyclerView rvKH;
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

    private OnFragmentInteractionListener listener;
    private PayAdapter payAdapter;
    private IPayPresenter mIPayPresenter;
    private String mEdong;
    private int mPageIndex;
    private Unbinder unbinder;
    private Common.TYPE_SEARCH typeSearch;
    private PayListBillsAdapter payListBillsAdapter;
    private View rootView;
    private Dialog dialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_thanh_toan, container, false);
        unbinder = ButterKnife.bind(this, rootView);

       /* ibBack.setOnClickListener(this);
        ibScaner.setOnClickListener(this);
        ibAdd.setOnClickListener(this);
        btnPay.setOnClickListener(this);*/

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

    private void setUpRecyclerFragment(final View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (rvKH != null)
            rvKH.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
        showDialogThanhToan();
    }
    //endregion

    //region onClick dialog
    @Optional
    @OnClick(R.id.btn_dialog_thanhtoan_cancel)
    public void clickCancelDialog(View view) {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Optional
    @OnClick(R.id.btn_dialog_thanhtoan_pay)
    public void clickPayDialog(View view) {
        mIPayPresenter.callPayOnline(mEdong);
    }
    //endregion

    //region listener tablayout
    @Optional
    @OnPageChange(R.id.vpage_frag_thanh_toan)
    public void onPageSelected(int position) {
        mPageIndex = FIRST_PAGE_INDEX;
        Common.TYPE_SEARCH typeSearch = Common.TYPE_SEARCH.findMessage(position);
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

        //set adapter
        if (pageIndex == FIRST_PAGE_INDEX) {
            payAdapter = new PayAdapter(this.getContext(), this, adapterList);
            rvKH.setAdapter(payAdapter);
        } else
            rvKH.invalidate();

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
        tvTotalBillsMoney.setText(String.valueOf(totalMoneyAllBills) + Common.TEXT_SPACE + Common.UNIT_MONEY);
    }

    @Override
    public void showPayRecyclerListBills(List<PayListBillsAdapter.Entity> listBillChecked) {
        if (listBillChecked == null)
            return;

        setUpRecyclerDialog();

//        if (payListBillsAdapter == null) {
            payListBillsAdapter = new PayListBillsAdapter(this.getContext(), listBillChecked);
            rvListBill.setAdapter(payListBillsAdapter);
//        } else
//            payListBillsAdapter.refreshData(listBillChecked);

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
        tvTotalBillsMoneyDialog.setText(String.valueOf(totalMoneyInDialog) + Common.TEXT_SPACE + Common.UNIT_MONEY);
    }

    @Override
    public void showMessageNotifyBillOnlineDialog(String message) {
        if (message == null)
            return;
        if (isHasNullViewDialog())
            return;

        showPayingRViewStart();
        showPayingRviewFinish();

        tvMessageDialog.setVisibility(View.VISIBLE);
        tvMessageDialog.setText(message);
    }

    @Override
    public void showPayingRViewStart() {
        if (isHasNullViewDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(2);
    }

    @Override
    public void showPayingRviewFinish() {
        if (isHasNullViewDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(3);
    }

    @Override
    public void showPayingRviewMessage() {
        if (isHasNullViewDialog())
            return;

        setVisibleViewDialogBillOnlineProcess(4);
    }

    @Override
    public void showTextCountBillsPayed(int countBillPayedSuccess, int totalBillsDialog) {
        if (isHasNullViewDialog())
            return;
        tvCountBillPayedSuccessDialog.setText(countBillPayedSuccess + Common.TEXT_SLASH + totalBillsDialog + Common.TEXT_BILL);
    }

    @Override
    public void showTextCountBillsPayedSuccess(int countBillPayedSuccess, int totalBillsChooseDialog) {
        if (isHasNullViewDialog())
            return;

        tvCountBillPayedSuccessDialog.setText(countBillPayedSuccess + Common.TEXT_SLASH + totalBillsChooseDialog + Common.TEXT_SPACE + Common.TEXT_BILL);
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

    public void showBillCheckedDialog(int pos, boolean isChecked) {
        mIPayPresenter.callProcessDataBillDialogChecked(pos, isChecked);
    }

    public void bindViewAgain() {
        if (unbinder != null)
            unbinder.unbind();
        unbinder = ButterKnife.bind(this, rootView);
    }

    private boolean isHasNullViewDialog() {
        return rvBillOnline == null || pbarDialogBilling == null || tvMessageDialog == null;
    }

    private void setVisibleViewDialogBillOnlineProcess(int type) {
        if (isHasNullViewDialog())
            return;

        //hide all
        if (type == 1) {
            rvBillOnline.setVisibility(View.INVISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && is paying
        if (type == 2) {
            rvBillOnline.setVisibility(View.VISIBLE);
            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.VISIBLE);
            tvMessageDialog.setVisibility(View.GONE);

            btnPayDialog.setEnabled(false);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && finish payed
        if (type == 3) {
            rvBillOnline.setVisibility(View.VISIBLE);
            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.GONE);
            tvMessageDialog.setVisibility(View.VISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }

        //show pbar && show message
        if (type == 4) {
            rvBillOnline.setVisibility(View.VISIBLE);
            mIPayPresenter.refreshTextCountBillPayedSuccess();
            pbarDialogBilling.setVisibility(View.GONE);
            tvMessageDialog.setVisibility(View.VISIBLE);

            btnPayDialog.setEnabled(true);
            btnCancelDialog.setEnabled(true);
        }

    }

    //endregion

    public interface OnFragmentInteractionListener {
    }

    private void showDialogThanhToan() {
        if (this.getActivity() instanceof CallbackDialog) {
            final CallbackDialog callbackDialog = (CallbackDialog) getContextView();

            dialog = new Dialog(this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_thanhtoan);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            unbinder.unbind();
            unbinder = ButterKnife.bind(this, dialog);

            rvBillOnline.setVisibility(View.INVISIBLE);

            setUpRecyclerDialog();
            mIPayPresenter.callPayRecyclerDialog(mEdong);


            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    callbackDialog.processOnDismissDialog();
                }
            });
            dialog.show();
        } else
            Log.e(TAG, "showDialogThanhToan: fragment cannot implement CallbackDialog");
    }

    public interface CallbackDialog {
        void processOnDismissDialog();
    }
}
