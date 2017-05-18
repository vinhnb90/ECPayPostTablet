package views.ecpay.com.postabletecpay.view.BaoCao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoTongHopFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tvNgayIn)
    TextView tvNgayIn;
    @BindView(R.id.tvThuNgan)
    TextView tvThuNgan;
    @BindView(R.id.tvSHDGiao)
    TextView tvSHDGiao;
    @BindView(R.id.tvSTGiao)
    TextView tvSTGiao;
    @BindView(R.id.tvSHDThu)
    TextView tvSHDThu;
    @BindView(R.id.tvSTThu)
    TextView tvSTThu;
    @BindView(R.id.tvSHDTon)
    TextView tvSHDTon;
    @BindView(R.id.tvSTTon)
    TextView tvSTTon;
    @BindView(R.id.tvSHDThuVangLai)
    TextView tvSHDThuVangLai;
    @BindView(R.id.tvSTThuVangLai)
    TextView tvSTThuVangLai;
    @BindView(R.id.tvSHDHoanTra)
    TextView tvSHDHoanTra;
    @BindView(R.id.tvSTHoanTra)
    TextView tvSTHoanTra;
    @BindView(R.id.btInBaoCao)
    Button btInBaoCao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_tonghop, container, false);
        ButterKnife.bind(this, view);

        btInBaoCao.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
