The LTS (labelled transition system, or SOS, for structural operational semantics) of CSSK is the set of "reduction rules" describing how the system reacts / evolves.
It is given, for instance, in Forward-Reverse Observational Equivalences in CCSK, page 4 (for the forward-only system):

![Axioms](https://user-images.githubusercontent.com/16466689/213241170-c74a833f-b513-46ad-a751-3febf681308d.png)

For simplification, we have written these rules down in 'ascii' format. They are as follows:
### (TOP):
```
std(X)
----------
a.X -a[m]-> a[m].X
```
Examples:
```
a.P -a[k0]-> [k0].P
a.b.P -a[k0]-> [k0].b.P
```
### (PREFIX)
```
X -b[n]-> X'
----------
a[m].X -b[n]-> a[m].X'
```
Examples:
```
[k0].b.P -b[k1]-> [k0].[k1].P
```
* (CHOICE)
```
X -a[m]-> X'
----------
X + Y -a[m]-> X' + Y

Y -a[m]-> Y'
----------
X + Y -a[m]-> X + Y'
```
Examples:
```
a.P + b.Q -a[k0]-> [k0].P + b.Q
```
### (PAR)
```
X -a[m]-> X'  m not in keys(Y)
----------
X|Y -a[m]-> X'|Y

Y -a[m]-> Y' m not in keys(X)
----------
X|Y -a[m]-> X|Y'
```
Examples:
```
a.P|b.Q -a[k0]-> [k0].P|b.Q
a.P|b.Q -b[k0]-> a.P|[k0].Q
```
* (SYNCH)
```
X -a[m]-> X'  Y -'a[m]-> Y'
----------
X|Y -Tau{a,'a}[m]-> X'|Y'
```
Examples:
```
a.P|'a.Q -Tau{a,'a}[k0]-> [k0].P|[k0].Q
```
* (RES)
```
X -z[m]-> X'
---------- z not in {a, 'a}
X\{a} -z[m]-> X'\{a}
```
Examples:
```
a.P\{b} -a[k0]-> [k0].P
```