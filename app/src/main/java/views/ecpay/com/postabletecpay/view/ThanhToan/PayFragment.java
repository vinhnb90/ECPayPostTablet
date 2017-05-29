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
    public static final int FIRST_PAGE_INDEX = 0;
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

    private OnFragmentInteractionListener listener;
    private PayAdapter payAdapter;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private IPayPresenter mIPayPresenter;
    private String mEdong;
    private int mPageIndex;

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

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tabLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLayout.setVisibility(View.GONE);
                }
            }
        });

        mIPayPresenter = new PayPresenter(this);
        mEdong = getArguments().getString(KEY_EDONG, Common.EMPTY_TEXT);

        setupPayRecyclerView();
        //first page
        mPageIndex = FIRST_PAGE_INDEX;
        mIPayPresenter.callPayRecycler(mEdong, mPageIndex);

        return view;
    }


    private void setupPayRecyclerView() {
        LinearL DayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
    public void showPayRecyclerFirstPage(List<PayAdapter.PayEntityAdapter> adapterList) {
        if (adapterList == null)
            return;

        mAdapterList.clear();
        mAdapterList.addAll(adapterList);

        //get ROWS_ON_PAGE first
        mPageIndex = FIRST_PAGE_INDEX;

        List<PayAdapter.PayEntityAdapter> adapterListFirstPage = new ArrayList<>();
        int index = ROWS_ON_PAGE * mPageIndex;

        for (; index < ROWS_ON_PAGE; index++) {
            adapterListFirstPage.add(adapterList.get(index));
        }
        ((PayAdapter)adapterList).refreshData(adapterListFirstPage);

    }

    @Override
    public void showPayRecyclerOtherPage(int pageIndexNew) {
        mPageIndex = pageIndexNew;
    }
    //endregion

    public interface OnFragmentInteractionListener {
    }
}
