#include <stdio.h> //标准输入输入出的头文件,printf和scanf都在这里了
#include <stdlib.h>  //标准库头文件，内存分配、数学运算等都在这里了

#define MAXLEN 1024
int main(int argc, char *argv[])
{
	if( argc < 3 )
	{
		printf("usage: %s %s/n", argv[0], "infile outfile");
		exit(1);
	}
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
	while( (rc = fread(buf,sizeof(unsigned char),MAXLEN,infile)) != 0 )
	{
		fwrite( buf, sizeof( unsigned char ), rc, outfile );
	}
	fclose(infile);
	fclose(outfile);

	return 0;
}
