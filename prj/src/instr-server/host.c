// Server running on the instrument server or as an daemon service on android
// S.HY 2013
// Waiting for connection from dalvik library loading process

//----- Include files ---------------------------------------------------------
#include <stdio.h>      // for printf()
#include <stdlib.h>     // for exit()
#include <string.h>     // for strcpy(),strerror() and strlen()
#include <unistd.h>
#include <fcntl.h>      // for file i/o constants
#include <sys/stat.h>       // for file i/o constants
#include <errno.h>

/* FOR BSD UNIX/LINUX  ---------------------------------------------------- */
#include <sys/types.h>      //
#include <netinet/in.h>     //
#include <sys/socket.h>     // for socket system calls
#include <arpa/inet.h>      // for socket system calls (bind)
#include <sched.h>
#include <pthread.h>        /* P-thread implementation        */
#include <signal.h>     /* for signal                     */
#include <semaphore.h>      /* for p-thread semaphores        */
/* ------------------------------------------------------------------------ */

//----- Defines -------------------------------------------------------------
#define BUF_SIZE            1024    // buffer size in bytes
#define PORT_NUM            6666    // Port number for a Web server (TCP 5080)
#define PEND_CONNECTIONS     100    // pending connections to hold
#define TRUE                   1
#define FALSE                  0
#define NTHREADS 10      /* Number of child threads        */
#define NUM_LOOPS  10       /* Number of local loops          */
#define SCHED_INTVL 5       /* thread scheduling interval     */
#define HIGHPRIORITY 10

#define MAX_DEX 20000000
#define ROOTDIR "/home/usi/disl-android/lib/"
#define min(a,b) a>b?b:a

pthread_mutex_t lock;
int doNothing = 0;

enum INSTR_CMD {
ASMDEX,
DEX2JAR,
DEX2JAR_CORE,
SMALI,
//SMALI2DEX,
CACHE,
ORIGINAL
};
int TOOL = SMALI;
const char* rootdir=ROOTDIR;
const char* instrument_cmd[10]= {
"conversion/asmdex.sh",
"conversion/dex2dex.sh",
"conversion/dex2dex-core.sh",
"conversion/dex2dex-smali.sh",
//"conversion/smali2dex.sh",
"conversion/empty.sh",
};

int stop = 0;
int whitesize = 0;
int whitelist[300] = {
	2575404,
};
int blacksize = 1;
int blacklist[300]= {
	2973048, /* core.jar */
	//24244,  /* core-junit.jar */
	//10157580, /* framework.jar */
	//866260, /* bouncycastle.jar */
	//1334004, /* ext.jar */
	//283636, /* android.policy.jar */
	//2027844, /* services.jar */
	//1223876, /* apache-xml.jar */
	//2916180,	/* Setting.apk */
	//978612,  /* Launcher2.apk */
};
int corelist[10] = {
	2973048,
	1334004,
};
const char*corename[10] = {
	"core.jar",
	"ext.jar",
};
int coresize = 2;
int liblist[300] = {
	36952,
	283636,
	166852,
	1223876,
	13160,
	866260,
	3728,
	6268,
	12552,
	2973048,
	24244,
	1334004,
	10157580,
	5664,
	4608,
	51608,
	105064,
	32860,
	7688,
	2027844,
	8260,
	73660
};
int libsize = 22;
const char* libname[300]= {
	"am.jar",
	"android.policy.jar",
	"android.test.runner.jar",
	"apache-xml.jar",
	"bmgr.jar",
	"bouncycastle.jar",
	"bu.jar",
	"com.android.location.provider.jar",
	"content.jar",
	"core.jar",
	"core-junit.jar",
	"ext.jar",
	"framework.jar",
	"ime.jar",
	"input.jar",
	"javax.obex.jar",
	"monkey.jar",
	"pm.jar",
	"requestsync.jar",
	"services.jar",
	"svc.jar",
	"uiautomator.jar"
};

