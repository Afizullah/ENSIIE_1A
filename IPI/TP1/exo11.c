#include<math.h>
#include<stdlib.h>
#include<stdio.h>

struct complexe{
int reel;
int ima;
};

typedef struct complexe comp;

int affiche(struct complexe nombre){
	printf("%d + i*%d\n",nombre.reel,nombre.ima);
	return 0;
}

comp addition(comp a, comp b){
	comp c;
	c.reel=a.reel+b.reel;
	c.ima=a.ima+b.ima;
	return c;
}

comp oppose(comp a){
	a.reel = -(a.reel);
	a.ima = -(a.ima);
	return a;
}
comp conjugue(comp a){
	a.ima = -(a.ima);
	return a;
}
comp produit(comp a, comp b){
	comp c;
	c.reel = a.reel*b.reel -(a.ima+b.ima);
	c.ima = a.reel*b.ima + a.ima*b.reel;
	return c;
}
double module (comp c){
	return sqrt(c.reel*c.reel+c.ima*c.ima);
}

double arg (comp c){

return atan2(c.ima,c.reel);
}

int main(void){
	int a; int b;
	printf("Entrez une partie réel :\n");
	scanf("%d",&a);
	printf("Entrez une partie imaginaire:\n");
	scanf("%d",&b);
	struct complexe nb = { .reel=a, .ima=b };
	affiche(oppose(addition(nb,nb)));
return 0;
}
