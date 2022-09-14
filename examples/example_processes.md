((a)|('a))+((a)|('a))  
(a|'a)\{a}  
a|b\{a}  
a.b.c.d | 'a.'b.'c.'d  
a + ('a | 'b) + b  
a + b + c + d  
<!-- The following three processes should be "stuck" -->
(a)\{a}  
(a)\{'a}  
(a.b | 'b.'a)\{a, b}  
<!-- It would be interesting to compare the following two processes: -->
a.a.(b+c)    
a.a.b + a.a.c  
<!-- The following two processes are important: they prove that HPB is different from HHPB. 
cf. Robert J. van Glabbeek and Ursula Goltz. Refinement of actions and equivalence notions for
concurrent systems. Acta Informatica, 37(4/5):229–327, 2001. doi:10.1007/s002360000041.
-->
(a | (b+c))+(a | b)+((a+c) |b)   
(a | (b + c)) + ((a + c) | b)   
<!-- The following two should be HHPB -->
a.(b + b)  
(a.b) + (a.b)  
