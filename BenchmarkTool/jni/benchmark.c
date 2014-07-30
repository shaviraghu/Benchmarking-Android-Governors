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
#include <unistd.h>
#include <math.h>
#include <android/log.h>
#include <jni.h>
#include "config.h"
#include "system.h"
#include "benchmark.h"

/**
 * compute how many rounds of calculation we should do
 * to get the given load time
 *
 * @param load aimed load time in µs
 *
 * @retval rounds of calculation
 **/

unsigned int calculate_timespace(long load, struct config *config)
{
	int i;
	long long now, then;
	unsigned int estimated = GAUGECOUNT;
	unsigned int rounds = 0;
	unsigned int timed = 0;

	if (config->verbose)
		//__android_log_print(ANDROID_LOG_INFO,"benchmark.c","calibrating load of %lius, please wait...\n", load);

	/* get the initial calculation time for a specific number of rounds */
	now = get_time();
	ROUNDS(estimated);
	then = get_time();

	timed = (unsigned int)(then - now);

	/* approximation of the wanted load time by comparing with the
	 * initial calculation time */
	for (i = 0; i < 4; i++) {
		rounds = (unsigned int)(load * estimated / timed);
		dprintf("calibrating with %u rounds\n", rounds);
		now = get_time();
		ROUNDS(rounds);
		then = get_time();

		timed = (unsigned int)(then - now);
		estimated = rounds;
	}
	if (config->verbose)
		//__android_log_print(ANDROID_LOG_INFO,"benchmark.c","calibration done\n");

	return estimated;
}

void pstate_off_performance_calculation_rounds(struct config *config)
{
	//_android_log_print(ANDROID_LOG_INFO,"benchmark.c","pstate_off_performance_calculation_rounds");
		//code for calculating performance rounds
	  unsigned int total_calculations[20];
	  unsigned int *ptr ;
	  int _round;

	  unsigned int calculations=0;
	  long load_time = 0;
	  int num;

	 FILE *f;
	 f = fopen("/sdcard/perf.txt", "w");
	 if (f == NULL)
	 {
		__android_log_print(ANDROID_LOG_INFO,"benchmark.c","opening perf.csv failed");
		 return;
	 }
	 for (_round = 0; _round < 20; _round++){
	 	load_time=config->load;
	 	ptr=total_calculations;
		 for (num = 0; num < 20; num++) {

			calculations = calculate_timespace(load_time, config);
			fprintf(f, "%u,", calculations);
			*ptr+=calculations;ptr++;
			load_time += config->load_step;
		 }
		 fprintf(f, "\n");
		 __android_log_print(ANDROID_LOG_INFO,"benchmark.c","round %d",_round);
	 }
	 fprintf(f,"Avg:\n");
	 ptr=total_calculations;
	 for(num=0;num<20;num++){
		 fprintf(f,"%u,",*ptr/20);
		 ptr++;
	 }
	 fclose(f);

}

/**
 * benchmark
 * generates a specific sleep an load time with the performance
 * governor and compares the used time for same calculations done
 * with the configured powersave governor
 *
 * @param config config values for the benchmark
 *
 **/

void start_benchmark(struct config *config, JNIEnv *env, jobject obj, jint onlinecpus)
{
	int i;
	unsigned int _round, cycle;
	long long now, then;
	long sleep_time = 0, load_time = 0;
	long performance_time = 0, powersave_time = 0;
	unsigned int calculations;
	unsigned long total_time = 0, progress_time = 0;
	char current_gov[30];
	char buffer [100];

	int rounds[20]={23,40,64,84,103,128,147,170,191,208,229,249,277,297,317,342,360,382,403,412};

//	pstate_off_performance_calculation_rounds(config);
//
//	jstring iString;
//
	jclass cls = (*env)->GetObjectClass(env, obj);

	jmethodID mid = (*env)->GetMethodID(env, cls, "broadcastIntent", "(ILjava/lang/String;)V");

	sprintf (buffer, "Benchmarking %s",config->governor);
	jstring iString = (*env)->NewStringUTF(env, buffer);
	(*env)->CallVoidMethod(env, obj, mid, 1002, iString);

	sleep_time = config->sleep;
	load_time = config->load;
    __android_log_print(ANDROID_LOG_INFO,"benchmark.c","Gov:%s",config->governor);
	strcpy(current_gov,config->governor);


	for (_round = 0; _round < config->rounds; _round++) {
		performance_time = 0LL;
		powersave_time = 0LL;

		if (config->verbose)
        __android_log_print(ANDROID_LOG_INFO,"benchmark.c","In round %i",_round);

        sprintf (buffer, "%s:rnd%i,%lius,%u",current_gov,_round,sleep_time,config->cycles);
        iString = (*env)->NewStringUTF(env, buffer);
        (*env)->CallVoidMethod(env, obj, mid, 1002, iString);
        sleep(5);

        calculations=rounds[_round];

		sprintf (buffer, "perf%i start",_round );
		iString = (*env)->NewStringUTF(env, buffer);
		(*env)->CallVoidMethod(env, obj, mid, 1002, iString);
		/* do some sleep/load cycles with the performance governor */
		for (cycle = 0; cycle < config->cycles; cycle++) {
			now = get_time();
			usleep(sleep_time);
			ROUNDS(calculations);
			then = get_time();
			performance_time += then - now - sleep_time;
		}

		sprintf (buffer, "perf%i end:%ld",_round, performance_time );
		iString = (*env)->NewStringUTF(env, buffer);
		(*env)->CallVoidMethod(env, obj, mid, 1002, iString);
		sleep(5);
		sleep_time += config->sleep_step;
		load_time += config->load_step;
	}
}




int set_scaling_governor(JNIEnv *env, jobject obj,jstring scaling_gov){

//	const char *str= (*env)->GetStringUTFChars(env,scaling_gov,0);
//	int res= set_cpufreq_governor(str, 0) ;
//	(*env)->ReleaseStringUTFChars(env, scaling_gov, str);
//	return res;
}



