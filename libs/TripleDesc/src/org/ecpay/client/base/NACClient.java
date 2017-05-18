package org.ecpay.client.base;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.NACChannel;

/**
 * Created by NhatNV on Dec 09, 2014 13:27.
 */
public class NACClient extends BaseClient {
  private BaseChannel channel;
  private byte[] header;

  @Override
  protected boolean checkNetwork() {
    return false;
  }

  @Override
  protected void myInit() {
    channel = new NACChannel(serverHost, serverPort, packager, header);
  }

  @Override
  protected void myTerminal() {

  }

  @Override
  protected ISOMsg send(ISOMsg reqMsg, int timeout) throws Exception {
    channel.setTimeout(timeout);
    channel.send(reqMsg);
    channel.receive();
    return null;
  }
}
