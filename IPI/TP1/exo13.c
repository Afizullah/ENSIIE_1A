#include<math.h>
#include<stdlib.h>
#include<stdio.h>
int* p_zero (){
	int* napoli=calloc(1,sizeof(int));
	return napoli;
}

void libere(int* a){
free(a);

}
int main(){
	int * ptr = p_zero();
	printf("%d\n",*ptr);
	libere(ptr);
	return 0;
}
