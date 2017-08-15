package com.ubtechinc.alpha.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/5/27.
 */

public final class BinderProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        return true;
    }


    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        if ("@".equals(method)) {
            Bundle bundle = new Bundle();
            bundle.putBinder("fetchBinder",MainServiceImpl.getMainServiceImplInstance());
            return bundle;
        }
        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
