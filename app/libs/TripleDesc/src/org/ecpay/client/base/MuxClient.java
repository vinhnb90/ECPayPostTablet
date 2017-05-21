package org.ecpay.client.base;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISORequest;
import org.jpos.iso.channel.ASCIIChannel;

/**
 * Created by NhatNV on Dec 03, 2014 11:28.
 */
public class MuxClient extends BaseClient {
  private ISOMux mux;

  public MuxClient() {
  }

  public MuxClient(String serverHost, int serverPort) {
    super(serverHost, serverPort);
  }

  public MuxClient(String serverHost, int serverPort, ISOPackager packager) {
    super(serverHost, serverPort, packager);
  }

  @Override
  protected boolean checkNetwork() {
    return mux.isConnected();
  }

  @Override
  protected void myInit() {
    BaseChannel channel = new ASCIIChannel(serverHost, serverPort, packager);
    if (localPort > 0) {
      channel.setLocalAddress("", localPort);
    }
    mux = new ISOMux(channel);
    Thread thread = new Thread(mux);
    thread.start();
  }

  @Override
  protected void myTerminal() {
    if (mux.isConnected())
      mux.terminate();
  }

  @Override
  protected ISOMsg send(ISOMsg reqMsg, int timeout) throws Exception {
    reqMsg.setDirection(ISOMsg.OUTGOING);
    ISORequest ir = new ISORequest(reqMsg);
    logger.info("state[isConnected : " + mux.isConnected() + ", doConnect : " + mux.getConnect() + "]");
    printInfo(timeout);
    mux.queue(ir);

    return ir.getResponse(timeout);
  }

  private void printInfo(int timeout) {
    BaseChannel channel = ((BaseChannel) mux.getISOChannel());
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    sb.append("host          : ").append(channel.getHost()).append("\n");
    sb.append("port          : ").append(channel.getPort()).append("\n");
    sb.append("connected     : ").append(mux.isConnected()).append("\n");
    sb.append("doConnect     : ").append(mux.getConnect()).append("\n");
    sb.append("timeout       : ").append(timeout).append("\n");
    sb.append("originalRealm : ").append(channel.getOriginalRealm()).append("\n");
    sb.append("realm         : ").append(channel.getRealm()).append("\n");
    logger.info(sb.toString());
  }
}
