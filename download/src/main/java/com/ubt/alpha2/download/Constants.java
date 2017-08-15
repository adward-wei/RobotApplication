package com.ubt.alpha2.download;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class Constants {

    public static final class CONFIG {
        public static final boolean DEBUG = true;
    }

    public static final class HTTP {
        public static final int CONNECT_TIME_OUT = 30 * 1000;
        public static final int READ_TIME_OUT = 60 * 1000;
        public static final String GET = "GET";
    }
}
