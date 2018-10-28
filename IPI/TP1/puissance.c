#include<math.h>
#include<stdlib.h>
#include<stdio.h>

int main(void){
	int i=1;
	while(pow(i,3)<1548){
		i++;
	}
	i--;
	printf("%d\n",i);
	return 0;
}
