package com.qzt.common.sms.webService.vpnClient;

public class CmdManagerProxy implements CmdManager {
  private String _endpoint = null;
  private CmdManager cmdManager = null;

  public CmdManagerProxy() {
    _initCmdManagerProxy();
  }

  public CmdManagerProxy(String endpoint) {
    _endpoint = endpoint;
    _initCmdManagerProxy();
  }

  private void _initCmdManagerProxy() {
    try {
      cmdManager = (new CmdManagerServiceLocator()).getCmdManager();
      if (cmdManager != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cmdManager)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cmdManager)._getProperty("javax.xml.rpc.service.endpoint.address");
      }

    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }

  public String getEndpoint() {
    return _endpoint;
  }

  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cmdManager != null)
      ((javax.xml.rpc.Stub)cmdManager)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

  }

  public CmdManager getCmdManager() {
    if (cmdManager == null)
      _initCmdManagerProxy();
    return cmdManager;
  }

  public String sendCmd(String strSN, int nType, String strCmd) throws java.rmi.RemoteException{
    if (cmdManager == null)
      _initCmdManagerProxy();
    return cmdManager.sendCmd(strSN, nType, strCmd);
  }


}
