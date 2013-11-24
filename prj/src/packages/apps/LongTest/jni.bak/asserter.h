#ifndef _ASSERTER_H
#define _ASSERTER_H

#ifndef ERR_PREFIX
#error ERR_PREFIX macro has to be deffined
#endif

#include <stdlib.h>

// true/false consts
#define TRUE 1
#define FALSE 0

// error nums
#define ERR 10000
#define ERR_STD 10002

/*
 * Reports error if condition is true
 */
void check_error(int cond, const char *str) {

	if (cond) {

		fprintf(stderr, "%s%s\n", ERR_PREFIX, str);

		exit(ERR);
	}
}

/*
 * Check error routine - reporting on one place
 */
void check_std_error(int retval, int errorval, const char *str) {

	if (retval == errorval) {

		static const int BUFFSIZE = 1024;

		char msgbuf[BUFFSIZE];

		snprintf(msgbuf, BUFFSIZE, "%s%s", ERR_PREFIX, str);

		perror(msgbuf);

		exit(ERR_STD);
	}
}

#endif
