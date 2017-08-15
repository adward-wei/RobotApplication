
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <time.h>
#include "dbg_utils.h"

#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>


#define IS_SUPPORT_HANDSIGN
#define IS_SUPPORT_VSIGN

#include "merror.h"
#include "ammem.h"
#include "arcsoft_face_tracking.h"

#ifndef abs
#define abs(x) ( (x) >=0 ? (x) : ((-x)) )
#endif

#define ERR_NULL_LIB_INSTANCE  (-20)
#define ERR_PARAM_INVALID  (-21)
#define ERR_NULL_INTERNAL_POINTER (-22)
#define CONFIG_PEAK_MEMORY (1024*1024*100)
#define MAX_FACE_NUM (10)
#define CAMERA_DEGREE (0)

#ifdef IS_SUPPORT_HANDSIGN
#include "arcsoft_handsign.h"
static MHandle handsignsEngine = NULL;
static AHS_RES ahs_res = {0};
static AHS_FACES local_ahs_Face = {0};
static int handsigns_share_Msg[2] = {0};
static int handsigns_share_HandNum[2] = {0};
static MRECT handsigns_share_rtPalm[2][MAX_FACE_NUM] = {0};
static MPOINT handsigns_share_ptTracking[2][MAX_FACE_NUM] = {0};
#endif
static MRECT local_rcRECT[MAX_FACE_NUM];
static MInt32 local_FaceRoll[MAX_FACE_NUM];

#ifdef IS_SUPPORT_VSIGN
#include "arcsoft_vsign_detection.h"
static MHandle vsignEngine = NULL;
static AHR_VSIGN_HANDS ahr_res = {0};
static AHR_VSIGN_FACES local_ahr_Face = {0};
static int vsign_share_HandNum[2] = {0};
static int vsign_share_rtHand[2][MAX_FACE_NUM] = {0};
#endif

#define ENABLE_MASK_OUTLINE    (1<<0)
#define ENABLE_MASK_HANDSIGN   (1<<1)
#define ENABLE_MASK_BODY       (1<<2)
#define ENABLE_MASK_AGR        (1<<3)
#define ENABLE_MASK_FR        (1<<4)
#define ENABLE_MASK_VSIGN        (1<<5)

#define MSG_NONE 		 	0
#define MSG_SWINGLEFT 	 	1
#define MSG_SWINGRIGHT   	2
#define MSG_SWINGUP 		3
#define MSG_WAVE 		    4
#define MSG_PALM 		    5
#define MSG_MUTE            6
#define MSG_CLICKDOWN       7
#define MSG_CLICKUP         8
#define AHS_PALMMOUSE 0x00003810



static int g_RecordMode = 0;
static int g_EnableMode = 0;
static unsigned char g_buffer[65536];

static unsigned char *g_FaceDataBuf = NULL;
static unsigned char *g_FaceWorkBuf = NULL;
static MHandle memHandle = NULL;

static AFT_ENGINE faceTrackEngine;
static AFT_FACERES ft_faceRes = {0};

static int ft_share_orient[2] = {0};
static int ft_share_facenum[2] = {0};
static MRECT ft_share_rcFace[2][MAX_FACE_NUM] ={0};

