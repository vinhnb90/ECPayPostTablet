package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;

/**
 * Class adapter GSON Serialization because gson not order sequence field when create json string
 */

public class LoginRequestAdapter extends TypeAdapter<LoginRequest> {
    @Override
    public void write(JsonWriter out, LoginRequest value) throws IOException {
        HeaderLoginRequest headerLoginRequest = value.getHeaderLoginRequest();
        BodyLoginRequest bodyLoginRequest = value.getBodyLoginRequest();
        FooterLoginRequest footerLoginRequest = value.getFooterLoginRequest();

        out.beginObject();
        //write headerLoginRequest
        if (headerLoginRequest != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerLoginRequest.getAgent());
            out.name("password").value(headerLoginRequest.getPassword());
            out.name("command-id").value(headerLoginRequest.getCommandId());
            out.endObject();
        }

        //write bodyLoginRequest
        if (bodyLoginRequest != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyLoginRequest.getAuditNumber());
            out.name("mac").value(bodyLoginRequest.getMac());
            out.name("disk-drive").value(bodyLoginRequest.getDiskDrive());
            out.name("signature").value(bodyLoginRequest.getSignature());
            out.name("pin-login").value(bodyLoginRequest.getPinLogin());
            out.name("version-app").value(bodyLoginRequest.getVersionApp());
            out.endObject();
        }

        //write footerLoginRequest
        if (footerLoginRequest != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerLoginRequest.getAccountIdt());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public LoginRequest read(JsonReader in) throws IOException {
        final LoginRequest loginRequest = new LoginRequest();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderLoginRequest headerLoginRequest = new HeaderLoginRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerLoginRequest.setAgent(in.nextString());
                                break;
                            case "password":
                                headerLoginRequest.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerLoginRequest.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setHeaderLoginRequest(headerLoginRequest);
                    break;

                case "body":
                    final BodyLoginRequest bodyLoginRequest = new BodyLoginRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                bodyLoginRequest.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                bodyLoginRequest.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                bodyLoginRequest.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                bodyLoginRequest.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                bodyLoginRequest.setPinLogin(in.nextString());
                                break;
                            case "version-app":
                                bodyLoginRequest.setVersionApp(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setBodyLoginRequest(bodyLoginRequest);
                    break;

                case "footer":
                    final FooterLoginRequest footerLoginRequest = new FooterLoginRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                footerLoginRequest.setAccountIdt(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setFooterLoginRequest(footerLoginRequest);
                    break;
            }
        }

        in.endObject();

        return loginRequest;
    }
}