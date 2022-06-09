# Presentation

## What is RCCS?
RCCS, or Reversible Calculus of Communicating Systems, is a formal language that describes the interaction of concurrent systems in a reversible paradigm.

## What is this project?
This project is a collaboration attempt between me and Dr. Clément Aubert (seen in commits) to be the first to implement a reversible formal language.
At its core, this is a parser and discovery tool. The program takes user input in the form of CCS equations (using syntax specified below) and parses, tokenizes, and traverses it at the user's will.
Everything in this program was written from scratch, down to even the string utilities used internally.


### Syntax and Precedence of Operators

This program follows a slightly modified semantic structure based off of CCSK. I've included some notes below.
- By default, all labels are given implicit null processes (a is implied to represent a.0). This is toggleable in the main config
- Restrictions are applied under the following format: `a.P\{a,b,c}`
- We assume that the operators have decreasing binding power, in the following order: \a, a., |, +.
-- This means that `a|b\{a}` will be interpreted as `(a)|(b\{a})`
-- More specifically, a.a + b | c \{a} is to be read as (a.a) + (b | (c\a)) (infix notation) or + . a a | b \a c  (postfix notation).
- Of course, parenthesis take precedence over all operators.
-- Redundant parenthesis are permitted `(((a)|(b)))`

## Requirements

### Building
`Maven  >=  3.0`
`JDK    >=  17`

To upgrade JDK to the latest version on Linux:

```
curl -O https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
tar -xvf jdk-17_linux-x64_bin.tar.gz
sudo mv jdk-17.0.2/ /opt/jdk17.0.2
sudo update-alternatives --install "/usr/bin/java" "java" "/opt/jdk17.0.2/bin/java" 100
sudo update-alternatives --config java
sudo update-alternatives --install "/usr/bin/javac" "javac" "/opt/jdk17.0.2/bin/javac" 100
sudo update-alternatives --config javac
export JAVA_HOME=/opt/jdk17.0.2/
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

### Running
`JRE >= 8`

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

