package views.ecpay.com.postabletecpay.view.TrangChu;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.CashTranferModel;
import views.ecpay.com.postabletecpay.presenter.CashTranferPresenter;
import views.ecpay.com.postabletecpay.presenter.ICashTranferPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.IBackHandler;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by MyPC on 19/06/2017.
 */

public class CashTranferFragment extends Fragment implements View.OnClickListener, ICashTranferView {


    public  enum STATE
    {
        KHOI_TAO,
        XAC_NHAN
    }


    private String eDong;

    @Nullable
    @BindView(R.id.layout_xac_nhan)
    View viewLayOutXacNhan;
    @Nullable
    @BindView(R.id.btnNext)
    Button btnNext;
    @Nullable
    @BindView(R.id.btnBack)
    Button btnBack;
    @Nullable
    @BindView(R.id.eSoTien)
    EditText eSoTien;
    @Nullable
    @BindView(R.id.eGhiChu)
    EditText eGhiChu;
    @Nullable
    @BindView(R.id.eMaXacNhan)
    EditText eMaXacNhan;
    @Nullable
    @BindView(R.id.txtSendPhone)
    TextView txtSendPhone;
    @Nullable
    @BindView(R.id.sViTong)
    Spinner sViTong;
    @Nullable
    @BindView(R.id.txtCaptcha)
    TextView txtCaptcha;
    @Nullable
    @BindView(R.id.btnRefresh)
    Button btnRefresh;
    @Nullable
    @BindView(R.id.hHeader)
    RelativeLayout hHeader;
    @Nullable
    @BindView(R.id.hContent)
    LinearLayout hContent;
    @Nullable
    @BindView(R.id.hLoading)
    LinearLayout hLoading;
    @Nullable
    @BindView(R.id.btnBack1)
    ImageButton btnBack1;



    private STATE mState;
    private IBackHandler backHandler;
    private String mViTongString;
    private Unbinder unbinder;

    private ICashTranferPresenter cashTranferPresenter;

    public static CashTranferFragment newInstance(String _edong, IBackHandler _backHandler) {


        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, _edong);


        CashTranferFragment cashTranferFragment = new CashTranferFragment();
        cashTranferFragment.setArguments(bundle);
        cashTranferFragment.backHandler = _backHandler;
        return cashTranferFragment;
    }

    public CashTranferFragment()
    {
        mState = STATE.KHOI_TAO;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chuyen_vi_tong, container, false);
        unbinder = ButterKnife.bind(this, view);

        eDong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);

        cashTranferPresenter = new CashTranferPresenter(this, eDong);

        mViTongString = "";

        List<String> list = new ArrayList<>();
        for (int i = 0; i < Common.NAME_VI_TONG.length; i ++)
        {
            list.add(Common.NAME_VI_TONG[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sViTong.setAdapter(dataAdapter);
        sViTong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViTongString = Common.PHONE_VI_TONG[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnBack1.setOnClickListener(this);

        this.UpdateState();


        txtSendPhone.setText(eDong);

        hLoading.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNext)
        {
            if(mState == STATE.KHOI_TAO)
            {

                if(mViTongString.length() == 0)
                {
                    showError("Vui lòng chọn Ví tổng!");
                    return;
                }

                if(eSoTien.getText().length() == 0)
                {
                    showError("Vui lòng nhập Số tiền!");
                    return;
                }

                if(Long.parseLong(eSoTien.getText().toString()) == 0)
                {
                    showError("Số tiền phải > 0 VND");
                    return;
                }

                if(Long.parseLong(eSoTien.getText().toString()) > cashTranferPresenter.getCurrentBalance())
                {
                    showError("Số tiền chuyển tối đa " + cashTranferPresenter.getCurrentBalance() + "VND");
                    return;
                }

                if(eGhiChu.getText().length() > 80)
                {
                    showError("Ghi chú tối đa chỉ 80 ký tự");
                    return;
                }


                mState = STATE.XAC_NHAN;
                this.refreshCaptcha();
                this.UpdateState();
            }else
            {
                if(eMaXacNhan.getText().toString().equals(txtCaptcha.getText().toString()))
                {
                    cashTranferPresenter.send(Long.parseLong(eSoTien.getText().toString()), txtSendPhone.getText().toString(), mViTongString, eGhiChu.getText().toString());
                }else
                {
                    showError("Mã xác thực không hợp lệ!");
                }
                //
            }
            return;
        }
        if(v.getId() == R.id.btnBack)
        {
            if(mState == STATE.XAC_NHAN)
            {
                mState = STATE.KHOI_TAO;
                this.UpdateState();
            }else
            {
                eSoTien.setText("");
                eGhiChu.setText("");
                sViTong.setSelection(0);
                mViTongString = "";

            }
            return;
        }

        if(v.getId() == R.id.btnRefresh)
        {
            this.refreshCaptcha();
            return;
        }

        if(v.getId() == R.id.btnBack1)
        {
            this.onBack();
            return;
        }
    }

    public void UpdateState()
    {
        switch (mState)
        {
            case KHOI_TAO:
                eGhiChu.setEnabled(true);
                eSoTien.setEnabled(true);
                sViTong.setEnabled(true);
                viewLayOutXacNhan.setVisibility(View.INVISIBLE);


                btnBack.setText("Nhập Lại");
                btnNext.setText("Tiếp Tục");
                break;
            case XAC_NHAN:
                eGhiChu.setEnabled(false);
                eSoTien.setEnabled(false);
                sViTong.setEnabled(false);
                eMaXacNhan.setText("");
                viewLayOutXacNhan.setVisibility(View.VISIBLE);


                btnBack.setText("Quay Lại");
                btnNext.setText("Xác Nhận");
                break;
        }

    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }


    @Override
    public void showError(String message) {
        try{
            Common.showDialog(this.getContext(), null, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, true, Common.TYPE_DIALOG.LOI);
//            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }

    @Override
    public void showText(String message) {
        try{
            Common.showDialog(this.getContext(), null, Common.TEXT_DIALOG.TITLE_DEFAULT.toString(), message, true, Common.TYPE_DIALOG.THANH_CONG);
//            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }

    @Override
    public void setVisibleBar(boolean visible) {

        if(hLoading == null)
            return;

        if(visible)
            hLoading.setVisibility(View.VISIBLE);
        else
            if (hLoading != null)
                hLoading.setVisibility(View.GONE);
    }

    void refreshCaptcha()
    {
        txtCaptcha.setText(Common.createCapcha(6));
    }


    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this.getContext(), "CODE: " + code + "\n DESCRIPTION: " + description, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public  void onBack()
    {
        this.backHandler.onBack(this.getActivity(), eDong);
    }
}
