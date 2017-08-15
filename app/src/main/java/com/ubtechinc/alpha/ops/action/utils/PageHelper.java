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

package com.ubtechinc.alpha.ops.action.utils;

import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Line;
import com.ubtechinc.alpha.ops.action.Page;

import java.util.ArrayList;

/**
 * @desc : 页工具类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/2
 * @modifier:
 * @modify_time:
 */

public final class PageHelper {

    private PageHelper(){
        throw new RuntimeException("");
    }

    /**
     * 页中查找指定块的某一端口上连接线
     * @param page
     * @param blockId
     * @param portId
     * @return
     */
    public static Line[] fineLinesByOutId(Page page, int blockId, int portId) {
        Line line;
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < page.getLines().size(); i++){
            line = page.getLines().get(i);
            if (line.getOutBlockId() == blockId && line.getOutPortId() == portId){
                lines.add(line);
            }
        }
        return lines.toArray(new Line[lines.size()]);
    }

    /**
     * 页中查找指定块
     * @param page
     * @param blockId
     * @return
     */
    public static Block findBlockById(Page page, int blockId){
        for (Block block:page.getBlocks()) {
            if (block.getId() == blockId){
                return block;
            }
        }
        return null;
    }
}
