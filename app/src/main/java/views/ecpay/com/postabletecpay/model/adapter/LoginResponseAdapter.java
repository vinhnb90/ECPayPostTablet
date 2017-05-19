package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.Body;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.Footer;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.Header;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponse;

/**
 * Created by VinhNB on 5/19/2017.
 */

public class LoginResponseAdapter extends TypeAdapter<LoginResponse> {
    @Override
    public void write(JsonWriter out, LoginResponse value) throws IOException {
        Header header = value.getHeader();
        Body body = value.getBody();
        Footer footer = value.getFooter();

        out.beginObject();
        //write header
        if (header != null) {
            out.name("header").beginObject();
            out.name("agent").value(header.getAgent());
            out.name("password").value(header.getPassword());
            out.name("command-id").value(header.getCommandId());
            out.endObject();
        }

        //write body
        if (body != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(body.getAuditNumber());
            out.name("mac").value(body.getMac());
            out.name("disk-drive").value(body.getDiskDrive());
            out.name("signature").value(body.getSignature());
            out.name("pin-login").value(body.getPinLogin());
            out.endObject();
        }

        //write footer
        if (footer != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footer.getAccountIdt());
            out.name("response-code").value(footer.getResponseCode());
            out.name("description").value(footer.getDescription());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public LoginResponse read(JsonReader in) throws IOException {
        final LoginResponse loginRequest = new LoginResponse();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final Header header = new Header();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                header.setAgent(in.nextString());
                                break;
                            case "password":
                                header.setPassword(in.nextString());
                                break;
                            case "command-id":
                                header.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setHeader(header);
                    break;

                case "body":
                    final Body body = new Body();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                body.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                body.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                body.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                body.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                body.setPinLogin(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setBody(body);
                    break;

                case "footer":
                    final Footer footer = new Footer();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                footer.setAccountIdt(in.nextString());
                                break;
                            case "response-code":
                                footer.setResponseCode(in.nextString());
                                break;
                            case "description":
                                footer.setDescription(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    loginRequest.setFooter(footer);
                    break;
            }
        }

        in.endObject();

        return loginRequest;
    }
}