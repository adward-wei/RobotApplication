// IMotorReadAngleListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

// Declare any non-default types here with import statements

interface IMotorReadAngleListener {
    void onReadMotorAngle(int nOpId, int nErr, int angle);
}
