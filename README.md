# IRDC-CCSK

Implementation of Reversible Distributed Calculus (CCSK)

# Presentation

## What is RCCS/CCSK?

RCCS, or Reversible Calculus of Communicating Systems, and CCSK, or Calculus of Communicating Systems with Keys, are formal language that describes the interaction of concurrent systems in a reversible paradigm.

## What is this project?

This project is a collaboration attempt between [Peter Browning](https://peterjbrowning.com/) and Dr. [Clément Aubert](https://spots.augusta.edu/caubert/) to be the first to implement a reversible formal language.
At its core, this is a parser and discovery tool. The program takes user input in the form of CCS equations (using syntax specified below) and parses, tokenizes, and traverses it at the user's will.
Everything in this program was written from scratch, down to even the string utilities used internally.


### Developer slang

During the design of this program, I was by no means an expert in CCS or CCSK. Thus, my understanding was limited.
<!-- I think you should remove this "warning" and simply explain that you named things a particular way. Also, remove all the "I", to make it more generic. -->
Because of this, I refer to things internally slightly differently than an expert may have. I have listed some of the relevant mappings below:

1. Channel names are referred to as 'Labels'
2. Parallel operators (`|`) are referred to as 'Concurrent Processes'
3. Deterministic operators (`+`) are referred to as 'Summation Processes'
4. Label prefixings (`a.b.P`) are referred to as 'Action Prefix Processes'
5. CCSK keys (`[a].b.P`) are referred to as Label Keys

### Syntax and Precedence of Operators

This program follows a slightly modified semantic structure based off of CCSK. I've included some notes below.
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


## Requirements

### Building
`Maven  >=  3.0`
`JDK    >=  17`


Please, use 

```
/opt/apache-maven-3.8.4/bin/mvn package
```

to build the project.

<!--
Mention  -DskipTests if we keep having tests that fail? 
-->


<!--

This should probably either be in a different file, or a simple link, but not in the "landing" readme.

To upgrade JDK to the latest version on Linux:

```
curl -O https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
tar -xvf jdk-17_linux-x64_bin.tar.gz
sudo mv jdk-17.0.4/ /opt/jdk17.0.4
sudo update-alternatives --install "/usr/bin/java" "java" "/opt/jdk17.0.4/bin/java" 100
sudo update-alternatives --config java
sudo update-alternatives --install "/usr/bin/javac" "javac" "/opt/jdk17.0.4/bin/javac" 100
sudo update-alternatives --config javac
export JAVA_HOME=/opt/jdk17.0.4/
```

To upgrade Maven to the latest version on Linux (courtesy of https://stackoverflow.com/a/71199477/)

```
wget https://apache.org/dist/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz -P /tmp
sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt
rm /tmp/apache-maven-*-bin.tar.gz
```

then use

```
/opt/apache-maven-3.8.5/bin/mvn
```

or link it.

-->

### Running

`JRE >= 8`

Please use e.g. 

```
java -jar target/RCCS-2.0-jar-with-dependencies.jar "a|b"
```

<!--
We should probably *not* have the version number in the .jar, since that makes this commeand 

then select the possible labels to act on by their number.



## Usage

### Build

To build this project, execute `mvn package` in the project root dir. This will create an executable jar file
in `target/`.

### Testing

To run unit tests, execute `mvn test` in the project root dir. It will run through a set of pre-written unit tests that are designed to represent difficult examples or niche scenarios.

### Running

Running this is fairly simple. One approach is to load the program into an IDE and run through its compiler.
The main class is `me.gmx.RCCS`. 

Alternatively, you can run the jar file created from the **building** section above with the command `java -jar target/RCCS-<version>.jar`.

