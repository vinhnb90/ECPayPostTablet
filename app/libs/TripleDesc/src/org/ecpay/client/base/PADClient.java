package org.ecpay.client.base;

import org.jpos.iso.ISOMsg;

/**
 * Created by NhatNV on Dec 09, 2014 14:10.
 */
public class PADClient extends BaseClient {
  @Override
  protected boolean checkNetwork() {
    return false;
  }

  @Override
  protected void myInit() {

  }

  @Override
  protected void myTerminal() {

  }

  @Override
  protected ISOMsg send(ISOMsg reqMsg, int timeout) throws Exception {
    return null;
  }
}
