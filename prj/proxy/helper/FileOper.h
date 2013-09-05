#include <stdio.h>

#include <stdlib.h>
#include <string.h>
#define MAXLEN 1024

int ReadFile(const char* fname, char* data) {
	char buf[MAXLEN];
	FILE* infile = fopen(fname, "rb");
	if(!infile)
		return -1;
	int rc;
	int cnt=0;
	while( (rc = fread(buf, sizeof(unsigned char), MAXLEN,infile)) != 0 )
	{
		strncpy(data,buf,rc);
		data += rc;
		cnt+=rc;
	} 
	fclose(infile);
	return cnt;
}
int WriteFile(const char* fname, const char* data, int length) {
	char buf[MAXLEN];
	FILE* outfile = fopen(fname,"wb");
	if(!outfile)
		return -1;
	int cnt=0;
	int rc;
	while(cnt<length) {
		rc = fwrite(data, sizeof( unsigned char ), length-cnt>MAXLEN?MAXLEN:length-cnt , outfile);
		data += MAXLEN;
		cnt+=rc;
	}
	fclose(outfile);
	return cnt;
}

int main(int argc, char *argv[])

{

	if( argc < 3 )
	{

		printf("usage: %s %s/n", argv[0], "infile outfile");

		exit(1);

	}
	char input[1300000];
	int cnt = ReadFile(argv[1],input);
	input[cnt]=0;
	printf("read %s\n",input);
	cnt = WriteFile(argv[2],input,cnt);

/*

	FILE * outfile, *infile;

	outfile = fopen(argv[2], "wb" );

	infile = fopen(argv[1], "rb");

	unsigned char buf[MAXLEN];

	if( outfile == NULL || infile == NULL )

	{

		printf("%s, %s",argv[1],"not exit/n");

		exit(1);

	}   



	int rc;

	while( (rc = fread(buf,sizeof(unsigned char), MAXLEN,infile)) != 0 )

	{

		fwrite( buf, sizeof( unsigned char ), rc, outfile );

	} 

	fclose(infile);

	fclose(outfile);

	system("PAUSE"); 

	return 0;
*/
}
