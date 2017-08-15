#include "arcsoft_vsign_detection.h"
#include <stdint.h>
#include <stdio.h>
#include <string.h>

extern "C" int fakeVsignsXXXX(){
	(*((void (*)(void))AHR_VSign_Create))();
	(*((void (*)(void))AHR_VSign_Release))();
	(*((void (*)(void))AHR_VSign_Detect))();
	(*((void (*)(void))AHR_VSign_GetVersion))();
	return 0;
}
