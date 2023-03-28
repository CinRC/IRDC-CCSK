# Implementation of Reversible Distributed Calculus (CCSK)

## What is RCCS/CCSK?

The Reversible Calculus of Communicating Systems (RCCS) and the Calculus of Communicating Systems with Keys (CCSK) are
two formal languages that describes the interaction of concurrent systems in a reversible paradigm.
They are both described in **Static versus dynamic reversibility in
CCS** ([doi:10.1007/s00236-019-00346-6](https://doi.org/10.1007/s00236-019-00346-6)), and shown to be equivalent (in
terms of labelled transition system isomorphism) in the same paper.

## What is this project?

This project is the first to publicly available implementation a functional formal language (in this case, inspired by
CCSK) that models concurrent reversible systems.
At its core, this is a parser and evaluation tool.
The program takes user input in the form of CCS processes (using the syntax [specified below](#syntax)) and parses,
tokenizes, and traverses it at the user's will (using CSSK's labelled transition system, described and exemplified
in [this documentation](docs/lts.md)).

Everything in this program is original, including string traversal libraries and GUI.
It is currently developed in the [School of Computer and Cyber Sciences](https://www.augusta.edu/ccs/), primarily
by [Peter Browning](https://petech.me/) and Dr.[Clément Aubert](https://spots.augusta.edu/caubert/).
Please, refer to [our list of contributors](https://github.com/CinRC/IRDC-CCSK/graphs/contributors) for an up-to-date
list of contributors.

## Getting Started

### Use-Only

You will need the Java Runtime Environment (JRE) (≥8) to execute this program.
Download the .jar file in our [latest release](https://github.com/CinRC/IRDC-CCSK/releases/latest), potentially using
this simple one-liner[^1]:
[^1]: Inspired by <https://gist.github.com/steinwaywhw/a4cd19cda655b8249d908261a62687f8>.

```
curl -s https://api.github.com/repos/CinRC/IRDC-CCSK/releases/latest \
| grep browser_download_url \
| cut -d : -f 2,3 \
| tr -d \" \
| wget -qi -
```

Then execute e.g., the process $((a+b) | \overline{b}) | \overline{a}) \backslash a$ using

```
java -jar IRDC-*.jar "(((a+b) |'b)|'a)\{a}"
```

To run with a GUI, use the `--gui` flag. Else, a command-line-interface will open instead, you can use it with:

```
java -jar IRDC-*.jar <FLAGS> "[Process]"
```

Some examples of processes are indicated in [`docs/example_processes.md`](docs/example_processes.md) if you need inspiration.


The flags are [documented below](#command-arguments-flags).

### Building

You will need [Maven](https://maven.apache.org/) (≥3.0) and the Java Development Kit (≥17) to compile this program.
Some of the possible phases are:

- `mvn package` to build from source (the executable will be in the `target/` folder),
- `mvn validate` to test for checkstyle violations (we use [google_checks.xml](google_checks.xml)),
- `mvn test` to run our pre-written unit tests (gathered in the [`src/test/java`](src/test/java) folder) that are
  designed to represent difficult a diverse range of different scenarios.

To run e.g., all the test methods whose name starts with `simulationIsStructural` in the `tests.SimulationTest` class,
use

```
mvn -Dtest="tests.SimulationTest#simulationIsStructural*" test
```

### Contributing

We are thrilled that you consider contributing to our project.
Please refer to our [contributing](CONTRIBUTING.md) guidelines.

## Additional Information

### Developer slang

Some parts of this program are named different from the convention. Some of the notable ones are are listed below:

1. Channel names are referred to as 'Labels'
2. Parallel operators (`|`) are referred to as 'Concurrent Processes'
3. Deterministic operators (`+`) are referred to as 'Summation Processes'
4. CCSK keys (`a[k0].P`) are referred to as 'Label Keys'

### Syntax and Precedence of Operators

This program follows a slightly modified semantic structure based off of CCSK. Some notes are included below.

- By default, all labels are given implicit null processes (`a` is implied to represent `a.0`). This is toggleable using [the `--require-explicit-null` flag](#command-arguments-flags).
- Channel labels are limited to lowercase english letters `[a-z]`.
- Complement channels are represented by an apostrophe before the label, `'a`, `'b`, etc.
- Process names are limited to uppercase english letters `[A-Z]`.
- We assume that all channels sharing the same label are complements. This means that `'a` is the complement to `a`, `'b` to `b`, and so forth.

- Restrictions are applied under the following format: `a.P\{a,b,c}`.
- We assume that the operators have decreasing binding power, in the following order: `\a`, `a.`, `|`, `+`.
    - This means that `a|b\{a}` will be interpreted as `(a)|(b\{a})`.
    - More specifically, `a.a + b | c \{a}` is to be read as `(a.a) + (b | (c\a))` (infix notation)
      or `+ . a a | b \a c`  (postfix notation).
    - Of course, parenthesis take precedence over all operators.
    - Redundant parenthesis are permitted, e.g., `(((a)|(b)))` will be accepted but interpreted as `(a|b)`.

### Command arguments (flags)

This program can be configured by using command-line arguments, or flags. The flags are as follows:

| **Flag**    	                       | **Description**                                                                                            	                                                                                                                                                                                                                        |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| --debug     	                       | Enables debug mode. Will print info to stdout                                                              	                                                                                                                                                                                                                        |
| --help      	                       | Prints help message documenting flags                                                                      	                                                                                                                                                                                                                        |
| --dL        	                       | Labels are visibly differentiated by integers                                                              	                                                                                                                                                                                                                        |
| --hide-keys 	                       | CCSK keys are hidden                                                                                       	                                                                                                                                                                                                                        |
| --sA        	                       | Alternative display mode for summation processes. Reversible summations are not annotated                  	                                                                                                                                                                                                                        |
| --process-names-equivalent        	 | Processes names will be treated as being equivalent (P == Q)                  	                                                                                                                                                                                                                                                     |
| --sC        	                       | Alternative display mode for summation processes. Reversible summations are hidden after execution         	                                                                                                                                                                                                                        |
| --require-explicit-null        	    | Labels explicitly require a trailing process. Labels will no longer have an implicit null process attached 	                                                                                                                                                                                                                        |
| --hP        	                       | Parenthesis surrounding complex processes will be omitted                                                  	                                                                                                                                                                                                                        |
| --dN        	                       | Null processes will be displayed explicitly                                                                	                                                                                                                                                                                                                        |
| --iU        	                       | Parser will ignore unrecognized characters in the process formula                                          	                                                                                                                                                                                                                        |
| --kL        	                       | Keys will be visibly similar to the label they represent                                                   	                                                                                                                                                                                                                        |
| --gui       	                       | Program will start with a GUI instead of CLI                                                               	                                                                                                                                                                                                                        |
| --interactive  	                    | Run the program in interactive mode. In interactive mode, the given process will be displayed and the user will be prompted to give a label or key to act on.                                                                   	                                                                                                   |
| --enumerate  	                      | Run the program in enumeration mode (Default). In enumeration mode, the given process will be enumerated to completion, and the transition tree will be printed to stdout                                                                    	                                                                                      |
| --validate  	                       | Run the program in validation mode. In validation mode, a user inputted file will be scanned for processes to parse. Processes in the file must be separated by newlines, with one process per line. Each process will be validated for syntax and formatting.                                                                    	 |
| --equivalence  	                    | Run the program in equivalence mode. In equivalence mode, your input will be in the form of a *list* of processes separated by commas (,). All equivalence relationships between the given processes will be printed.                                                                    	                                          |

## Contributing

If you'd like to contribute to this project, please refer to the CONTRIBUTING.md file

### Versioning

When contributing, the project version must be appropriately incremented inside the `pom.xml` file. We use a semantic
Maj.Min.Patch.Rev system, where:

* Maj = Major feature or system overhaul
* Min = Feature addition
* Patch = Bug fix
* Rev = Revision (code cleanup, minor edits)

When making changes, increment the version number according to changes made.
## Alternatives

This project, in particular, is a working implementation of the _forward-only_ Calculus of Communicating Systems (CSS).
Some of the other implementations of [process algebras](https://en.wikipedia.org/wiki/Process_calculus) that are
publicly available are:

- This [student project](https://github.com/ComputerScience-Projects/Calculus-of-Communicating-Systems),
- The [Concurrency Workbench, Aalborg Edition](http://caal.cs.aau.dk/),
- This [implementation of HOcore](https://people.rennes.inria.fr/Alan.Schmitt/research/hocore/), an intermediate
  languages between CSS and the π-calculus.
- [muccs](https://github.com/andreasimonetto/muccs), an implementation of CCS in Prolog with some nice examples.
- [CCS_Prolog](https://github.com/CoffeeStraw/CCS_Prolog), an interpreter for CCS language written in SWI-Prolog.

The implementation of CSSk
described [in this master thesis](https://leicester.figshare.com/articles/thesis/SimCCSK_simulation_of_the_reversible_process_calculi_CCSK/10091681)
is to our knowledge not publicly available.
