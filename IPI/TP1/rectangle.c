#include<stdlib.h>
#include<stdio.h>

int main(void){

	int n;
	scanf("%d",&n);
	int i;
	int j;

	for (i=1;i<=n;i++) {printf("*");} printf("\n");
	
	for (i=2;i<=n-1;i++){
		for (j=1;j<=n;j++){	
			if (j==1||j==n)
				printf("*");
			else printf(" ");
		}
		printf("\n");
	}
	
	for (i=1;i<=n;i++){printf("*");} printf("\n");

	return 0;
}
