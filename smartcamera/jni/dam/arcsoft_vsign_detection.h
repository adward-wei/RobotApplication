#ifndef ARCSOFT_VSIGN_DETECTION_1_4_H
#define ARCSOFT_VSIGN_DETECTION_1_4_H

#include "asvloffscreen.h"

#ifdef ENABLE_DLL_EXPORT
#define AHR_VSIGN_API_EXPORT    __declspec(dllexport)
#else
#define AHR_VSIGN_API_EXPORT    
#endif

// Error definition
#define AHR_VSIGN_ERR_NOERROR				0			//No error
#define AHR_VSIGN_ERR_UNKNOWN				-1			//Unknown error
#define AHR_VSIGN_ERR_INVALID_PARAM			-2			//Invalid parameter, Usually caused by NULL pointer.
#define AHR_VSIGN_ERR_USER_ABORT			-3			//Abort the detection processing
#define AHR_VSIGN_ERR_IMAGE_FORMAT			-101		//Invalid color format
#define AHR_VSIGN_ERR_ALLOC_MEM_FAIL		-201		//Memory alloc failed
#define AHR_VSIGN_ERR_EXPIRED				-301		//The library is expired

// Left/Right Hand
typedef MLong AHR_VSIGN_LEFTRIGHT;
//#define AHR_VSIGN_MASK_LEFTRIGHT			0x00000003
#define AHR_VSIGN_HAND_LEFT				0x00000001
#define AHR_VSIGN_HAND_RIGHT			0x00000002
#define AHR_VSIGN_HAND_BOTH				(AHR_VSIGN_HAND_LEFT|AHR_VSIGN_HAND_RIGHT)

/* Defines for result event*/
typedef struct{
	MRECT					*prtHands;
	MInt32					lHandNumber;
}AHR_VSIGN_HANDS;

/* Defines for face input*/
typedef struct{
	MRECT				*prtFaces;				// The bounding box array of faces
	MInt32				lFaceNumber;			// Number of faces are detected	
}AHR_VSIGN_FACES;

// version info    
typedef struct
{
	MInt32				lCodebase;			// Code base version number 
	MInt32				lMajor;				// Major version number 
	MInt32				lMinor;				// Minor version number
	MInt32				lBuild;				// Build version number, increasable only
	const MChar*		strVersion;			// Version in string form
	const MChar*		strBuildDate;		// Latest build Date
	const MChar*		strCopyRight;		// Copyright 
}AHR_VSIGN_VERSION;

#ifdef __cplusplus
extern "C" {
#endif

	/************************************************************************/
	/* The handle create/release function.                                  */
	/************************************************************************/
	AHR_VSIGN_API_EXPORT MRESULT AHR_VSign_Create(
		MHandle                 hMemMgr,		// [in] The handle for memory manager
		MHandle                 *phDetector 	// [out]The pointer to the handle for hand sign detect
	);

	AHR_VSIGN_API_EXPORT MVoid   AHR_VSign_Release(
		MHandle					*phDetector		// [in]	The pointer to the handle for hand sign detect
	);

	/************************************************************************/
	/* The main function for hand sign detect                               */
	/************************************************************************/
	AHR_VSIGN_API_EXPORT MRESULT AHR_VSign_Detect(
		MHandle					hDetector,		// [in]	The handle for hand sign detect
		const ASVLOFFSCREEN*	pImgSrc,		// [in]	The source image
		MLong					lRoll,			// [In] Roll of camera. Support for 0, 90, 180, 270 currently
		AHR_VSIGN_LEFTRIGHT		eSignLeftRight, // [in]	The hand left/right
		const AHR_VSIGN_FACES*	psFaces,		// [in] The array for face	
		AHR_VSIGN_HANDS			*psSigns		// [out]The result of hand detection
	);

	/************************************************************************/
	/* The function used to get version information of the library.         */
	/************************************************************************/
	AHR_VSIGN_API_EXPORT const AHR_VSIGN_VERSION* AHR_VSign_GetVersion(void);


#ifdef __cplusplus
}
#endif

#endif //ARCSOFT_VSIGN_DETECTION_1_4_H
