/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */
package com.ubtechinc.nlu.iflytekmix;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.ubtech.utilcode.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @desc: apk内预置语义excel文件解析助手
 * @author: Logic
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

final class ExcelSemanticHelper {

    private static final String TAG = "LocalExcelHelper";
    private static final String semantic_file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/local_semantic.xls";

    private ArrayList<ExcelSemantic> mChatList = new ArrayList<>();

    private Workbook mWorkbook = null;
    private Sheet mSheet = null;
    private boolean inited = false;

    public ExcelSemanticHelper(Context cxt) {
        try {
            //获取Excel文件对象
            mWorkbook = Workbook.getWorkbook(new File(semantic_file_path));
            //Sheet的下标是从0开始
            //获取文件的指定工作表 获取第二张Sheet表
            mSheet = mWorkbook.getSheet(1);
        } catch (Exception e) {
            LogUtils.w(semantic_file_path + " is not exist..." );
        }
        if(mWorkbook == null || mSheet == null) {
            try {
                mWorkbook = Workbook.getWorkbook(cxt.getAssets().open("local_semantic.xls"));
                //获取文件的指定工作表 获取第二张Sheet表
                mSheet = mWorkbook.getSheet(1);
            } catch (Exception e) {
                LogUtils.w("Assert File local_semantic.xml is not exist...");
            }
        }
    }

    public ArrayList<ExcelSemantic> getLocalSemantic(){
        if (mChatList.size() > 0)
            return mChatList;
        else
            return null;
    }

    public synchronized boolean isInited() {
        return inited;
    }

    public synchronized boolean  readConfig() {
        inited = true;
        if(mWorkbook == null || mSheet == null) return false;
        //列数
        int columnCount = mSheet.getColumns();
        //行数(表头的目录不需要，从1开始)
        int rowCount = mSheet.getRows();
        //从第三行开始找
        for (int i = 2; i < rowCount; i++) {
            ExcelSemantic semantic = new ExcelSemantic();
            int index = 1;
            //从第二列开始找问题
            String question = readContent(mSheet, index++, i, columnCount);
            String answer;
            if (!TextUtils.isEmpty(question)) {
                semantic.speechResult = question;
                int j = 3;
                do {
                    //从第三、四、五列开始找答案
                    answer = readContent(mSheet, index++, i, columnCount);
                    if (!TextUtils.isEmpty(answer)) {
                        semantic.answers.add(answer);
                    }
                }while (--j > 0);

                //从第六列获取类型
                answer = readContent(mSheet, index++, i, columnCount);
                if (!TextUtils.isEmpty(answer)) {
                    semantic.type = ExcelSemantic.namesToType(answer);
                    //从第七列获取操作
                    answer = readContent(mSheet, index++, i,columnCount);
                    if (!TextUtils.isEmpty(answer)) {
                        semantic.operation = answer;
                        //从第八列获取位置
                        answer = readContent(mSheet, index++, i,columnCount);
                        if (!TextUtils.isEmpty(answer)) {
                            semantic.slot = answer;
                        }
                    }
                }else {
                    semantic.type = ExcelSemantic.TYPE_CHAT;
                }
                mChatList.add(semantic);
            }

        }
        return true;
    }

    private String readContent(Sheet sheet, int column, int row, int totalColumn) {
        if (column >= totalColumn) return null;
        Cell cell = sheet.getCell(column, row);
        if (cell.getType() == CellType.NUMBER) {
            LogUtils.v((((NumberCell) cell).getValue()) + "");
        } else if (cell.getType() == CellType.DATE) {
            LogUtils.v((((DateCell) cell).getDate()).toString());
        } else {
            String contents = cell.getContents();
            if(contents.contains("？")) {
                contents = contents.replaceAll("？", "");
            }
            return contents;
        }
        return null;
    }

    public void clear() {
        if (mWorkbook != null)
            mWorkbook.close();
        mWorkbook = null;
    }
}
