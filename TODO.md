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

- Document restricition in "Developer slang"? // **FIXED**

- Fix the restriction: if you have ""(a|'a)\{a}"", then you cannot act on a nor 'a, you can only do tau. Restriction on a means "you cannot communicate along channel a", which means "you cannot input or output on a", so both a and 'a should be forbidden: the only possible transition is tau.

- For a process such as "((a)|('a))+((a)|('a))", there are 6 different transitions (first a, first 'a, first tau, second a, second a', second tau), but the program lists only 3.
**FIXED**
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
