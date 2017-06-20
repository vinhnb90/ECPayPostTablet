package views.ecpay.com.postabletecpay.view.TrangChu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by MyPC on 19/06/2017.
 */

public class ChuyenViTongFragment extends Fragment implements View.OnClickListener {

    public  enum STATE
    {
        KHOI_TAO,
        XAC_NHAN
    }


    @BindView(R.id.layout_xac_nhan)
    View viewLayOutXacNhan;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.eSoTien)
    EditText eSoTien;
    @BindView(R.id.eGhiChu)
    EditText eGhiChu;
    @BindView(R.id.eMaXacNhan)
    EditText eMaXacNhan;


    private STATE mState;

    public static ChuyenViTongFragment newInstance() {
        return new ChuyenViTongFragment();
    }

    public  ChuyenViTongFragment()
    {
        mState = STATE.KHOI_TAO;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chuyen_vi_tong, container, false);
        ButterKnife.bind(this, view);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        this.UpdateState();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNext)
        {
            if(mState == STATE.KHOI_TAO)
            {
                mState = STATE.XAC_NHAN;
                this.UpdateState();
            }else
            {

            }
            return;
        }if(v.getId() == R.id.btnBack)
        {
            if(mState == STATE.XAC_NHAN)
            {
                mState = STATE.KHOI_TAO;
                this.UpdateState();
            }else
            {

            }
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
                viewLayOutXacNhan.setVisibility(View.INVISIBLE);
                break;
            case XAC_NHAN:
                eGhiChu.setEnabled(false);
                eSoTien.setEnabled(false);
                eMaXacNhan.setText("");
                viewLayOutXacNhan.setVisibility(View.VISIBLE);
                break;
        }

    }

}
