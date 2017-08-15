package com.ubtechinc.nets.http.cookie;

import com.ubtech.utilcode.utils.CloseUtils;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtech.utilcode.utils.EncodeUtils;
import com.ubtech.utilcode.utils.EncryptUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import okhttp3.Cookie;

/**
 * @desc : cookie 工具
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/15
 * @modifier:
 * @modify_time:
 */

final class CookieUtil {
    private static final String TAG = "CookieUtil";
    private static long NON_VALID_EXPIRES_AT = -1L;
    static String serialize(Cookie cookie){
        if (cookie == null) return null;
        cookie.persistent();
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            writeObject(oos, cookie);
            return ConvertUtils.bytes2HexString(EncodeUtils.base64Encode(bos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            CloseUtils.closeIOQuietly(oos);
        }
        return null;
    }

    /**
     * 反序列化Cookie
     * @param hexCookieStr
     * @return
     */
    static Cookie deserialize(String hexCookieStr){
        if (hexCookieStr == null) return null;
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(EncodeUtils.base64Decode(ConvertUtils.hexString2Bytes(hexCookieStr)));
            ois = new ObjectInputStream(bis);
            return readObject(ois);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIOQuietly(ois);
        }
        return null;
    }

    static String cookieKey(Cookie cookie) {
        return EncryptUtils.encryptMD5ToString(cookie.toString());
    }

    static boolean cookieExpires(Cookie cookie){
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    private static void writeObject(ObjectOutputStream out, Cookie cookie) throws IOException {
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
        out.writeLong(cookie.persistent() ? cookie.expiresAt() : NON_VALID_EXPIRES_AT);
        out.writeObject(cookie.domain());
        out.writeObject(cookie.path());
        out.writeBoolean(cookie.secure());
        out.writeBoolean(cookie.httpOnly());
        out.writeBoolean(cookie.hostOnly());
    }

    private static Cookie readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name((String) in.readObject());
        builder.value((String) in.readObject());
        long expiresAt = in.readLong();
        if (expiresAt != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(expiresAt);
        }
        final String domain = (String) in.readObject();
        builder.domain(domain);
        builder.path((String) in.readObject());
        if (in.readBoolean())
            builder.secure();
        if (in.readBoolean())
            builder.httpOnly();
        if (in.readBoolean())
            builder.hostOnlyDomain(domain);
        return builder.build();
    }
}
