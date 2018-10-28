#include<math.h>
#include<stdlib.h>
#include<stdio.h>

float* tab_log(int n){
	float* tab = malloc(n*sizeof(float));
	int nap; 
	for(nap = 0; nap<n;nap++){
		tab[nap]=log(nap+1);
	}

	return tab;
}
void affiche(float * tab,int n){
	unsigned int i;
	for(i=0;i<n;i++){
		printf("%lf\n",*(tab+i));	
	}
}
int main(){

	float* nap = tab_log(5);
	affiche(nap,5);
	free(nap);
	return 0;
}
