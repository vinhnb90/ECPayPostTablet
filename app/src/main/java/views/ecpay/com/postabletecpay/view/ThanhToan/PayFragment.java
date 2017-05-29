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
import android.widget.TextView;

import java.util.ArrayList;
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
    @BindView(R.id.rvHoaDon)
    RecyclerView rvHoaDon;
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
        mEdong = getArguments().getString(KEY_EDONG, Common.EMPTY_TEXT);

        setupPayRecyclerView();
        //first page
        typeSearch = Common.TYPE_SEARCH.ALL;
        mPageIndex = FIRST_PAGE_INDEX;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString());

        return view;
    }

    private void setupPayRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (rvHoaDon != null)
            rvHoaDon.setLayoutManager(linearLayoutManager);
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
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex);


    }

    @OnClick(R.id.btn_frag_thanh_toan_pre)
    public void clickPre(View view) {
        if (payAdapter == null)
            return;

        mPageIndex -= PAGE_INCREMENT;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex);
    }
    //endregion

    //region listener viewpage
    @OnPageChange(R.id.view_pager)
    public void onPageSelected(int position) {
        mPageIndex = FIRST_PAGE_INDEX;
        Common.TYPE_SEARCH typeSearch = Common.TYPE_SEARCH.findCodeMessage(position);
        String type = viewPager.getAdapter().getPageTitle(position).toString();
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString().trim());
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
    }

    @OnTextChanged(R.id.etSearch)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex, typeSearch, etSearch.getText().toString());
    }

    @OnTextChanged(R.id.etSearch)
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @OnTextChanged(R.id.etSearch)
    public void afterTextChanged(CharSequence sequence, int i, int j, int k) {

    }
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

    @Override
    public void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList, int pageIndex, int totalPage) {
        if (payAdapter == null)
            payAdapter = new PayAdapter(getContext(), adapterList);
        else
            payAdapter.refreshData(adapterList);

        btnPre.setEnabled(false);
        btnNext.setEnabled(true);
        tvPage.setText(String.valueOf(mPageIndex).concat(Common.SPLASH_TEXT).concat(String.valueOf(totalPage == 0 ? FIRST_PAGE_INDEX : totalPage)));


    }

    @Override
    public void showPayRecyclerOtherPage(int pageIndexNew) {
        int totalPage = Math.round(mAdapterList.size() / ROWS_ON_PAGE);
        if (totalPage * ROWS_ON_PAGE != mAdapterList.size())
            totalPage++;

        if (pageIndexNew < FIRST_PAGE_INDEX || pageIndexNew > totalPage)
            return;

        btnPre.setEnabled(true);
        btnNext.setEnabled(true);
        tvPage.setText(String.valueOf(mPageIndex).concat(Common.SPLASH_TEXT).concat(String.valueOf(totalPage)));

        if (pageIndexNew < FIRST_PAGE_INDEX)
            btnPre.setEnabled(false);
        if (pageIndexNew == totalPage)
            btnNext.setEnabled(false);

        mPageIndex = pageIndexNew;

        int index = ROWS_ON_PAGE * (mPageIndex - PAGE_INCREMENT);
        int maxIndex = ROWS_ON_PAGE * mPageIndex;
        List<PayAdapter.PayEntityAdapter> adapterList = new ArrayList<>();

        for (; index < maxIndex; index++) {
            adapterList.add(adapterList.get(index));
        }

        payAdapter.refreshData(adapterList);
    }

    @Override
    public void showPayRecyclerSearch(int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        if (infoSearch == null)
            return;
        if (typeSearch == null)
            return;

        //fitter list
        List<PayAdapter.PayEntityAdapter> fitterList = new ArrayList<>();

    }
    //endregion

    public interface OnFragmentInteractionListener {
    }
}
