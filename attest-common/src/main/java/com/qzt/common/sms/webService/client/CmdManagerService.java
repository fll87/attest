/**
 * CmdManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qzt.common.sms.webService.client;

public interface CmdManagerService extends javax.xml.rpc.Service {
	public String getCmdManagerAddress();

	public CmdManager getCmdManager() throws javax.xml.rpc.ServiceException;

	public CmdManager getCmdManager(java.net.URL portAddress)
			throws javax.xml.rpc.ServiceException;
}
