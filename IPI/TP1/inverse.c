#include<stdlib.h>
#include<stdio.h>
#include<math.h>
int main (int argc,char* argv[]){
	if(argc!=2){
		printf("On vous demande d'entrer un entier svp");
	}
	long a = strtol(argv[1],NULL,10);
	double d = 1/(double)a;
	if(a>1){
		printf("%ld, %lf\n",a,d);
	}
return 0;
}