int apklist[300] = {
	24204,
	10508,
	328424,
	2575404,
	1908964,
	796164,
	1837188,
	39684,
	3224276,
	2258196,
	19220,
	410476,
	131416,
	1602564,
	39272,
	12632,
	3060496,
	2556276,
	28332,
	1446728,
	28168,
	6648,
	3980,
	28484,
	986536,
	978612,
	101084,
	25588,
	44148,
	142360,
	882960,
	230192,
	85908,
	33460,
	313612,
	34132,
	30648,
	651436,
	17616,
	147792,
	1308,
	1912684,
	2916180,
	65584,
	3528,
	26580,
	12808,
	479692,
	229668,
	14936,
	331228,
	51696,
	66952,
	9932,
	144276
};
int apksize = 55;
const char* apkname[300]= {
	"ApplicationsProvider.apk",
	"BackupRestoreConfirmation.apk",
	"Bluetooth.apk",
	"Browser.apk",
	"Calculator.apk",
	"Calendar.apk",
	"CalendarProvider.apk",
	"CertInstaller.apk",
	"Contacts.apk",
	"ContactsProvider.apk",
	"DefaultContainerService.apk",
	"DeskClock.apk",
	"Development.apk",
	"DownloadProvider.apk",
	"DownloadProviderUi.apk",
	"DrmProvider.apk",
	"Email.apk",
	"Exchange2.apk",
	"Galaxy4.apk",
	"Gallery2.apk",
	"HoloSpiralWallpaper.apk",
	"HTMLViewer.apk",
	"InputDevices.apk",
	"KeyChain.apk",
	"LatinIME.apk",
	"Launcher2.apk",
	"LiveWallpapers.apk",
	"LiveWallpapersPicker.apk",
	"MagicSmokeWallpapers.apk",
	"MediaProvider.apk",
	"Mms.apk",
	"Music.apk",
	"MusicFX.apk",
	"NoiseField.apk",
	"OpenWnn.apk",
	"PackageInstaller.apk",
	"PhaseBeam.apk",
	"Phone.apk",
	"PicoTts.apk",
	"PinyinIME.apk",
	"Provision.apk",
	"QuickSearchBox.apk",
	"Settings.apk",
	"SettingsProvider.apk",
	"SharedStorageBackup.apk",
	"SoundRecorder.apk",
	"SpeechRecorder.apk",
	"SystemUI.apk",
	"TelephonyProvider.apk",
	"UserDictionaryProvider.apk",
	"VideoEditor.apk",
	"VisualizationWallpapers.apk",
	"VoiceDialer.apk",
	"VpnDialogs.apk",
	"WAPPushManager.apk"
};



/* global variables ---------------------------------------------------- */

sem_t thread_sem[NTHREADS];
int next_thread;
int can_run;
int i_stopped[NTHREADS];
char* dexes[NTHREADS];


int parsedlist[300];
int parsedsize=0;
//unsigned int client_s;      // Client socket descriptor

int iscore(int size){
	int i = 0;
	for(; i<coresize; i++)
		if(corelist[i]==size)
			return 1;
	return 0;
}
int isparsed(int size){
	int i = 0;
	for(; i<parsedsize; i++)
		if(parsedlist[i]==size)
			return 1;
	return 0;
}
void print_list(int* arr, int len) {
	int i=0;
	for(;i < len;i++)
		printf("%d,\n",arr[i]);
}

int find_apk(int size){
	int i = 0;
	for(; i<apksize; i++)
		if(apklist[i]==size)
			return i;
	return -1;
}
int find_lib(int size){
	int i = 0;
	for(; i<libsize; i++)
		if(liblist[i]==size)
			return i;
	return -1;
}
int iswhite(int size){
	int i = 0;
	for(; i<whitesize; i++)
		if(whitelist[i]==size)
			return 1;
	return 0;
}

int isblack(int size){
	int i = 0;
	for(; i<blacksize; i++)
		if(blacklist[i]==size)
			return 1;
	return 0;
}


/* Child thread implementation ----------------------------------------- */
	void *
