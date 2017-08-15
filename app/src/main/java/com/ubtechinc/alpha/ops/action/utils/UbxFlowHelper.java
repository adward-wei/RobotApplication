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
import com.ubtechinc.alpha.ops.action.Constraints;
import com.ubtechinc.alpha.ops.action.Page;
import com.ubtechinc.alpha.ops.action.UbxFlow;

import java.util.List;

/**
 * @desc : flow工具类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/2
 * @modifier:
 * @modify_time:
 */

public final class UbxFlowHelper {

    private UbxFlowHelper(){
        throw  new RuntimeException("");
    }

    public static Page findRootPage(UbxFlow flow) {
        return findPageById(flow, Constraints.ROOT_PAGE_ID);
    }

    public static Page findPageById(UbxFlow flow, int pageId){
        List<Page> pages = flow.getPages();
        for (Page page: pages) {
            if (page.getId() == pageId)
                return page;
        }
        return null;
    }

    public static Page findLastPage(UbxFlow flow, int nPageID){
        Page page = null;
        for(Page p : flow.getPages()){
            for (Block b: p.getBlocks()) {
                if (b.getLinkToId() == nPageID){
                    page = p;
                }
            }
        }
        return page;
    }

    public static Block findLastBlock(UbxFlow flow, int linkToId){
        Block block = null;
        for(Page p : flow.getPages()){
            for (Block b: p.getBlocks()) {
                if (b.getLinkToId() == linkToId){
                    block = b;
                }
            }
        }
        return block;
    }
}
