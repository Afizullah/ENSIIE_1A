type expr =
| Var of string
| Int of int
| Plus of expr * expr
| Mult of expr * expr;;

(* Question 1)
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
val nbe_occurences : expr -> string -> int = <fun>
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
(* 
@example : 
	# simplify y;;
	Exception:
	Failure "cette expression ne peut pas \195\170tre simplifi\195\169".
           - : string list = ["b"]
	# simplify z;;
	- : int = 11	
*)
(* cette fonction utilise simplement la fonction variables qui est déjà adapté donc elle ne nécessite pas de modification
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

let rec substitute e x v = match e with
	|Var k -> if (k = x) then Var v else Var k
	|Int y -> Int y
	|Plus (a,b) -> Plus (substitute a x v, substitute b x v)
	|Mult (c,d) -> Mult (substitute c x v, substitute d x v)
	|If (e,t,f) -> if e = Int 0 then (substitute f x v) else (substitute t x v)
	
(*	@example : 
	# substitute y "b" "j";;
	- : expr = Var "j"
	# substitute z "b" "j";;
	- : expr = Plus (Int 5, Int 6)
*)

(* QUESTION 7 :

*)
type expr =
| Var of string
| Int of int
| Plus of expr * expr
| Mult of expr * expr
| If of expr * expr * expr
| Let of string*expr*expr
