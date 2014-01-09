#ifndef _DEBUGHEADER_H_
#define _DEBUGHEADER_H_

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <netinet/in.h>
#include <string.h>

#define ASSERT(cond, ...) do{if(!cond) printf(__VA_ARGS__); printf("\n"); }while(0)

#define ERROR(...) do{printf(__VA_ARGS__);printf("\n");}while(0)

#define DEBUG(...) do{printf(__VA_ARGS__);printf("\n");}while(0)


#endif