my_thread (void *arg)
{
	// pthread_mutex_lock(&lock);
	unsigned int myClient_s;    //copy socket
	int success = 1;
	int coreinst = 0;

	/* other local variables ------------------------------------------------ */
	char buf[BUF_SIZE]; // buffer for socket
	unsigned int retcode;       // Return code

	int index = ((int*)arg)[1]%NTHREADS;
	printf("in %d thread\n",index);
	char* dex = (char*)malloc(20000000);
	unsigned int dex_size=0;
	unsigned int cnt=0;

	myClient_s = *(unsigned int *) arg; // copy the socket
	FILE* output = NULL;

	// receive from the client the dex content
	
	//int flag;
	//retcode = recv(myClient_s, &flag, sizeof(int), 0);
	//printf("flag: %d\n", flag);
	//flag = ntohl(flag);
	//printf("flag: %d\n", flag);


	int namelen;
	retcode = recv(myClient_s, &namelen, sizeof(int), 0);
	namelen = ntohl(namelen);
	if(namelen == -1) {
		stop = 1;
		goto release;
	}

	retcode = recv(myClient_s, &dex_size, sizeof(int), 0);
	dex_size = ntohl(dex_size);
	recv(myClient_s, buf, namelen, 0);
	buf[namelen] = 0;
	printf("receive name: %s\n", buf);

	//printf("receive dex size: %d\n", dex_size);
	if(dex_size>20000000) {
		goto release;
		printf("too big size %d\n", dex_size);
	}
	pthread_mutex_lock(&lock);
	if(find_apk(dex_size)>=0){
		printf("receive apk: %s of size %d \n",apkname[find_apk(dex_size)],dex_size);
		success = 1;
	}else if(find_lib(dex_size)>=0){
		printf("receive lib: %s of size %d \n",libname[find_lib(dex_size)],dex_size);
		if(iscore(dex_size))
			coreinst = 1;
		success = 1;
	}else {
		success = 0;
		printf("found new apk of size %d \n", dex_size);
	}

	if(isparsed(dex_size)){
		success = 1;
		printf("the second parse\n");
	}
	else {
		//success = 1;
		parsedlist[parsedsize++]=dex_size;
	}

	int i = 0;
	if(iswhite(dex_size)){
		success = 1;
	}
	if(success){
		if(isblack(dex_size)){
			success=0;
			printf("this one is in blacklist\n");
		}
	}
	pthread_mutex_unlock(&lock);
	//success = 0;
	//printf("*****************************************\n");
	//print_list(alllist,a_size);
	//printf("*****************************************\n");
	if(doNothing)
		success = 0;
	if(success)
		printf("instrumenting file...\n ");
	else
		printf("sending back oringal file...\n");

	if(success) {
		char filename[100];
		char filename2[100];
		int len = snprintf(filename,100,"tmp%d.dex",dex_size);
		int len2 = snprintf(filename2,100,"tmp%d.output.dex",dex_size);
		filename[len]=0;
		filename2[len2]=0;
		output = fopen(filename,"wb");
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			memcpy(dex+cnt,buf,retcode);
			fwrite(buf, sizeof(char),retcode,output);
			cnt+=retcode;
		}
		fclose(output);
		output=NULL;
		char cmd[300]="";

		if(TOOL == DEX2JAR || TOOL == DEX2JAR_CORE) {
			if(coreinst) {
				snprintf(cmd+strlen(cmd),300,"%s%s ",rootdir,instrument_cmd[DEX2JAR_CORE]);
			}else{
				snprintf(cmd+strlen(cmd),300,"%s%s ",rootdir,instrument_cmd[DEX2JAR]);
			}
		}else {
			snprintf(cmd+strlen(cmd),300,"%s%s ",rootdir,instrument_cmd[TOOL]);
		}
		snprintf(cmd+strlen(cmd),300,"%s ",filename);
		snprintf(cmd+strlen(cmd),300,"%s",filename2);
		system(cmd);

		FILE * input = NULL;
		input = fopen(filename2,"rb");
		if(!input) {
			printf("cannot open file");
			success = -1;
		}
		int newsize = 0;
		while( (retcode=fread(buf,sizeof(unsigned char),BUF_SIZE, input))!=0)
		{
			newsize += retcode;
		}
		fclose(input);
		printf("old size/new size: %d/%d\n",dex_size,newsize);
		retcode = send(myClient_s, &newsize, sizeof(int),0);
		if(retcode != sizeof(int))
			goto release;
		input = fopen(filename2,"rb");
		FILE* tmp = fopen("tmp.dex","wb");
		while( (retcode=fread(buf,sizeof(unsigned char),BUF_SIZE, input))!=0)
		{
			newsize -= retcode;
			send(myClient_s, buf, retcode,0);
			fwrite(buf, sizeof(char), retcode, tmp);
		}
		fclose(tmp);
		if(newsize != 0) {
			printf("err happens during sending new file\n");
		}

		fclose(input);
	}else {
		cnt = 0;
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			memcpy(dex+cnt,buf,retcode);
			cnt+=retcode;
		}
		//	printf("received %d bytes\n",cnt);
		retcode = send(myClient_s, &dex_size, sizeof(int), 0);
		cnt = 0;
		while(cnt < dex_size) {
			retcode = send(myClient_s, dex+cnt, min(BUF_SIZE, dex_size-cnt),0);
			if(retcode < 0)
				goto release;
			cnt+=retcode;
		}
		//	printf("return size returned: %d\n", cnt);
	}