static void detectHandSigns(unsigned char *indata,int format, int width, int height,int bufferIndex,int indexofface){
#ifdef IS_SUPPORT_HANDSIGN
do{
	if(handsignsEngine == NULL){
		MRESULT hr = AHS_Create(memHandle, &(handsignsEngine)) ;
		if(hr != AHS_ERR_NOERROR){
			MYLOGE("AHS_Create failed! res=%ld", hr);
			break;
		}
		AHS_SetHandLeftRight(handsignsEngine, AHS_HAND_LEFT);
	}

	MInt32 lRoll = AHS_ROLL_NONE;
	if(ft_share_facenum[bufferIndex] > 0){

		memcpy(&local_rcRECT[0],&ft_share_rcFace[bufferIndex][indexofface],sizeof(MRECT));

		int orient = ft_share_orient[bufferIndex];
		if(orient == AFT_FOC_0){
			local_FaceRoll[0] = AHS_ROLL_0;
		}else if(orient == AFT_FOC_90){
			local_FaceRoll[0] = AHS_ROLL_90;
		}else if(orient == AFT_FOC_270){
			//TODO Fix the BUG:failed in 270
			local_FaceRoll[0] = AHS_ROLL_270;
		}else if(orient == AFT_FOC_180){
			local_FaceRoll[0] = AHS_ROLL_180;
		}else{
			local_FaceRoll[0] = AHS_ROLL_NONE;
		}

		lRoll = local_FaceRoll[0];

		local_ahs_Face.prtFaces = &(local_rcRECT[0]);
		local_ahs_Face.pFaceRolls = &(local_FaceRoll[0]);
		local_ahs_Face.lFaceNumber = 1;

		MRESULT hr = AHS_SetActiveFaceRect(handsignsEngine, &local_ahs_Face);
		if(hr != AHS_ERR_NOERROR){
			MYLOGE("AHS_SetActiveFaceRect failed!res=%ld", hr);
			break ;
		}
	}

	ASVLOFFSCREEN frame = {0};
	if(format == ASVL_PAF_NV21){
		frame.i32Width = width;
		frame.i32Height = height;
		frame.pi32Pitch[0] = frame.i32Width;
		frame.pi32Pitch[1] = frame.i32Width;
		frame.u32PixelArrayFormat = ASVL_PAF_NV21;
		frame.ppu8Plane[0] = indata;
		frame.ppu8Plane[1] = frame.ppu8Plane[0] + (frame.pi32Pitch[0] * frame.i32Height);
	}else if(format == ASVL_PAF_YUYV){
		frame.i32Width = width;
		frame.i32Height = height;
		frame.pi32Pitch[0] = frame.i32Width*2;
		frame.u32PixelArrayFormat = ASVL_PAF_YUYV;
		frame.ppu8Plane[0] = indata;
	}else {
		frame.i32Width = width;
		frame.i32Height = height;
		frame.pi32Pitch[0] = frame.i32Width;
		frame.pi32Pitch[1] = frame.i32Width/2;
		frame.pi32Pitch[2] = frame.i32Width/2;
		frame.u32PixelArrayFormat = ASVL_PAF_I422H;
		frame.ppu8Plane[0] = indata;
		frame.ppu8Plane[1] = frame.ppu8Plane[0] + (frame.pi32Pitch[0] * frame.i32Height);
		frame.ppu8Plane[2] = frame.ppu8Plane[1] + (frame.pi32Pitch[1] * frame.i32Height);
	}

	{
		MRESULT hr = AHS_Detect(handsignsEngine, &frame, lRoll, AHS_PALMMOUSE, &ahs_res);
		if(hr != AHS_ERR_NOERROR) {
			MYLOGE("AHS_Detect failed! res=%ld", hr);
			break ;
		}
	}

	handsigns_share_HandNum[bufferIndex] = ahs_res.lHandNum;
	if(handsigns_share_HandNum[bufferIndex] > 0){
		memcpy(&handsigns_share_rtPalm[bufferIndex][0],&ahs_res.prtPalm[0],handsigns_share_HandNum[bufferIndex]*sizeof(MRECT));
		memcpy(&handsigns_share_ptTracking[bufferIndex][0],&ahs_res.pptTracking[0],handsigns_share_HandNum[bufferIndex]*sizeof(MPOINT));
	}

	//Samples for deal with the hand signs.
	if (ahs_res.lHandNum <= 0) {
		//MYLOGE("No Sign detected!");
	} else if (ahs_res.lHandNum == 1) {
		if (ahs_res.peStatus[0] == AHS_STATUS_NONE) {
			//MYLOGE("No Sign detected!");
			break;
		}

		if (ahs_res.peStatus[0] & AHS_STATUS_CLICKDOWN) {
			//MYLOGE("Finger Click Down Detected!");
		}

		if (ahs_res.peStatus[0] & AHS_STATUS_CLICKUP) {
			//MYLOGE("Finger Click Up Detected!");
		}

		if (ahs_res.peStatus[0] & AHS_STATUS_SWING) {
			//MYLOGE("Hand Swing Detected!");

			if (ahs_res.peStatus[0] & AHS_ORIENT_LEFT) {
				MYLOGE("Swing Left");
				handsigns_share_Msg[bufferIndex] = MSG_SWINGLEFT;
				break;
			}

			if (ahs_res.peStatus[0] & AHS_ORIENT_RIGHT) {
				MYLOGE("Swing Right");
				handsigns_share_Msg[bufferIndex] = MSG_SWINGRIGHT;
				break;
			}
		}

		if(ahs_res.peStatus[0]&AHS_STATUS_WAVE){
			//MYLOGE("Wave Sign Detected!");
			handsigns_share_Msg[bufferIndex] = MSG_WAVE;
			break;
		}

		if (ahs_res.peStatus[0] & AHS_STATUS_PALM) {
			//MYLOGE("PALM Detected!");
			handsigns_share_Msg[bufferIndex] = MSG_PALM;
			break;
		}


		if (ahs_res.peStatus[0] & AHS_HAND_LEFT) {
			//MYLOGE("Left hand actived!");
		}

		if (ahs_res.peStatus[0] & AHS_HAND_RIGHT) {
			//MYLOGE("Right hand actived!");
		}

	} else {
		//MYLOGE("Wrong Sign detected!");
	}
}while(false);
#endif
}
static void detectVSign(unsigned char *indata,int format, int width, int height,int bufferIndex,int indexofface){
#ifdef IS_SUPPORT_VSIGN
do{
	if(vsignEngine == NULL){
		MRESULT hr = AHR_VSign_Create(memHandle, &(vsignEngine)) ;
		if(hr != AHR_VSIGN_ERR_NOERROR){
			MYLOGE("AHR_VSign_Create failed! res=%ld", hr);
			break;
		}
	}

	MInt32 lRoll = 0;
	if(ft_share_facenum[bufferIndex] > 0){

		memcpy(&local_rcRECT[0],&ft_share_rcFace[bufferIndex][indexofface],sizeof(MRECT));

		int orient = ft_share_orient[bufferIndex];
		if(orient == AFT_FOC_0){
			local_FaceRoll[0] = 0;
		}else if(orient == AFT_FOC_90){
			local_FaceRoll[0] = 90;
		}else if(orient == AFT_FOC_270){
			local_FaceRoll[0] = 270;
		}else if(orient == AFT_FOC_180){
			local_FaceRoll[0] = 180;
		}else{
			local_FaceRoll[0] = 0;
		}

		lRoll = local_FaceRoll[0];
		local_ahr_Face.prtFaces = &(local_rcRECT[0]);
		local_ahr_Face.lFaceNumber = 1;

		ASVLOFFSCREEN frame = {0};
		if(format == ASVL_PAF_NV21){
			frame.i32Width = width;
			frame.i32Height = height;
			frame.pi32Pitch[0] = frame.i32Width;
			frame.pi32Pitch[1] = frame.i32Width;
			frame.u32PixelArrayFormat = ASVL_PAF_NV21;
			frame.ppu8Plane[0] = indata;
			frame.ppu8Plane[1] = frame.ppu8Plane[0] + (frame.pi32Pitch[0] * frame.i32Height);
		}else if(format == ASVL_PAF_YUYV){
			frame.i32Width = width;
			frame.i32Height = height;
			frame.pi32Pitch[0] = frame.i32Width*2;
			frame.u32PixelArrayFormat = ASVL_PAF_YUYV;
			frame.ppu8Plane[0] = indata;
		}else {
			frame.i32Width = width;
			frame.i32Height = height;
			frame.pi32Pitch[0] = frame.i32Width;
			frame.pi32Pitch[1] = frame.i32Width/2;
			frame.pi32Pitch[2] = frame.i32Width/2;
			frame.u32PixelArrayFormat = ASVL_PAF_I422H;
			frame.ppu8Plane[0] = indata;
			frame.ppu8Plane[1] = frame.ppu8Plane[0] + (frame.pi32Pitch[0] * frame.i32Height);
			frame.ppu8Plane[2] = frame.ppu8Plane[1] + (frame.pi32Pitch[1] * frame.i32Height);
		}

		{
			MRESULT hr = AHR_VSign_Detect(vsignEngine, &frame, lRoll, AHR_VSIGN_HAND_BOTH, &local_ahr_Face,&ahr_res);
			if(hr != AHR_VSIGN_ERR_NOERROR) {
				MYLOGE("AHR_VSign_Detect failed! res=%ld", hr);
				break ;
			}
		}

		vsign_share_HandNum[bufferIndex] = ahr_res.lHandNumber;
		if(vsign_share_HandNum[bufferIndex] > 0){
			memcpy(&vsign_share_rtHand[bufferIndex][0],&ahr_res.prtHands[0],vsign_share_HandNum[bufferIndex]*sizeof(MRECT));
		}

	}
	//MYLOGE("AHR_VSign E");
}while(false);
#endif
}

