package com.ubt.alpha2.download.util;

import java.util.List;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class ListUtils {

    public static final boolean isEmpty(List list) {
        return !(list != null && list.size() > 0);
    }
}
