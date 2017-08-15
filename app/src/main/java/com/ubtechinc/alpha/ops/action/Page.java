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

package com.ubtechinc.alpha.ops.action;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @desc : 页Model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class Page implements IByteProcessor {

    private int mLength;
    private int id;
    private int portTotalLength;
    private ArrayList<Port> ports = new ArrayList<>();
    private int lineTotalLength;
    private ArrayList<Line> lines = new ArrayList<>();
    private int blockTotalLength;
    private ArrayList<Block> blocks = new ArrayList<>();

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length)
            return false;

        bbl.get(bytes);
        id = ConvertUtils.l_byte2Int(bytes);//ID

        //端口解析
        bbl.get(bytes);
        portTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (portTotalLength > 0) {
            bbl.get(4);
            bbl.get(bytes);
            int portCount = ConvertUtils.l_byte2Int(bytes);
            for (int i = 0 ; i < portCount; i++){
                bbl.get(bytes);
                int portLength = ConvertUtils.l_byte2Int(bytes);
                ByteBufferList portBBL = bbl.get(portLength);
                Port port = new Port();
                boolean ret = port.analysis(portBBL, portLength);
                if (!ret) return false;
                ports.add(port);
            }
        }

        //line解析
        bbl.get(bytes);
        lineTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (lineTotalLength > 0){
            bbl.get(4);
            bbl.get(bytes);
            int lineCount = ConvertUtils.l_byte2Int(bytes);
            for(int i = 0; i < lineCount; i ++){
                bbl.get(bytes);
                int lineLength = ConvertUtils.l_byte2Int(bytes);
                ByteBufferList lineBBL = bbl.get(lineLength);
                Line line = new Line();
                boolean ret = line.analysis(lineBBL, lineLength);
                if (!ret) return false;
                lines.add(line);
            }
        }

        //block解析
        bbl.get(bytes);
        blockTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (blockTotalLength > 0){
            bbl.get(4);
            bbl.get(bytes);
            int blockCount = ConvertUtils.l_byte2Int(bytes);
            for (int i = 0; i < blockCount; i++){
                bbl.get(bytes);
                int blockLength = ConvertUtils.l_byte2Int(bytes);
                ByteBufferList blockBBL = bbl.get(blockLength);
                Block block = new Block();
                boolean ret = block.analysis(blockBBL, blockLength);
                if (!ret) return false;
                blocks.add(block);
            }
        }

        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = mLength + 4;//长度写了两边
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));//长度，注意，要写两遍...
        bb.put(ConvertUtils.l_int2Byte(mLength));

        bb.put(ConvertUtils.l_int2Byte(id));//id

        //端口数据
        bb.put(ConvertUtils.l_int2Byte(portTotalLength));
        bb.put(ConvertUtils.l_int2Byte(portTotalLength));
        bb.put(ConvertUtils.l_int2Byte(ports.size()));
        int sum = 0;
        for (int i =0 ; i < ports.size(); i++){
            bb.put(ports.get(i).toBytes());
            sum += ports.get(i).toBytes().length;
        }
        Timber.d("port:%d, %d", portTotalLength, sum);

        //line数据序列
        bb.put(ConvertUtils.l_int2Byte(lineTotalLength));
        bb.put(ConvertUtils.l_int2Byte(lineTotalLength));
        bb.put(ConvertUtils.l_int2Byte(lines.size()));
        sum = 0;
        for(int i = 0 ; i < lines.size(); i ++){
            bb.put(lines.get(i).toBytes());
            sum += lines.get(i).toBytes().length;
        }
        Timber.d("line:%d, %d", sum, lineTotalLength);

        //块数据
        bb.put(ConvertUtils.l_int2Byte(blockTotalLength));
        bb.put(ConvertUtils.l_int2Byte(blockTotalLength));
        bb.put(ConvertUtils.l_int2Byte(blocks.size()));
        sum = 0;
        for (int i =0 ; i < blocks.size(); i ++){
            bb.put(blocks.get(i).toBytes());
            sum += blocks.get(i).toBytes().length;
        }
        Timber.d("block:%d = %d", blockTotalLength, sum);

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }


    //////////////////getter////////////////////
    public int getId() {
        return id;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

}
