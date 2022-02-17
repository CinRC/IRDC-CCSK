# Presentation

## What is RCCS?
RCCS, or Reversible Calculus of Communicating Systems, is a formal language that describes the interaction of concurrent systems in a reversible paradigm.

## Why is it important?

Rversible computation is important because 
//TODO

# Requirements

<!-- Please, list the requirements for your project, even briefly. Just to make sure I match them. Do I need anything beside Maven? 

I have:

mvn -version
Apache Maven 3.6.0
Maven home: /usr/share/maven
Java version: 11.0.14, vendor: Debian, runtime: /usr/lib/jvm/java-11-openjdk-amd64
Default locale: fr_FR, platform encoding: UTF-8
OS name: "linux", version: "4.19.0-18-amd64", arch: "amd64", family: "unix"
--> 

# Compiling and Executing

<!--

I get an error when trying to compile your project:

mvn compile
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.google.inject.internal.cglib.core.$ReflectUtils$1 (file:/usr/share/maven/lib/guice.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of com.google.inject.internal.cglib.core.$ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------------------------< me.gmx:RCCS >-----------------------------
[INFO] Building RCCS 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ RCCS ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /home/au/travail/git/RCCS_Impl/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ RCCS ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 6 source files to /home/au/travail/git/RCCS_Impl/target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.839 s
[INFO] Finished at: 2022-02-16T22:55:33-05:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:compile (default-compile) on project RCCS: Fatal error compiling: error: invalid target release: 17 -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException


The warnings seem to be customary (cf. https://github.com/google/guice/issues/1133 ), but I'm not sure I can debug the errors.
-->
