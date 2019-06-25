# Abstract Composition Engine

  * Author: [Sébastien Mosser](mosser.sebastien@uqam.ca)
  * Status: [![Build Status](https://travis-ci.com/ace-design/ace.svg?branch=master)](https://travis-ci.com/ace-design/ace)

## Technical Environment

  * Java 8
  * Scala 2.11
  * Maven 3.6
  * Neo4J 3.6
  
## How to install the ACE

Simply use Maven to install the engine by invoking the `mvn` command from the root directory.

    lucifer:ace mosser$ mvn install

_The library will soon be available on the OSS Repository, so you'll not have to install it manually in the future._

## Why Java 8?

The ACE has a dependency to Neo4J as underlying graph engine, using a scala version of the Neo4J API. This version relies on Scala 2.11, which is itself not compatible with Java 9+.

### How to manage multiple versions of Java on a Mac?

Homebrew can help you to manage a consistent installation of multiple JDks on the very same system. For example, one can install openJDK using the following cask: 

```
lucifer:ace mosser$ brew tap adoptopenjdk/openjdk
lucifer:ace mosser$ brew cask install adoptopenjdk8
lucifer:ace mosser$ brew cask install adoptopenjdk11
```

Then to easily switch from one JDK to the other, the `jenv` command will help a lot.

```
lucifer:ace mosser$ brew install jenv
lucifer:ace mosser$ echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.bash_profile
lucifer:ace mosser$ echo 'eval "$(jenv init -)"' >> ~/.bash_profile
lucifer:ace mosser$ jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
lucifer:ace mosser$ jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home
lucifer:ace mosser$ jenv enable-plugin export
lucifer:ace mosser$ jenv enable-plugin maven
```

Since the environment is now configured, it is possible to switch from one version of the JDL to the other using the `jenv` command. The `global` argument changes the configuration at the system level, and the `local` one changes the configuration for a given directory.

```
lucifer:tmp mosser$ jenv versions
* system (set by /Users/mosser/.jenv/version)
  1.8
  1.8.0.212
  11.0
  11.0.3
  openjdk64-1.8.0.212
  openjdk64-11.0.3
  
lucifer:tmp mosser$ jenv global 11.0
lucifer:tmp mosser$ mvn --version
Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T15:00:29-04:00)
Maven home: /usr/local/Cellar/maven/3.6.1/libexec
Java version: 11.0.3, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
Default locale: en_CA, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.5", arch: "x86_64", family: "mac"

lucifer:tmp mosser$ jenv global 1.8
lucifer:tmp mosser$ mvn --version
Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T15:00:29-04:00)
Maven home: /usr/local/Cellar/maven/3.6.1/libexec
Java version: 1.8.0_212, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre
Default locale: en_CA, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.5", arch: "x86_64", family: "mac"
lucifer:tmp mosser$ java global 11.0
Error: Could not find or load main class global

lucifer:tmp mosser$ jenv global 11.0
lucifer:tmp mosser$ jenv local 1.8
lucifer:tmp mosser$ mvn --version
Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T15:00:29-04:00)
Maven home: /usr/local/Cellar/maven/3.6.1/libexec
Java version: 1.8.0_212, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre
Default locale: en_CA, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.5", arch: "x86_64", family: "mac"

lucifer:tmp mosser$ cd ..
lucifer:~ mosser$ mvn --version
Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T15:00:29-04:00)
Maven home: /usr/local/Cellar/maven/3.6.1/libexec
Java version: 11.0.3, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
Default locale: en_CA, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.5", arch: "x86_64", family: "mac"
lucifer:~ mosser$ 
```