static int detectFaceCamera(unsigned char *indata,int format,int width, int height,int bufferIndex){
	bool bOutline = (g_EnableMode&ENABLE_MASK_OUTLINE)>0?true:false;
	bool bHandsign = (g_EnableMode&ENABLE_MASK_HANDSIGN)>0?true:false;
	bool bBody = (g_EnableMode&ENABLE_MASK_BODY)>0?true:false;
	bool bAGR = (g_EnableMode&ENABLE_MASK_AGR)>0?true:false;
	bool bFR = (g_EnableMode&ENABLE_MASK_FR)>0?true:false;
	bool bVSign = (g_EnableMode&ENABLE_MASK_VSIGN)>0?true:false;

	ft_share_facenum[bufferIndex] = 0;
#ifdef IS_SUPPORT_HANDSIGN
	handsigns_share_Msg[bufferIndex] = MSG_NONE;
	handsigns_share_HandNum[bufferIndex] = 0;
#endif

#ifdef IS_SUPPORT_VSIGN
	vsign_share_HandNum[bufferIndex] = 0;
#endif

	if(g_FaceWorkBuf == NULL){
		g_FaceWorkBuf= (unsigned char*)malloc(CONFIG_PEAK_MEMORY);
		memHandle = MMemMgrCreate(g_FaceWorkBuf, CONFIG_PEAK_MEMORY);
	}

	int colorbuffersize = 0;
	ASVLOFFSCREEN ImgDataInput = {0};
	if(format == ASVL_PAF_NV21){
		ImgDataInput.i32Height	= height;
		ImgDataInput.i32Width	= width;
		ImgDataInput.u32PixelArrayFormat = ASVL_PAF_NV21;
		ImgDataInput.pi32Pitch[0] = ImgDataInput.i32Width;
		ImgDataInput.pi32Pitch[1] = ImgDataInput.i32Width;
		ImgDataInput.ppu8Plane[0] = indata;
		ImgDataInput.ppu8Plane[1] = ImgDataInput.ppu8Plane[0]+(ImgDataInput.pi32Pitch[0]*ImgDataInput.i32Height);
		colorbuffersize = width*height*3/2;
	}else if(format == ASVL_PAF_YUYV){
		ImgDataInput.i32Height	= height;
		ImgDataInput.i32Width	= width;
		ImgDataInput.u32PixelArrayFormat = ASVL_PAF_YUYV;
		ImgDataInput.pi32Pitch[0] = ImgDataInput.i32Width*2;
		ImgDataInput.ppu8Plane[0] = indata;
		colorbuffersize = width*height*2;
	}else if(format == ASVL_PAF_I422H){
		ImgDataInput.i32Height	= height;
		ImgDataInput.i32Width	= width;
		ImgDataInput.u32PixelArrayFormat = ASVL_PAF_I422H;
		ImgDataInput.pi32Pitch[0] = ImgDataInput.i32Width;
		ImgDataInput.pi32Pitch[1] = ImgDataInput.i32Width/2;
		ImgDataInput.pi32Pitch[2] = ImgDataInput.i32Width/2;
		ImgDataInput.ppu8Plane[0] = indata;
		ImgDataInput.ppu8Plane[1] = ImgDataInput.ppu8Plane[0]+(ImgDataInput.pi32Pitch[0]*ImgDataInput.i32Height);
		ImgDataInput.ppu8Plane[2] = ImgDataInput.ppu8Plane[1]+(ImgDataInput.pi32Pitch[1]*ImgDataInput.i32Height);
		colorbuffersize = width*height*2;
	}else{
		MYASSERT_MSG(0,"format no support");
	}

	int indexofSelect = 0;
	if(faceTrackEngine == NULL){

		MRESULT hr = AFT_InitialFaceEngine(memHandle ,&ImgDataInput,&ft_faceRes, &faceTrackEngine, AFT_OPF_0_HIGHER_EXT ,16,MAX_FACE_NUM);
		if((hr != MOK) || (faceTrackEngine == MNull)){
			if (bHandsign){
				detectHandSigns(indata,format,width,height,bufferIndex,indexofSelect);
			}
			if (bVSign){
				detectVSign(indata,format,width,height,bufferIndex,indexofSelect);
			}
			return 0;
		}
		AFT_SetFaceOutPriority(faceTrackEngine,AFT_FOP_CENTER);
	}

	{
		AFT_ORIENTCODE	iOrient;
	#if (CAMERA_DEGREE == 0)
		iOrient = AFT_FOC_0;
	#elif (CAMERA_DEGREE == 90)
		iOrient = AFT_FOC_90;
	#else
		#error Camera degree no support
	#endif
		MRESULT hr = AFT_FaceFeatureDetect(memHandle,faceTrackEngine, &ImgDataInput,&ft_faceRes,iOrient, MNull,MNull,MNull,MNull);
		if(hr != MOK){
			if (bHandsign){
				detectHandSigns(indata,format,width,height,bufferIndex,indexofSelect);
			}
			if (bVSign){
				detectVSign(indata,format,width,height,bufferIndex,indexofSelect);
			}
			return 0;
		}
	}

	if((ft_faceRes.nFace < 1)||(ft_faceRes.lfaceOrient == 0)){
		if (bHandsign){
			detectHandSigns(indata,format,width,height,bufferIndex,indexofSelect);
		}
		if (bVSign){
			detectVSign(indata,format,width,height,bufferIndex,indexofSelect);
		}
		return 0;
	}

	ft_share_orient[bufferIndex] = ft_faceRes.lfaceOrient;
	ft_share_facenum[bufferIndex] = ft_faceRes.nFace;
	memcpy(&ft_share_rcFace[bufferIndex][0],&ft_faceRes.rcFace[0],ft_faceRes.nFace*sizeof(MRECT));

	if (bHandsign){
		detectHandSigns(indata,format,width,height,bufferIndex,indexofSelect);
	}
	if (bVSign){
		detectVSign(indata,format,width,height,bufferIndex,indexofSelect);
	}
	return 0;
}



