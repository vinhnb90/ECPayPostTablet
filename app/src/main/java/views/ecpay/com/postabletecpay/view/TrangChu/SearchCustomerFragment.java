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
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

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
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.Util.BarcodeScannerDialog;

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

    private View rootView;


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
    @BindView(R.id.btnScanCode)
    ImageButton btnScanCode;
    @Nullable
    @BindView(R.id.btnSearch2)
    Button btnSearch2;
    @Nullable
    @BindView(R.id.btnBack1)
    ImageButton btnBack1;

    @Nullable
    @BindView(R.id.eSearchCustomer)
    EditText eSearchCustomer;

    @Nullable
    @BindView(R.id.eSearchTen)
    EditText eSearchTen;

    @Nullable
    @BindView(R.id.eSearchDT)
    EditText eSearchDT;

    @Nullable
    @BindView(R.id.eSearchBookCmis)
    EditText eSearchBookCmis;

    @Nullable
    @BindView(R.id.eSearchAddress)
    EditText eSearchAddress;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_customer, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        eDong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);


        searchCustomerPresenter = new SearchCustomerPresenter(this, eDong);



        btnExpand.setOnClickListener(this);
        btnScanCode.setOnClickListener(this);
        btnSearch2.setOnClickListener(this);
        btnBack1.setOnClickListener(this);

        isCurrentExpand = false;
        btnExpand.setRotation(180F);

        layout_timkiem_nangcao.setVisibility(View.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKH.setLayoutManager(layoutManager);


        rvKH.setAdapter(new CustomerAdapter(this));
        rvKH.invalidate();


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    void changeStateViewSearch()
    {
        float deg = btnExpand.getRotation() + 180F;
        btnExpand.animate().rotation(deg).setDuration(1);

        isCurrentExpand = !isCurrentExpand;
        layout_timkiem_nangcao.setVisibility(isCurrentExpand ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExpand) {
            //layout_timkiem_nangcao.setVisibility(layout_timkiem_nangcao.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            this.changeStateViewSearch();
            return;
        }


        if(v.getId() == R.id.btnSearch2)
        {

            String codeEcard = eSearchCustomer.getText().toString();
            String name = eSearchTen.getText().toString();
            String address = eSearchAddress.getText().toString();
            String bookCmis = eSearchBookCmis.getText().toString();
            String phone = eSearchDT.getText().toString();


            if(codeEcard.length() == 0 && (name.length() == 0 && address.length() == 0 && bookCmis.length() == 0 && phone.length() == 0))
            {
                this.showMessage("Bạn Chưa Nhập Tham Số Tìm Kiếm");
                return;
            }

            if(codeEcard.length() > 0 && codeEcard.length() < 2)
            {
                this.showMessage("Mã Khách Hàng/Mã Thẻ Phải Nhập Tối Thiếu 2 Ký Tự");
                return;
            }


            boolean isSearchOnline = false;

            if(codeEcard.length() == 16 || (codeEcard.length() == 13 && codeEcard.charAt(0) == 'P'))
            {
                isSearchOnline = true;
            }


            isCurrentExpand = false;
            btnExpand.setRotation(180F);

            layout_timkiem_nangcao.setVisibility(View.GONE);

            searchCustomerPresenter.search(isSearchOnline, eSearchCustomer.getText().toString(), eSearchTen.getText().toString(), eSearchAddress.getText().toString(), eSearchDT.getText().toString(), eSearchBookCmis.getText().toString());
            return;
        }
        if(v.getId() == R.id.btnScanCode)
        {
            this.showDialogBarcode();
            return;
        }

        if(v.getId() == R.id.btnBack1)
        {
            FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, MainPageFragment.newInstance(eDong));
            fragmentTransaction.commit();
            return;
        }
    }

    @Override
    public void showDialogBarcode() {
        BarcodeScannerDialog dialog = new BarcodeScannerDialog((MainActivity) this.getActivity(), new BarcodeScannerDialog.OnResultListener() {
            @Override
            public void onResult(String text) {
                eSearchCustomer.setText(text);
            }
        });
        dialog.show();
    }


    @Override
    public Context getContextView() {
        return this.getContext();
    }

    @Override
    public void refreshView(List<EntityKhachHang> lst) {
        CustomerAdapter customerAdapter = (CustomerAdapter)rvKH.getAdapter();
        customerAdapter.setCustomers(lst);
        customerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCustomerInfo(EntityKhachHang customer) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, CustomerInfoFragment.newInstance(customer, eDong));
        fragmentTransaction.commit();
    }

    @Override
    public void showMessage(String message) {
        try{
            Common.showDialog(this.getContext(), null, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, true, Common.TYPE_DIALOG.LOI);
//            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }
}
