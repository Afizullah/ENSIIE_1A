#include<math.h>
#include<stdlib.h>
#include<stdio.h>
#define G 6.67e-11
int main(void){
int a;
int b;
int c;
double g = G;

printf("Entrez la masse m1 :\n");
scanf("%d",&a);
printf("Entrez la masse m2:\n");
scanf("%d",&b);
printf("Entrez la distance :\n");
scanf("%d",&c);
double resultat = (g*a*b)/(c*c);
printf("La force de gravité est %le\n",resultat);
return 0;
}