extern "C" JNIEXPORT JNICALL jint process(
JNIEnv* env, jobject obj,jbyteArray indata, jint format,jint width, jint height,jint index){

	if(indata == NULL){
		return ERR_PARAM_INVALID;
	}

	if((format != ASVL_PAF_NV21)&&(format != ASVL_PAF_YUYV)&&(format != ASVL_PAF_I422H)){
		MYASSERT_MSG(0,"format no support");
		return ERR_PARAM_INVALID;
	}

	if(format == ASVL_PAF_NV21){
		if(env->GetArrayLength(indata)!= (width*height*3/2)){
			return ERR_PARAM_INVALID;
		}

		if(g_FaceDataBuf == NULL){
			g_FaceDataBuf = (unsigned char*)malloc(width*height*3/2);
		}

	    env->GetByteArrayRegion(indata, 0, width*height*3/2, (signed char*)g_FaceDataBuf);
	}else{
		if(env->GetArrayLength(indata)!= (width*height*2)){
			return ERR_PARAM_INVALID;
		}

		if(g_FaceDataBuf == NULL){
			g_FaceDataBuf = (unsigned char*)malloc(width*height*2);
		}
	    env->GetByteArrayRegion(indata, 0, width*height*2, (signed char*)g_FaceDataBuf);
	}

	detectFaceCamera(g_FaceDataBuf,format,width,height,index);

	return 0;
}

