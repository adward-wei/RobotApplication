package com.ubtechinc.alpha.serverlibutil.aidl;

import com.ubtechinc.alpha.serverlibutil.aidl.IRemoteLedListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IRemoteLedOperationResultListener;
interface ILedInterface{
   int getLedList(IRemoteLedListResultListener listener);

   int turnOnEye(int color,IRemoteLedOperationResultListener listener);
   void turnOffEye(IRemoteLedOperationResultListener listener);
   int turnOnEyeBlink(IRemoteLedOperationResultListener listener);
   void turnOffEyeBlink(IRemoteLedOperationResultListener listener);
   int turnOnEyeFlash(int color, int bright, int onTime, int TotalTime, IRemoteLedOperationResultListener listener);
   void turnOffEyeFlash(IRemoteLedOperationResultListener listener);
   int turnOnEyeMarquee(int color, int bright, int onTime, int TotalTime, IRemoteLedOperationResultListener listener);
   void turnOffEyeMarquee(IRemoteLedOperationResultListener listener);

   int turnOnHead(int color, int bright, IRemoteLedOperationResultListener listener);
   void turnOffHead(IRemoteLedOperationResultListener listener);
   int turnOnHeadFlash(int color, int bright, int onTime, int totalTime, IRemoteLedOperationResultListener listener);
   void turnOffHeadFlash(IRemoteLedOperationResultListener listener);
   int turnOnHeadMarquee(int color, int bright, int onTime, int totalTime, IRemoteLedOperationResultListener listener);
   void turnOffHeadMarquee(IRemoteLedOperationResultListener listener);
   int turnOnHeadBreath(int color, int bright, int onTime, int totalTime, IRemoteLedOperationResultListener listener);
   void turnOffHeadBreath(IRemoteLedOperationResultListener listener);

   int turnOnChestLed(IRemoteLedOperationResultListener listener);
   void turnOffChestLed(IRemoteLedOperationResultListener listener);
}  