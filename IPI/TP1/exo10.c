#include<math.h>
#include<stdlib.h>
#include<stdio.h>
//fonction qui print un cercle de centre (n,n) et de rayon n.
int cercle(int n){
	int i;
	int j;
	printf("\n");
	//le cercle est inclus dans un carré de taille 2*n+1	
	for(i=0;i<=(2*n+1);i++){
		for(j=0;j<=(2*n+1);j++){
			/*
			si le point de coordonnée (i,j) est à l'intérieur de centre (n,n) et de rayon n, alors il vérifie la condition suivante
			*/
			if(((i-n)*(i-n)+(j-n)*(j-n))<=n*n){ 
			printf("*");}
			else {printf(" ");}
			}
		printf("\n");
	}
return 0;
}

int main(void){
	printf("Entrez un nombre :\n");
	int n;	scanf("%d",&n);
	cercle(n);
return 0;
}
