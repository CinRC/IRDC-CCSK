# Presentation

## What is RCCS?
RCCS, or Reversible Calculus of Communicating Systems, is a formal language that describes the interaction of concurrent systems in a reversible paradigm.

<<<<<<< HEAD
## Why is it important?
=======
### Syntax and Precedence of Operators

We assume that the operators have decreasing binding power, in the following order: \a, a., |, +.
    
So, a.a + b | c \a is to be read as (a.a) + (b | (c\a)) (infix notation) or + . a a | b \a c  (postfix notation).

Of course, parenthesises take precedence over all operators.

>>>>>>> 542ce1dd724e4aac02b04d711ef797ed08dee56d

Rversible computation is important because 
//TODO

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
<<<<<<< HEAD
Running this is fairly simple. One approach is to load the program into an IDE and run through its compiler.
The main class is `me.gmx.RCCS`. 

Alternatively, you can run the executable jar specified above with the command `java -jar RCCS.jar "`.
=======
To run the project, use the command `java -jar <filename>`.
>>>>>>> 542ce1dd724e4aac02b04d711ef797ed08dee56d
