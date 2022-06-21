This is just for me, so it may not make sense. But it can stay public.

- Explain / add a way to quit the program or to input a different process.
- Add more tests
- GUI?
- Enumeration
- Loading examples
- Unicode symbols
- Asymmetrical branch concurrency tau-checking (will a top level branch match tau from left right to right left?)
- Actionable label caching
- Add a way for normal people to change config options
- Command args, config file, etc
- Fix tests
Ideally, acting should always return a clone.
- Add flag to remove debugging info?

<!-- Should those be issues instead? -->

- The key should *not* be identical to the label. // **FIXED**

\\ Only partially:


- In:

Please input the index of the label you'd like to act on:
2
(a)|('a) -Tau{'a, a}-> ([k2]a)|([k3]'a)
------| Actionable Labels |------
[0] [k2]
------------
([k2]a)|([k3]'a)
Please input the index of the label you'd like to act on:

Normally, k2 = k3, to exhibit that they did synchronized in the past. Somehow you should have access "internally" to that information, since you know that I can bakctrack only on k2, and not on k3 in isolation.

- Document restricition in "Developer slang"? // **FIXED**

- Fix the restriction: if you have ""(a|'a)\{a}"", then you cannot act on a nor 'a, you can only do tau. Restriction on a means "you cannot communicate along channel a", which means "you cannot input or output on a", so both a and 'a should be forbidden: the only possible transition is tau.

- For a process such as "((a)|('a))+((a)|('a))", there are 6 different transitions (first a, first 'a, first tau, second a, second a', second tau), but the program lists only 3.
**FIXED**

\\ Only partially: 
 java -jar target/RCCS-2.6-jar-with-dependencies.jar "((a)|('a))+((a)|('a))"
------| Actionable Labels |------
[0] a
[1] 'a
[2] Tau{'a, a}
[3] a
[4] 'a
[5] Tau{'a, a}
------------
((a)|('a))+((a)|('a))
Please input the index of the label you'd like to act on:
0
((a)|('a))+((a)|('a)) -a-> (([k0]0)|('a)) + [k1]{(a)|('a)}
[0] 'a
[1] [k1]
------------
(([k0]0)|('a)) + [k1]{(a)|('a)}


- ony 1 a should have been acted upon!
- from that state, I should have been able to backtrack on k0 or k1.


- There should be a way of giving arguments to the program to give the strategy (for testing purposes?). So that, e.g. a flag "s" could be given with a series of decisions to do:

java -jar target/RCCS-2.0-jar-with-dependencies.jar "(a|'a)+(a|'a)" -s 0,1 

would mean "do the first, then second possible action", and returns e.g. 
([k1]a|[k2]'a)+(a|'a)

- There is a bug 

    (([a]a)|('a))+((a)|('a))
    Please input the index of the label you'd like to act on:
    1
    [debug] Checking if (([a]a)|('a))+((a)|('a)) can act on 'a
    Could not act on label!

This is not correct: there are two 'a that can be acted upon.
**Fixed in 0721b6024cd208cc754f4eacc481e0c9fa22aed0**

//Fix tauMatch
