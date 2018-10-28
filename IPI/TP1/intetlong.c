#include<math.h>
#include<stdlib.h>
#include<stdio.h>

int main(void){
	int i = 1;
	int a = 1;
	int c = 1;
	while(a>0){
	c=a;
	a=2*a;
	i++;
	}
	
	printf("2 puissance i vaut %d\n",c);
	printf("i vaut %d\n",i-2);
	
	int j = 1;
	long b = 1;
	while(2*b>0){
	b=2*b;
	j++;
	}
	printf("j vaut %d\n",j-2);
	printf("2 puissance j vaut %ld\n",b);
	return 0;
}
