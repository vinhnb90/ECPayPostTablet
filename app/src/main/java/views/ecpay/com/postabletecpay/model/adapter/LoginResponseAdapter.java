package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.BodyLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.FooterLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.HeaderLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;

/**
 * Created by VinhNB on 5/19/2017.
 */

public class LoginResponseAdapter extends TypeAdapter<LoginResponseReponse> {
    @Override
    public void write(JsonWriter out, LoginResponseReponse value) throws IOException {
        HeaderLoginResponse headerLoginResponse = value.getHeaderLoginResponse();
        BodyLoginResponse bodyLoginResponse = value.getBodyLoginResponse();
        FooterLoginResponse footerLoginResponse = value.getFooterLoginResponse();

        out.beginObject();
        //write headerLoginResponse
        if (headerLoginResponse != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerLoginResponse.getAgent());
            out.name("password").value(headerLoginResponse.getPassword());
            out.name("command-id").value(headerLoginResponse.getCommandId());
            out.endObject();
        }

        //write bodyLoginResponse
        if (bodyLoginResponse != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyLoginResponse.getAuditNumber());
            out.name("mac").value(bodyLoginResponse.getMac());
            out.name("disk-drive").value(bodyLoginResponse.getDiskDrive());
            out.name("signature").value(bodyLoginResponse.getSignature());
            out.name("pin-login").value(bodyLoginResponse.getPinLogin());
            out.endObject();
        }

        //write footerLoginResponse
        if (footerLoginResponse != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerLoginResponse.getAccountIdt());
            out.name("response-code").value(footerLoginResponse.getResponseCode());
            out.name("description").value(footerLoginResponse.getDescription());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public LoginResponseReponse read(JsonReader in) throws IOException {
        final LoginResponseReponse loginRequest = new LoginResponseReponse();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderLoginResponse headerLoginResponse = new HeaderLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerLoginResponse.setAgent(in.nextString());
                                break;
                            case "password":
                                headerLoginResponse.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerLoginResponse.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setHeaderLoginResponse(headerLoginResponse);
                    break;

                case "body":
                    final BodyLoginResponse bodyLoginResponse = new BodyLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                bodyLoginResponse.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                bodyLoginResponse.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                bodyLoginResponse.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                bodyLoginResponse.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                bodyLoginResponse.setPinLogin(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setBodyLoginResponse(bodyLoginResponse);
                    break;

                case "footer":
                    final FooterLoginResponse footerLoginResponse = new FooterLoginResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                footerLoginResponse.setAccountIdt(in.nextString());
                                break;
                            case "response-code":
                                footerLoginResponse.setResponseCode(in.nextString());
                                break;
                            case "description":
                                footerLoginResponse.setDescription(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setFooterLoginResponse(footerLoginResponse);
                    break;
            }
        }

        in.endObject();

        return loginRequest;
    }
}