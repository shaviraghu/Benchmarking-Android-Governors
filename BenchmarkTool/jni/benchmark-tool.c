/*  cpufreq-bench CPUFreq microbenchmark
 *
 *  Copyright (C) 2008 Christian Kornacker <ckornacker@suse.de>
 *  Copyright (C) 2013 Raghavendra Shavi <shaviraghu@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <errno.h>
#include <jni.h>
#include <android/log.h>

#include "config.h"
#include "parse.h"
#include "benchmark.h"

static struct option long_options[] = {
	{"output",	1,	0,	'o'},
	{"sleep",	1,	0,	's'},
	{"load",	1,	0,	'l'},
	{"verbose",	0,	0,	'v'},
	{"cpu",		1,	0,	'c'},
	{"governor",1,	0,	'g'},
	{"prio",	1,	0,	'p'},
	{"file",	1,	0,	'f'},
	{"cycles",	1,	0,	'n'},
	{"rounds",	1,	0,	'r'},
	{"load-step",	1,	0,	'x'},
	{"sleep-step",	1,	0,	'y'},
	{"help",	0,	0,	'h'},
	{0, 0, 0, 0}
};

/*******************************************************************
 usage
*******************************************************************/

void usage()
{
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c","usage: ./bench\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c","Options:\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -l, --load=<long int>\t\tinitial load time in us\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -s, --sleep=<long int>\t\tinitial sleep time in us\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -x, --load-step=<long int>\ttime to be added to load time, in us\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -y, --sleep-step=<long int>\ttime to be added to sleep time, in us\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -c, --cpu=<cpu #>\t\t\tCPU Nr. to use, starting at 0\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -p, --prio=<priority>\t\t\tscheduler priority, HIGH, LOW or DEFAULT\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -g, --governor=<governor>\t\tcpufreq governor to test\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -n, --cycles=<int>\t\t\tload/sleep cycles\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -r, --rounds<int>\t\t\tload/sleep rounds\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -f, --file=<configfile>\t\tconfig file to use\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -o, --output=<dir>\t\t\toutput path. Filename will be OUTPUTPATH/benchmark_TIMESTAMP.log\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -v, --verbose\t\t\t\tverbose output on/off\n");
	__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c"," -h, --help\t\t\t\tPrint this help screen\n");
	exit(1);
}

int Java_com_example_benchmarktool_BenchmarkActivity_runBenchmark(JNIEnv * env, jobject this, jstring name, jint onlineCPUCount, jstring scl_gov)
{
	int c;
		int option_index = 0;
		struct config *config = NULL;

		config = prepare_default_config();
		char* nativeString = (*env)->GetStringUTFChars(env, scl_gov, 0);

		strcpy(config->governor,nativeString);




		if (config == NULL)
			return EXIT_FAILURE;

		__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c","starting benchmark with parameters:\n");
		if (config->verbose) {
			__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c","starting benchmark with parameters:\n");
			__android_log_print(ANDROID_LOG_INFO,"benchmark-tool.c","config:\n\t"
			       "sleep=%li\n\t"
			       "load=%li\n\t"
			       "sleep_step=%li\n\t"
			       "load_step=%li\n\t"
			       "cpu=%u\n\t"
			       "cycles=%u\n\t"
			       "rounds=%u\n\t"
			       "governor=%s\n\n",
			       config->sleep,
			       config->load,
			       config->sleep_step,
			       config->load_step,
			       config->cpu,
			       config->cycles,
			       config->rounds,
			       config->governor);
		}

		prepare_user(config);
		//prepare_system(config);
		start_benchmark(config,env,this,onlineCPUCount);

		if (config->output != stdout)
			fclose(config->output);

		free(config);

		return 3;

}
int Java_com_example_benchmarktool_BenchmarkActivity_setScalingGovernor(JNIEnv * env, jobject this, jstring gov)
{
	return set_scaling_governor(env,this,gov);
}



