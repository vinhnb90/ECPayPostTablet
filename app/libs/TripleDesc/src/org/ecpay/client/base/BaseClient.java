package org.ecpay.client.base;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

/**
 * Created by NhatNV on Dec 03, 2014 11:18.
 */
public abstract class BaseClient {
  public Logger logger = Logger.getLogger(getClass());
  protected String serverHost;
  protected int serverPort;
  protected int localPort = -1;
  protected ISOPackager packager;

  public BaseClient() {
  }

  protected BaseClient(String serverHost, int serverPort) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
  }

  protected BaseClient(String serverHost, int serverPort, ISOPackager packager) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
    this.packager = packager;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public int getLocalPort() {
    return localPort;
  }

  public void setLocalPort(int localPort) {
    this.localPort = localPort;
  }

  public ISOPackager getPackager() {
    return packager;
  }

  public void setPackager(ISOPackager packager) {
    this.packager = packager;
  }

  public ISOMsg request(ISOMsg isoMsg, int timeout) throws Exception {
    return send(isoMsg, timeout);
  }

  public void terminal() {
    myTerminal();
  }

  public void init() {
    myInit();
  }

  public boolean isConnected() {
    return checkNetwork();
  }

  protected abstract boolean checkNetwork();

  protected abstract void myInit();

  protected abstract void myTerminal();

  protected abstract ISOMsg send(ISOMsg reqMsg, int timeout) throws Exception;

  static final String PACK_MESSAGE = "pack message = ";
  static final String NEW_LINE = "\n";
  static final String MESSAGE_DETAIL = "message detail : \n";
  static final String FIELD = "field_";
  static final String EQUAL = " = ";
  static final int BA = 3;
  static final char O = '0';

  public String getString(ISOMsg isoMsg) {
    if (isoMsg != null) {
      try {
        StringBuilder s = new StringBuilder();
        s.append(PACK_MESSAGE).append(new String(isoMsg.pack())).append(NEW_LINE);
        s.append(MESSAGE_DETAIL);
        int maxField = isoMsg.getMaxField();
        for (int i = 0; i <= maxField; i++) {
          if (isoMsg.hasField(i)) {
            s.append(FIELD).append(padLeft(i, BA, O)).append(EQUAL).append(isoMsg.getString(i)).append(NEW_LINE);
          }
        }
        return s.toString();
      } catch (Exception e) {
        logger.error(e);
      }
    }
    return "is null";
  }

  public static String padLeft(String s, int len, char c) throws Exception {
    s = s.trim();
    if (s.length() > len)
      throw new Exception("invalid len " + s.length() + "/" + len);
    StringBuilder d = new StringBuilder(len);
    int fill = len - s.length();
    while (fill-- > 0)
      d.append(c);
    d.append(s);
    return d.toString();
  }

  public static String padLeft(int s, int len, char c) throws Exception {
    return padLeft(String.valueOf(s), len, c);
  }

}
