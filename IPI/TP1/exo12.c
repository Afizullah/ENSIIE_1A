#include<math.h>
#include<stdlib.h>
#include<stdio.h>
int permute (int* a, int* b, int* c){
	int d = *a;
	*a = *b;
	*c = *b;
	*b = d ;
	return 0;
}
int main(){
	int a; printf("Entrez un 1er nombre :\n"); scanf("%d",&a);
	int b; printf("Entrez un 1er nombre :\n"); scanf("%d",&b);
	int c; printf("Entrez un 1er nombre :\n"); scanf("%d",&c);
	permute (&a, &b, &c);
	printf("%d,%d,%d\n",a,b,c);
}
