# "Generic" Processes

```
((a)|('a))+((a)|('a))  
(a|'a)\{a}  
a|b\{a}  
a.b.c.d | 'a.'b.'c.'d  
a + ('a | 'b) + b  
a + b + c + d

```

# Stuck Processes

The following three processes should be "stuck":

```
(a)\{a}  
(a)\{'a}  
(a.b | 'b.'a)\{a, b}

```

# Process interesting from the perpective of bisimulation

It would be interesting to compare the following two processes:

```
a.a.(b+c)    
a.a.b + a.a.c

```

The following two processes are important: they prove that HPB is different from HHPB.
cf. Robert J. van Glabbeek and Ursula Goltz. Refinement of actions and equivalence notions for
concurrent systems. Acta Informatica, 37(4/5):229–327, 2001. [doi:10.1007/s002360000041](https://doi.org/10.1007/s002360000041).

```
(a | (b+c)) + (a|b) + ((a+c) |b)   
(a | (b + c)) + ((a + c) | b)

```

The following two should be HHPB

```
a.(b + b)  
(a.b) + (a.b)

```

# Example taken from the examples in the LTS / SOS presentation

Those are examples of reduction:

```
a.P -a[k0]-> a[k0].P  
a.b.P -a[k0]-> a[k0].b.P  
a.b.P-a[k0]->a[k0].b.P -b[k1]-> a[k0].b[k1].P  
a.(b.X|Y) -> a[k0].(b.X|Y) -b[k1]-> a[k0].(b[k1].X|Y)  
a.P\{b} -a[k0]-> a[k0].P\{b}  
a.P|b.Q -a[k0]-> a[k0].P|b.Q  
a.P|b.Q -b[k0]-> a.P|b[k0].Q  
a.P + b.Q -a[k0]-> a[k0].P + b.Q  
a.P + b.Q -b[k0]-> a.P + b[k0].Q  
a.P|'a.Q -Tau{a,'a}[k0]-> Tau{a,'a}[k0].P|Tau{a,'a}[k0].Q  

```
