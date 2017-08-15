/*******************************************************************************
Copyright ArcSoft, All right reserved.

This file is ArcSoft's property. It contains ArcSoft's trade secret, proprietary 
and confidential information. 

The information and code contained in this file is only for authorized ArcSoft 
employees to design, create, modify, or review.

DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER 
AUTHORIZATION.

If you are not an intended recipient of this file, you must not copy, 
distribute, modify, or take any action in reliance on it. 

If you have received this file in error, please immediately notify ArcSoft and 
permanently delete the original and any copy of any file and any printout 
thereof.
*******************************************************************************/

#ifndef _ARC_FACETRACKING_H_
#define _ARC_FACETRACKING_H_

#include "amcomdef.h"
#include "asvloffscreen.h"

#ifdef __cplusplus
extern "C" {
#endif
	

typedef MVoid**				AFT_ENGINE;	
typedef MInt32				AFT_ORIENTPRI;
typedef MInt32				AFT_ORIENTCODE;


//orientation priority
enum OrientPriority{
	AFT_OPF_EQUAL			= 0x1,		// equal priority: 0,90,270,0,90,270...
	AFT_OPF_0_HIGHER		= 0x2,		// 0 higher priority: 0,90,0,270,0,90...
	AFT_OPF_0_ONLY			= 0x3,		// 0 only:0,0...
	AFT_OPF_90_ONLY			= 0x4,		// 90 only:90,90...
	AFT_OPF_270_ONLY		= 0x5,		// 270 only:270,270...		
	AFT_OPF_180_ONLY		= 0x6,		// 180 only:180,180...
	AFT_OPF_EQUAL_EXT		= 0x7,		// equal priotirty supporting 180 degree: 0,90,270,180,0...
	AFT_OPF_0_HIGHER_EXT	= 0x8,		// 0 higher priority supporting 180 degree: 0,90,0,270,0,180,0...
	AFT_OPF_USER_DEFINED	= 0x9		// user defined priority	
};


//face orientation code
enum OrientCode{
	AFT_FOC_0			= 0x1,		// 0 degree
	AFT_FOC_90			= 0x2,		// 90 degree
	AFT_FOC_270			= 0x3,		// 270 degree
	AFT_FOC_180			= 0x4		// 180 degree
};

/*Face output priority */
enum FaceOutPriority{
	AFT_FOP_SIMILARITY	= 0x0,		// similarity priority
	AFT_FOP_CENTER		= 0x1,		// center priority
	AFT_FOP_SIZE			= 0x2		// size priority
};


typedef struct{
	MRECT 			*rcFace;				// The bounding box of face
	MInt32			nFace;					// number of faces detected
	MInt32           lfaceOrient;            // the face angle 1:0angle 2:90angle, 3:270 angle
} AFT_FACERES, *LPAFT_FACERES;

typedef struct
{
	MInt32 lCodebase;			// Codebase version number 
	MInt32 lMajor;				// major version number 
	MInt32 lMinor;				// minor version number
	MInt32 lBuild;				// Build version number, increasable only
	const MChar Version[40];		// version in string form
	const MChar BuildDate[20];	// latest build Date
	const MChar CopyRight[50];	// copyright 
} ArcSoft_Face_Tracking_Version;

/************************************************************************
 * This function is implemented by the caller and will be called 
 * when faces are detected.
 ************************************************************************/
typedef MRESULT (*AFT_FNPROGRESS) (
	MVoid       *pParam1,					// detection result data.
	MVoid		*pParam2					// Caller-defined data
);

/************************************************************************
 * The function used to detect and track face automatically.  
 *
 * Comment:
 *	The incoming image is scanned for faces.The result pFaceRes will be 
 *  passed to this interface in the next calling round.
 ************************************************************************/
MRESULT AFT_FaceFeatureDetect(					// return MOK if success, otherwise fail
	MHandle				hMemMgr,					// [in]  User defined memory manager
	AFT_ENGINE		hEngine,					// [in]	 Face Tracking engine
	LPASVLOFFSCREEN		pImgData,					// [in]  The original image data
	LPAFT_FACERES	pFaceRes,					// [out] The tracking result
	AFT_ORIENTCODE	iOrient,					// [in]  The orientation of face to detect
	AFT_FNPROGRESS	fnGetPartialFaceRes,		// [in]  The callback function to get detection result earlier.
	MVoid				*pGetPartialFaceResParam,	// [in]  Caller-specific data that will be passed into the callback function fnGetPartialFaceRes.
	AFT_FNPROGRESS	fnTimeOut,					// [in]  The callback function to terminate detection interface.
	MVoid				*pTimeOutParam				// [in]  Caller-specific data that will be passed into the callback fnTimeOut.
);

/************************************************************************
 * The function used to Initialize the face tracking engine. 
 ************************************************************************/
MRESULT AFT_InitialFaceEngine(	 
	MHandle				hMemMgr,		// [in]	User defined memory manager
	LPASVLOFFSCREEN		pImgData,		// [in] Pointing to the AFT_IMAGEINFO structure containing input image information
	LPAFT_FACERES	pFaceRes,		// [out] The tracking result
	AFT_ENGINE		*pEngine,		// [out] pointing to the AFT_ENGINE structure containing the tracking engine.
	AFT_ORIENTPRI	iOrientsFlag,	// [in] Defining the priority of face orientation.
	MInt32				nScale,			// [in]	An integer defining the minimal face to detect relative to the maximum of image width and height.
	MInt32				nMaxFaceNum		// [in]	An integer defining the number of max faces to track
);

/************************************************************************
 * The function used to Uninitialize the detection module. 
 ************************************************************************/
MRESULT AFT_UninitialFaceEngine(
	MHandle	hMemMgr,					// [in]	User defined memory manager
	AFT_ENGINE *pEngine,				// [in] pointing to the AFT_ENGINE structure containing the tracking engine.
	LPAFT_FACERES pFaceRes			// [in] The tracking result
);

/************************************************************************
 * The function used to set face priority. 
 ************************************************************************/
MRESULT AFT_SetFaceOutPriority(
	 AFT_ENGINE	hEngine,			// [in]	 Face Tracking engine
	 MInt32			lPrioity);			// [in]  Face priority

/************************************************************************
 * The function used to set the number of frames to be skipped between two successive whole frame detections . 
 ************************************************************************/
MRESULT AFT_SetTrackingFrameNumber(
     AFT_ENGINE	hEngine,			// [in ] Face Tracking engine
	 MInt32 nFrames);					// [in]  number of frames to be tracked


/************************************************************************
 * The function used to enable and disable the functions
 ************************************************************************/
MRESULT AFT_EnableFaceTracking(
	AFT_ENGINE	hEngine,           // [in ] Face Tracking engine
	MBool nFlag);                      // [in ] the parameter to control the facetracking function

/************************************************************************
 * The function used to tune face rectangls. 
 ************************************************************************/

MRESULT AFT_TuneRectangleStability(
	AFT_ENGINE			hEngine,	//[in] Face Tracking engine
	MInt32					nLevel);	//[in] parameter to tune face rectangles stability.

/************************************************************************
 * The function used to get version information of face tracking library. 
 ************************************************************************/
const ArcSoft_Face_Tracking_Version * ArcSoft_Face_Tracking_GetVersion();



#ifdef __cplusplus
}
#endif

#endif //_ARC_FACETRACKING_H_
