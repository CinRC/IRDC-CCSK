# IRDC-CCSK

Implementation of Reversible Distributed Calculus (CCSK)


## What is RCCS/CCSK?

RCCS, or Reversible Calculus of Communicating Systems, and CCSK, or Calculus of Communicating Systems with Keys, are formal language that describes the interaction of concurrent systems in a reversible paradigm.

## What is this project?

This project is a collaboration attempt between [Peter Browning](https://peterjbrowning.com/) and Dr. [Clément Aubert](https://spots.augusta.edu/caubert/) to be the first to implement a functional formal language (in this case, CCSK-ish) that models concurrent reversible systems.
At its core, this is a parser and evaluation tool. The program takes user input in the form of CCS equations (using syntax specified below) and parses, tokenizes, and traverses it at the user's will.
Everything in this program is original, including string traversal libraries and GUI.


### Developer slang

Some parts of this program are named different from the convention. Some of the notable ones are are listed below:

1. Channel names are referred to as 'Labels'
2. Parallel operators (`|`) are referred to as 'Concurrent Processes'
3. Deterministic operators (`+`) are referred to as 'Summation Processes'
4. Label prefixings (`a.b.P`) are referred to as 'Action Prefix Processes'
5. CCSK keys (`[a].b.P`) are referred to as Label Keys

### Syntax and Precedence of Operators

This program follows a slightly modified semantic structure based off of CCSK. Some notes are included below.
- By default, all labels are given implicit null processes (`a` is implied to represent `a.0`). This is toggleable in the main config
- Channel labels are limited to lowercase english letters [a-z]
- Complement channels are represented by an apostrophe before the label, `'a`, `'b`, etc
- Process names are limited to uppercase english letters [A-Z]
- We assume that all channels sharing the same label are duplicates. This means that `'a` is the complement to `a`, `'b` to `b`, and so forth
- Restrictions are applied under the following format: `a.P\{a,b,c}`
- We assume that the operators have decreasing binding power, in the following order: \a, a., |, +.
  - This means that `a|b\{a}` will be interpreted as `(a)|(b\{a})`
  - More specifically, `a.a + b | c \{a}` is to be read as `(a.a) + (b | (c\a))` (infix notation) or `+ . a a | b \a c`  (postfix notation).
- Of course, parenthesis take precedence over all operators.
  - Redundant parenthesis are permitted `(((a)|(b)))`

## Command arguments (flags)

This program can be configured by using command-line arguments, or flags. The flags are as follows:

| **Flag**    	 | **Description**                                                                                            	 |
|---------------|--------------------------------------------------------------------------------------------------------------|
| --debug     	 | Enables debug mode. Will print info to stdout                                                              	 |
| --help      	 | Prints help message documenting flags                                                                      	 |
| --uC        	 | (Currently broken, do not use)                                                                             	 |
| --dL        	 | Labels are visibly differentiated by integers                                                              	 |
| --hide-keys 	 | CCSK keys are hidden                                                                                       	 |
| --kM        	 | (Currently broken, do not use)                                                                             	 |
| --sA        	 | Alternative display mode for summation processes. Reversible summations are not annotated                  	 |
| --sC        	 | Alternative display mode for summation processes. Reversible summations are hidden after execution         	 |
| --eN        	 | Labels explicitly require a trailing process. Labels will no longer have an implicit null process attached 	 |
| --hP        	 | Parenthesis surrounding complex processes will be omitted                                                  	 |
| --dN        	 | Null processes will be displayed explicitly                                                                	 |
| --iU        	 | Parser will ignore unrecognized characters in the process formula                                          	 |
| --kL        	 | Keys will be visibly similar to the label they represent                                                   	 |
| --gui       	 | Program will start with a GUI instead of CLI                                                               	 |
| --enumerate  	 | Print enumeration tree of given process                                                                    	 |


## Requirements

### Building
`Maven  >=  3.0`
`JDK    >=  17`

Building from source can be done with the `mvn package` goal.

### Running

`JRE >= 8`

To run with a GUI, use the `--gui` flag. Else, a CLI will open instead.

```
java -jar target/[Jar file].jar <FLAGS> "[Process]"
```

### Testing

To run unit tests, execute `mvn test` in the project root dir.
It will run through a set of pre-written unit tests (gathered in the [`src/test/java`](src/test/java) folder) that are designed to represent difficult a diverse range of different scenarios.
To run e.g., all the test methods whose name starts with `simulationIsStructural` in the `SimulationTest` class, use

```
mvn -Dtest="SimulationTest#simulationIsStructural*" test
```

## Contributing

If you'd like to contribute to this project, we encourage you to make a fork of the project and work locally. PRs will be reviewed by Dr. Aubert or Mr. Browning and merged into the main branch.

### Versioning

When contributing, the project version must be appropriately incremented inside the `pom.xml` file. We use a semantic Maj.Min.Patch.Rev system, where:
* Maj     = Major feature or system overhaul
* Min     = Feature addition
* Patch   = Bug fix 
* Rev     = Revision (code cleanup, minor edits)

When making changes, increment the version number according to changes made.



## Alternatives

This project, in particular, is a working implementation of the _forward-only_ Calculus of Communicating Systems (CSS).
Some of the other implementations of [process algebras](https://en.wikipedia.org/wiki/Process_calculus) that are publicly available are:

- This [student project](https://github.com/ComputerScience-Projects/Calculus-of-Communicating-Systems),
- The [Concurrency Workbench, Aalborg Edition](http://caal.cs.aau.dk/),
- This [implementation of HOcore](https://people.rennes.inria.fr/Alan.Schmitt/research/hocore/), an intermediate languages between CSS and the π-calculus.

The implementation of CSSk described [in this master thesis](https://leicester.figshare.com/articles/thesis/SimCCSK_simulation_of_the_reversible_process_calculi_CCSK/10091681) is to our knowledge not publicly available.
