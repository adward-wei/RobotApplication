package com.ubtechinc.alpha2ctrlapp.events;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;

/**
 * @author：tanghongyu
 * @date：6/15/2017 2:43 PM
 * @modifier：tanghongyu
 * @modify_date：6/15/2017 2:43 PM
 * [A brief description]
 * version
 */

public class ActionDownloadStatusChangeEvent {

   ActionFileEntrity actionFileEntrity;

    public ActionFileEntrity getActionFileEntrity() {
        return actionFileEntrity;
    }

    public void setActionFileEntrity(ActionFileEntrity actionFileEntrity) {
        this.actionFileEntrity = actionFileEntrity;
    }
}
