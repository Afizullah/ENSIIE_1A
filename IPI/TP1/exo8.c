#include<math.h>
#include<stdlib.h>
#include<stdio.h>

int main(void){
	/*la base du triangle est 2*n-1*/
	printf("Entrez un nombre :\n");
	int n;	scanf("%d",&n);
	int i; /*indice de ligne*/
	int j; /*indice de colonne*/
	int k=2;/* abscisse de la première étoile*/

	//affichage de la première ligne.
	for(j=1;j<=2*n-1;j++){printf("*");}
	printf("\n");

	//affichage du reste du triangle.
	for(i=2;i<=n;i++){
		for(j=1;j<=2*n-1;j++){
			if(j==k || j==2*n-k) {
				printf("*");
			}
			else printf(" ");
		}
		k++;
		printf("\n");
	}

	return 0;
}
