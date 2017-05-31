package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.presenter.PayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by macbook on 4/28/17.
 */

public class PayFragment extends Fragment implements IPayView, View.OnClickListener {
    public static final int FIRST_PAGE_INDEX = 1;
    public static final int PAGE_INCREMENT = 1;
    public static final int ROWS_ON_PAGE = 10;
    //    private int heightRecycler, heightTabLayout;
//    private int widthRecycler, widthTabLayout;
    @BindView(R.id.ibtn_frag_user_info_back)
    ImageButton ibBack;
    @BindView(R.id.ibScaner)
    ImageButton ibScaner;
    @BindView(R.id.ibAdd)
    ImageButton ibAdd;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvHoaDon)
    TextView tvHoaDon;
    @BindView(R.id.tvTongTien)
    TextView tvTongTien;
    @BindView(R.id.btThanhToan)
    Button btThanhToan;
    @BindView(R.id.rvKhachHang)
    RecyclerView rvKH;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_frag_thanh_toan_pre)
    Button btnPre;
    @BindView(R.id.btn_frag_thanh_toan_next)
    Button btnNext;
    @BindView(R.id.tv_frag_thanh_toan_page)
    TextView tvPage;
    @BindView(R.id.ll_frag_thanh_toan_page)
    LinearLayout llCountPage;

    //Search online
    @BindView(R.id.rv_frag_thanhtoan_search_online_progress)
    RelativeLayout rvProgressSearchOnline;
    @BindView(R.id.tv_frag_thanh_toan_search_online_message)
    TextView tvMessageNotifySearchOnlne;
    @BindView(R.id.pbar_frag_thanhtoan_search_online)
    ProgressBar pbarSearchOnline;
    @BindView(R.id.ibtn_frag_thanhtoan_action_research_online)
    ImageButton ibtnResearchOnline;
    @BindView(R.id.ibtn_frag_thanhtoan_action_cancel_search_online)
    ImageButton ibtnCancelSearchOnline;

    private OnFragmentInteractionListener listener;
    private PayAdapter payAdapter;
    private IPayPresenter mIPayPresenter;
    private String mEdong;
    private int mPageIndex;
    private Common.TYPE_SEARCH typeSearch;

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
        View view = inflater.inflate(R.layout.fragment_thanh_toan, container, false);
        ButterKnife.bind(this, view);

        ibBack.setOnClickListener(this);
        ibScaner.setOnClickListener(this);
        ibAdd.setOnClickListener(this);
        btThanhToan.setOnClickListener(this);

        viewPager.setAdapter(new TimKiemAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        mIPayPresenter = new PayPresenter(this);
        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);

        setupPayRecyclerView(view);
        //first page
        typeSearch = Common.TYPE_SEARCH.ALL;
        mPageIndex = FIRST_PAGE_INDEX;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
        return view;
    }

    private void setupPayRecyclerView(final View view) {
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

    //region onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_frag_user_info_back:
                break;
            case R.id.ibScaner:
                break;
            case R.id.ibAdd:
                break;
            case R.id.btThanhToan:
                showDialogThanhToan();
                break;
        }
    }

    @OnClick(R.id.btn_frag_thanh_toan_next)
    public void clickNext(View view) {
        if (payAdapter == null)
            return;

        mPageIndex += PAGE_INCREMENT;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    @OnClick(R.id.btn_frag_thanh_toan_pre)
    public void clickPre(View view) {
        if (payAdapter == null)
            return;

        mPageIndex -= PAGE_INCREMENT;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }
    //endregion

    //region listener viewpage
    @OnPageChange(R.id.view_pager)
    public void onPageSelected(int position) {
        mPageIndex = FIRST_PAGE_INDEX;
        Common.TYPE_SEARCH typeSearch = Common.TYPE_SEARCH.findMessage(position);
        boolean isSeachOnline = checkUserNeedSearchOnline(etSearch.getText().toString());
        if (isSeachOnline)
            return;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString().trim(), false);
    }

    private boolean checkUserNeedSearchOnline(String infoSearch) {
        if ((infoSearch.length() == Common.LENGTH_MIN && String.valueOf(infoSearch.charAt(Common.ZERO)).equalsIgnoreCase(Common.SYMBOL_FIRST)) || infoSearch.length() == Common.LENGTH_MAX) {
            return true;
        }
        return false;
    }


    @OnPageChange(R.id.view_pager)
    public void onPageScrolled(int position) {
    }

    @OnPageChange(R.id.view_pager)
    public void onPageScrollStateChanged(int position) {
    }
    //endregion

    //region listener et search
    @OnFocusChange(R.id.etSearch)
    public void OnFocusChangeListener(boolean hasFocus) {
        if (hasFocus) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString(), false);
    }

    @OnTextChanged(R.id.etSearch)
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

            etSearch.post(new Runnable() {
                @Override
                public void run() {
                    etSearch.setHint(etSearch.getText().toString().concat(Common.TEXT_MULTI_SPACE).concat(Common.TEXT_SEARCHING));
                }
            });
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

    private void showDialogThanhToan() {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thanhtoan);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvSoHoaDon = (TextView) dialog.findViewById(R.id.tvSoHoaDon);
        TextView tvTongTien = (TextView) dialog.findViewById(R.id.tvTongTien);
        RecyclerView rvDanhSach = (RecyclerView) dialog.findViewById(R.id.rvDanhSach);
        Button btHuy = (Button) dialog.findViewById(R.id.btHuy);
        Button btThanhToan = (Button) dialog.findViewById(R.id.btThanhToan);

        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }

    //region IPayView
    @Override
    public Context getContextView() {
        return getContext();
    }
