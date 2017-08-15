
package com.ubtech.utilcode.utils.network;

import android.text.TextUtils;



/**
 * IP地址字符串和数值形式之间转换实用类。
 * 
 * @author nongfuliu
 */
public class IPAddressUtils 
{
	/**
	 * 日志标志
	 */
	private static final String TAG = "IPAddressUtils";
	
	/**
	 * 把字符串类型的IP地址转换成数值。
	 * 
	 * @param ipStr
	 * 		要转换的IP地址字符串
	 * @return
	 * 		IP地址的数值表示
	 */
	public static long ipToLong(String ipStr)
	{
		if (TextUtils.isEmpty(ipStr))
			throw new IllegalArgumentException("empty ip!");

        int[] ip = new int[4];   
        String[] ips = ipStr.split(".");
        
        if (ips == null || ips.length != 4)
        {

        	return	0;
        }
        
        long ret = 0;
        for (int i = 0; i < 3; i++)
        {        	
        	try
			{
        		ip[i] = Integer.parseInt(ips[i]);   
        		if (ip[i] < 0 || ip[i] > 255)
            	{
            		return	0;
            	}
        		ret <<= 8;
        		ret |= ip[i];
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return 0;
			}
        }       
        
        return ret;  
	}
	
	/**
	 * 把数值类型的IP地址转换成字符串
	 * 
	 * @param ip
	 * 		要转换的IP地址的数值表示
	 * @return
	 * 		IP地址的字符串表示
	 */
	public static String longToIP(long ip)
	{
        StringBuffer buffer = new StringBuffer(""); 
        
        //直接右移24位  
        buffer.append(String.valueOf((ip >>> 24) & 0xff));  
        buffer.append(".");  
        
        //将高8位置0，然后右移16位  
        buffer.append(String.valueOf((ip>>> 16) &0xff));  
        buffer.append(".");  
        
        //将高16位置0，然后右移8位  
        buffer.append(String.valueOf((ip >>> 8) & 0xff));  
        buffer.append(".");  
        
        //将高24位置0  
        buffer.append(String.valueOf((ip & 0xff)));  
        
        return buffer.toString();  
	}
}
