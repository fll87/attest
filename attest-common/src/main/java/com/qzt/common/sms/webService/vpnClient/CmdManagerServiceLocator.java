/**
 * CmdManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qzt.common.sms.webService.vpnClient;

import javax.xml.namespace.QName;

public class CmdManagerServiceLocator extends org.apache.axis.client.Service implements CmdManagerService {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CmdManagerServiceLocator() {
    }


    public CmdManagerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CmdManagerServiceLocator(String wsdlLoc, QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CmdManager
    private String CmdManager_address = "http://localhost:8080/31Test/services/CmdManager";

    public String getCmdManagerAddress() {
        return CmdManager_address;
    }

    // The WSDD service name defaults to the port name.
    private String CmdManagerWSDDServiceName = "CmdManager";

    public String getCmdManagerWSDDServiceName() {
        return CmdManagerWSDDServiceName;
    }

    public void setCmdManagerWSDDServiceName(String name) {
        CmdManagerWSDDServiceName = name;
    }

    public CmdManager getCmdManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CmdManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCmdManager(endpoint);
    }

    public CmdManager getCmdManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	CmdManagerSoapBindingStub _stub = new CmdManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getCmdManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCmdManagerEndpointAddress(String address) {
        CmdManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CmdManager.class.isAssignableFrom(serviceEndpointInterface)) {
            	CmdManagerSoapBindingStub _stub = new CmdManagerSoapBindingStub(new java.net.URL(CmdManager_address), this);
                _stub.setPortName(getCmdManagerWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("CmdManager".equals(inputPortName)) {
            return getCmdManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://server.webService.qzt360.com", "CmdManagerService");
    }

    private java.util.HashSet<QName> ports = null;

    public java.util.Iterator<QName> getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet<QName>();
            ports.add(new QName("http://server.webService.qzt360.com", "CmdManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("CmdManager".equals(portName)) {
            setCmdManagerEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
