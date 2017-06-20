package views.ecpay.com.postabletecpay.presenter;

/**
 * Created by MyPC on 20/06/2017.
 */

public interface ICashTranferPresenter {
    public void send(Long amount, String sendPhone, String receivedPhone, String description);
    Long getCurrentBalance();
}
