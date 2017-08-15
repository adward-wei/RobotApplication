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

import com.ubtechinc.alpha.model.speech.SlotValue;

/**
 * @desc: 离线语义信息
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier: logic
 * @modify_time: 2017/5/22
 */

public class OfflineSemantic extends MixSemantic {
     @SlotValue.Type int slot;

     public int getSlot() {
          return slot;
     }
}
