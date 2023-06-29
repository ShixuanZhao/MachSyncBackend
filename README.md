# Description

This is the backend of synchronization platform for machinations. 
The program is written in Java using Spring Boot and Gradle to setup a simple webserver.


### Goal
The goal of this project is to detect the data file change like excel, and propogate it to the machinations

###  Why we need it

While machinations is extremely powerful, the framework it uses is outdated. The actions script or the flash is no longer supported.
In addition, its library can be hardly found on the internet. The compiler like FL is no longer supported too, which means we cannot download it from official website.
To avoid future development difficulties, we decide to move those features which are not directly related to machinations out of it.
Like the file change detection, batch processing and export etc. 
The alternative is to develop these features in the machinations, here we will compare the pros and cons of it.

Pros of development side features in machinations
- a more consistent UI, a consistent layout
- better integration with machinations, can control the status of machinations directly, do not need extra steps to communicate with servers
- fewer deployment process, no need to maintain and develop different projects

Pros of development side features in web server
- more productive, framework or the code can be easily found on the internet, like decoding excel and file management
- debugging tools, IDE is more powerful than the machinations, when debugging the change is directly reflected, for the machinations, I use FL so I need to compile it and rerun it for any small features, and `watch` if it works
- reduce the chance of bugs to affect correctness of machinations. Since the core algorithm of machinations is tricky, move the unnecessary part out of it can reduce the chance of introducing bugs
- To do a right UI layout is time-consuming compared with current framework. The pixel need to be hard coded.

Since we currently do not find any efficient tools to debug, we decide to use other framework to continue development just like machinations2 to develop on the web.
Things might change if the future developer find a more efficient tool or IDE to support development

## Project structure

This is a traditional spring boot mvc application. 


For development of spring mvc, resources can be found here: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html

One thing needs to be taken care of is the database, we used H2 which means that the data is not persistent if backend application restarts.
The benefits it brings is that we no longer need to maintain database schema locally, since current usage is one-time sync. If we finish the experiment, we just shutdown the application. The next time, just to restart it and input the file name

If future requirement change, we could consider use sqlite to store it or move it to servers

## How to Develop
For mac users, just download the IDE and import it. We recommend IntelliJ IDEA, since it is the most popular IDE for Java development.

For windows users, you need to install JDK first, then download the IDE and import it.

Refresh the gradle build file, and gradle will automatically download the dependencies.

Then you can run the application by clicking the run button.

## How to Build

Just use the command gradle build. It will build a fatjar which is located under build/libs xx-snapshot.jar not the plain one since it lacks the dependencies.

Then you can run it by java -jar build/libs/xxx.jar, it will start the server for the backend.

To integrate with the front end, we need to put built front end css and js files under main/resources/static folder.
Then the backend will serve the front end files. and rebuild it by running gradle build and these files are within the jar.

## How to Deploy

### locally
after building the jar, we can transfer this jar to the windows. Under windows, we open the command prompt https://support.kaspersky.com/common/windows/14637#block0
It can be run by normal users not as administrator.

And change to the jar file directory and input
`java -jar xxx.jar` 

The windows will start looking for the correct java version, it normally has been installed locally.
The first time it runs, it will ask the permission to get the network port access, because it needs to listen to the port 8080 to transfer data to machinations.
And the server will start running. If we want to cancel it. just press `ctrl + c`

Another way to run, is to double click it. After finishing the task, kill it by task manager by finding the related jar process

if it fails to start sometimes it means, there is another application running to occupy the port.






