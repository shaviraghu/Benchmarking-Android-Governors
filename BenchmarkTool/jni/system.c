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
#include <time.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <sched.h>

#include <sys/syscall.h> // set cpu affinity
#include <pthread.h> // set cpu affinity

#include <android/log.h>

#include <cpufreq.h>

#include "config.h"
#include "system.h"

/**
 * returns time since epoch in µs
 *
 * @retval time
 **/

long long int get_time()
{
	struct timeval now;

	gettimeofday(&now, NULL);

	return (long long int)(now.tv_sec * 1000000LL + now.tv_usec);
}

/**
 * sets the cpufreq governor
 *
 * @param governor cpufreq governor name
 * @param cpu cpu for which the governor should be set
 *
 * @retval 0 on success
 * @retval -1 when failed
 **/

int set_cpufreq_governor(char *governor, unsigned int cpu)
{

	dprintf("set %s as cpufreq governor\n", governor);

	if (cpufreq_cpu_exists(cpu) != 0) {
		perror("cpufreq_cpu_exists");
		fprintf(stderr, "error: cpu %u does not exist\n", cpu);
		__android_log_print(ANDROID_LOG_ERROR,"system.c","error: cpu %u does not exist\n", cpu);
		return -1;
	}

	if (cpufreq_modify_policy_governor(cpu, governor) != 0) {
		perror("cpufreq_modify_policy_governor");
		__android_log_print(ANDROID_LOG_ERROR,"system.c","error: unable to set %s governor\n", governor);
		fprintf(stderr, "error: unable to set %s governor\n", governor);
		return -1;
	}

	return 0;
}

/**
 * sets cpu affinity for the process
 *
 * @param cpu cpu# to which the affinity should be set
 *
 * @retval 0 on success
 * @retval -1 when setting the affinity failed
 **/



int set_cpu_affinity(unsigned int cpu)
{
	int syscallres=setCurrentThreadAffinityMask(cpu);

	if (syscallres) {
		perror("sched_setaffinity");
		fprintf(stderr, "warning: unable to set cpu affinity\n");
		return -1;
	}
	__android_log_print(ANDROID_LOG_INFO,"system.c","successfully set affinity to %d",cpu);
	return 0;
}
//void setCurrentThreadAffinityMask(int mask)
//{
//    int err, syscallres;
//    pid_t pid = gettid();
//    syscallres = syscall(__NR_sched_setaffinity, pid, sizeof(mask), &mask);
//    if (syscallres)
//    {
//        err = errno;
//        LOGE("Error in the syscall setaffinity: mask=%d=0x%x err=%d=0x%x", mask, mask, err, err);
//    }
//}
void setCurrentThreadAffinityMask(int mask)
{
    int err, syscallres;
    pid_t pid = gettid();
    syscallres = syscall(__NR_sched_setaffinity, pid, sizeof(mask), &mask);
    if (syscallres)
        {
            err = errno;
           __android_log_print("Error in the syscall setaffinity: mask=%d=0x%x err=%d=0x%x", mask, mask, err, err);
        }
    return syscallres;
}

/**
 * sets the process priority parameter
 *
 * @param priority priority value
 *
 * @retval 0 on success
 * @retval -1 when setting the priority failed
 **/

int set_process_priority(int priority)
{
	struct sched_param param;

	dprintf("set scheduler priority to %i\n", priority);

	param.sched_priority = priority;

	if (sched_setscheduler(0, SCHEDULER, &param) < 0) {
		perror("sched_setscheduler");
		fprintf(stderr, "warning: unable to set scheduler priority\n");
		return -1;
	}

	return 0;
}

/**
 * notifies the user that the benchmark may run some time
 *
 * @param config benchmark config values
 *
 **/

void prepare_user(const struct config *config)
{
	unsigned long sleep_time = 0;
	unsigned long load_time = 0;
	unsigned int round;


	for (round = 0; round < config->rounds; round++) {
		sleep_time +=  2 * config->cycles *
			(config->sleep + config->sleep_step * round);
		load_time += 2 * config->cycles *
			(config->load + config->load_step * round) +
			(config->load + config->load_step * round * 4);
	}

	if (config->verbose || config->output != stdout)
		__android_log_print(ANDROID_LOG_INFO,"system.c","approx. test duration: %im\n",
		       (int)((sleep_time + load_time) / 60000000));
}

/**
 * sets up the cpu affinity and scheduler priority
 *
 * @param config benchmark config values
 *
 **/

void prepare_system(const struct config *config)
{

	strcpy(config->governor,cpufreq_get_policy(config->cpu)->governor);

//	if (config->verbose)
//			__android_log_print(ANDROID_LOG_INFO,"system.c","set cpu affinity to cpu #%u\n", config->cpu);
//
//	set_cpu_affinity(config->cpu);
//
//	switch (config->prio) {
//	case SCHED_HIGH:
//		if (config->verbose)
//			__android_log_print(ANDROID_LOG_INFO,"system.c","high priority condition requested\n");
//
//		set_process_priority(PRIORITY_HIGH);
//		break;
//	case SCHED_LOW:
//		if (config->verbose)
//			__android_log_print(ANDROID_LOG_INFO,"system.c","low priority condition requested\n");
//
//		set_process_priority(PRIORITY_LOW);
//		break;
//	default:
//		if (config->verbose)
//			__android_log_print(ANDROID_LOG_INFO,"system.c","default priority condition requested\n");
//
//		set_process_priority(PRIORITY_DEFAULT);
//	}
}

