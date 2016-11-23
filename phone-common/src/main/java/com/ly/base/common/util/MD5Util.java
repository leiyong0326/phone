package com.ly.base.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * MD5工具类
 * @author LeiYong
 *
 */
public class MD5Util {
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	 
    /** 
     * base64进制加密 
     * 
     * @param content 
     * @return 
     */ 
    public static String encryptBase64(String content) {
        byte[] bytes = content.getBytes(); 
        return Base64.encodeBase64String(bytes); 
    } 
    /** 
     * base64进制解密 
     * @param cipherText 
     * @return 
     */ 
    public static String decryptBase64(String cipherText) { 
        return new String(Base64.decodeBase64(cipherText)); 
    } 
    /** 
     * 16进制加密 
     * 
     * @param password 
     * @return 
     */ 
    public static String encryptHex(String password) { 
        byte[] bytes = password.getBytes(); 
        return Hex.encodeHexString(bytes); 
    } 
    /** 
     * 16进制解密 
     * @param cipherText 
     * @return 
     */ 
    public static String decryptHex(String cipherText) {
    	try {
    		return new String(Hex.decodeHex(cipherText.toCharArray()));
		} catch (DecoderException e) {
			
		}
        return null; 
    } 

    /**
     * md5加密
     * @param content
     * @return
     */
    public static String md5(String content){
    	return DigestUtils.md5Hex(content);
    }
    /**
     * md5组合加密
     * @param content
     * @return
     */
    public static String md5s(String... content){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < content.length; i++) {
			sb.append(md5(content[i]));
		}
    	return sb.toString();
    }
    
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }
    
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
//    /** 
//     * 对密码进行md5加密,并返回密文和salt，包含在User对象中 
//     * @param username 用户名 
//     * @param password 密码 
//     * @return 密文和salt 
//     */ 
//    public static User md5Password(String username,String password){ 
//        SecureRandomNumberGenerator secureRandomNumberGenerator=new SecureRandomNumberGenerator(); 
//        // salt随机生成
//        String salt= secureRandomNumberGenerator.nextBytes().toHex(); 
//        //组合username,两次迭代，对密码进行加密 
//        String password_cipherText= new Md5Hash(password,username + salt,2).toBase64(); 
//        
//        User user=new User(); 
//        user.setPassword(password_cipherText); 
//        user.setSalt(salt); 
//        user.setUsername(username); 
//        return user; 
//    } 
    
	/**
	 * 获取文件md5值
	 * 
	 * @param file
	 *            要被计算的file
	 * @return md5
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException
	{
		String value = null;
		if (file.isFile())
		{
			try {
				value = DigestUtils.md5Hex(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	 * 混合加密
	 * @param value
	 * @return
	 */
	public static String encryptBlend(String value){
		return encryptBase64(encryptHex(value));
	}
	/**
	 * 混合解密
	 * @param value
	 * @return
	 */
	public static String decryptBlend(String value){
		return decryptHex(decryptBase64(value));
	}
}