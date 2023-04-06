package com.qzt.common.sms;

import cn.hutool.core.util.StrUtil;
import com.qzt.common.constant.GlobalErrorCodeConstants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.sms.webService.client.CmdManager;
import com.qzt.common.sms.webService.client.CmdManagerProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Slf4j
@Component
public class SMSManager {

	private static String SMS_SERVER_URL = "http://authdic.qzt360.com:8008/services/CmdManager";

	/**
	 * 发送验证码,同一IP24小时内发送次数不超过20次
	 */
	public static void sendSMS(String strTel, String strContent){
		if (StrUtil.isBlank(strTel) || StrUtil.isBlank(strContent)) {
			throw new ServiceException(GlobalErrorCodeConstants.BAD_REQUEST);
		}
		CmdManager cm = new CmdManagerProxy(SMS_SERVER_URL);
		try {
			StringBuffer sbCmd = new StringBuffer();
			sbCmd.append("kkpt-istep");
			sbCmd.append(";");
			sbCmd.append(strTel);
			sbCmd.append(";");
			sbCmd.append(strContent.replace("【企智通】", ""));

			String strResult = cm.sendCmd("HelpMeHelpYou", 5000, sbCmd.toString() );
			log.info("sendSMS:{} result:{}", sbCmd.toString(), strResult);
		} catch (RemoteException e) {
			log.error("error:{}", e);
			throw new ServiceException(SysErrorCodeConstants.SMS_SEND_FAIL);
		}
	}

}