extern "C" JNIEXPORT JNICALL int getInfo(
JNIEnv* env, jobject obj,jint index,jbyteArray infodata){
	int offset = 0;

	if(ft_share_facenum[index] != 0){
		int len = 4+4+4+4+(ft_share_facenum[index]*sizeof(MRECT));
		*(int*)(&g_buffer[offset]) = len;
		*(int*)(&g_buffer[offset+4]) = 0x00008001;
		*(int*)(&g_buffer[offset+8]) = ft_share_facenum[index];
		*(int*)(&g_buffer[offset+12]) = ft_share_orient[index];

		memcpy(&g_buffer[offset+16],&ft_share_rcFace[index][0],(ft_share_facenum[index]*sizeof(MRECT)));
		offset += len;
	}

#ifdef IS_SUPPORT_HANDSIGN
	if(handsigns_share_HandNum[index] != 0){
		int len = 4+4+4+4+(handsigns_share_HandNum[index]*(sizeof(MRECT)+sizeof(MPOINT)));
		*(int*)(&g_buffer[offset]) = len;
		*(int*)(&g_buffer[offset+4]) = 0x00008003;
		*(int*)(&g_buffer[offset+8]) = handsigns_share_HandNum[index];
		*(int*)(&g_buffer[offset+12]) = handsigns_share_Msg[index];

		memcpy(&g_buffer[offset+16],&handsigns_share_rtPalm[index][0],handsigns_share_HandNum[index]*sizeof(MRECT));
		memcpy(&g_buffer[offset+16+handsigns_share_HandNum[index]*sizeof(MRECT)],&handsigns_share_ptTracking[index][0],handsigns_share_HandNum[index]*sizeof(MPOINT));
		offset += len;
	}
#endif

#ifdef IS_SUPPORT_VSIGN
	if(vsign_share_HandNum[index] != 0){
		int len = 4+4+4+(vsign_share_HandNum[index]*(sizeof(MRECT)));
		*(int*)(&g_buffer[offset]) = len;
		*(int*)(&g_buffer[offset+4]) = 0x0000800A;
		*(int*)(&g_buffer[offset+8]) = vsign_share_HandNum[index];

		memcpy(&g_buffer[offset+12],&vsign_share_rtHand[index][0],vsign_share_HandNum[index]*sizeof(MRECT));
		offset += len;
	}
#endif
	*(int*)(&g_buffer[offset]) = 0;
	env->SetByteArrayRegion(infodata, 0, offset+4, (signed char*)&g_buffer[0]);
	return 0;
}

