import math
from random import randint
import scipy.stats
import numpy
import matplotlib.pyplot as plt
import scipy.special


# n = instant à laquelle on choisit d'exercer ou non notre option
# h , b = valeur de gain et perte relative possible entre 2 intervalles de temps
# r = valeur de rendement de l'actif sans risque
# f = fonction qui nous donne le gain lié à la vente de l'action
# q = probabilité risque neutre

# Question 3
def price1(n, r, s, h, b, f):
    x = 0
    q = (r - b) / (h - b)
    for k in range(n + 1):
        x += f(s * (1 + h) ** k * (1 + b) ** (n - k)) * (scipy.special.binom(n,k)) * (q ** k) * ((1 - q) ** (n - k))
    return x / ((1 + r) ** n)


def f_price1(x):
    return max(x - 100, 0)


# Question 4
print("\nQ4 : le pricer 1 donne P(n) =", price1(10, 0.02, 100, 0.05, -0.05, f_price1), "\n")

#Réponse: P(n) = 18.568659118212537

# Question 5
# il y'a 2**n différent scénarios possible, il est plus économe d'écrire une fonction recursive
# que d'alloue une matrice de dimension 2**n ou d'utiliser les coefficient binomiaux

# fonction récursive permettant d'éviter d'utiliser les coefficient binomiaux

def v(n, r, s, h, b, f, t):
    q = (r - b) / (h - b)
    if (t == n - 1):
        # cas terminale on arrive à l'instant N
        return (1 / (1 + r)) * ((q * f(s * (1 + h))) + ((1 - q) * f(s * (1 + b))))
    else:
        # cas intermédiaire, v(t) = 1/(1+r) *  (q*v_haute(t+1) + (1-q)*v_basse(t+1))
        # avec T0 <= t < N
        return (1 / (1 + r)) * (
            (q * v(n, r, s * (1 + h), h, b, f, t + 1)) + ((1 - q) * v(n, r, s * (1 + b), h, b, f, t + 1)))


# fonction du pricer2
def f_price2(x):
    return max(x - 95, 0)


# Question 6

# price2
def price2(n, r, s, h, b, f):
    # il s'agit en fait de v(0)
    return v(n, r, s, h, b, f, 0)


# liste des prix à l'instant N
def prix_initiale(n, s, h, b, f):
    L = []
    for k in range(n + 1):
        L.append(f(s * ((1 + h) ** (n - k)) * ((1 + b) ** k)))
    return L


prix = prix_initiale(n=4, b=-0.05, s=100, h=0.05, f=f_price2)

print("Q6 : le pricer 2 donne : P(n) = ", price2(f=f_price2, s=100, r=0.02, h=0.05, b=-0.05, n=3), "\n")
print("v(3) hhh = ", prix[0], "\n")
print("v(3) hhb = ", prix[1], "\n")
print("v(3) hbb = ", prix[2], "\n")
print("v(3) bbb = ", prix[3], "\n")
print("v(2) hh  = ", v(f=f_price2, s=100 * 1.05 * 1.05, r=0.02, h=0.05, b=-0.05, n=3, t=2), "\n")
print("v(2) hb  = ", v(f=f_price2, s=100 * 1.05 * 0.95, r=0.02, h=0.05, b=-0.05, n=3, t=2), "\n")
print("v(2) bb  = ", v(f=f_price2, s=100 * 0.95 * 0.95, r=0.02, h=0.05, b=-0.05, n=3, t=2), "\n")
print("v(1) h   = ", v(f=f_price2, s=100 * 1.05, r=0.02, h=0.05, b=-0.05, n=3, t=1), "\n")
print("v(1) b   = ", v(f=f_price2, s=100 * 0.95, r=0.02, h=0.05, b=-0.05, n=3, t=1), "\n")

#Réponse: P(n) =  10.757339748663794

# Question 7
N = numpy.random.randint(5, 15)

print("N = ", N, "\n")

print("le pricer 2 donne :", price2(f=f_price1, s=100, r=0.03, h=0.05, b=-0.05, n=N), "\n")
print("\nle pricer 1 donne ", price1(n=N, r=0.03, s=100, h=0.05, b=-0.05, f=f_price1), "\n")

#le pricer 2 donne : 14.067068522900382
#le pricer 1 donne  14.067068522900389

# Question 9
# On réutilise v pour les fonction alpha et beta
def alpha(n_a, s_a, r_a, h_a, b_a, f_a, t_a):
    # cas terminale
    if t_a == n_a - 1:
        return (f_a((1 + h_a) * s_a) - f_a((1 + b_a) * s_a)) / ((h_a - b_a) * s_a)
    # cas intermédiare
    return (v(n=n_a, r=r_a, s=((1 + h_a) * s_a), h=h_a, b=b_a, f=f_a, t=t_a + 1) - v(n=n_a, r=r_a, s=((1 + b_a) * s_a),
                                                                                     h=h_a, b=b_a, f=f_a,
                                                                                     t=t_a + 1)) / ((h_a - b_a) * s_a)


