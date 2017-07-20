package views.ecpay.com.postabletecpay.view.TrangChu;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.CustomerInfoPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.Util.BarcodeScannerDialog;

/**
 * Created by duydatpham on 6/23/17.
 */

public class CustomerInfoFragment extends Fragment implements ICustomerInfoView, View.OnClickListener
{


    public static CustomerInfoFragment newInstance(EntityKhachHang customer, String edong) {
        CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
        customerInfoFragment.mCustomer = customer;
        customerInfoFragment.mEDong = edong;
        return customerInfoFragment;
    }

    Unbinder unbinder;
    CustomerInfoPresenter customerInfoPresenter;

    @Nullable
    @BindView(R.id.fragment_customer_info_fragment_customer_info_eSearchCustomer)
    EditText fragment_customer_info_fragment_customer_info_eSearchCustomer;

    @Nullable
    @BindView(R.id.fragment_customer_info_eSdtECPAY)
    EditText fragment_customer_info_eSdtECPAY;

    @Nullable
    @BindView(R.id.fragment_customer_info_eTKNganHang)
    EditText fragment_customer_info_eTKNganHang;

    @Nullable
    @BindView(R.id.fragment_customer_info_eBookCmis)
    TextView fragment_customer_info_eBookCmis;

    @Nullable
    @BindView(R.id.fragment_customer_info_eAddressNH)
    EditText fragment_customer_info_eAddressNH;

    @Nullable
    @BindView(R.id.fragment_customer_info_fragment_customer_info_txtMaKH)
    TextView fragment_customer_info_fragment_customer_info_txtMaKH;

    @Nullable
    @BindView(R.id.fragment_customer_info_txtTenKH)
    TextView fragment_customer_info_txtTenKH;

    @Nullable
    @BindView(R.id.fragment_customer_info_txtDiaChi)
    TextView fragment_customer_info_txtDiaChi;

    @Nullable
    @BindView(R.id.fragment_customer_info_txtPhoneEvn)
    TextView fragment_customer_info_txtPhoneEvn;

    @Nullable
    @BindView(R.id.fragment_customer_info_btnThoat)
    Button fragment_customer_info_btnThoat;
    @Nullable
    @BindView(R.id.fragment_customer_info_btnDangKy)
    Button fragment_customer_info_btnDangKy;
    @Nullable
    @BindView(R.id.fragment_customer_info_fragment_customer_info_btnScanCode)
    ImageButton fragment_customer_info_fragment_customer_info_btnScanCode;
    @Nullable
    @BindView(R.id.layout_loading)
    RelativeLayout layout_loading;
    @Nullable
    @BindView(R.id.fragment_customer_info_fragment_customer_info_btnBack1)
    ImageButton fragment_customer_info_fragment_customer_info_btnBack1;


    private EntityKhachHang mCustomer;
    private String mEDong;

    private boolean isUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        customerInfoPresenter = new CustomerInfoPresenter(this);


        fragment_customer_info_btnDangKy.setOnClickListener(this);
        fragment_customer_info_btnThoat.setOnClickListener(this);
        fragment_customer_info_fragment_customer_info_btnScanCode.setOnClickListener(this);
        fragment_customer_info_fragment_customer_info_btnBack1.setOnClickListener(this);


        layout_loading.setVisibility(View.GONE);

        this.fill(mCustomer);

        return view;
    }

    @Override
    public void back() {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, SearchCustomerFragment.newInstance(mEDong));
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_customer_info_btnThoat || v.getId() == R.id.fragment_customer_info_fragment_customer_info_btnBack1)
        {
            FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, SearchCustomerFragment.newInstance(mEDong));
            fragmentTransaction.commit();
            return;
        }

        if (v.getId() == R.id.fragment_customer_info_btnDangKy)
        {
            if(isUpdate)
            {
                customerInfoPresenter.update(mCustomer, mEDong, fragment_customer_info_fragment_customer_info_eSearchCustomer.getText().toString(),
                        fragment_customer_info_eSdtECPAY.getText().toString(),fragment_customer_info_eTKNganHang.getText().toString(),
                        fragment_customer_info_eAddressNH.getText().toString());
            }else
            {
                customerInfoPresenter.update(mCustomer, mEDong, fragment_customer_info_fragment_customer_info_eSearchCustomer.getText().toString(),
                        fragment_customer_info_eSdtECPAY.getText().toString(),fragment_customer_info_eTKNganHang.getText().toString(),
                        fragment_customer_info_eAddressNH.getText().toString());
            }
            return;
        }

        if(v.getId() == R.id.fragment_customer_info_fragment_customer_info_btnScanCode)
        {
            BarcodeScannerDialog dialog = new BarcodeScannerDialog((MainActivity) getContextView(), new BarcodeScannerDialog.OnResultListener() {
                @Override
                public void onResult(String text) {
                    fragment_customer_info_fragment_customer_info_eSearchCustomer.setText(text);
                }
            });
            dialog.show();
            return;
        }

    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    void fill(EntityKhachHang customer)
    {
        setLoading(true);

        fragment_customer_info_fragment_customer_info_eSearchCustomer.setText(this.getStringField(customer.getMA_THE()));
        fragment_customer_info_eSdtECPAY.setText(this.getStringField(customer.getSDT_ECPAY()));
        fragment_customer_info_eTKNganHang.setText(this.getStringField(""));
        fragment_customer_info_eBookCmis.setText(this.getStringField(customer.getSO_GCS()));
        fragment_customer_info_eAddressNH.setText(this.getStringField(""));
        fragment_customer_info_fragment_customer_info_txtMaKH.setText(this.getStringField(customer.getMA_KHANG()));
        fragment_customer_info_txtTenKH.setText(this.getStringField(customer.getTEN_KHANG()));
        fragment_customer_info_txtDiaChi.setText(this.getStringField(customer.getDIA_CHI()));
        fragment_customer_info_txtPhoneEvn.setText(this.getStringField(customer.getSDT_EVN()));

        if(customer.getMA_THE() == null || customer.getMA_THE().length() == 0)
        {
            fragment_customer_info_btnDangKy.setText("Đăng Ký");
        }else
        {
            fragment_customer_info_btnDangKy.setText("Cập Nhật");
        }


        isUpdate = !(mCustomer.getMA_THE() == null || mCustomer.getMA_THE().length() == 0);
    }

    String getStringField(String obj)
    {
        if(obj == null)
            return "";
        return  obj;
    }

    @Override
    public void setLoading(boolean pShow) {
        layout_loading.setVisibility(pShow ? View.INVISIBLE : View.GONE);
    }

    @Override
    public void showMessageText(String message, Common.TYPE_DIALOG typeLoi) {
        try{
            Common.showDialog(this.getContext(), null, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, true, typeLoi);
//            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }

    @Override
    public void refill(EntityKhachHang customer) {
        this.fill(customer);
    }
}
