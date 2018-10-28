struct Node;
struct Node* arbre_vide(void);
int est_vide(struct Node*);
struct Node* concat_arbre(int, struct Node*, struct Node*);
unsigned long cardinal(struct Node*);
unsigned long hauteur(struct Node*);
int recherche(int, struct Node*);
void clear(struct Node*);



