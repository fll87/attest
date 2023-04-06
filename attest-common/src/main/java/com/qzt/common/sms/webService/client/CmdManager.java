/**
 * CmdManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qzt.common.sms.webService.client;

public interface CmdManager extends java.rmi.Remote {

	public String sendSMS(int nCustomerId, TelInfo telInfo)
			throws java.rmi.RemoteException;

	public String sendCmd(String strSN, int nType, String strCmd)
			throws java.rmi.RemoteException;

}
