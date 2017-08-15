#ifndef _ARCSOFT_HANDSIGN_1_2_H_
#define _ARCSOFT_HANDSIGN_1_2_H_

#include "asvloffscreen.h"

#ifdef ENABLE_DLL_EXPORT
#define AHS_API_EXPORT    __declspec(dllexport)
#else
#define AHS_API_EXPORT    
#endif

// Error definition
#define AHS_ERR_NOERROR                 0           // No error
#define AHS_ERR_UNKNOWN                 -1          // Unknown error
#define AHS_ERR_INVALID_PARAM           -2          // Invalid parameter, Usually caused by NULL pointer.
#define AHS_ERR_USER_ABORT              -3          // Abort the detection processing
#define AHS_ERR_IMAGE_FORMAT            -101        // Invalid color format
#define AHS_ERR_ALLOC_MEM_FAIL          -201        // Memory alloc failed
#define AHS_ERR_NO_STATUS               -1402       // No Status

// Status definition
typedef MInt32 AHS_STATUS;
#define AHS_STATUS_NONE                 0x00000000
#define AHS_STATUS_FINGER               0x00000001
#define AHS_STATUS_VSIGN                0x00000002
#define AHS_STATUS_THUMBUP              0x00000004
#define AHS_STATUS_PALM                 0x00000010
#define AHS_STATUS_FIST                 0x00000020
#define AHS_STATUS_MUTE                 0x00000040
#define AHS_STATUS_SWING                0x00010000        //| with AHS_HAND_ORIENTATION for different orientation
#define AHS_STATUS_WAVE                 0x00080000
#define AHS_STATUS_CLICKDOWN            0x00200000
#define AHS_STATUS_CLICKUP              0x00400000
#define AHS_STATUS_MOVE_DEPTH           0x20000000
#define AHS_STATUS_MOVE_CIRCLE          0x80000000
#define AHS_STATUS_MOVE_HORZ            0x01000000

typedef MInt32 AHS_ENUM_LEFTRIGHT;
#define AHS_HAND_LEFT                   0x00001000
#define AHS_HAND_RIGHT                  0x00002000

typedef MInt32 AHS_ENUM_ORIENTATION;
#define AHS_ORIENT_LEFT                 0x00000100
#define AHS_ORIENT_RIGHT                0x00000200
#define AHS_ORIENT_UP                   0x00000400
#define AHS_ORIENT_DOWN                 0x00000800

// Model definition
typedef MInt32 AHS_MODEL;
#define AHS_PALMMOUSE_SWING                 0x00093810      //PALMMOUSE+SWING+WAVE

/* Defines for hand sign roll*/
typedef MInt32 AHS_ENUM_ROLL;        
#define AHS_ROLL_NONE       0
#define AHS_ROLL_0          0x00000001
#define AHS_ROLL_90		    0x00000040
#define AHS_ROLL_180	    0x00001000
#define AHS_ROLL_270	    0x00040000	// -90
/* Defines for face rectangle array*/
typedef struct{
    MRECT *prtFaces; // The bounding box array of faces
    AHS_ENUM_ROLL*pFaceRolls; // The roll array for faces
    MInt32 lFaceNumber; // Number of faces are detected    
}AHS_FACES;

// Defines for result event
typedef struct  {
    AHS_STATUS *peStatus;
    MRECT *prtPalm;
    MPOINT *pptTracking;
    MInt32 lHandNum;
}AHS_RES;

#ifdef __cplusplus
extern "C" {
#endif

// Create
AHS_API_EXPORT MRESULT AHS_Create(
    MHandle                 hMemMgr, 
    MHandle                 *phController        //[Out]
);

//Release
AHS_API_EXPORT MVoid AHS_Release(
    MHandle                 hController
);

//Detect sign
AHS_API_EXPORT MRESULT AHS_Detect(
    MHandle                 hController, 
    const ASVLOFFSCREEN*    psFrame,            //[In]  Image data
    MInt32                  lRoll,              //[In]  Camera roll
    AHS_MODEL               eModel,             //[In]  Current model
    AHS_RES*                psRes               //[Out]
);

//////////////////////////////////////////////////////////////////////////
// Get the virtual screen position. It is valid with  PALM or FINGER
AHS_API_EXPORT MRESULT AHS_GetScreenPos(
    MHandle                 hController,        
    MInt32                  lScreenW,            
    MInt32                  lScreenH,            
    MInt32                  lSensitivity,        //[In] Mouse sensitivity. Between [1, 100]. Default as 50
    MInt32                  lHandNo,             // 0 or 1
    MPOINT*                 pptScreen            //[Out] Point in virtual screen    
);

// Get the virtual offset screen. It is valid with  PALM or FINGER
AHS_API_EXPORT MRESULT AHS_GetPosOffSet(
    MHandle                 hController,        
    MInt32                  lScreenW,            
    MInt32                  lScreenH,            
    MInt32                  lSensitivity,       //[In] Mouse sensitivity. Between [1, 100]. Default as 50
    MInt32                  lHandNo,            //0 or 1
    MPOINT*                 pptScreen           //[Out] Offset in virtual screen    
);

//Get the active human face that control sign.
AHS_API_EXPORT MRESULT AHS_GetActiveFace(
    MHandle                 hController,        
    MRECT*                  prtFace
);

// Get zoom/roll/offset for two hands. It is valid with AHS_STATUS_MOVE_TWOHAND
AHS_API_EXPORT MRESULT AHS_GetTwoHandsVal(
    MHandle                 hController,        
    MFloat*                 pfZoom,              //[Out] [0, infinite)    1 for no zoom    
    MInt32*                 plRoll               //[Out] [-180, 180] 0 for no roll. 
);

// Set the hot hand left/right
AHS_API_EXPORT MRESULT AHS_SetHandLeftRight(
    MHandle                hController,
    AHS_ENUM_LEFTRIGHT     eHandLeftRight
);

// Set the detected faces
AHS_API_EXPORT MRESULT AHS_SetActiveFaceRect(
    MHandle                 hController, 
    const AHS_FACES*        psFaceRes
);

// Get version info    
typedef struct
{
    MInt32                lCodebase;            // Code base version number 
    MInt32                lMajor;               // Major version number 
    MInt32                lMinor;               // Minor version number
    MInt32                lBuild;               // Build version number, increasable only
    const MChar*        strVersion;             // Version in string form
    const MChar*        strBuildDate;           // Latest build Date
    const MChar*        strCopyRight;           // Copyright 
}AHS_VERSION;
AHS_API_EXPORT const AHS_VERSION* AHS_GetVersion();

#ifdef __cplusplus
}
#endif

#endif //_ARCSOFT_HANDSIGN_1_2_H_
