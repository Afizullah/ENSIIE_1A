(* PROJET IPF A RENDRE POUR LE 03/04/18
RAHMANY AFIZULLAH, GROUPE 4.2
*)

type expr =
| Var of string
| Int of int
| Plus of expr * expr
| Mult of expr * expr;;

(* Question 1)
@type : expr_to_string : expr -> string 
@param :  expr (qui est une expression)
@return : string (= "expr" sous sa forme litteral (i.e. infixe parenthésée 
	           si Plus ou Mult, simple sinon))
@example : let x = Plus (Int 5, Int 6);;
	   let y = expr_to_string x;; 
	   val y : string = "(5+6)"
*)
let rec expr_to_string expr = match expr with
	|Var x -> x
 	|Int y -> string_of_int y
	|Plus (a,b) -> "("^(expr_to_string a)^"+"^(expr_to_string b)^")"
	|Mult (c,d) -> "("^(expr_to_string c)^"*"^(expr_to_string d) ^")"

(* Question 2)
@type : expr -> string list
@param : expr
@return : string list (la liste des variables trouvées dans l'expression)
@example : let x = Mult (Var "h", Int 6);;
	   let y = variables x;;
	   val y : string list = ["h"]
*)
let rec variables expr = match expr with
	|Var x -> [x]
 	|Int y -> []
	|Plus (a,b) -> (variables a)@(variables b) 
	|Mult (c,d) -> (variables c)@(variables d)

(* Question 3)
@type : expr -> int 
@param : expr
@return : int (= expr sous forme simplifié)
@example : simplify (Plus(Int 5, Int 6));;
	  - : int = 11
*)
let rec simplify expr = match expr with
	|Var x -> failwith "cette expression ne peut pas être simplifié" 
	|Int y -> y
	|Plus (a,b) -> (simplify a)+(simplify b) 
	|Mult (c,d) -> (simplify c)*(simplify d) 

(* Question 4)
@type : : expr -> string -> int 
@param : e (= expr), v ( = string, un nom de variable)
@return : int (= nombre d'occurence de la variable v dans l'expression e)
@example :# x;;
	 - : expr = Mult (Var "h", Int 6)
	 # y;;
	 - : string list = ["h"]
	 # nbe_occurences x "h";;
	 - : int = 1
*)
let nbe_occurences e v = 
	 let liste = variables e in  
		let rec nb_occ l e = match l with
			|[] -> 0
			|d::r -> if d=e then 1 + nb_occ r e
					else nb_occ r e
			in nb_occ liste v
(* Question 5)
@type : expr -> string -> string -> expr
@param : e ( = expr), x (= string, nom de variable), v (= expr)
@return : expr (= l'expression e où on l'on a substitué les x en v)
@example : #  let x = Plus (Plus (Var "e", Int 5), Var "k")
	   # substitute x "e" "f";;
           - : expr = Plus (Plus (Var "f", Int 5), Var "k")	    
*)

let rec substitute e x v = match e with
	|Var k -> if (k = x) then Var v else Var k
	|Int y -> Int y
	|Plus (a,b) -> Plus (substitute a x v, substitute b x v)
	|Mult (c,d) -> Mult (substitute c x v, substitute d x v)

(* Question 6)
DANS CES QUESTIONS J'UTILISERAIS DANS MES EXAMPLES LE DEUX VARIABLES SUIVANTES :
	let y = If (Int 0, Plus (Int 5, Int 6), Var "b");; qui sera égale à Var "b"
	let z = If (Var "bleu", Plus (Int 5, Int 6), Var "b") qui sera égale à Plus (Int 5, Int 6) *)
type expr =
| Var of string
| Int of int
| Plus of expr * expr
| Mult of expr * expr
| If of expr * expr * expr ;;

(*
@example : val y = If (Int 0, Plus (Int 5, Int 6), Var "b");;
	   val z : expr = If (Var "bleu", Plus (Int 5, Int 6), Var "b")	   
	   # expr_to_string2 y;;
	   - : string = "b"
	   # expr_to_string2 z;;
	   - : string = "(5+6)"
*)
let rec expr_to_string2 expr = match expr with
	|Var x -> x
 	|Int y -> string_of_int y
	|Plus (a,b) -> "("^(expr_to_string a)^"+"^(expr_to_string b)^")"
	|Mult (c,d) -> "("^(expr_to_string c)^"*"^(expr_to_string d) ^")"
	|If (e,v,f) -> if e = Int 0 then expr_to_string f else expr_to_string v
(* 
@example : # variables y;;
           - : string list = ["b"]
	   # variables z;;
	   - : string list = []
*)

let rec variables expr = match expr with
	|Var x -> [x]
 	|Int y -> []
	|Plus (a,b) -> (variables a)@(variables b) 
	|Mult (c,d) -> (variables c)@(variables d)
	|If (e,v,f) -> if e = Int 0 then variables f else variables v