def beta(n, s, r, h, b, f, t):
    # cas terminale
    if (t == n - 1):
        return (f((1 + b) * s) * (h + 1) - f((1 + h) * s) * (b + 1)) / ((h - b) * (1 + r) ** (t + 1))
    # cas intermédiaire
    return ((1 + h) * v(n, r, (1 + b) * s, h, b, f, t + 1) - (1 + b) * v(n, r, (1 + h) * s, h, b, f, t + 1)) / (
        (h - b) * (1 + r) ** (t + 1))


print("La couverture adéquate à la date 0 est :\nalpha = ",
      alpha(t_a=0, n_a=2, s_a=100, r_a=0.03, h_a=0.05, b_a=-0.05, f_a=f_price1))
print("beta = ", beta(2, 100, 0.03, 0.05, -0.05, f_price1, 0), "\n")
print("La couverture adéquate à la date 1 dans le cas d'un premier succès est :\nalpha = ",
      alpha(t_a=1, n_a=2, s_a=1.05 * 100, r_a=0.03, h_a=0.05, b_a=-0.05, f_a=f_price1))
print("beta = ", beta(2, 1.05 * 100, .03, 0.05, -0.05, f_price1, 1), "\n")
print("La couverture adéquate à la date 1 dans le cas d'une baisse est :\nalpha = ",
      alpha(t_a=1, n_a=2, s_a=0.95 * 100, r_a=0.03, h_a=0.05, b_a=-0.05, f_a=f_price1))
print("beta = ", beta(2, 0.95 * 100, .03, 0.05, -0.05, f_price1, 1), "\n")

'''
La couverture adéquate à la date 0 est :
    alpha =  0.7961165048543688
    beta =  -73.42822132151944
    
La couverture adéquate à la date 1 dans le cas d'un premier succès est :
    alpha =  0.9761904761904762
    beta =  -91.78527665189932
    
La couverture adéquate à la date 1 dans le cas d'une baisse est :
    alpha =  0.0
    beta =  0.0
'''

# Question 12

def f_price3(x):
    return max(100 - x, 0)


def price3(n, s, r, sigma, T, f):
    somme = 0
    for k in range(1, n + 1):
        somme += math.exp(-r * T) * f(
            s * math.exp((r - sigma * sigma / 2) * T + sigma * math.sqrt(T) * numpy.random.normal(0, 1, None)))
    return somme / n


t = numpy.arange(1.0 * 10 ** 5, 11.0 * 10 ** 5, 1.0 * 10 ** 5)
L_price3 = []
for x in t:
    L_price3.append(price3(r=0.03, sigma=0.1, s=100, T=1, f=f_price3, n=int(x)))

print(L_price3)


# plt.plot(t, L_price3)
# plt.show()


# Question 15

def F(x):
    return scipy.stats.norm.cdf(x, 0, 1)


def put(s, r, sigma, T, K):
    d = (1 / (sigma * math.sqrt(T))) * (math.log(s / K) + (r + sigma * sigma / 2) * T)
    return -s * F(-d) + K * math.exp(-r * T) * F(-d + sigma * math.sqrt(T))


# Question 16
print("La fonction put renvoit pour cette exemple : ", put(s=100, r=0.04, sigma=0.1, T=1, K=100), "\n")
#Réponse: put = 2.257405468637131

# Question 17

L_put = []
rep_17 = put(s=100, r=0.03, sigma=0.1, T=1, K=100)
for x in t:
    L_put.append(rep_17)

# plt.plot(t, L_price3,'b-',t,L_put,'r-')
# plt.show()

# Question 19
def pricer1_bis(s, sigma, r, T, N, f):
    return price1(N, r * T / N, 100, (1 + r * T / N) * math.exp(sigma * math.sqrt(T / N)) - 1,
                  (1 + r * T / N) * math.exp(-sigma * math.sqrt(T / N)) - 1, f=f_price3)
#price1(n, r, s, h, b, f)

L_price1 = []
tab = numpy.arange(1.0 * 10,1.0*1010 , 10.0)
L_put_bis=[]
rep_19 = put(s=100, r=0.04, sigma=0.2, T=1, K=100)

for x in tab :
    L_put_bis.append(rep_19)

for x in tab:
    L_price1.append(pricer1_bis(s=100, sigma=0.2, r=0.04, T=1, f=f_price3, N=int(x)))

plt.plot(tab, L_price1, 'g-', tab, L_put_bis, 'r-')
plt.show()

#les graphiques obtenu dans les questions restantes ont été obtenu avec Scilab
