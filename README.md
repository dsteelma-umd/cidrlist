# cidrlist

## Introduction

This application calculates the minimum set of CIDR-formatted IP address
ranges needed to cover a specified set of IP addresses.


> I would have written a shorter letter, but I did not have the time.
>
> -- <cite>Blaise Pascal</cite>

## Requirements

* Java 11
* Maven 3.8.7

### Sample Build and Run
```
$ mvn clean package
$ java -cp target/cidrlist-0.0.1-SNAPSHOT.jar edu.umd.lib.cidrlist.App

$ add 123.45.67.88/32
$ add 14.24.36.12/24
$ subtract 14.24.36.12/32
$ show
---------
Current Node List
---------
123.45.67.88/32
14.24.36.13/32
14.24.36.14/31
14.24.36.8/30
14.24.36.0/29
14.24.36.16/28
14.24.36.32/27
14.24.36.64/26
14.24.36.128/25
---------


$ test 14.24.36.12/32
False - 14.24.36.12/32 is not in the list
$ test 14.24.36.13/32
True - 14.24.36.13/32 is contained in the list
$ reset
$ show
---------
Current Node List
---------
---------


$ <Empty line exits>
Exited
```

### Available Commands

* `add <CIDR>` - Adds the given CIDR range to the current node list
* `subtract <CIDR>` - Subtracts the given CIDR range from the current node list
* `show` - Outputs the current node list
* `test <CIDR>` - Returns whether or not the given CIDR range is in the current
                  node list
* `reset` - Resets the node list to an empty list

## Developer Instructions

### Compile the Project

```
$ mvn clean package
```

### Run the tests

```
mvn test
```

### Example JShell Usage

```
$ jshell --class-path target/cidrlist-0.0.1-SNAPSHOT.jar

jshell>import edu.umd.lib.cidrlist.*
jshell>NodeList nodeList = new NodeList(3)
jshell>nodeList = nodeList.addNode(new Node(1, 3), nodeList)
```

### Run Checkstyle Checks

```
$ mvn checkstyle:check
``

## License

See the [LICENSE](LICENSE.md) file for license rights and limitations
(Apache 2.0).
