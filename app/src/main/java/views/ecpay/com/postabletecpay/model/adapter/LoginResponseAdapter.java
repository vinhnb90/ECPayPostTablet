package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.BodyLogin;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.FooterLogin;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.HeaderLogin;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.EntityLogin.LoginResponseLogin;

/**
 * Created by VinhNB on 5/19/2017.
 */

public class LoginResponseAdapter extends TypeAdapter<LoginResponseLogin> {
    @Override
    public void write(JsonWriter out, LoginResponseLogin value) throws IOException {
        HeaderLogin headerLogin = value.getHeaderLogin();
        BodyLogin bodyLogin = value.getBodyLogin();
        FooterLogin footerLogin = value.getFooterLogin();

        out.beginObject();
        //write headerLogin
        if (headerLogin != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerLogin.getAgent());
            out.name("password").value(headerLogin.getPassword());
            out.name("command-id").value(headerLogin.getCommandId());
            out.endObject();
        }

        //write bodyLogin
        if (bodyLogin != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyLogin.getAuditNumber());
            out.name("mac").value(bodyLogin.getMac());
            out.name("disk-drive").value(bodyLogin.getDiskDrive());
            out.name("signature").value(bodyLogin.getSignature());
            out.name("pin-login").value(bodyLogin.getPinLogin());
            out.endObject();
        }

        //write footerLogin
        if (footerLogin != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerLogin.getAccountIdt());
            out.name("response-code").value(footerLogin.getResponseCode());
            out.name("description").value(footerLogin.getDescription());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public LoginResponseLogin read(JsonReader in) throws IOException {
        final LoginResponseLogin loginRequest = new LoginResponseLogin();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderLogin headerLogin = new HeaderLogin();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerLogin.setAgent(in.nextString());
                                break;
                            case "password":
                                headerLogin.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerLogin.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setHeaderLogin(headerLogin);
                    break;

                case "body":
                    final BodyLogin bodyLogin = new BodyLogin();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                bodyLogin.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                bodyLogin.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                bodyLogin.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                bodyLogin.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                bodyLogin.setPinLogin(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setBodyLogin(bodyLogin);
                    break;

                case "footer":
                    final FooterLogin footerLogin = new FooterLogin();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                footerLogin.setAccountIdt(in.nextString());
                                break;
                            case "response-code":
                                footerLogin.setResponseCode(in.nextString());
                                break;
                            case "description":
                                footerLogin.setDescription(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setFooterLogin(footerLogin);
                    break;
            }
        }

        in.endObject();

        return loginRequest;
    }
}