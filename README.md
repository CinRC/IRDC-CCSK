# Presentation

## What is RCCS?
RCCS, or Reversible Calculus of Communicating Systems, is a formal language that describes the interaction of concurrent systems in a reversible paradigm.

### Syntax and Precedence of Operators

We assume that the operators have decreasing binding power, in the following order: \a, a., |, +.
    
So, a.a + b | c \a is to be read as (a.a) + (b | (c\a)) (infix notation) or + . a a | b \a c  (postfix notation).

Of course, parenthesises take precedence over all operators.

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

### Running

Running this is fairly simple. One approach is to load the program into an IDE and run through its compiler.
The main class is `me.gmx.RCCS`. 

Alternatively, you can run the executable jar specified above with the command `java -jar RCCS.jar "`.

