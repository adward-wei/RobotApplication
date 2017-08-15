#include "arcsoft_face_tracking.h"
#include <stdint.h>
#include <stdio.h>
#include <string.h>

extern "C" int fakeFaceTrackingXXXX(){
	(*((void (*)(void))AFT_FaceFeatureDetect))();
	(*((void (*)(void))AFT_InitialFaceEngine))();
	(*((void (*)(void))AFT_UninitialFaceEngine))();
	(*((void (*)(void))AFT_SetTrackingFrameNumber))();
	(*((void (*)(void))AFT_SetFaceOutPriority))();
	return 0;
}
