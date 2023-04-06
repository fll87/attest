package com.qzt.common.utils.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.qzt.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;

/**
* @Title: ToolUtil
* @Description: 国密数据加解密
* @author wangyf
* @date 2022/6/29
*/
@Slf4j
@Component
public class ToolUtil {

	//private static final String defaultCharset = "UTF-8";
    private static final String strKey = "Qzt%^424$%fstABC";
    private static final String strIv = "ABCsdfs$^*bstQzt";

    /**
     * AES加密
     */
    public static String encryptAES(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
    	try {
    		//AES aes =SecureUtil.aes(strKey.getBytes());
    		AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                    new SecretKeySpec(strKey.getBytes(), "AES"),
                    new IvParameterSpec(strIv.getBytes()));
           return aes.encryptHex(content);
		} catch (Exception e) {
			 log.error("error",e);
		}
    	 return content;
    }

    /**
     * AES解密
     */
    public static String decryptAES(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
        try {
        	AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                    new SecretKeySpec(strKey.getBytes(), "AES"),
                    new IvParameterSpec(strIv.getBytes()));

            return aes.decryptStr(content);
        } catch (Exception e) {
        	 log.error("error",e);
        }
        return content;
    }

    /**
     * AES加密
     */
    public static String encrypt(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
    	try {
    		AES aes =SecureUtil.aes(strKey.getBytes());
           return aes.encryptHex(content);
		} catch (Exception e) {
			 log.error("error",e);
		}
    	 return content;
    }

    /**
     * AES解密
     */
    public static String decrypt(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
        try {
        	AES aes =SecureUtil.aes(strKey.getBytes());
            return aes.decryptStr(content);
        } catch (Exception e) {
        	 log.error("error",e);
        }
        return content;
    }

    /**
     * AES文件加密
     */
    public static boolean encryptFileAES(String sourceFilePath, String destFilePath) {
    	InputStream in =null;
    	OutputStream out =null;
    	try {
    		File sourceFile = new File(sourceFilePath);
	        File destFile = new File(destFilePath);
	        if (sourceFile.exists() && sourceFile.isFile()) {
	            if (!destFile.getParentFile().exists()) {
	                destFile.getParentFile().mkdirs();
	            }
	            destFile.createNewFile();
	            in = new FileInputStream(sourceFile);
	            out = new FileOutputStream(destFile);

	            AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
	                    new SecretKeySpec(strKey.getBytes(), "AES"),
	                    new IvParameterSpec(strIv.getBytes()));

	           out.write(aes.encrypt(in));
               out.flush();
	           return true;
	        }
		} catch (Exception e) {
			log.error("error",e);
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
				if(in!=null) {
					in.close();
				}
			} catch (Exception e) {
				log.error("error",e);
			}

		}
    	 return false;
    }
    /**
     * AES文件解密
     */
    public static boolean decryptFileAES(String sourceFilePath, String destFilePath) {
    	InputStream in =null;
    	OutputStream out =null;
    	try {
    		File sourceFile = new File(sourceFilePath);
            File destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
	                    new SecretKeySpec(strKey.getBytes(), "AES"),
	                    new IvParameterSpec(strIv.getBytes()));
  		        out.write(aes.decrypt(in));
            	out.flush();
                return true;
            }
		} catch (Exception e) {
			log.error("error",e);
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
				if(in!=null) {
					in.close();
				}

			} catch (Exception e) {
				log.error("error",e);
			}

		}
    	return false;
    }
    /**
     * SM3加密
     */
    public static String encryptSm3(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
    	try {
    		SM3 sm3 = SmUtil.sm3();
           return sm3.digestHex(content);
		} catch (Exception e) {
			 log.error("error",e);
		}
    	 return content;
    }

    /**
     * SM4加密
     */
    public static String encryptSm4(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
    	try {
    		//SymmetricCrypto sm4 = SmUtil.sm4(strKey.getBytes());
    		SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, strKey.getBytes(), strIv.getBytes());
           return sm4.encryptHex(content);
		} catch (Exception e) {
			 log.error("error",e);
		}
    	 return content;
    }

    /**
     * SM4解密
     */
    public static String decryptSm4(String content) {
    	if(StringUtils.isNull(content)){
    		return content;
    	}
        try {
        	SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, strKey.getBytes(), strIv.getBytes());
            return sm4.decryptStr(content);
        } catch (Exception e) {
        	 log.error("error",e);

        }
        return null;
    }

    /**
     * SM4解密
     */
    public static String decryptSm4ByKey(String content) {
        try {
        	SymmetricCrypto sm4 = SmUtil.sm4(strKey.getBytes());
            return sm4.decryptStr(content);
        } catch (Exception e) {
        	log.error("error",e);
        }
        return null;
    }

    /**
     * SM4文件加密
     */
    public static boolean encryptFileSm4(String sourceFilePath, String destFilePath) {
    	InputStream in =null;
    	OutputStream out =null;
    	try {
    		File sourceFile = new File(sourceFilePath);
	        File destFile = new File(destFilePath);
	        if (sourceFile.exists() && sourceFile.isFile()) {
	            if (!destFile.getParentFile().exists()) {
	                destFile.getParentFile().mkdirs();
	            }
	            destFile.createNewFile();
	            in = new FileInputStream(sourceFile);
	            out = new FileOutputStream(destFile);
	            //SymmetricCrypto sm4 = SmUtil.sm4(strKey.getBytes());
	            SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, strKey.getBytes(), strIv.getBytes());
	            out.write(sm4.encrypt(in));
	            out.flush();
	            return true;
	        }
		} catch (Exception e) {
			log.error("error",e);
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
				if(in!=null) {
					in.close();
				}
			} catch (Exception e) {
				log.error("error",e);
			}

		}
    	 return false;
    }

    /**
     * SM4文件解密
     */
    public static boolean decryptFileSm4(String sourceFilePath, String destFilePath) {
    	InputStream in =null;
    	OutputStream out =null;
    	try {
    		File sourceFile = new File(sourceFilePath);
            File destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                //SymmetricCrypto sm4 = SmUtil.sm4(strKey.getBytes());
                SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, strKey.getBytes(), strIv.getBytes());
  		        out.write(sm4.decrypt(in));
            	out.flush();
                return true;
            }
		} catch (Exception e) {
			log.error("error",e);
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
				if(in!=null) {
					in.close();
				}

			} catch (Exception e) {
				log.error("error",e);
			}

		}
		return false;
    }

    /**
     * SM4文件解密
     */
    public static boolean decryptFileSm4ByKey(String sourceFilePath, String destFilePath) {
    	InputStream in =null;
    	OutputStream out =null;
    	try {
    		File sourceFile = new File(sourceFilePath);
            File destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                SymmetricCrypto sm4 = SmUtil.sm4(strKey.getBytes());
  		        out.write(sm4.decrypt(in));
            	out.flush();
                return true;
            }
		} catch (Exception e) {
			log.error("error",e);
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
				if(in!=null) {
					in.close();
				}

			} catch (Exception e) {
				log.error("error",e);
			}

		}
    	return false;
    }

    public static void main(String[] args) {
		System.out.println(encryptSm4("123456"));
    }


}
