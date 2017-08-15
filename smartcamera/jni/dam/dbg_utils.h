#ifndef __DBG_UTILS_H__
#define __DBG_UTILS_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MYDEBUG_EBANLE
#ifdef MYDEBUG_EBANLE

#define _MYSTR(X) _MYVAL(X)  
#define _MYVAL(X) #X  

#if defined(ANDROID) || defined(__ANDROID__)
#include <android/log.h>
#define MYLOGRAW(format,...)  __android_log_print(ANDROID_LOG_ERROR,__FILE__,format,##__VA_ARGS__)
#define MYLOGE(format,...)    __android_log_print(ANDROID_LOG_ERROR,__FILE__,"ERROR:" __FILE__ "-" _MYSTR(__LINE__) "-%s:" format,__FUNCTION__,##__VA_ARGS__)
#define MYLOGI(format,...)    __android_log_print(ANDROID_LOG_ERROR,__FILE__,"INFO:" format,##__VA_ARGS__)
#elif !defined(WIN32)
#define MYLOGRAW(format,...)  printf(format "\n",##__VA_ARGS__)
#define MYLOGE(format,...)    printf("ERROR:" __FILE__ "-" _MYSTR(__LINE__) "-%s:" format "\n",__FUNCTION__,##__VA_ARGS__)
#define MYLOGI(format,...)    printf("INFO:" format "\n",##__VA_ARGS__)
#else

#define MYLOGRAW(format,...)  printf(format"\r\n", ##__VA_ARGS__)
#define MYLOGE(format,...)    printf("ERROR:" __FILE__ "-" _MYSTR(__LINE__) "-" __FUNCTION__ ":" format "\r\n", ##__VA_ARGS__)
#define MYLOGI(format,...)    printf("INFO:" format "\r\n", ##__VA_ARGS__)

#endif

#define MYASSERT_MSG(cond,format,...)  \
		do{\
			if ((!(cond))){\
				MYLOGRAW("assert here %s-%d-%s",__FILE__,__LINE__,__FUNCTION__); \
                MYLOGRAW("assert condition %s",#cond);   \
                MYLOGRAW("assert message " format,##__VA_ARGS__); \
				abort(); \
			}\
		} while (0)

#define MYASSERT(cond)  MYASSERT_MSG(cond," ")
#else
#define MYLOGRAW
#define MYLOGE
#define MYLOGI
#define MYASSERT_MSG
#define MYASSERT
#endif
#ifdef __cplusplus
}
#endif

#endif
