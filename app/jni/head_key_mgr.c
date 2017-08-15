#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <fcntl.h>
#include <unistd.h>  
#include <sys/stat.h>  
#include <sys/time.h>  
#include <stdlib.h>
#include <android/log.h>
#include<linux/input.h>
#include <sys/poll.h>
#include <poll.h>
#include <pthread.h>
#include <head_key_mgr.h>
#define  LOG_MIX    "nativeRun"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_MIX,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_MIX,__VA_ARGS__)

///rk29-keypad
#define RF_EVENT "/dev/input/event0"
#define RF_KEY_LOG "/sdcard/keyjnilog.txt"

#define MAX_EVENT 32

#define KEY_LOG_FILE

//ȫ�ֱ���
static volatile int threadRunFlag = 0;
JavaVM *gJavaVM;
jobject gJavaObj;
jmethodID g_methodId;
jclass  g_clz;

jint INFTIM2;
jint direction;

int event_fd;
int event_key_fd[MAX_EVENT];
struct pollfd pfd[1];

int keylog_fd;
time_t time_kay;
char textbuf[128];
int keycunt;
int eventcunt;
int readcunt;

int add(int x1, int x2)
{
	return (x1 + x2);
}

static void *native_thread(void *var)
{
	LOGI("%s enter\r\n",__FUNCTION__);
	int count = 1;
	int nfd = 1;
	int len;
	int dwCount;
	struct input_event data;
	JNIEnv *env;
	
	if((*gJavaVM)->AttachCurrentThread(gJavaVM, &env, NULL) != JNI_OK)
	{
		LOGE("AttachCurrentThread failed\r\n");
          #ifdef KEY_LOG_FILE
          memset(textbuf,0x00,sizeof(textbuf));
		  time (&time_kay);
		  sprintf(textbuf,"AttachCurrentThread failed,%s",ctime(&time_kay));
		  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		  if(keylog_fd)
		  {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		  }else
		  {
           close(keylog_fd);
		  }
          #endif
		return NULL;
	}else
    {
          #ifdef KEY_LOG_FILE
          memset(textbuf,0x00,sizeof(textbuf));
		  time (&time_kay);
		  sprintf(textbuf,"AttachCurrentThread suss,%s",ctime(&time_kay));
		  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		  if(keylog_fd)
		  {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		  }else
		  {
           close(keylog_fd);
		  }
          #else
          LOGE("AttachCurrentThread suss.\r\n");
          #endif
    }
	
	LOGI("native_thread loop enter\r\n");
	dwCount=1;
	
	while(threadRunFlag)
	{
		//�ص�java��ĺ���
		//(*env)->CallVoidMethod(env, gJavaObj, g_methodId, count++);
		if (poll(pfd, nfd, INFTIM2) > 0)
		{
          len = read(event_fd, &data, sizeof(data));
		  //LOGI("len:%d\n",len);
		  if(len)
		  {
			  if(data.value&0x0001)
			  {
                  keycunt++;
				  direction=0;
				  direction|=(data.code&0xFF);
				  direction<<=8;
				  direction|=(data.value&0xFF);
				  time (&time_kay);
				  memset(textbuf,0x00,sizeof(textbuf));
				  LOGI("key:0x%x,%d,%s\r\n",direction,keycunt,ctime(&time_kay));
                  #ifdef KEY_LOG_FILE
				  sprintf(textbuf,"key:0x%x,%d,%s",direction,keycunt,ctime(&time_kay));
				  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
				  if(keylog_fd)
				  {
				    write(keylog_fd,textbuf,strlen(textbuf));
				    close(keylog_fd);
				  }else
				  {
                    close(keylog_fd);
				  }
                  #endif
				  (*env)->CallVoidMethod(env, gJavaObj, g_methodId, direction);
			  }
		  }         
		}else
        {
          #ifdef KEY_LOG_FILE
          if((++eventcunt)>500)
          {
              eventcunt=0;
              LOGI("no event.\r\n");
                  time (&time_kay);
				  memset(textbuf,0x00,sizeof(textbuf));
				  sprintf(textbuf,"no event:%s",ctime(&time_kay));
				  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
				  if(keylog_fd)
				  {
				    write(keylog_fd,textbuf,strlen(textbuf));
				    close(keylog_fd);
				  }else
				  {
                    close(keylog_fd);
				  }
              if((++readcunt)>9000)///clear log file
              {
                  readcunt=0;
                  LOGI("file too large,clear.\r\n");
                  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_CREAT, 0666);
	              if(0<=keylog_fd)
	              {
                     LOGI("open log file clear ok:0x%x\r\n",keylog_fd);
		             ftruncate(keylog_fd,0);//clear file.
		             lseek(keylog_fd,0,SEEK_SET);
                     close(keylog_fd);
                  }else
                  {
                     close(keylog_fd);
                  }
              }
          }
          #endif
        }
		//usleep(5000);
	}
	(*gJavaVM)->DetachCurrentThread(gJavaVM);
                  #ifdef KEY_LOG_FILE
	              time (&time_kay);
				  memset(textbuf,0x00,sizeof(textbuf));
	              sprintf(textbuf,"Thread stop,%s",ctime(&time_kay));
				  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
				  if(keylog_fd)
				  {
				    write(keylog_fd,textbuf,strlen(textbuf));
				    close(keylog_fd);
				  }else
				  {
                    close(keylog_fd);
				  }
                  #endif
	LOGI("%s exit\n",__FUNCTION__);
	return NULL;
}