static int setModelEnable(JNIEnv* env, jobject obj,int mode){
	g_EnableMode = mode;
	return 0;
}

static JNINativeMethod gMethods[] = {
	{"getInfo", "(I[B)I",(void*)getInfo},
    {"process", "([BIIII)I",(void*)process},

    {"setModelEnable", "(I)I",(void*)setModelEnable},

};

const char* JNI_NATIVE_INTERFACE_CLASS = "com/arcsoft/dam/DamEngine";
extern "C"  jint JNI_OnLoad(JavaVM* vm, void* reserved){
    JNIEnv *env = NULL;
    if (vm->GetEnv((void**)&env, JNI_VERSION_1_4)){
        return JNI_ERR;
    }

    jclass cls = env->FindClass(JNI_NATIVE_INTERFACE_CLASS);
    if (cls == NULL){
        return JNI_ERR;
    }

    jint nRes = env->RegisterNatives(cls, gMethods, sizeof(gMethods)/sizeof(gMethods[0]));
    if (nRes < 0){
        return JNI_ERR;
    }

	//sem_init(&(g_sem_notifyCalcThreadToCalc), 0, 0);
	//sem_init(&(g_sem_notifyMainCalcfinishCalc), 0, 1);

	//g_bRunThread = true;
	//pthread_create(&g_calcThread, NULL, calcThread_Func, NULL);


	return JNI_VERSION_1_4;
}

extern "C" void JNI_OnUnload(JavaVM* vm, void* reserved){

   JNIEnv *env = NULL;
   if (vm->GetEnv((void**)&env, JNI_VERSION_1_4)){
       return;
   }

   jclass cls = env->FindClass(JNI_NATIVE_INTERFACE_CLASS);
   if (cls == NULL){
       return;
   }
   jint nRes = env->UnregisterNatives(cls);

   	//g_bRunThread = false;
	//sem_post(&(g_sem_notifyCalcThreadToCalc));
	////pthread_join(g_calcThread,NULL);
	//sem_destroy(&(g_sem_notifyCalcThreadToCalc));
	//sem_destroy(&(g_sem_notifyMainCalcfinishCalc));
	
//	if(g_threadColorBuf != NULL){
//		MMemFree(memHandle,g_threadColorBuf);
//		g_threadColorBuf = NULL;
//	}
#ifdef IS_SUPPORT_VSIGN
    if(vsignEngine != NULL){
	    AHR_VSign_Release(&vsignEngine);
	    vsignEngine = NULL;
    }
#endif
#ifdef IS_SUPPORT_HANDSIGN
    if(handsignsEngine != NULL){
	    AHS_Release(handsignsEngine);
	    handsignsEngine = NULL;
    }
#endif

	if(faceTrackEngine != NULL){
		AFT_UninitialFaceEngine(memHandle, &faceTrackEngine ,&ft_faceRes);
		faceTrackEngine = NULL;
	}

	if(g_FaceWorkBuf != NULL){
		free(g_FaceWorkBuf);
		g_FaceWorkBuf = NULL;
	}

}

