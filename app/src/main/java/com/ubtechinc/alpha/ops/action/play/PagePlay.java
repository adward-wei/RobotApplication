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

package com.ubtechinc.alpha.ops.action.play;

import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Constraints;
import com.ubtechinc.alpha.ops.action.Line;
import com.ubtechinc.alpha.ops.action.Page;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;
import com.ubtechinc.alpha.ops.action.utils.PageHelper;
import com.ubtechinc.alpha.ops.action.utils.UbxFlowHelper;

import timber.log.Timber;

/**
 * @desc : 页播放
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/5
 * @modifier:
 * @modify_time:
 */

class PagePlay implements IPlayer {
    private final Page page;
    private final int startBlockId;
    private final int startPortId;

    private PlayController player;

    PagePlay(Page page, int startBlockId, int startPortId) {
        this.page = page;
        this.startBlockId = startBlockId;
        this.startPortId = startPortId;
    }

    void setPlayer(PlayController player) {
        this.player = player;
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        if (player == null) return;
        handlePagePlay(data, nType);
    }

    private void handlePagePlay(byte[] data, @ILayerResult.DataType int nType) {
        Line[] lines = PageHelper.fineLinesByOutId(page, startBlockId, startPortId);
        for (int i = 0; i < lines.length; i++) {
            Line curLine = lines[i];
            if (curLine.getInBlockId() == Constraints.STOP_BLOCK_ID) {
                if (page.getId() == Constraints.ROOT_PAGE_ID) {
                    // FIXME: 2017/5/5 这里该直接return还是该让继续处理剩余的连？不一定是播放完成
                    Timber.d("play completed...??");
                } else {
                    //查找上一页
                    Page lastPage = UbxFlowHelper.findLastPage(player.getFlow(), page.getId());
                    Block lastBlock = UbxFlowHelper.findLastBlock(player.getFlow(), page.getId());
                    if (lastBlock == null || lastBlock == null) {
                        Timber.e("error...111");
                        continue;
                    }
                    PagePlay lastPagePlay = new PagePlay(lastPage, lastBlock.getId(), BlockHelper.findPortIdByPortType(lastBlock, Port.TYPE_STOP));
                    lastPagePlay.setPlayer(player);
                    lastPagePlay.play(data, nType);
                }
            } else {
                Block block = PageHelper.findBlockById(page, curLine.getInBlockId());
                if (block == null) {
                    Timber.e("error...222");
                    continue;
                }

                Port port = BlockHelper.findPortByPortId(block, curLine.getInPortId());
                if (data != null) {
                    port.writePortData(data, nType);
                }

                if (port.getType() == Port.TYPE_GENERAL || port.getType() == Port.TYPE_START) {
                    if (block.getType() == Block.BLOCK_FLOWCHART) {
                        Page nextPage = UbxFlowHelper.findPageById(player.getFlow(), block.getLinkToId());
                        PagePlay nextPlay = new PagePlay(nextPage, Constraints.START_BLOCK_ID, Constraints.START_BLOCK_START_PORT_ID);
                        nextPlay.setPlayer(player);
                        nextPlay.play(data, nType);
                        continue;
                    }
                }
                player.playBlock(page, block, port);
            }
        }
    }
}