/*
 * Class:     com_ubtechinc_alpha_jni_headkey_HeadKeyMgr
 * Method:    Init
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_ubtechinc_alpha_jni_headkey_HeadKeyMgr_Init
  (JNIEnv *env, jobject obj)
  {
	  int i;
	  int ret;
	  char name[64];
	  char buf[256]={0,};
	  LOGI("%s enter,2017-2-27\r\n",__FUNCTION__);
	 #if 1       
		for(i=0;i<MAX_EVENT;i++)
		{
           memset(name,0x00,sizeof(name));
		   sprintf(name,"/dev/input/event%d",i);
		   LOGI("open[%d]: %s\n",i,name);
		   event_fd = open(name, O_RDWR);
		   if(0<=event_fd) 
		    {
              LOGI("open fd =0x%x suss \r\n", event_fd);
		      ioctl(event_fd, EVIOCGNAME(sizeof(buf)), buf);
		      LOGI("name: %s\r\n",buf);
			  ret=strcmp(buf,"rk29-keypad");
			  if(ret==0)
			  {
                LOGI("key event,%d\r\n",i);
				break;
			  }else
			  {
                LOGI("not key event,%d\r\n",i);
				close(event_fd);
			  }
		    }else
		    {
		      LOGI("open error,%d\r\n",event_fd);
		      close(event_fd);
		    }
			//usleep(1000);
		}
	    //event_fd=event_key_fd[i];
		LOGI("key fd:0x%x\r\n",event_fd);
	    pfd[0].fd = event_fd;
	    pfd[0].events = POLLIN;

		INFTIM2 = 20;
        direction = -1;
		threadRunFlag = 1;
        keycunt=0;
        eventcunt=0;
        readcunt=0;
	#endif
	/////create log file
      #ifdef KEY_LOG_FILE
      keylog_fd = open(RF_KEY_LOG, O_RDWR|O_CREAT, 0666);
	  if(0<=keylog_fd)
	  {
         LOGI("open log file ok:0x%x\r\n",keylog_fd);
		 ftruncate(keylog_fd,0);//clear file.
		 lseek(keylog_fd,0,SEEK_SET);
		 memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"open file ok,0x%x,%d,%s",event_fd,i,ctime(&time_kay));
		 write(keylog_fd,textbuf,strlen(textbuf));
		 close(keylog_fd);
	  }else
	  {
         LOGI("open log file error:0x%x\r\n",keylog_fd);
		 close(keylog_fd);
	  }
      #endif
	  LOGI("%s exit\r\n",__FUNCTION__);
	  return 0;
  }

  
/*
 * Class:     com_ubtechinc_alpha_jni_headkey_HeadKeyMgr
 * Method:    Add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_ubtechinc_alpha_jni_headkey_HeadKeyMgr_Add
  (JNIEnv * env, jobject obj, jint x, jint y)
  {
	  int ret;
	  ret = add(x, y);
	  LOGI("%s enter ret:%d\r\n",__FUNCTION__,ret);
	     #ifdef KEY_LOG_FILE
         memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"nativeClass_Add enter,%s",ctime(&time_kay));
		 keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		 if(keylog_fd)
		 {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		 }else
		 {
           close(keylog_fd);
		 }
         #endif
	  LOGI("%s exit\r\n",__FUNCTION__);
	  return ret;
  }

/*
 * Class:     com_ubtechinc_alpha_jni_headkey_HeadKeyMgr
 * Method:    nativeInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ubtechinc_alpha_jni_headkey_HeadKeyMgr_nativeInit
  (JNIEnv *env, jobject obj)
  {
		LOGI("%s enter\n",__FUNCTION__);

         #ifdef KEY_LOG_FILE
         memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"HeadKeyMgr_nativeInit enter,%s",ctime(&time_kay));
		 keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		 if(keylog_fd)
		 {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		 }else
		 {
           close(keylog_fd);
		 }
         #endif

		(*env)->GetJavaVM(env, &gJavaVM);
		
		gJavaObj = (*env)->NewGlobalRef(env,obj);
		
		g_clz = (*env)->FindClass(env,"com/ubtechinc/alpha/jni/headkey/HeadKeyMgr");
		g_methodId = (*env)->GetMethodID(env, g_clz, "onNativeCallback", "(I)V");
		if(g_methodId)
		{
			int var = 25;
			LOGI("%s 1\r\n",__FUNCTION__);
			//�ص�java��ĺ���
			(*env)->CallVoidMethod(env, gJavaObj, g_methodId, var);
			LOGI("%s 2\r\n",__FUNCTION__);
		}
		
		pthread_t threadId;
		if(pthread_create(&threadId, NULL, native_thread, NULL) !=0)
		{
			LOGI("thread create failed!\r\n");
          #ifdef KEY_LOG_FILE
          memset(textbuf,0x00,sizeof(textbuf));
		  time (&time_kay);
		  sprintf(textbuf,"thread create failed,%s",ctime(&time_kay));
		  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		  if(keylog_fd)
		  {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		  }else
		  {
           close(keylog_fd);
		  }
          #endif
		}else
        {
          #ifdef KEY_LOG_FILE
          memset(textbuf,0x00,sizeof(textbuf));
		  time (&time_kay);
		  sprintf(textbuf,"thread create suss,%s",ctime(&time_kay));
		  keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		  if(keylog_fd)
		  {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		  }else
		  {
           close(keylog_fd);
		  }
          #else
          LOGI("thread create suss!\r\n");
          #endif
         }
        
        #ifdef KEY_LOG_FILE
         memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"HeadKeyMgr_nativeInit exit,%s",ctime(&time_kay));
		 keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		 if(keylog_fd)
		 {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		 }else
		 {
           close(keylog_fd);
		 }
         #endif
         	
		LOGI("%s exit\r\n",__FUNCTION__);
  }
  
/*
 * Class:     com_ubtechinc_alpha_jni_headkey_HeadKeyMgr
 * Method:    nativeThreadStart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ubtechinc_alpha_jni_headkey_HeadKeyMgr_nativeThreadStart
  (JNIEnv *env, jobject obj)
  {
		LOGI("%s enter\r\n",__FUNCTION__);

		threadRunFlag = 1;
        #ifdef KEY_LOG_FILE
		 memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"HeadKeyMgr_nativeThreadStart,%s",ctime(&time_kay));
		 keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		 if(keylog_fd)
		 {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		 }else
		 {
           close(keylog_fd);
		 }
         #endif
		LOGI("%s exit\r\n",__FUNCTION__);
  }

/*
 * Class:     com_ubtechinc_alpha_jni_headkey_HeadKeyMgr
 * Method:    nativeThreadStop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ubtechinc_alpha_jni_headkey_HeadKeyMgr_nativeThreadStop
  (JNIEnv *env, jobject obj)
  {
	  int result;
		LOGI("%s enter\r\n",__FUNCTION__);

		threadRunFlag = 0;
		result = close(event_fd);
	    if (result < 0) 
		{
		     LOGI("close %d error \r\n", result);
	    }
	    LOGI("close= %d ok \r\n", result);
		LOGI("%s exit\r\n",__FUNCTION__);
         #ifdef KEY_LOG_FILE
		 memset(textbuf,0x00,sizeof(textbuf));
		 time (&time_kay);
		 sprintf(textbuf,"jni ThreadStop,%s",ctime(&time_kay));
		 keylog_fd = open(RF_KEY_LOG, O_RDWR|O_APPEND, 0666);
		 if(keylog_fd)
		 {
		   write(keylog_fd,textbuf,strlen(textbuf));
		   close(keylog_fd);
		 }else
		 {
           close(keylog_fd);
		 }
         #endif
  }

