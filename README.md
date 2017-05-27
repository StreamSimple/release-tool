## Building

### Pre-requisites

   1 Install java 8
   1 Install mvn
   1 Set the environment variable <b>JAVA8_HOME</b> to point to the home directory of your java 8 installation

### Build

Use the dvn script in the project to do builds. It simply passes all your args to mvn and sets the JAVA8_HOME variable in your bash_profile file to JAVA_HOME

Ex.

```
./dvn clean install
```
