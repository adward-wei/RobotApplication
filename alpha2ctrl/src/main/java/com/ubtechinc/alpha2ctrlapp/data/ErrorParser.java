package com.ubtechinc.alpha2ctrlapp.data;

import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * Created by tanghongyu on 2017/6/11.
 */

public class ErrorParser {

    private static ErrorParser instance;

    private ErrorParser() {
    }

    public static ErrorParser get(){
        if (instance != null) return instance;
        synchronized(ErrorParser.class){
            if (instance == null) instance = new ErrorParser();
        }
        return instance;
    }


    private  ThrowableWrapper throwableWrapper;

    public  ThrowableWrapper getThrowableWrapper() {
        return throwableWrapper;
    }




    public  boolean isSuccess(int code) {
        int errorStringId = R.string.error_unknown_error;
        switch (code) {

            case 200:
                return true;

            case -1:

                errorStringId = R.string.error_message_$1;
                break;

            case -100:
                errorStringId = R.string.error_message_$100;
                break;
            case 101:
                errorStringId = R.string.error_message_101;
                break;
            case 1001:
                errorStringId = R.string.error_message_1001;
                break;
            case 1002:
                errorStringId = R.string.error_message_1002;
                break;
            case 1003:
                errorStringId = R.string.error_message_1003;
                break;
            case 1004:
                errorStringId = R.string.error_message_1004;
                break;
            case 1005:
                errorStringId = R.string.error_message_1005;
                break;
            case 1006:
                errorStringId = R.string.error_message_1006;
                break;
            case 1007:
                errorStringId = R.string.error_message_1007;
                break;
            case 1008:
                errorStringId = R.string.error_message_1008;
                break;
            case 102:
                errorStringId = R.string.error_message_102;
                break;
            case 104:
                errorStringId = R.string.error_message_104;
                break;
            case 105:
                errorStringId = R.string.error_message_105;
                break;
            case 2001:
                errorStringId = R.string.error_message_2001;
                break;
            case 2002:
                errorStringId = R.string.error_message_2002;
                break;
            case 2003:
                  errorStringId = R.string.error_message_2003;
                break;
            case 2004:
                errorStringId = R.string.error_message_2004;
                break;
            case 201:
                errorStringId = R.string.error_message_201;
                break;
            case 3001:
                errorStringId = R.string.error_message_3001;
                break;
            case 4001:
                errorStringId = R.string.error_message_4001;
                break;
            case 4002:
                errorStringId = R.string.error_message_4002;
                break;
            case 500:
                errorStringId = R.string.error_message_500;
                break;
            case 550:
                errorStringId = R.string.error_message_550;
                break;
            case 750:
                errorStringId = R.string.error_message_750;
                break;
            case 751:
                errorStringId = R.string.error_message_751;
                break;

            default:
                errorStringId = R.string.error_unknown_error;
                break ;
        }

        String msg = Utils.getContext().getString(errorStringId);
        throwableWrapper = new ThrowableWrapper(msg, code);
        return false;
    }
}
