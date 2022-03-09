## What is RCCS?
RCCS, or Reversible Calculus of Communicating Systems, is a formal language that describes the interaction of concurrent systems in a reversible paradigm.

### Syntax and Precedence of Operators

We assume that the operators have decreasing binding power, in the following order: \a, a., |, +.
    
So, a.a + b | c \a is to be read as (a.a) + (b | (c\a)) (infix notation) or + . a a | b \a c  (postfix notation).

Of course, parenthesises take precedence over all operators.


Reversible computation is important because //TODO

## Requirements

### Building
`Maven  >=  3.0`
`JDK    >=  17`

### Running
`JRE >= 8`

## Usage

### Build

To build this project, execute `mvn package` in the project root dir. This will create an executable jar file
in `target/`.

### Running
To run the project, use the command `java -jar <filename>`.