(* 
@example : 
	# simplify y;;
	Exception:
	Failure "cette expression ne peut pas être simplifié".
           - : string list = ["b"]
	# simplify z;;
	- : int = 11	
*)
let rec simplify expr = match expr with
	|Var x -> failwith "cette expression ne peut pas être simplifié" 
	|Int y -> y
	|Plus (a,b) -> (simplify a)+(simplify b) 
	|Mult (c,d) -> (simplify c)*(simplify d) 
	|If (e,v,f) -> if e = Int 0 then simplify f else simplify v

(* cette fonction utilise simplement la fonction variables qui est déjà adapté donc elle ne nécessite 
pas de modification
@example : # nbe_occurences y "b";;
	   - : int = 1
	   # nbe_occurences z "b";;
	   - : int = 0
*)
let nbe_occurences e v = 
	 let liste = variables e in  
		let rec nb_occ l e = match l with
			|[] -> 0
			|d::r -> if d=e then 1 + nb_occ r e
					else nb_occ r e
			in nb_occ liste v
	
(*	@example : 
	# substitute y "b" "j";;
	- : expr = Var "j"
	# substitute z "b" "j";;
	- : expr = Plus (Int 5, Int 6)
*)
let rec substitute e x v = match e with
	|Var k -> if (k = x) then Var v else Var k
	|Int y -> Int y
	|Plus (a,b) -> Plus (substitute a x v, substitute b x v)
	|Mult (c,d) -> Mult (substitute c x v, substitute d x v)
	|If (e,t,f) -> if e = Int 0 then (substitute f x v) else (substitute t x v)

(* QUESTION 7 :

*)
type expr =
| Var of string
| Int of int
| Plus of expr * expr
| Mult of expr * expr
| If of expr * expr * expr
| Soit of string * expr * expr


-------------------------------------------------------------------------------------------------------
(*	
@type : substitute_val : string * expr -> expr -> expr
@param : un couple (x,v) avec x nom de variable et v l'expression associé et une expression e
@return : l'expression e où les occurences de x ont été remplacé par l'expression v dans e
	  dans le cas d'un Soit (h,i,j) on évalue d'abord j dans (h,i)
	  puis dans (x,v) donc on évalue j dans (x,v) augmenté de (h,i)

@example : # let y = Soit ("x", Int 5, Plus (Var "x", Var "z"));;
   	   val y : expr = Soit ("x", Int 5, Plus (Var "x", Var "z"))
	   # substitute_val ("z", Int 2) y;;
	   - : expr = Plus (Int 5, Int 2)	
*)


let rec substitute_val (x,v) e = match e with
|Var k -> if (k = x) then v else Var k
|Int y -> Int y
|Plus (a,b) -> Plus (substitute_val (x,v) a, substitute_val (x,v) b)
|Mult (c,d) -> Mult (substitute_val (x,v) c, substitute_val (x,v) d)
|If (j,t,f) -> if j = Int 0 then (substitute_val (x,v) f) else (substitute_val (x,v) t)
|Soit (h,i,j) ->  substitute_val (x,v) (substitute_val (h,i) j)

(*	@type : eval_val : expr -> (string * expr) list -> expr
 	@param : une variable e et un environnement E
	@return :l'expression e evaluer dans l'environnement env
 *)
let rec eval_val e env = match env with 
|[]-> e
|a::r -> eval_val (substitute_val a e) r
---------------------------------------------------------------------------------------------------------
(*
@type : substitute_nom : string * expr -> expr -> expr
@param : un couple (x,v) avec x nom de variable et v l'expression associé et une expression e
@return : l'expression e où les occurences de x ont été remplacé par l'expression v dans e
*)
let rec substitute_nom (x,v) e = match e with
|Var k -> if (k = x) then v else Var k
|Int y -> Int y
|Plus (a,b) -> Plus (substitute_nom (x,v) a, substitute_nom (x,v) b)
|Mult (c,d) -> Mult (substitute_nom (x,v) c, substitute_nom (x,v) d)
|If (j,t,f) -> if j = Int 0 then (substitute_nom (x,v) f) else (substitute_nom (x,v) t)
|Soit (h,i,j) ->  failwith "substitution d'un Soit interdit"

(*	@type : eval_nom : expr -> (string * expr) list -> expr
 	@param : une variable e et un environnement E
	@return :l'expression e evaluer dans l'environnement env
*)
let rec eval_nom e env = match env with
|[] -> e
|a::r -> match a with 
	|(m, Soit (v,w,u)) -> eval_nom (substitute_nom (m,u) e) ((v,w)::r)
	| _ -> eval_nom (substitute_val a e) r
