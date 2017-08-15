#include <string.h>
#include <jni.h>
#include <fcntl.h>
#include <errno.h>
#include <android/log.h>
#include <head_led.h>
#define  LOG_MIX    "head_led"
#define  LOGM(...)  __android_log_print(ANDROID_LOG_INFO,LOG_MIX,__VA_ARGS__)
#define  LOGG(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_MIX,__VA_ARGS__)

#define LED_ON  0x40044c00
#define LED_OFF 0x40044c01
#define LED_SET_EYE 0x40044c02
#define LED_SET_HEAD 0x40044c03
#define LED_SET_MOUTH 0x40044c04
#define LED_READ_KEYSENS 0x40044c05
#define LED_SET_KEYSENS 0x40044c06

//#define RF_EVENT "/sys/devices/virtual/input/input0/event0/uevent"
#define RF_HEAD_LED "/dev/led_eye"

#define INFTIM -1
#define JNI_FALSE 0
#define JNI_TRUE 1
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */

//ȫ�ֱ���
int led_fd=0;
struct led_eye_set_hal
{
  int rgb;
  int brightness;
  int reye;
  int leye;
  int on;
  int off;
  int total;
  int state;
};

struct key_sens_set_hal
{
  char key1;
  char key2;
  char ret;
};

JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_open(JNIEnv* env,jobject thiz)
{
    if(led_fd<=0)
      led_fd = open(RF_HEAD_LED, O_RDWR);
	if (led_fd < 0) 
    {
		LOGM("open %s error \n", RF_HEAD_LED);
		LOGM("led_fd =%x  \n", led_fd);
		return led_fd;
	}else
    {
        LOGM("open %s ok,0x%x \n", RF_HEAD_LED,led_fd);
    }
	return led_fd;
}

JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_close(JNIEnv* env,jobject thiz)
{
  int result;

       #if 0
       result = close(led_fd);
	   if (result < 0) 
       {
		 LOGM("close %d error \n", result);
         return result;
	   }
       #else
       result=0;
       #endif
	   LOGM("close= %d ok \n", result);
       return result;
}

JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_ledSetOFF(JNIEnv* env,jobject thiz, jint val)
{
  int result;
  int value;

       value=val;
       result=ioctl(led_fd, LED_OFF, value);
       if(result < 0)
       {
         LOGM("ledSetOFF ioctl error --- %s.", strerror(errno));
         return -EFAULT;
       }else
       {
         LOGM("ledSetOFF:0x%x\r\n",value);
       }   
       return 0;
}

JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_ledSetOn(JNIEnv* env,jobject thiz, jint val)
{
  int result;
  int value;

       value=val;
       result=ioctl(led_fd, LED_ON, value);
       if(result < 0)
       {
         LOGM("ledSetOn ioctl error --- %s.", strerror(errno));
         return -EFAULT;
       }else
       {
         LOGM("ledSetOn:0x%x\r\n",value);
       }
       return 0;
}
		
JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_ledSetEye(JNIEnv* env, jobject thiz,
		jint rgb,jint brightness,jint reye,jint leye,jint on,jint off,jint total,jint state)
{
  int result;
  struct led_eye_set_hal *peyes;

       peyes = (struct led_eye_set_hal *)malloc(sizeof(struct led_eye_set_hal));
       memset(peyes, 0, sizeof(struct led_eye_set_hal));
       peyes->rgb=rgb;
       peyes->brightness=brightness;
       peyes->reye=reye;
       peyes->leye=leye;
       peyes->on=on;
       peyes->off=off;
       peyes->total=total;
       peyes->state=state;
       result=ioctl(led_fd, LED_SET_EYE, peyes);
       if(result < 0)
       {
           LOGM("ledSetEye ioctl error --- %s.", strerror(errno));
           return -EFAULT;
       }else
       {
           LOGM("ledSetEye:0x%x,0x%x,0x%x,0x%x,0x%x,0x%x,0x%x,0x%x\r\n",rgb,brightness,reye,leye,on,off,total,state);
       }
       return 0;
}
		
JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_ledSetHead(JNIEnv* env, jobject thiz,
		jint rgb,jint brightness,jint reye,jint leye,jint on,jint off,jint total,jint state)
{
  int result;
  struct led_eye_set_hal *peyes;

       peyes = (struct led_eye_set_hal *)malloc(sizeof(struct led_eye_set_hal));
       memset(peyes, 0, sizeof(struct led_eye_set_hal));
       peyes->rgb=rgb;
       peyes->brightness=brightness;
       peyes->reye=reye;
       peyes->leye=leye;
       peyes->on=on;
       peyes->off=off;
       peyes->total=total;
       peyes->state=state;
       result=ioctl(led_fd, LED_SET_HEAD, peyes);
       if(result < 0)
       {
           LOGM("ledSetHead ioctl error --- %s.", strerror(errno));
           return -EFAULT;
       }else
       {
           LOGM("ledSetHead:0x%x,0x%x,0x%x,0x%x,0x%x,0x%x,0x%x,0x%x\r\n",rgb,brightness,reye,leye,on,off,total,state);
       }
       return 0;
}
		
JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_LedControl_ledSetMouth(JNIEnv* env,jobject thiz,jint brightness,jint on,jint off,jint total,jint state)
{
  int result;
  struct led_eye_set_hal *peyes;

       peyes = (struct led_eye_set_hal *)malloc(sizeof(struct led_eye_set_hal));
       memset(peyes, 0, sizeof(struct led_eye_set_hal));
       peyes->brightness=brightness;
       peyes->on=on;
       peyes->off=off;
       peyes->total=total;
       peyes->state=state;
       result=ioctl(led_fd, LED_SET_MOUTH, peyes);
       if(result < 0)
       {
           LOGM("ledSetMouth ioctl error --- %s.", strerror(errno));
           return -EFAULT;
       }else
       {
           LOGM("ledSetMouth:0x%x,0x%x,0x%x,0x%x,0x%x\r\n",brightness,on,off,total,state);
       }
       return 0;
}

jboolean Java_com_ubtechinc_alpha_jni_LedControl_keySetSens(JNIEnv* env,jobject thiz,jint key1,jint key2)
{
  int result;
  struct key_sens_set_hal *pkey;

       pkey = (struct key_sens_set_hal *)malloc(sizeof(struct key_sens_set_hal));
       memset(pkey, 0, sizeof(struct key_sens_set_hal));
       pkey->key1=key1;
       pkey->key2=key2;
       pkey->ret=3;
       result=ioctl(led_fd, LED_SET_KEYSENS, pkey);
       if(result < 0)
       {
           LOGM("keySetSens ioctl error --- %s.", strerror(errno));
           return -EFAULT;
       }else
       {
           LOGM("keySetSens:0x%x,0x%x,%d\r\n",key1,key2,pkey->ret);
           if(pkey->ret==0)
           {
               LOGM("keySetSens ok.\r\n");
           }else
           {
               LOGM("keySetSens error.\r\n");
               return -EFAULT;
           }
       }
       return 0;
}

jboolean Java_com_ubtechinc_alpha_jni_LedControl_keyReadSens(JNIEnv* env,jobject thiz,jint *key1,jint *key2)
{
  int result;
  struct key_sens_set_hal *pkey;

       pkey = (struct key_sens_set_hal *)malloc(sizeof(struct key_sens_set_hal));
       memset(pkey, 0, sizeof(struct key_sens_set_hal));
       result=ioctl(led_fd, LED_READ_KEYSENS, pkey);
       if(result < 0)
       {
           LOGM("keyReadSens ioctl error --- %s.", strerror(errno));
           return -EFAULT;
       }else
       {
           *key1=pkey->key1;
           *key2=pkey->key2;
           LOGM("LED_READ_KEYSENS:0x%x,0x%x\r\n",pkey->key1,pkey->key2);
       }
       return 0;
}
