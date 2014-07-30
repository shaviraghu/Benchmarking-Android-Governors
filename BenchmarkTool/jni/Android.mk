LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_MODULE    := BenchmarkTool
LOCAL_SRC_FILES := benchmark.c cpufreq.c sysfs.c parse.c system.c benchmark-tool.c


include $(BUILD_SHARED_LIBRARY)