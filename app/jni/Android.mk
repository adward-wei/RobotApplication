#
# Copyright 2009 Cedric Priscal
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License. 
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_PLATFORM := android-3
LOCAL_MODULE    := serial_port
LOCAL_SRC_FILES := SerialPortFile.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

TARGET_PLATFORM := android-3
LOCAL_MODULE    := head_led
LOCAL_SRC_FILES := head_led.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

TARGET_PLATFORM := android-3
LOCAL_MODULE    := head_key_mgr
LOCAL_SRC_FILES := head_key_mgr.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)