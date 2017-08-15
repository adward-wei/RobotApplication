# Copyright 2011 Tero Saarni
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := libmpbase
LOCAL_SRC_FILES := libmpbase.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libarcsoft_handsigns
LOCAL_SRC_FILES := libarcsoft_handsigns.so
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := share_vsign
LOCAL_CFLAGS    := -std=c++11
LOCAL_SRC_FILES := share_vsign.cpp
LOCAL_LDLIBS    := -L$(LOCAL_PATH) -larcsoft_vsign_detection
LOCAL_SHARED_LIBRARIES := libmpbase 
LOCAL_LDFLAGS += -Wl,--version-script=jni/dam/exportSymVSigns.lds
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := shareface_tracking
LOCAL_CFLAGS    := -std=c++11
LOCAL_SRC_FILES := shareface_tracking.cpp
LOCAL_LDLIBS    := -L$(LOCAL_PATH) -larcsoft_face_tracking -larcsoft_face_base
LOCAL_SHARED_LIBRARIES := libmpbase libarcsoft_handsigns
LOCAL_LDFLAGS += -Wl,--version-script=jni/dam/exportSymFaceTracking.lds
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := dam
LOCAL_CFLAGS    := -std=c++11
LOCAL_SRC_FILES :=  DamInterface.cpp
LOCAL_LDLIBS    := -llog -landroid -ljnigraphics -lEGL -lGLESv2 -L$(LOCAL_PATH)   -larcsoft_mobilecv 
LOCAL_SHARED_LIBRARIES := libarcsoft_handsigns  libshareface_tracking  libshare_vsign libmpbase
LOCAL_LDFLAGS += -Wl,--version-script=jni/dam/exportSymDam.lds
include $(BUILD_SHARED_LIBRARY)
