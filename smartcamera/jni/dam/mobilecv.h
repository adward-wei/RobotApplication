/*----------------------------------------------------------------------------------------------
*
* This file is ArcSoft's property. It contains ArcSoft's trade secret, proprietary and 		
* confidential information. 
* 
* The information and code contained in this file is only for authorized ArcSoft employees 
* to design, create, modify, or review.
* 
* DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER AUTHORIZATION.
* 
* If you are not an intended recipient of this file, you must not copy, distribute, modify, 
* or take any action in reliance on it. 
* 
* If you have received this file in error, please immediately notify ArcSoft and 
* permanently delete the original and any copy of any file and any printout thereof.
*
*-------------------------------------------------------------------------------------------------*/

#ifndef _MOBILECV_H_
#define _MOBILECV_H_

#include "amcomdef.h"
#include "asvloffscreen.h"

#ifdef FORDLLEXPORT
#define MCV_API __declspec(dllexport)
#else
#define MCV_API
#endif


#ifdef __cplusplus
extern "C" {
#endif

typedef MVoid (*MPVoidProc)(MVoid* lpPara); 
#define MWAIT_INFINITE		(~0)


/*************************
 * separate yuyv to planner y and planner uv
 *************************/
MInt32 mcvYUYVToOrgData(MUInt8 *pYUYVData, MLong lImgWidth, MLong lImgHeight, MUInt8 *pGreyData, MUInt8 *pCbCrData);

				  
/*************************
 * sqrt(a) for 64bits size,fast version.Accuracy:1% more or less
 *************************/
MInt32 mcvFastSqrts64(MInt64 a);

/*************************
 * sqrt(x) for 32bits size.Accuracy:1% more or less
 *************************/
MInt32 mcvFastSqrts32(MInt32 x); 

/**************************************
Fast sqrt approx.
note:fX should > 0;
**************************************/
MFloat mcvSqrtf32(MFloat fX);

/**************************************
Fast vector sqrt approx.
* pSrc	: Input singel precision numbers.
* pDst	: Output singel precision numbers.
* lLength	: The length of the numbers.
* note:
*       every element of pSrc should be > 0;
**************************************/
MInt32 mcvSqrtVectorf32(MFloat* pSrc, MFloat* pDst, MInt32 lLength);


/**************************************
Fast reciprocal sqrt approx.  return 1/sqrt(fX)
note:fX should > 0;
**************************************/
MFloat mcvInvSqrtf32(MFloat fX);

/**************************************
Fast vector reciprocal sqrt approx. 
* pSrc	: Input singel precision numbers.
* pDst	: Output singel precision numbers.
* lLength	: The length of the numbers.
* note:
*       every element of pSrc should be > 0;
**************************************/
MInt32 mcvInvSqrtVectorf32(MFloat* pSrc, MFloat* pDst, MInt32 lLength);

/**************************************
Fast vector reciprocal sqrt approx. 
* fDividend	: the dividend.
* fDivisor	:  the divisor.
* note:
*       fDivisor should be > 0;
**************************************/
MFloat mcvDivf32(MFloat fDividend, MFloat fDivisor);

/**************************************
Fast vector reciprocal sqrt approx. 
* pDividend	: the dividend vector.
* pQuotient	:  the quotient vector.
* fDivisor	:  the divisor.
* lLength	:  the length.
* note:
*       fDivisor should be > 0;
**************************************/
MInt32 mcvVectorDivf32(MFloat* pDividend,  MFloat* pQuotient, MFloat fDivisor, MInt32 lLength);

/**************************************
Performs per-element bitwise-OR operation on two 8-bit single channel images.
* pSrc1  : First Input image.
* pSrc2  : Second Input image, must has the same size as pSrc1.
* pDst  : Output image, must has the same size as pSrc1.
* iStrideSrc1 : stride of pSrc1.
* iStrideSrc2 : stride of pSrc2.
* iStrideDst  : stride of pDst.
**************************************/
MInt32 mcvBitwiseOru8(MByte* pSrc1, MByte* pSrc2, MByte* pDst, 
            MInt32 iwidth,MInt32 iheight,
            MInt32 iStrideSrc1,MInt32 iStrideSrc2,MInt32 iStrideDst);

/*************************
 * Dot product of two 8-bit vectors.
 *************************/
MLong mcvDotProducts8(MUInt8* a,MUInt8*  b,MUInt32 absize );
 
 /*************************
 * Counts vectors "1" bits .
 *************************/
MLong mcvBitCountu8 (MUInt8*  src,MLong  srcLength);

/***********2-norm of two vectors************/
MDouble mcvVectorDiffNorm2u32(MUInt32 *vec1,MUInt32 *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2s32(MInt32 *vec1,MInt32 *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2f32(MFloat*vec1,MFloat *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2Fasts16(MInt16 *vec1,MInt16 *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2Fastu16(MUInt16 *vec1,MUInt16 *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2Fasts8(MInt8 *vec1,MInt8 *vec2,MInt32 len);

MDouble mcvVectorDiffNorm2Fastu8(MUInt8 *vec1,MUInt8 *vec2,MInt32 len);
/***********End of 2-norm of two vectors************/


/***********calc of two matrixs************/
//calc M1 + M2
MInt32 mcvMatrixAddMatrix_f32(MFloat *matrixOut,MFloat *matrix1,MFloat *matrix2,MInt32 row,MInt32 column);

//calc M1 - M2
MInt32 mcvMatrixSubMatrix_f32(MFloat *matrixOut,MFloat *matrix1,MFloat *matrix2,MInt32 row,MInt32 column);

//calc  scaler x (M1)
MInt32 mcvMatrixMulScalar_f32(MFloat *matrixOut,MFloat *matrixIn,MFloat lamda,MInt32 row,MInt32 column);

//calc M1 * M2
MInt32 mcvMatrixMulMatrixRowMajor_f32(MFloat *matrixOut,MFloat *matrix1,MFloat *matrix2,MInt32 row1,MInt32 column1,MInt32 column2);
MInt32 mcvMatrixMulMatrixRowMajor_s32(MInt32 *matrixOut,MInt32 *matrix1,MInt32 *matrix2,MInt32 row1,MInt32 column1,MInt32 column2);
MInt32 mcvMatrixMulMatrixRowMajor_s64(MInt64 *matrixOut,MInt16 *matrix1,MInt32 *matrix2,MInt32 row1,MInt32 column1,MInt32 column2);
MInt32 mcvMatrixMulMatrixColMajor_f32(MFloat *matrixOut,MFloat *matrix1,MFloat *matrix2,MInt32 row1,MInt32 column1,MInt32 column2);
MInt32 mcvMatrixMulMatrixColMajor_s32(MInt32 *matrixOut,MInt32 *matrix1,MInt32 *matrix2,MInt32 row1,MInt32 column1,MInt32 column2);

//calc M1 * M2 + M3
MInt32 mcvMatrixMulAddRowMajor_f32(MFloat *matrixOut,MFloat *matrix1,MFloat *matrix2,MFloat *matrix3,MInt32 row1,MInt32 column1,MInt32 column2);

/***********End of calc of two matrixs************/

/*************************
* src  : Input grey image.
* dst  : Output grey image.
* srcWidth : src Image width.
          NOTE :  must be equal or greater than 4.
* srcHeight: src Image Height.
          NOTE :  must be equal or greater than 4.
 *************************/
MInt32 mcvScaleDownBy2u8(MUInt8*  src,MLong  srcWidth,MLong  srcHeight,MUInt8* dst );


/**************************************
  Function Description:mcvResizeYUYVToYUYVBilinear	
  	Resize one frame in YUYV format for both input and output.
  	Use bilinear interpolation for Y component and neighbor interpolation for CbCr. 
  In:
  	plTmpWidthBuf:     index value for H direction.
  			  should be (lDstWidth*3*sizeof(MInt32)+lDstWidth*2*sizeof(MUInt16)).
  	pYUYVData: YUYV input
  	lSrcLineStep: line stride of input buffer. normally lSrcWidth*2 for YUYV format.
  	lDstWidth
  	buflength: size of buffer plTmpWidthBuf, in byte unit.
  Out:
  	pDstYUYVData: output buffer.
  Note:
  	lDstWidth should be '2n',or else  we will lost last 1 column pixels.
**************************************/
MInt32 mcvResizeYUYVToYUYVBilinear(MInt32 *plTmpWidthBuf,MInt32 buflength,
						MUInt8 *pYUYVData, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcLineStep, MUInt8 *pDstYUYVData,
						MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstLineStep);

/**************************************
  Function Description:mcvResizeYUYVToLPI422HBilinear
	Resize one frame. Input frame is ASVL_PAF_YUYV format and output frame is ASVL_PAF_LPI422H format.
	Use bilinear interpolation for Y component and neighbor interpolation for CbCr.
  In:
  	plTmpBuf:     index value for H direction.
  			  should be (lDstWidth*5*sizeof(MUInt16)).
	buflength: size of buffer plTmpBuf, in byte unit.
  	srcImage: YUYV input
  	
  Out:
  	dstImage:  LPI422H output.
  Note:
  	width of dstImage should be '2n',or else  we will lost last 1 column pixels.
**************************************/
MInt32 mcvResizeYUYVToLPI422HBilinear(MUInt16 *plTmpBuf,MInt32 buflength,
                        LPASVLOFFSCREEN srcImage,LPASVLOFFSCREEN dstImage);


/**************************************
  Function Description:
  	bilinear interpolation for one line in YUYV format for input and output with Y format.
  In:
  	pSrcTop:
  	pTmp:     index value for H direction.
  			  should be (lDstWidth*2*sizeof(MInt32)+lDstWidth*2*sizeof(MUInt16)).
  	pXTop1  : The value of the first row in H direction.
  	pXTop2
  	lSrcLineStep
  	lDstWidth
  	Ry1
  Out:
  	pGrey:buffer pointer for a line.
  	pCbCr: null
  Note:
	1. lDstWidth will be rounded to '2n'.
	2. pGrey will hold all data,pCbCr is not used.

**************************************/
MInt32 mcvResizeYUYVToYBilinear(MInt32 *plTmpWidthBuf,MInt32 buflength,
						MUInt8 *pYUYVData, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcLineStep, MUInt8 *pDstYData, 
						MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstLineStep);



/**************************************
  *	Resize an image and split  to 3 plane. input :YUYV output: y... u...v...separated. 
  *  Y: bilinear interpolation. C: neighbor interpolation.
  *  plTmpBuf:at least  MInt16*(lDstWidth * 4 + lDstWidth/2) 
  buflength: size of buffer plTmpWidthBuf, in byte unit.
  **************************************/
MInt32 mcvResizeYUYVToI422HBilinearY(MUInt16 *plTmpBuf,MInt32 buflength,
						MUInt8 *pSrcYUYV, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcLineStep, MUInt8 *pDstY, MUInt8 *pDstCb, MUInt8 *pDstCr,
						MInt32 lDstWidth, MInt32 lDstHeight, 
						MInt32 lDstStrideY,MInt32 lDstStrideCb,MInt32 lDstStrideCr);
						
/**************************************
  *	Resize an image and split  to 3 plane. input :YUYV output: y... u...v...separated. 
  *  Y: bilinear interpolation. C: neighbor interpolation.
  *  plTmpBuf:at least  MInt16*(lDstWidth * 4 + lDstWidth/2) 
  buflength: size of buffer plTmpWidthBuf, in byte unit.
  **************************************/
MInt32 mcvResizeYUYVToI420BilinearY(MUInt16 *plTmpBuf,MInt32 buflength,
						MUInt8 *pSrcYUYV, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcLineStep, MUInt8 *pDstY, MUInt8 *pDstCb, MUInt8 *pDstCr,
						MInt32 lDstWidth, MInt32 lDstHeight, 
						MInt32 lDstStrideY,MInt32 lDstStrideCb,MInt32 lDstStrideCr);


/**************************************
  *	Resize an image . no matter it is y or u or v.
  *  bilinear interpolation. C: neighbor interpolation.
  *  plTmpBuf:at least MInt16*(lDstWidth * 4) 
  *  buflength: 
  	size of buffer plTmpWidthBuf, in byte unit.
  	used to check if plTmpBuf is valid!
  **************************************/
MInt32 mcvResizeSingleComponentBilinear(MUInt16 *plTmpBuf,MInt32 buflength,
                        MUInt8 *pSrc, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcStride, MUInt8 *pDst,MInt32 lDstWidth, 
						MInt32 lDstHeight, MInt32 lDstStride);

/**************************************
  Function Description:
  	Resize the an image of ASVL_PAF_NV21 format and convert to ASVL_PAF_LPI422H format.
  	bilinear for Y,nearest for UV
  In:
  	plTmpBuf  :     tmp buffer allocated outside.
  			  must not be less than sizeof(MInt16)*((lDstWidth << 2)+(lDstWidth>>1))
  	buflength  : The length of plTmpBuf in Bytes.
  	srcImage  :  input image descriptor
  Out:
  	dstImage: output image descriptor.
  Note:
	1. Width and Height of both input and output image should be even and greater than 2.
	1. plTmpBuf should not be null.

**************************************/
MInt32 mcvResizeNV21ToLPI422HBilinear(MUInt16 *plTmpBuf,MInt32 buflength,
						LPASVLOFFSCREEN srcImage,LPASVLOFFSCREEN dstImage);

/**************************************
  Function Description:
    Resize the an image of ASVL_PAF_NV21 format and convert to ASVL_PAF_LPI420 format.
    bilinear for Y,nearest for UV
  In:
    plTmpBuf  :     tmp buffer allocated outside.
          must not be less than sizeof(MInt16)*((lDstWidth << 2))
    buflength  : The length of plTmpBuf in Bytes.
    pSrcY  :  input src Y data.
    pSrcUV  :  input src UV data.
  Out:
    pDstY : Output dest Y data.
    pDstU: Output dest U data.
    pDstV: Output dest V data.
  Note:
  1. Width and Height of both input and output image should be even and greater than 2.
  2. plTmpBuf should not be null.

**************************************/
MUInt32 mcvResizeNV21ToI420Bilinear(MUInt16 *plTmpBuf, MUInt32 buflength,
     MUInt8 *pSrcY, MUInt32 lSrcStrideY,  MUInt8* pSrcUV, MUInt32 lSrcStrideUV, 
     MUInt32 lSrcWidth, MUInt32 lSrcHeight, 
     MUInt8 *pDstY, MUInt32 lDstStrideY, MUInt8 *pDstU, MUInt32 lDstStrideU, MUInt8 *pDstV, MUInt32 lDstStrideV,
     MUInt32 lDstWidth, MUInt32 lDstHeight);

/**************************************
  Function Description:
    Resize the an image of ASVL_PAF_LPI422H format and convert to ASVL_PAF_LPI420 format.
    bilinear for Y,nearest for UV
  In:
    plTmpBuf  :     tmp buffer allocated outside.
          must not be less than sizeof(MInt16)*((lDstWidth << 2))
    buflength  : The length of plTmpBuf in Bytes.
    pSrcY  :  input src Y data.
    pSrcUV  :  input src UV data.
  Out:
    pDstY : Output dest Y data.
    pDstU: Output dest U data.
    pDstV: Output dest V data.
  Note:
  1. Width and Height of both input and output image should be even and greater than 2.
  2. plTmpBuf should not be null.

**************************************/
MUInt32 mcvResizeLPI422HToI420Bilinear(MUInt16 *plTmpBuf, MUInt32 buflength,
    MUInt8 *pSrcY, MUInt32 lSrcStrideY, MUInt8* pSrcUV, MUInt32 lSrcStrideUV, 
    MUInt32 lSrcWidth, MUInt32 lSrcHeight, 
    MUInt8 *pDstY, MUInt32 lDstStrideY, MUInt8 *pDstU, MUInt32 lDstStrideU, MUInt8 *pDstV, MUInt32 lDstStrideV,
    MUInt32 lDstWidth, MUInt32 lDstHeight);

/**************************************
  Function Description:
  	bilinear interpolation for ASVL_PAF_NV21 format.
  	bilinear for Y,nearest for VU
  In:
  	plTmpBuf:     tmp buffer allocated outside.
  			  must not be less than sizeof(MInt16)*((lDstWidth << 2)+(lDstWidth>>1))
  	buflength  : The length of plTmpBuf in Bytes.
  Out:
  	pDst: output buffer pointer.
  Note:
	1. lDstWidth and lDstHeight should be even and greater than 2.
	1. plTmpBuf,pSrc,pDst should not be null.

**************************************/
MInt32 mcvResizeNV21Bilinear(MUInt16 *plTmpBuf,MInt32 buflength,
						MUInt8 *pSrc, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcStride, MUInt8 *pDst, 
						MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstStride);

/**************************************
  Function Description:
  	bilinear interpolation for ASVL_PAF_NV12 format.
  	bilinear for Y,nearest for UV
  In:
  	plTmpBuf:    tmp buffer allocated outside.
  			  must not be less than sizeof(MInt16)*((lDstWidth << 2)+(lDstWidth>>1))
  	buflength  : The length of plTmpBuf in Bytes.
  Out:
  	pDst: output buffer pointer.
  Note:
	1. lDstWidth and lDstHeight should be even and greater than 2.
	1. plTmpBuf,pSrc,pDst should not be null.

**************************************/
MInt32 mcvResizeNV12Bilinear(MUInt16 *plTmpBuf,MInt32 buflength,
						MUInt8 *pSrc, MInt32 lSrcWidth, MInt32 lSrcHeight, 
						MInt32 lSrcStride, MUInt8 *pDst, 
						MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstStride);

/**************************************
  Function Description:
  	bilinear interpolation for ASVL_PAF_I420 format.
  	bilinear for Y,nearest for U and V
  In:
  	plTmpBuf:    tmp buffer allocated outside.
  			  must not be less than sizeof(MInt16)*((lDstWidth << 2)+(lDstWidth>>1))
  	buflength  : The length of plTmpBuf in Bytes.
  Out:
  	pDst: output buffer pointer.
  Note:
	1. lDstWidth and lDstHeight should be even and greater than 2.
	1. plTmpBuf,pSrc,pDst should not be null.

**************************************/
MInt32 mcvResizeI420Bilinear(MUInt16 *plTmpBuf,MInt32 buflength,
                        MUInt8 *pSrc, MInt32 lSrcWidth, MInt32 lSrcHeight, 
                        MInt32 lSrcStride, MUInt8 *pDst, 
                        MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstStride);

/**************************************
* Resize rectangle region of YUYV image to I422H Y U V channel down samply by 2.
* pSrc			 :  Source image with YUYV format.
* lSrcStep		 : 	Line step of source image.
* lSrcWidth		 : 	Width of source image.
* lSrcHeight  	 :	Height of source image.
* pDstY		 	 :	Output dest Y data.
* lDstYStep		 :	Line step of dest Y.
* pDstU		 	 :	Output dest U data.
* lDstUStep		 :	Line step of dest U.
	Note : lDstYStep, lDstUStep should be euqual.
* pDstV		  	 :	Output dest V data.
* lDstVStep		 :	Line step of dest V.
	Note : lDstVStep should be double size of lDstYStep and lDstUStep.
* roi			 :  The process rectangle region.
**************************************/
MInt32 mcvResizeYUYVtoI422HDownSampleby2WithRect(MByte* pSrc, MLong lSrcStep, MLong lSrcWidth, MLong lSrcHeight, 
	MByte* pDstY, MLong lDstYStep, MByte* pDstU, MLong lDstUStep, MByte* pDstV, MLong lDstVStep, MRECT *roi);


/**************************************
* Resize YUYV image to I422H Y U V channel down sample by 2.
* pSrc			 :  Source image with YUYV format.
* lSrcStep		 : 	Line step of source image.
* lSrcWidth		 : 	Width of source image.
* lSrcHeight  	 :	Height of source image.
* pDstY		 	 :	Output dest Y data.
* lDstYStep		 :	Line step of dest Y.
* pDstU		 	 :	Output dest U data.
* lDstUStep		 :	Line step of dest U.
	Note : lDstYStep, lDstUStep should be euqual.
* pDstV		  	 :	Output dest V data.
* lDstVStep		 :	Line step of dest V.
	Note : lDstVStep should be double size of lDstYStep and lDstUStep.
**************************************/
MInt32 mcvResizeYUYVtoI422HDownSampleby2(MByte* pSrc, MLong lSrcStep, MLong lSrcWidth, MLong lSrcHeight, 
	MByte* pDstY, MLong lDstYStep, MByte* pDstU, MLong lDstUStep, MByte* pDstV, MLong lDstVStep);


/**************************************
  Function Description:
  	bilinear interpolation and color transformation for ASVL_PAF_NV21 to YUYV .
  	bilinear for Y,nearest for VU
  In:
  	pSrcNV21:
  				the source img
  	plTmpBuf:   
  				tmp buffer allocated outside.
  			  	must not be less than sizeof(MInt16)*((lDstWidth << 2)+(lDstWidth>>1))
  	buflength: 
  				The length of plTmpBuf in Bytes.
  Out:
  	pDstYUYV:
  				the dst img
  Note:
	1. pSrcNV21,plTmpBuf,pDstYUYV should not be null.

**************************************/
MInt32 mcvResizeNV21toYUYVBilinear(LPASVLOFFSCREEN pSrcNV21, 
								   LPASVLOFFSCREEN pDstYUYV, 
								   MUInt16 *plTmpBuf, MInt32 buflength);

/**************************************
  Function Description:
  	bilinear interpolation for ASVL_PAF_RGB24_R8G8B8 .
  In:
  	plTmpBuf:   
  				tmp buffer allocated outside.
  			  	must not be less than sizeof(MUInt16)*(lDstWidth << 3)
  	buflength:      The length of plTmpBuf in Bytes.
  	pSrc       :       the source RGB888 image
  	lSrcWidth       :       the source RGB888 image width in [pixel] unit.
  	lSrcHeight       :       the source RGB888 image height in [pixel] unit
  	lSrcStride       :       the source RGB888 image line stride in [byte] unit
  	lDstWidth       :       the dst RGB888 image width in [pixel] unit.
  	lDstHeight       :       the dst RGB888 image height in [pixel] unit
  	lDstStride       :       the dst RGB888 image line stride in [byte] unit
  Out:
  	pDstYUYV:
  				the dst img
  Note:
	1. pSrc,plTmpBuf,pDst should not be null.
	2. lSrcStride >= 3*lSrcWidth,lDstStride >= 3*lDstWidth
	3. the size of plTmpBuf should not less than (lDstWidth<<3)*sizeof(MUInt16)

**************************************/
MInt32 mcvResizeRGB888Bilinear(MUInt16 *plTmpBuf,MInt32 buflength,
                        MUInt8 *pSrc, MInt32 lSrcWidth, MInt32 lSrcHeight, 
                        MInt32 lSrcStride, MUInt8 *pDst, 
                        MInt32 lDstWidth, MInt32 lDstHeight, MInt32 lDstStride);

/**************************************
  *	Motion detect in three image：current frame，prev frame，prevprev frame.
  **************************************/
MInt32 mcvDetectMotion(ASVLOFFSCREEN* prevprev_colors[3], 
                      ASVLOFFSCREEN* prev_colors[3], ASVLOFFSCREEN* curr_colors[3], 
					  ASVLOFFSCREEN* motionimage);	

/**********************************************
Function:    mcvIntegral.
Description:
	Calc the Integral value of an image.
Input:
	pSrc
		src image,will not be changed inside this function.
	lSumPitch: in byte unit.
		next row will be :sum + lSumPitch.
Output:
	sum  
Note: 
	1.  lSrcWidth  < lSumPitch
	2.  lSrcWidth  <= lSrcPitch
	3. The 1'st row and the 1'st column of sum will always be 0
**********************************************/
MInt32 mcvIntegral(MUInt8* pSrc, MUInt32 lSrcWidth, MUInt32 lSrcHeight,
					MUInt32 lSrcPitch,MUInt32* sum,MUInt32 lSumPitch);

/**********************************************
Function:    mcvIntegral.
Description:
	Calc the Integral value of an image.
Input:
	pSrc
		src image,will not be changed inside this function.
	lSumPitch: in byte unit.
		next row will be :sum + lSumPitch.
	roi
		dst rect you want to calculate integral
Output:
	sum  
Note: 
	1.  lSrcWidth  < lSumPitch
	2.  lSrcWidth  <= lSrcPitch
	3. The 1'st row and the 1'st column of sum will always be 0
**********************************************/

MInt32 mcvIntegralWithRect(MByte* src, MLong srcstep, MInt32* sum, MLong sumstep, MLong width, MLong height, MRECT *roi);

 /*************************
 * Calculate the integral image.
 *************************/
MInt32 mcvImgIntegralu8(MUInt8* src,MLong  srcWidth,MLong  srcHeight,MLong *pIntegralImg, MLong  *pIntegralImg2);
 

/**************************************
  Function Description:
  	mcviCalcGradientMagAngle_I422H_FixPoint
  	calc the magnitude and angle of the gradient of an image
  In:
  	pChns[3]:     start address of y��u��v.
  	step[3]   :     line step of y��u��v, in [byte] unit.
  	width      :     width of y,  width/2 for u and v;width > 0
  	nBins      :     the number of zones to classify the angle data,nBins >= 0
  			        for example,6
  	magstep :     line step of magbuffer , in [byte] unit.magstep > 0
  	lDirectionX: left hand of right hand ,the tmplate in x direction will be
  				1  :   -1  0   1
  				-1:     1  0  -1
  Out:
  	magptr:     magnitude of the gradient of the image, width*2 valid,
  				each gradient contributes 2 magnitude data
  	
  	angleptr:     angle of the gradient of the image, width*2 valid,
  				each gradient contributes 2 angle data
**************************************/
MInt32 mcviCalcGradientMagAngle_I422H_FixPoint(MUInt8* pChns[3], MInt32 step[3], MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins, MInt32 lDirectionX);


/**************************************
  Function Description:mcviCalcGradientMagAngle_I422H_left
  	calc the magnitude and angle of the gradient of an image
  	this is just for [left] hand, H gradient tmplate will be   -1  0  1
  In:
  	pChns[3]:     start address of y��u��v.
  	step[3]   :     line step of y��u��v, in [byte] unit.
  	width      :     width of y,  width/2 for u and v
  	nBins      :     the number of zones to classify the angle data,
  			        for example,6
  	magstep :     line step of magbuffer , in [byte] unit.
  Out:
  	magptr:     magnitude of the gradient of the image, width*2 valid,
  				each gradient contributes 2 magnitude data
  	
  	angleptr:     angle of the gradient of the image, width*2 valid,
  				each gradient contributes 2 angle data
  Note:
  	.
**************************************/
MInt32 mcviCalcGradientMagAngle_I422H_left(MUInt8* pChns[3], MInt32 step[3], MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins);


/**************************************
  Function Description:
  	mcviCalcGradientMagAngle_I422H_right
  	calc the magnitude and angle of the gradient of an image
  	this is just for [right] hand,  H gradient tmplate will be   1  0 -1
  In:
  	pChns[3]:     start address of y,u,v.
  	step[3]   :     line step of y,u,v, in [byte] unit.
  	width      :     width of y,  width/2 for u and v
  	nBins      :     the number of zones to classify the angle data,
  			        for example,6
  	magstep :     line step of magbuffer , in [byte] unit.
  Out:
  	magptr:     magnitude of the gradient of the image, width*2 valid,
  				each gradient contributes 2 magnitude data
  	
  	angleptr:     angle of the gradient of the image, width*2 valid,
  				each gradient contributes 2 angle data
  Note:
  	.
**************************************/
MInt32 mcviCalcGradientMagAngle_I422H_right(MUInt8* pChns[3], MInt32 step[3], MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins);



/*********************************************
* Calc the absolute difference of two image.
**********************************************/
MInt32 mcvGetMotionCue(MUInt8 *pCurGreyImage,MUInt8 *pPreGreyImage,
				   MInt32 lImgWidth, MInt32 lImgHeight, MInt32 lLineStep, 
				   MUInt8 *pFrameDiffImage);


/**************************************
  Function Description:
  	mcvICmCalc_Bx_By
	Original version: lack of description
  In:
	pSrcI: buffer size--->nHeight*nPitch*sizeof(MUInt8)
	pSrcJ: buffer size--->nHeight*nPitch*sizeof(MUInt8)
	nPitch
	nWidth
	nHeight
	pIxIy:buffer size--->nHeight*nIPitch*sizeof(MInt32)
	nIPitch
	nDx:range--->[0,256]
	nDy:range--->[0,256]
  Out:
	pBx: Pointer of output
	pBy: Pointer of output
  Note:
	1.pSrcI,pSrcJ,pBx,pBy should not be 0;
	2.nPitch >= nWidth+1;nPitch is in [BYTE] unit.
	3.nIPitch >= (nWidth+1)*2;nIPitch is in [MInt32] unit.
	4.nWidth >= 2;
	5.nHeight >=0;
**************************************/
MInt32 mcvICmCalc_Bx_By(MUInt8*pSrcI,MUInt8 *pSrcJ,MInt32 nPitch,
								MInt32 nWidth,MInt32 nHeight,MInt32 *pIxIy,
								MInt32 nIPitch,MInt32 nDx,MInt32 nDy,
								MInt32 *pBx,MInt32 *pBy);

/**************************************
  Function Description:
  	mcvIcmCalc_Bx_By_Gxx_Gxy_Gyy,just like mcvICmCalc_Bx_By
	Original version: lack of description
  In:
	pSrcI: buffer size--->nHeight*nPitch*sizeof(MUInt8)
	pSrcJ: buffer size--->nHeight*nPitch*sizeof(MUInt8)
	nPitch
	nWidth
	nHeight
	pIxIy:buffer size--->nHeight*nIPitch*sizeof(MInt32)
	nIPitch
	nDx:range--->[0,256]
	nDy:range--->[0,256]
  Out:
	pBx,pBy,pGxx,pGxy,pGyy: Pointer of output
  Note:
	1.pSrcI,pSrcJ,pBx,pBy should not be 0;
	2.nPitch >= nWidth+1;nPitch is in [BYTE] unit.
	3.nIPitch >= (nWidth+1)*2;nIPitch is in [MInt32] unit.
	4.nWidth >= 2;
	5.nHeight >=0;
**************************************/
MInt32 mcvIcmCalc_Bx_By_Gxx_Gxy_Gyy(MUInt8 *pSrcI,MUInt8 *pSrcJ,MInt32 nPitch,MInt32 nWidth,MInt32 nHeight,
		MInt32 *pIxIy,MInt32 nIPitch,MInt32 nDx,MInt32 nDy,MInt32 *pBx,MInt32 *pBy,MInt32 *pGxx,MInt32 *pGxy,MInt32 *pGyy);

/**************************************
  Function Description:
  	mcvCalcGradientMagAngleFix_Gray
  	calc the magnitude and angle of the gradient of an image
  In:
  	pSrc:     start address of y.
  	lSrcStep   :     line step of y, in [byte] unit. lSrcStep >= pSrc
  	width      :     width of y;width > 0
  	nBins      :     the number of zones to classify the angle data, 255=>nBins >= 0
  			        for example,6
  	magstep :     line step of magbuffer , in [byte] unit.magstep > 0
  	lDirectionX: left hand of right hand ,the tmplate in x direction will be
  				1  :   -1  0   1
  				-1:     1  0  -1
  Out:
  	magptr:     magnitude of the gradient of the image, width*2 valid,
  				each gradient contributes 2 magnitude data
  	
  	angleptr:     angle of the gradient of the image, width*2 valid,
  				each gradient contributes 2 angle data
**************************************/
MInt32 mcvCalcGradientMagAngleFix_Gray(MUInt8* pSrc, MInt32 lSrcStep, MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins, MInt32 lDirectionX);

/**************************************
  Function Description:
  	mcvCalcGradientMagAngleFix_Gray_left
  	lDirectionX = 1 for mcvCalcGradientMagAngleFix_Gray.
**************************************/
MInt32 mcvCalcGradientMagAngleFix_Gray_left(MUInt8* pSrc, MInt32 lSrcStep, MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins);

/**************************************
  Function Description:
  	mcvCalcGradientMagAngleFix_Gray_right
  	lDirectionX = -1 for mcvCalcGradientMagAngleFix_Gray.
**************************************/
MInt32 mcvCalcGradientMagAngleFix_Gray_right(MUInt8* pSrc, MInt32 lSrcStep, MInt32* magptr, MInt32 magstep, MUInt8* angleptr, 
MInt32 anglestep, MInt32 width, MInt32 height, MInt32 nBins);

/**************************************
* Extract Y component from YUYV fromat.
* pDst : width*height valid, buffer size is lDstStride*height
* width: should be 2n.
**************************************/
MInt32 mcvExtract_Y_From_YUYV(MUInt8 *pSrc,MUInt8 *pDst,MUInt32 width,MUInt32 height,MUInt32 lSrcStride,MUInt32 lDstStride);


/**************************************
* Convert RGB888 to BGR565 .
* pSrc  : Input RGB888 format image.
* pDst  : Output BGR565 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorRGB888toBGR565u8(MByte *pSrc,MByte *pDst,MInt32 lWidth,MInt32 lHeight);

/**************************************
* Convert BGR888 to RGB565 .
* pSrc  : Input BGR888 format image.
* pDst  : Output RGB565 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorBGR888toRGB565u8(MByte* pSrc, MByte* pDst, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Convert BGR888 to ARGB8888 .
* pSrc  : Input BGR888 format image.
* pDst  : Output ARGB8888 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorBGR888toARGB8888u8(MByte* pSrc, MByte* pDst, MLong lWidth,MLong lHeight);

/**************************************
* Convert RGB888 to YUV420.
* rgbSrc  : Input RGB888 format image.
* yuvDst  : Output YUV420 format image.
* width : Image width.
          NOTE :  must be a multiple of 2.
* height: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorRGB888toYUV420u8(MByte *rgbSrc,MByte *yuvDst,MLong width,MLong height);

/**************************************
* Convert RG888 to YUYV .
* rgbSrc  : Input RG888 format image.
* yuvDst  : Output YUYV format image.
* width : Image width.
          NOTE :  must be a multiple of 2.
* height: Image Height.
**************************************/
MInt32 mcvColorRGB888toYUYVu8(MByte *rgbSrc,MByte *yuvDst,MLong width,MLong height);

/**************************************
* Convert RG888 to NV21 .
* rgbSrc  : Input RG888 format image.
* yuvDst  : Output NV21 format image.
* width : Image width.
          NOTE :  must be a multiple of 2.
* height: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorRGB888toNV21u8(MByte *rgbSrc,MByte *yuvDst,MLong width,MLong height);

/*********** RGB888 to other formats******************/
MInt32 mcvColorRGB888toBGR888u8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toRGB565u8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toYVYUu8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toUYVYu8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toVYUYu8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toYV24u8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toI422Hu8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
MInt32 mcvColorRGB888toNV12u8(MByte* pSrc, MByte* pDst, MLong width,MLong height);
/*********** End of RGB888 to other formats******************/


/**************************************
* Convert NV21 to RGB888 .
* srcImg  : Input NV21 format image.
* dstImg  : Output RGB888 format image.
* NOTE:
*   image width and height must be a multiple of 2 and >=2.
**************************************/
MInt32 mcvColorNV21toRGB888u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);

/**************************************
* Convert NV21 to BGR888 .
* srcImg  : Input NV21 format image.
* dstImg  : Output BGR888 format image.
* NOTE:
*   image width and height must be a multiple of 2 and >=2.
**************************************/
MInt32 mcvColorNV21toBGR888u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);

/**************************************
* Convert NV21 to RGBA8888 .
* srcImg   : Input ASVL_PAF_NV21 format image.
* dstImg  : Output ASVL_PAF_RGB32_R8G8B8 format image.
* alpha    : alpha value to set.
* NOTE : 
*        Image width and height must be a multiple of 2.
**************************************/
MInt32 mcvColorNV21toRGBA8888u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg, MUInt8 alpha);


/**************************************
* Convert NV21 to YUV420 .
* pSrc  : Input NV21 format image.
* pDst  : Output I420(YUV420) format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorNV21toI420u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);

/**************************************
* Convert YUYV to I420 .
* srcImg  : Input YUYV format image.
* dstImg  : Output I420 format image.
* NOTE: 
    image width and height must be a multiple of 2 and >= 2;
**************************************/
MInt32 mcvColorYUYVtoYUV420u8(MByte *yuvSrc,MByte *yuvDst,MInt32 width,MInt32 height);




/**************************************
* Convert YUYV to NV21 .
* pSrc  : Input YUYV format image.
* pDst  : Output NV21 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorYUYVtoNV21u8(MByte *pSrc,MByte *pDst,MInt32 lWidth,MInt32 lHeight);


/**************************************
* Convert YUYV to NV12 .
* pSrc  : Input YUYV format image.
* pDst  : Output NV12 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorYUYVtoNV12u8(MByte *pSrc,MByte *pDst,MInt32 lWidth,MInt32 lHeight);

/**************************************
* Convert YUYV to RGB888 .
* yuvSrc  : Input YUYV format image.
* rgbDst  : Output RGB888 format image.
* width : Image width.
          NOTE :  must be a multiple of 2.
* height: Image Height.
**************************************/
MInt32 mcvColorYUYVtoRGB888u8(MByte *yuvSrc,MByte *rgbDst,MInt32 width,MInt32 height);


/**************************************
* Convert I420 to RGB888 .
* srcImg  : Input I420 format image.
* dstImg  : Output RGB888 format image.
* NOTE:
*   image width\height must be a multiple of 2 and >= 2;
**************************************/
MInt32 mcvColorI420toRGB888u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);

/**************************************
* Convert I420 to YUYV .
* pSrc  : Input I420 format image.
* pDst  : Output YUYV format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorI420toYUYVu8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);

/**************************************
* Convert I420(YUV420) to NV21 .
* pSrc  : Input I420 format image.
* pDst  : Output NV21 format image.
* srcImg : input Image .
* dstImg : output Image .
* NOTE :  
*   width and height must be a multiple of 2 and >2.
**************************************/
MInt32 mcvColorI420toNV21u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);


/**************************************
* Convert NV12 to I420 .
* srcImg  : Input NV12 format image.
* dstImg  : Output I420 format image.
* NOTE:
*   image width and height must be a multiple of 2 and >= 2.
**************************************/
MInt32 mcvColorNV12toI420u8(LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg);


/**************************************
* Convert YV12 to RGB888 .
* pSrc  : Input YV12 format image.
* pDst  : Output RGB888 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorYV12toRGB888u8(MByte* pSrc, MByte *pDst, MLong width, MLong height);

/**************************************
* Convert YV12 to NV21 .
* pSrc  : Input YV12 format image.
* pDst  : Output NV21 format image.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvColorYV12toNV21u8(MByte* pSrc, MByte *pDst,MLong width,MLong height);



/**** Color Convert for big image engine****/
/*return a handle :mcvColorCvtHandle*/
MHandle mcvColorCvtInit_MultiThreads();
MInt32 mcvColorCvtProcess_MultiThreads(MHandle mcvColorCvtHandle,LPASVLOFFSCREEN srcImg,LPASVLOFFSCREEN dstImg, MVoid *extParam);
MInt32 mcvColorCvtUnInit_MultiThreads(MHandle mcvColorCvtHandle);

/**** End of Color Convert for big image engine****/



/**************************************
* Computes the per-element absolute difference between two uint32 image.
* pSrc1  : First input image.
* pSrc2  : Second input image, must has the same size as pSrc1.
* pDst   : Output image, must has the same size as pSrc1.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.	
**************************************/
MInt32 mcvAbsDiffu32(MUInt32* pSrc1, MUInt32* pSrc2, MUInt32* pDst, MInt32 lWidth, MInt32 lHeight, MInt32 lLineBytes);

/**************************************
* Computes the per-element absolute difference between two int32 image.
* pSrc1  : First input image.
* pSrc2  : Second input image, must has the same size as pSrc1.
* pDst   : Output image, must has the same size as pSrc1.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.	
**************************************/
MInt32 mcvAbsDiffs32(MInt32* pSrc1, MInt32* pSrc2, MInt32* pDst,MInt32 lWidth, MInt32 lHeight, MInt32 lLineBytes);

/**************************************
* Computes the per-element absolute difference between two byte image.
* pSrc1  : First input image.
* pSrc2  : Second input image, must has the same size as pSrc1.
* pDst   : Output image, must has the same size as pSrc1.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.	
**************************************/
MInt32 mcvAbsDiffu8(MByte* pSrc1, MByte* pSrc2,MByte* pDst, MInt32 lWidth, MInt32 lHeight, MInt32 lLineBytes);

/**************************************
* Computes the per-element absolute difference between one int32 image and one value.
* pSrc  : Input image.
* lValue: Input Value
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvAbsDiffVs32(MInt32* pSrc, MInt32 lValue, MInt32* pDst, MInt32 lWidth, MInt32 lHeight, MInt32 lLineBytes);

/**************************************
* Computes the per-element absolute difference between one float image and one value.
* pSrc  : Input image.
* lValue: Input Value
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvAbsDiffVf32(MFloat* pSrc, MFloat lValue, MFloat* pDst, MInt32 lWidth, MInt32 lHeight, MInt32 lLineBytes);

/**************************************
* Binarizes a grayscale image based on a threshold value. 
* Sets the pixel to max(255) if it's value is greater than the threshold; else, set the pixel to min(0).
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lThreshold  : Input threshold.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
* lLineBytes: Image length of a row in bytes.
		  NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterThresholdu8(MByte* pSrc, MByte* pDst,MInt32 lThreshold,MInt32 lWidth, MInt32 lHeight,MInt32 lLineBytes);

/**************************************
* Dilate a grayscale image by taking the local maxima of 3x3 neighborhood window.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterDilate3x3u8(MByte* pSrc, MByte* pDst, MByte* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Erode a grayscale image by taking the local minima of 3x3 neighborhood window.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterErode3x3u8(MByte* pSrc, MByte* pDst, MByte* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Blurs an image with 3x3 median filter.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterMedian3x3u8(MByte* pSrc, MByte* pDst, MInt32 lWidth, MInt32 lHeight);


/**************************************
* Blur with 5x5 Gaussian filter.
* Convolution with 5x5 Gaussian kernel: 
* 1 4 6 4 1 
* 4 16 24 16 4 
* 6 24 36 24 6 
* 4 16 24 16 4 
* 1 4 6 4 1
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be a multiple of 2.
* lHeight: Image Height.
          NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterGaussian5x5u8(MByte* pSrc, MByte* pDst, MByte* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Blur a greyScale image with 7x7 Gaussian filter.
* Convolution with 7x7 Gaussian kernel in both direction: 
* 0.0126, 0.0788, 0.2373, 0.3426, 0.2373, 0.0788, 0.0126
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be >=  6.
* lHeight: Image Height.
          NOTE :  must be >= 6.
**************************************/
MInt32 mcvFilterGaussian7x7f32(MFloat* pSrc, MFloat* pDst, MFloat* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Blur a greyScale image with 7x7 Gaussian filter.
* Convolution with 7x7 Gaussian kernel in both direction: 
* 0.0126, 0.0788, 0.2373, 0.3426, 0.2373, 0.0788, 0.0126
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be >=  6.
* lHeight: Image Height.
          NOTE :  must be >= 6.
**************************************/
MInt32 mcvFilterGaussian7x7u16(MUInt16* pSrc, MUInt16* pDst, MUInt16* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Blur a greyScale image with 7x7 Gaussian filter.
* Convolution with 7x7 Gaussian kernel : 
0.000158,   0.000990,   0.002980,    0.004304,    0.002980,    0.000990,   0.000158,
0.000990,   0.006214,   0.018706,    0.027009,    0.018706,    0.006214,   0.000990,
0.002980,   0.018706,   0.056309,    0.081305,    0.056309,    0.018706,   0.002980,
0.004304,   0.027009,   0.081305,    0.117396,    0.081305,    0.027009,   0.004304,
0.002980,   0.018706,   0.056309,    0.081305,    0.056309,    0.018706,   0.002980,
0.000990,   0.006214,   0.018706,    0.027009,    0.018706,    0.006214,   0.000990,
0.000158,   0.000990,   0.002980,    0.004304,    0.002980,    0.000990,   0.000158
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be >=  6.
* lHeight: Image Height.
          NOTE :  must be >= 6.
**************************************/
MInt32 mcvFilterGaussian7x7f32_2D(MFloat* pSrc, MFloat* pDst, MInt32 lWidth, MInt32 lHeight);

/**************************************
* Blur a greyScale image with 7x7 Gaussian filter.
* Convolution with 7x7 Gaussian kernel : 
0.000158,   0.000990,   0.002980,    0.004304,    0.002980,    0.000990,   0.000158,
0.000990,   0.006214,   0.018706,    0.027009,    0.018706,    0.006214,   0.000990,
0.002980,   0.018706,   0.056309,    0.081305,    0.056309,    0.018706,   0.002980,
0.004304,   0.027009,   0.081305,    0.117396,    0.081305,    0.027009,   0.004304,
0.002980,   0.018706,   0.056309,    0.081305,    0.056309,    0.018706,   0.002980,
0.000990,   0.006214,   0.018706,    0.027009,    0.018706,    0.006214,   0.000990,
0.000158,   0.000990,   0.002980,    0.004304,    0.002980,    0.000990,   0.000158
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
          NOTE :  must be >=  6.
* lHeight: Image Height.
          NOTE :  must be >= 6.
**************************************/
MInt32 mcvFilterGaussian7x7u16_2D(MUInt16* pSrc, MUInt16* pDst, MInt32 lWidth, MInt32 lHeight);


/**************************************
This function calculates an image derivative by convolving the image with an appropriate 3x3 kernel.
Border values are ignored.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
**************************************/
MInt32 mcvFilterSobel3x3u8(MByte* pSrc, MByte* pDst, int lWidth, int lHeight);

/**************************************
Smooth a uint8_t image with a 3x3 box filter.
smooth with 3x3 box kernel and normalize: 
([ 1 1 1 ]/3 + [ 1 1 1 ]/3 + [ 1 1 1 ]/3)/3
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* pTmp  : Temporary buffer for use, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be > 2.
* lHeight: Image Height.
		NOTE :  must be > 2.
**************************************/
MInt32 mcvFilterBox3x3u8(MByte* pSrc, MByte* pDst, MByte* pTmp, MInt32 lWidth, MInt32 lHeight);

/**************************************
Smooth a uint8_t image with a 3x3 box filter.
smooth with 3x3 box kernel and normalize: 
[ 1 1 1 
1 1 1 
1 1 1 ]/9
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be > 2.
* lHeight: Image Height.
		NOTE :  must be > 2.
* lStrideSrc: src Image line stride in [BYTE] unit.
		NOTE :  must be >= lWidth.
* lStrideDst: dst Image line stride in [BYTE] unit.
		NOTE :  must be >= lWidth.		
**************************************/
MInt32 mcvFilterBox3x3u8_2D(MByte* pSrc, MByte* pDst,MInt32 lWidth,MInt32 lHeight,MInt32 lStrideSrc,MInt32 lStrideDst);

/*
    * Filter an image with box filter. the size of the box is variable.
    * pSrc : input image;
    * pDst : output image;  (can be the same as pSrc)
    * pRowBuf: tmp buffer for internal usage.
   
    * kernelSize: box filter size(1D),say, 3 if 3x3 filter,5 if 5x5 filter
    * lWidth:image width
    * lHeight:image height
    * lStride:image buffer line stride
    *NOTE:
            1.  pRowBuf:lWidth*sizeof(MUInt32) + kernelSize *lWidth*sizeof(MUInt16) and must be 4bytes alligned
            2. pSrc == pDst supported.
            3. kernelSize must >= 3 and be odd.
            4. lWidth and lHeight >= 4.
*/
MInt32 mcvFilterBoxu8(MByte* pSrc,MByte* pDst,MUInt16* pRowBuf,MUInt32 kernelSize,MUInt32 lWidth,MUInt32 lHeight,MUInt32 lStride);


/**************************************
Smooth a YUYV image with a (1 << lSmoothLenShift * 1 << lSmoothLenShift) box using integral
smooth with (1 << lSmoothLenShift * 1 << lSmoothLenShift) box kernel and normalize: 
* pu8YUYV  : Input image & output smooth image.
* pYSum   : Temporary buffer for use of calculating Y integral.
		NOTE :  buffer size of pYSum should be ceil(lWidth+1) * (lHeight+1).
* pCbSum  : Temporary buffer for use of calculating U integral.
		NOTE :  buffer size of pCbSum should be ceil(lWidth/2+1) * (lHeight+1).
* pCrSum  : Temporary buffer for use of calculating V integral.
		NOTE :  buffer size of pCrSum should be ceil(lWidth/2+1) * (lHeight+1).
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
* lPitch: Image pitch.
* lSmoothLenShift: smooth box length(box length is 1 << lSmoothLenShift).
**************************************/

MInt32	mcvFilterBoxYUYV(MUInt8* pu8YUYV, MUInt32 *pYSum, MUInt32 *pCbSum, MUInt32 *pCrSum, 
								MInt32 lPitch, MInt32 lWidth, MInt32 lHeight, MInt32 lSmoothLenShift);

/**************************************
Smooth the Y component of a YUYV image with a (kernelSize * kernelSize) box filter
* pSrc          : Input & output YUYV image
* pRowBuf    : Temporary row buffer used inside.
* kernelSize  : box filter size.
* lWidth       : Image width.
* lHeight      : Image Height.
* lStride       : input Image buffer line stride (normally >= lWidth*2).
* NOTE :  
*   1. buffer size of pRowBuf  >= (kernelSize ) * lWidth * sizeof(MUInt16) +  lWidth * sizeof(MUInt32).
*   2. lWidth must be a multiple of 2;
*   3. lWidth and lHeight  >= 4.
*   4. kernelSize must be odd; kernelSize >= 3;
**************************************/
MInt32 mcvFilterBoxYUYVInplaceLuma(MByte* pSrc,MUInt16* pRowBuf,MUInt32 kernelSize,MUInt32 lWidth,MUInt32 lHeight,MUInt32 lStride);

/**************************************
* pyrdown using guass 5x5.
* pTmp  : tmp buffer, size: srcStep * 5 * sizeof(MByte).
* pSrc  : Input image.
* srcWidth : src Image width.
		NOTE :  must be >= 5.
* srcHeight: src Image Height.
		NOTE :  must be >= 5.
* srcStep: src Image step.
* pDst  : Output image, valid  area is ((srcWidth + 1)/2) * ((srcHeight + 1)/2),buffer size is (dstStep) * ((srcHeight + 1)/2) * sizeof(MByte).
* dstStep: dst Image step.
* Note: 
*       1/16[1  4  6  4  1] for every direction
**************************************/
MInt32 mcvPyrDownGauss5x5u8c1(MByte *pTmp,MByte *pSrc,MInt32 srcWidth,MInt32 srcHeight,MInt32 srcStep,
                                        MByte *pDst,MInt32 dstStep);


/*
*REQUIREMENT
*the width and height of input and output images should be even
*lSrcImgWidth should be equal to lDstImgHeight, and vise versa
*buflength >= lSrcImgHeight*sizeof(MUInt8**)
* plTmpBuf : its size is buflength
* Note:no neon version.
*/
MVoid mcvYRotate(MLong lDegree,
   MUInt8 *pSrcY, MLong lSrcLineStep, MLong lSrcImgWidth, MLong lSrcImgHeight, 
   MUInt8 *pDstY, MLong lDstLineStep, MLong lDstImgWidth, MLong lDstImgHeight,
   MVoid *plTmpBuf,MInt32 buflength);


/*
*REQUIREMENT
*the width and height of input and output images should be even
*lSrcImgWidth should be equal to lDstImgHeight, and vise versa
*buflength >= lSrcImgHeight*sizeof(MUInt8**)
* Note:no neon version.
*/
MVoid mcvUVRotate(MLong lDegree,
   MUInt8 *pSrcUV, MLong lSrcLineStep, MLong lSrcImgWidth, MLong lSrcImgHeight, 
   MUInt8 *pDstUV, MLong lDstLineStep, MLong lDstImgWidth, MLong lDstImgHeight,
   MVoid *plTmpBuf,MInt32 buflength);



/**************************************
* Sets every element of an int32_t array to a given value.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
* lChannel: Channel of one pixel, such as RGB has 3 channel.
* lValue: The input int32_t value.
**************************************/
MInt32 mcvSetElementss32(MInt32* pSrc, MInt32 lWidth, MInt32 lHeight, MInt32 lChannel, MInt32 lValue);

/**************************************
* Sets every element of an uint8_t array to a given value.
* pSrc  : Input image.
* pDst  : Output image, must has the same size as pSrc.
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
* lChannel: Channel of one pixel, such as RGB has 3 channel.
* lValue: The input uint8_t value.
**************************************/
MInt32 mcvSetElementsu8(MByte* pSrc, MInt32 lWidth, MInt32 lHeight, MInt32 lChannel, MByte lValue);



/**************************************
*Calculate Surf Integral Image for 8 channels.
* pGraySrc  : Input gray image.
* lWidth : Image width.
		NOTE :  must be a multiple of 2.
* lHeight: Image Height.
		NOTE :  must be a multiple of 2.
* surf_int_image: The Integral Image.
* pRect: The ROI rect.
**************************************/
MInt32 mcvCalcSurfIntegralImage_Detect_Surf(MByte *pGraySrc,MInt32 lWidth,MInt32 lHeight,
									MVoid* surf_int_image,MRECT *pRect);



/**************************************
* Computes the Hamming distance between the two supplied arbitrary length vectors.
* pA  : Pointer to vector to compute distance.
* pB  : Pointer to vector to compute distance.
* lLength : Length in bits of each of the vectors.
* pDistance: Pointer to the distance.
**************************************/
MInt32 mcvHammingDistanceu8(MByte* pA, MByte* pB, int lLength, int* pDistance);

/**************mcv parallel engine begin*****************/
/********************************************
Step 1:Call mcvParallelInit  at the very beginning of your program(call it only once):
        MHandle mcvParallelMonitor = mcvParallelInit(MemHandle,1000)
	if(mcvParallelMonitor == 0)
	{
		printf("Failed to start parallel engine!!\n");
	}
	
Step 2:Package your function like this :

	MVoid yourFunction1(MVoid *param)
	{
		if(param != MNull)
		{
			int *p = (int *)param;
			unsigned char *pSrc = (unsigned char *)(*p);p++;
			int width = *p;p++;
			int height = *p;p++;
			....

			resize(pSrc,width,height,...);//this is what you will actually do
		}
	}

Step 3: Execute tasks:
	task 1:
		MVoid *param1 = (MVoid *)malloc(...);
		int *p = (int *)pParam1;
		MInt32 taskId1,taskId2,...;

		*p = (int)pSrc;p++;
		*p = (int)lSrcWidth;p++;
		*p = (int)lSrcHeight;p++;
		....

		taskId1 = mcvAddTask(yourFunction1,param1);
	task 2:
		...
		taskId2 = mcvAddTask(yourFunction2,param2);
		...
	task 3:
		...

Step 4: Wait tasks to finish:
	mcvWaitTask(taskId1);
	mcvWaitTask(taskId2);
	....
	

Step 5: Shutdown parallel engine when your program is coming to an end(call it only once):
	if(mcvParallelUninit(mcvParallelMonitor) < 0)
	{
		printf("Failed to shut down parallel engine!!\n");
	}

**********************************************/

/* Function:
  * 		mcvParallelInit
  * Description:
  * 		Initialize parallel engine;
  * Input:
  *		hContext   handle returned by MMemMgrCreate. Use mpbase lib to allocate memory inside or malloc
  *                         hContext is NULL, use malloc.
  * 		iCoreNumHint		number of  cores(threads) to be used;
  *						range [0,0xffffffff],if iCoreNumHint == 0, use default cpu number.
  *					this value is just a hint,actual used core number is limited by the platform.
  * Return:
  * 		A handle of internal struct,valid only when non-zero. Users should not care about this.
  * Notes:
  * 		1.Call only once.
  *          2. About 2K Bytes will be allocated inside;
  */
MHandle mcvParallelInit(MHandle hContext,MUInt32 iCoreNumHint);


/* Function:
  * 		mcvAddTask
  * Description:
  * 		Add a task ,after this function returns, the task will be exec when there is a free thread;
  * Input:
  *         mcvParallelMonitor handle returned by mcvParallelInit.
  * 		process  function name, should be  void *(f)(void *);
  * 		arg         function parameter;
  * Return:
  * 		return a taskId, which can be used in function "mcvWaitTask";
  *		Failed to add task when taskId is negative.
  */
MInt32 mcvAddTask(MHandle mcvParallelMonitor,MPVoidProc process, MVoid *arg);


/* Function:
  * 		mcvWaitTask
  * Description:
  * 		wait a task to finish;
  * Input:
  *         mcvParallelMonitor handle returned by mcvParallelInit.
  * 		taskId  return value of "mcvAddTask";
  * Return:
  * 		MCV_OK;
  *		MCV_NULL_POINTER.
  * Notes:
  * 		a.This function will block;
  *		b.The times you call this function shall be the same as calling 'mcvAddTask'.
  */
MInt32 mcvWaitTask(MHandle mcvParallelMonitor,MInt32 taskId);


/* Function:
  * 		mcvParallelUninit
  * Description:
  * 		Destroy parallel engine;
  * Input:
  *         mcvParallelMonitor handle returned by mcvParallelInit.
  * Return:
  * 		MCV_OK : ok;  MCV_INVALID_CALL : failed;
  * Note:
  * 		Call only once.
  */
MInt32 mcvParallelUninit(MHandle mcvParallelMonitor);

/**************mcv parallel engine end*****************/


/**************************************
* Detect motion of rectangle region of 3 images with grey data.
* pPrevPrevData  :  Source grey image.
* pPrevData		 : 	Source grey image.
* pCurrData		 : 	Source grey image.
* lPrevPrevStep  :	Line step of PrevPrevData.
* lPrevStep		 :	Line step of PrevData.
* lCurrStep		 :	Line step of CurrData.
* lWidth		 :	Width of three source images.
		Note : Width of three source images should be equal.
* lHeight		 :	Height of three source images.
		Note : Height of three source images should be equal.
* pDstData		 :	Dest image.
* lDstStep		 :	Line step of dest image.
		Note : lPrevPrevStep,lPrevStep,lCurrStep,lDstStep should be equal.
* roi			 : 	The detected rectangle region.
		Note : If roi is null rectangle is lWidth * lHeight.
**************************************/
MInt32 mcvDetectMotion3FrameDiffYWithRect(MByte *pPrevPrevData, MLong lPrevPrevStep, MByte *pPrevData, MLong lPrevStep,
	MByte *pCurrData, MLong lCurrStep, MLong lWidth, MLong lHeight, MByte* pDstData, MLong lDstStep, MRECT *roi);


/**************************************
* Detect motion of 3 images with grey data.
* pPrevPrevData  :  Source grey image.
* pPrevData		 : 	Source grey image.
* pCurrData		 : 	Source grey image.
* lPrevPrevStep  :	Line step of PrevPrevData.
* lPrevStep		 :	Line step of PrevData.
* lCurrStep		 :	Line step of CurrData.
* lWidth		 :	Width of three source images.
		Note : Width of three source images should be equal.
* lHeight		 :	Height of three source images.
		Note : Height of three source images should be equal.
* pDstData		 :	Dest image.
* lDstStep		 :	Line step of dest image.
		Note : lPrevPrevStep,lPrevStep,lCurrStep,lDstStep should be equal.
**************************************/
MInt32 mcvDetectMotion3FrameDiffY(MByte *pPrevPrevData, MLong lPrevPrevStep, MByte *pPrevData, MLong lPrevStep,
	 MByte *pCurrData, MLong lCurrStep, MLong lWidth, MLong lHeight, MByte* pDstData, MLong lDstStep);


/**************************************
* Map histogram of rectangle region after weighting the Y U V channel data.
* pOffScreen : Data structure stores source and dest image.
	Note : Source image format should be ASVL_PAF_I422H.
* table		 : The table stores historam mapping relation.
* roi		 : The rectangle region to calculate histogram.
**************************************/
MInt32 mcvCalcHistBackProject_I422HWithRect(LPASVLOFFSCREEN pOffScreen, MLong* table, MRECT *roi);


/**************************************
* Map histogram of whole image after weighting the Y U V channel data.
* pOffScreen : Data structure stores source and dest image.
	Note : Source image format should be ASVL_PAF_I422H.
	Note : Width and height of image should be a multiple of 2.
* table		 : The table stores historam mapping relation.
**************************************/
MInt32 mcvCalcHistBackProject_I422H(LPASVLOFFSCREEN pOffScreen, MLong* table);


/******************* OPTICAL FLOW *********************/
typedef struct
{
    float x;
    float y;
} mcvPoint2D32f;
typedef struct
{
    int width;
    int height;
} mcvSize;
typedef struct
{
    int		type;  /* may be MCV_CM_TERMCRIT_ITER | MCV_CM_TERMCRIT_EPS */
    int		max_iter;
    float	epsilon;
} mcvTermCriteria;

#define  MCV_CM_LKFLOW_PYR_A_READY       (1)
#define  MCV_CM_LKFLOW_PYR_B_READY       (2)
#define  MCV_CM_LKFLOW_INITIAL_GUESSES   (4)
#define  MCV_CM_LKFLOW_MT                (8)

#define MCV_CM_TERMCRIT_ITER    1
#define MCV_CM_TERMCRIT_EPS     2

/***************/
MInt32 mcvIcmCalcOpticalFlowPyrLK_u8c1( const MByte* imgA, const MByte* imgB,
                      int imgStep, mcvSize imgSize, MByte* pyrA, MByte* pyrB, 
					  const mcvPoint2D32f* featuresA, mcvPoint2D32f* featuresB, 
					  int count, mcvSize winSize, int level, char* status, float* error, 
					  mcvTermCriteria criteria, int flags,MHandle hMemMgr);


/******************* ENDOF OPTICAL FLOW *********************/



/********** mthread API ****************/
MHandle		mcvEventCreate(MBool bAutoReset);
MRESULT		mcvEventDestroy(MHandle hEvent);
MRESULT		mcvEventWait(MHandle hEvent, MDWord dwTimeOut);
MRESULT		mcvEventSignal(MHandle hEvent);
MHandle     mcvThreadCreate(MPVoidProc proc, MVoid* pParam);
MRESULT		mcvThreadExit(MHandle hThread);
MRESULT		mcvThreadDestory(MHandle hThread);
/********** end of mthread API ****************/



/************************ OPENCL API***********************/

/*** COMMON OPENCL API*********/
MHandle mcvOCLInit(MHandle hContext);

MInt32 mcvOCLUnInit(MHandle mcvOCLHandle);

MInt32 mcvOCLWaitGpu(MHandle mcvOCLHandle);

/*** END OF OPENCL GPU API**/

/*** MATRIX OPERATION OPENCL API*********/
/**************************
m1Rows,m1Cols describe the first matrix;
m1Cols,m1Cols describe the second matrix;
RowColMajor: 0 : RowMajor  ; 1 : ColMajor:CURRENTLY SUPPORT ONLY ROW MAJOR 
****************************/
MHandle mcvOCLMatrixMulInit(MHandle mcvOCLHandle, MInt32 m1Rows,  MInt32 m1Cols, MInt32 m2Cols);

MInt32 mcvOCLMatrixMulUnInit(MHandle mcvOCLMatrixHandle);

MInt32 mcvOCLMatrixMul_RowMajor_f32_begin(MHandle mcvOCLMatrixHandle, MFloat *M_out,MFloat *M1_in,MFloat *M2_in,MInt32 m1Rows,  MInt32 m1Cols, MInt32 m2Cols);

MInt32 mcvOCLMatrixMul_RowMajor_f32_end(MHandle mcvOCLMatrixHandle);

/*** END OF MATRIX OPERATION OPENCL API**/


/******* pyramid blend API *********/
MHandle mcvOCLPyrUpDownInit(MHandle mcvOCLHandle,MInt32 iWidth,MInt32 iHeight,MInt32 ilayer);
MInt32 mcvOCLPyrUpDownBeginPyr1(MHandle pyrUpDownHandle,MInt16 **pyramid);
MInt32 mcvOCLPyrUpDownBeginPyr1_NVXX_UV(MHandle pyrUpDownHandle,MInt16 **pyramid);
MInt32 mcvOCLPyrUpDownBeginPyr2(MHandle pyrUpDownHandle,MInt16 **pyramid);
MInt32 mcvOCLPyrUpDownBeginPyr2_NVXX_UV(MHandle pyrUpDownHandle,MInt16 **pyramid);
MInt32 mcvOCLPyrDownWeightTableBegin(MHandle pyrUpDownHandle,MInt16 **weightedTable);
MInt32 mcvOCLPyrDownWeightedAddBeginS16(MHandle pyrUpDownHandle,MInt32 **pyrResult);
MInt32 mcvOCLPyrDownWeightedAddBeginS16_NVXX_UV(MHandle pyrUpDownHandle,MInt32 **pyrResult);
MInt32 mcvOCLPyrUpDownUpdateParam(MHandle pyrUpDownHandle,MInt32 iWidth,MInt32 iHeight);
MInt32 mcvOCLPyrUpDownWaitGpu(MHandle pyrUpDownHandle);
MInt32 mcvOCLPyrUpDownUnInit(MHandle pyrUpDownHandle);
/************************ End of pyramid blend API***********************/



/************************ End of GPU API***********************/

typedef struct
{
	    MLong lCodebase;             	// Codebase version number 
	    MLong lMajor;                		// major version number 
	    MLong lMinor;                		// minor version number
	    MLong lBuild;                		// Build version number, increasable only
	    const MChar *Version;        	// version in string form
	    const MChar *BuildDate;      	// latest build Date
	    const MChar *CopyRight;      	// copyright 
}MCV_Version;

/************************************************************************
 * The function used to get version information of mcv library. 
 ************************************************************************/
const MCV_Version *MCV_GetVersion();

#ifdef __cplusplus
}
#endif

#endif //_MOBILECV_H_
