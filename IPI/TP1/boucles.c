#include<stdlib.h>
#include<stdio.h>

int main(void){
	int n;
	scanf("%d",&n);
	if(n<=10){
		printf("Le nombre doit être strictement supérieur à 10\n");
		return 0;
	}
	printf("%d\n",n);
	int sum = 0;
	int i;
	for (i=1;i<=n;i++){sum=sum+i;}
	printf("%d\n",sum);
	return 0;
}
