/**
 * TelInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qzt.common.sms.webService.client;

public class TelInfo implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String strContent;

	private String strTel;

	public TelInfo() {
	}

	public TelInfo(String strContent, String strTel) {
		this.strContent = strContent;
		this.strTel = strTel;
	}

	/**
	 * Gets the strContent value for this TelInfo.
	 *
	 * @return strContent
	 */
	public String getStrContent() {
		return strContent;
	}

	/**
	 * Sets the strContent value for this TelInfo.
	 *
	 * @param strContent
	 */
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

	/**
	 * Gets the strTel value for this TelInfo.
	 *
	 * @return strTel
	 */
	public String getStrTel() {
		return strTel;
	}

	/**
	 * Sets the strTel value for this TelInfo.
	 *
	 * @param strTel
	 */
	public void setStrTel(String strTel) {
		this.strTel = strTel;
	}

	private Object __equalsCalc = null;

	public synchronized boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof TelInfo))
			return false;
		TelInfo other = (TelInfo) obj;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.strContent == null && other.getStrContent() == null)
						|| (this.strContent != null && this.strContent.equals(other.getStrContent())))
				&& ((this.strTel == null && other.getStrTel() == null)
						|| (this.strTel != null && this.strTel.equals(other.getStrTel())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getStrContent() != null) {
			_hashCode += getStrContent().hashCode();
		}
		if (getStrTel() != null) {
			_hashCode += getStrTel().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			TelInfo.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.webService.qzt360.com", "TelInfo"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("strContent");
		elemField.setXmlName(new javax.xml.namespace.QName("http://server.webService.qzt360.com", "strContent"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("strTel");
		elemField.setXmlName(new javax.xml.namespace.QName("http://server.webService.qzt360.com", "strTel"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(String mechType,
                                                                    Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(String mechType,
                                                                        Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