/*
    @Override
    public void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage, String infoSearch, boolean isSeachOnline) {
        btnPre.setEnabled(false);
        btnNext.setEnabled(true);
        tvPage.setText(String.valueOf(mPageIndex).concat(Common.TEXT_SLASH).concat(String.valueOf(totalPage == 0 ? FIRST_PAGE_INDEX : totalPage)));

//        setNewAdapterRecycler(adapterList);
        payAdapter = new PayAdapter(getContext(), this, adapterList);

        rvKH.setAdapter(payAdapter);
        rvKH.invalidate();

        //if isSeachOnline
        if (isSeachOnline == true && infoSearch == null)
            return;
        mIPayPresenter.callSearchOnline(mEdong, infoSearch);
    }*/

    /*private void setNewAdapterRecycler(List<PayAdapter.PayEntityAdapter> adapterList) {
        int widthRecyclerReal, heightRecyclerReal;

        widthRecyclerReal = (tabLayout.getVisibility() == View.VISIBLE) ? (widthRecycler - widthTabLayout) : widthRecycler;
        heightRecyclerReal = (tabLayout.getVisibility() == View.VISIBLE) ? (heightRecycler - heightTabLayout) : heightRecycler;
        payAdapter = new PayAdapter(getContext(), adapterList, widthRecyclerReal, heightRecyclerReal);

        rvKH.setAdapter(payAdapter);
        rvKH.invalidate();
    }*/

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
            payAdapter = new PayAdapter(getContext(), this, adapterList);
            rvKH.setAdapter(payAdapter);
        } else
            payAdapter.refreshData(adapterList);
        rvKH.invalidate();

        //if isSeachOnline
        if (isSeachOnline == true && infoSearch == null)
            return;
        mIPayPresenter.callSearchOnline(mEdong, infoSearch);
    }

    @Override
    public void showSearchOnlineProcess() {
        if (rvProgressSearchOnline.getVisibility() == View.GONE)
            rvProgressSearchOnline.setVisibility(View.VISIBLE);

        pbarSearchOnline.setVisibility(View.VISIBLE);
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
    //endregion

    public interface OnFragmentInteractionListener {
    }
}