release:
	if(success) {
		printf("***********************************************\n");
		printf("***********************************************\n");
	}else
		printf("===============================================\n");
	if(output)
		fclose(output);
	free(dex);
	close(myClient_s);
	// pthread_mutex_unlock(&lock);
	pthread_detach(pthread_self());
	pthread_exit(NULL);
}

//===== Main program ========================================================
	int
main (int argc, const char* argv[])
{
	//const char* progname = NULL;
	printf("you can specify which tool to use for instrumentation: smali/dex2jar/asmdex/cache\n");
	TOOL = ORIGINAL;
	if(argc >= 2) {
		if(!strcmp(argv[1],"smali")) {
			TOOL = SMALI;
		}else if(!strcmp(argv[1],"dex2jar")) {
			TOOL = DEX2JAR;
		}else if(!strcmp(argv[1],"asmdex")) {
			TOOL = ASMDEX;
		}else if(!strcmp(argv[1],"cache")) {
			TOOL = CACHE;
		}else if(!strcmp(argv[1],"original")) {
			TOOL = ORIGINAL;
		}
	}
	switch(TOOL){
		case SMALI:
			printf("use smali to instrument\n");
			break;
		case DEX2JAR:
			printf("use dex2jar to instrument\n");
			break;
		case ASMDEX:
			printf("use asmdex to instrument\n");
			break;
		case CACHE:
			printf("use result of last time\n");
			break;
		case ORIGINAL:
			printf("use original to instrument\n");
			doNothing = 1;
			break;
	};
	/* local variables for socket connection -------------------------------- */
	unsigned int server_s;  // Server socket descriptor
	struct sockaddr_in server_addr; // Server Internet address
	unsigned int            client_s;           // Client socket descriptor
	struct sockaddr_in client_addr; // Client Internet address
	struct in_addr client_ip_addr;  // Client IP address
	int addr_len;           // Internet address length

	unsigned int ids;       // holds thread args
	pthread_attr_t attr;        //  pthread attributes
	pthread_t threads;      // Thread ID (used by OS)

	/* create a new socket -------------------------------------------------- */
	server_s = socket (AF_INET, SOCK_STREAM, 0);

	/* fill-in address information, and then bind it ------------------------ */
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons (PORT_NUM);
	server_addr.sin_addr.s_addr = htonl (INADDR_ANY);
	bind (server_s, (struct sockaddr *) &server_addr, sizeof (server_addr));

	/* Listen for connections and then accept ------------------------------- */
	listen (server_s, PEND_CONNECTIONS);
	/* the web server main loop ============================================= */
	pthread_attr_init (&attr);

	int i = 0;
	////for(; i < NTHREADS; i++)
	//	dexes[i] = malloc(20000000);

	int index=0;
	//test system



	if (pthread_mutex_init(&lock, NULL) != 0)
	{
		printf("\n mutex init failed\n");
		return 1;
	}
	while (!stop)
	{

		/* wait for the next client to arrive -------------- */
		addr_len = sizeof (client_addr);
		client_s =
			accept (server_s, (struct sockaddr *) &client_addr, &addr_len);

		if (client_s == FALSE)
		{
			printf ("ERROR - Unable to create socket \n");
			exit (FALSE);
		}

		else
		{
			/* Create a child thread --------------------------------------- */
			ids = client_s;
			int a[2]={client_s,index};
			pthread_create (    /* Create a child thread        */
					&threads,   /* Thread ID (system assigned)  */
					&attr,  /* Default thread attributes    */
					my_thread,  /* Thread routine               */
					a);  /* Arguments to be passed       */

		}
		index++;
	}

	/* To make sure this "main" returns an integer --- */
	close (server_s);       // close the primary socket
	return (TRUE);      // return code from "main"
}

