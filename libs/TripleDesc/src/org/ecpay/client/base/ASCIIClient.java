package org.ecpay.client.base;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.channel.ASCIIChannel;

/**
 * Created by NhatNV on Dec 03, 2014 11:26.
 */
public class ASCIIClient extends BaseClient {
  private BaseChannel channel;

  public ASCIIClient() {
  }

  public ASCIIClient(String serverHost, int serverPort) {
    super(serverHost, serverPort);
  }

  public ASCIIClient(String serverHost, int serverPort, ISOPackager packager) {
    super(serverHost, serverPort, packager);
  }

  @Override
  protected boolean checkNetwork() {
    return channel.isConnected();
  }

  @Override
  protected void myInit() {
    channel = new ASCIIChannel(serverHost, serverPort, packager);
    if (localPort > 0) {
      channel.setLocalAddress("", localPort);
    }
  }

  @Override
  protected void myTerminal() {
    try {
      if (channel.isConnected())
        channel.disconnect();
    } catch (Exception e) {
      logger.error(e);
    }
  }

  protected void myTerminal(BaseChannel channel) {
    try {
      if (channel.isConnected())
        channel.disconnect();
    } catch (Exception e) {
      logger.error(e);
    }
  }

  @Override
  protected ISOMsg send(ISOMsg reqMsg, int timeout) throws Exception {
    reqMsg.setDirection(ISOMsg.OUTGOING);
    BaseChannel chan = (BaseChannel) channel.clone();
    chan.setTimeout(timeout);
    long t1 = System.currentTimeMillis();
    chan.connect();
    long t2 = System.currentTimeMillis();
    System.out.println("time to connected : " + (t2 - t1) + " milliseconds");
    printInfo(chan);
    chan.send(reqMsg);
    ISOMsg msg = chan.receive();
    t1 = System.currentTimeMillis();
    myTerminal(chan);
    t2 = System.currentTimeMillis();
    System.out.println("time to disconnected : " + (t2 - t1) + " milliseconds");
    return msg;
  }

  private void printInfo(BaseChannel channel) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    sb.append("host          : ").append(channel.getHost()).append("\n");
    sb.append("port          : ").append(channel.getPort()).append("\n");
    sb.append("connected     : ").append(channel.isConnected()).append("\n");
    sb.append("timeout       : ").append(channel.getTimeout()).append("\n");
    sb.append("originalRealm : ").append(channel.getOriginalRealm()).append("\n");
    sb.append("realm         : ").append(channel.getRealm()).append("\n");
    logger.info(sb.toString());
  }
}
