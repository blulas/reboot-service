This repository contains the Server Reboot micro-service, for use by the AT&T Halos project.  

The REST API
-----------------------------------
In order to test the various REST API's, you can use a tool like Postman and the supplied Postman collection, located in the `src/test/resources/postman` folder.

There are also examples in `Curl` located in the `src/test/resources/curl` folder.

Local Development Environment Build
-----------------------------------
Before you can build the docker image you will first need to copy the config/settings.xml file in this repository to your local maven/conf directory.

```
cp config/settings.xml $M2_HOME/conf/
```

If you are performing a local build on your development machine, utilizing your local Maven repository, you need to run the following command `mvn clean install` at the root of the repository.

Enterprise Build (possibly via Pipeline)
----------------------------------------
If you are performing a build based on the enterprise Maven repository, either directly or via Jenkins, the order in which you do a Maven build is the same as a local build, you must run the following command at the root of the repository first: `mvn clean deploy`, which downloads and installs all the dependencies specified in the parent pom.xml into the enterprise Maven cache.

The Docker Image
-----------------------------------
In order to build the Docker image, type the following command at the root of this repository:

```
docker build . -f src/main/docker/Dockerfile.jvm --no-cache -t quay.io/redhattelco/intelli-infra-full:latest
```

Before you can publish the image you must fist login to quay.io.  The Docker CLI stores passwords entered on the command line in plaintext. It is therefore highly recommended to first go to quay.io, login, and create an encrypted password.  Once you have your enctyped password, use ` docker login ` to login to quay.io with the following command, inserting your username and your encrypted password: 

```
docker login -u="" -p="" quay.io
```

In order to publish the image, type the following command at the root of this repository:

```
docker push quay.io/redhattelco/intelli-infra-full:latest
```

In order to get the latest image, type the following command at the root of this repository:

```
docker pull quay.io/redhattelco/intelli-infra-full:latest
```

To run the Spring Boot app:
======================================

The Database
-----------------------------------
The database used by the service is based on PostgreSQL 10.17 and managed as a Docker container.  The image is available using the following command on your local workstation:

**This command gets the image from quay.io:**:

```
docker pull quay.io/redhattelco/intelli-infra-db:latest
```

**This command starts the database container:**

```
docker run -d -p 5432 --name ii-db quay.io/redhattelco/intelli-infra-db:latest
```

**This command starts the database container:**

```
docker run -d -p 5432:5432 --name ii-db quay.io/redhattelco/intelli-infra-db:latest
```

**This command gets the public IP address of the database container:**

```
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ii-db
```

The Spring Boot app
-----------------------------------


**This command gets the image from quay.io:**:

```
docker pull quay.io/redhattelco/intelli-infra-full:latest
```

**This command starts the Spring Boot container:**
**Replace $DBHOST with the IP address of the Database container**

```
docker run -d -p 8080:8080 --name ii-dm --env DB_HOST=$DBHOST --env DB_PORT=5432 quay.io/redhattelco/intelli-infra-full:latest
```

**This command starts the Spring Boot container:**
**Replace $DBHOST with the IP address of the Database container**

```
docker run -d -p 8080:8080 --name ii-dm --env DB_HOST=$DBHOST --env DB_PORT=$DBPORT quay.io/redhattelco/intelli-infra-full:latest
```


Additional Information (*Appendicies*)
======================================

This section includes references to any product or technology that is relevant to this repository, including a full set of repository documentation.


This repository is focused on business automation using Red Hat’s Business Automation products, which in turn rely on various open source
tools and technology. The following online documentation is available in order to learn various aspects of these tools:

-   [**Drools**](https://docs.jboss.org/drools/release/7.76.0.Final/drools-docs/html_single/index.html)

-   [**Apache Maven**](https://maven.apache.org/) is a free and open source software project management and comprehension tool. Based on
    the concept of a project object model (POM), Maven can manage a project’s build, reporting and documentation from a central piece of
    information. A **getting started guide** is available [here](http://maven.apache.org/guides/getting-started/).

-   [**Git**](https://git-scm.com//) is a free and open source distributed version control system designed to handle everything
    from small to very large projects with speed and efficiency. There is a **handbook** available [here](https://guides.github.com/introduction/git-handbook/), as
    well as various **guides** for learning and working with Git  available [here](https://guides.github.com/).

-   [**Spring Boot**](https://spring.io/projects/spring-boot) makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

-   [**PostgreSQL**](http://www.postgresql.org/) is a fully managed database service to deploy applications.

-   [**Docker**](https://docker.com) takes away repetitive, mundane configuration tasks and is used throughout the development lifecycle for fast, easy and portable application development - desktop and cloud. Docker’s comprehensive end to end platform includes UIs, CLIs, APIs and security that are engineered to work together across the entire application delivery lifecycle.