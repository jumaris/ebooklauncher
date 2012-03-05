CXXFLAGS   := -O2 -g -Wall -fmessage-length=0
LOCAL_PATH := $(call my-dir)

#include $(call all-subdir-makefiles)

#############################################################################################################
#
# now we build our native library

include $(CLEAR_VARS)

LOCAL_CFLAGS           += -DBOOST_EXCEPTION_DISABLE -D_STLP_NO_EXCEPTIONS -DOS_ANDROID -D_STLP_USE_SIMPLE_NODE_ALLOC
LOCAL_CPP_FEATURES     += exceptions
LOCAL_CPPFLAGS         += -fexceptions
LOCAL_LDLIBS           += -llog
#LOCAL_LDLIBS           += -landroid
LOCAL_STATIC_LIBRARIES := cpufeatures boost_thread boost_iostreams libboost_date_time
LOCAL_MODULE           := ndk1

LOCAL_SRC_FILES := \
	native.cpp \
	calibremetadatafile.cpp \
	ebook.cpp \
    CppSQLite3.cpp \
    sqlite3.c

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/include

include $(BUILD_SHARED_LIBRARY)

