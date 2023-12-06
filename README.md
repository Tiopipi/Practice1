# WeatherApp

## Subject: DACD
### Curse: 2º

#### Degree: Data Science and Engineering 
#### School: Computer School
#### University: ULPGC

---

# Summary of the application:
### The aim of this application is to periodically collect weather information from the 8 Canary Islands using an API called Open Weather Map, and once the application has that information, it generates an event of each prediction that will be stored in the activemq broker, and then these events will be read by another module that will store these events in a file.

---
# How to use
### First you have to run the Main.java of the event_store_builder module and then the Main.java of the weather_supplier module. In order to see the file where the events are stored for the first time, you have to stop the application.

---

# Resources used
### This application has been developed in the IntelIJ environment, using Git as the version control system. Maven has also been used as a build and dependency management Tool.

---

# Structure of the project

- ## Module weather_supplier:
 #### This module is in charge of obtaining weather forecasts from the Open Weather Map API and generates an event for each forecast that will be stored in the prediction.Weather topic of the activemq broker. It consists of the following classes and interfaces.
### Main.java:
- #### Entry point of the module.
- #### Loads the locations and starts a timer to periodically update the weather data.

### OpenWeatherMapSupplier.java:
- #### Implements the WeatherSupplier interface.
- #### Gets weather data from the OpenWeatherMap API.

### JMSWeatherStore.java:
- #### Implements the WeatherStore interface.
- #### Has the method to create the connection to the activemq broker and store the events of each prediction.

### WeatherControl.java:
- #### Controls the main logic of the application.
- #### Gets weather data and saves it.

### WeatherStore.java:
- #### Defines the interface for storing and retrieving weather data.

### WeatherSupplier.java:
- #### Defines the interface for obtaining weather data.

### Location.java:
- #### Model to represent a geographic location.

### Weather.java:
- #### Model to represent weather data.

### UML class diagram
![weather supplier](weather%20supplier.JPG)
--- 
### Dependencies
- #### Gson: Library for JSON data manipulation in Java.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).

- ## Module event_store_builder
#### This module subscribes to the prediction.Weather topic to read incoming events and stores them in a file with a specific directory.
### Main.java:
- #### Entry point of the module.
- #### Executes the method that reads the events.

### JMSEventSupplier.java:
- #### Implements the EventSupplier interface.
- #### Connects to the broker, subscribes to the topic and reads the incoming events.

### FileEventStore.java:
- #### Implements the EventStore interface.
- #### Writes each event it receives to a file in a specific directory.

### EventStore.java:
- #### Defines the interface for storing events.

### EventSupplier.java:
- #### Defines the interface for obtaining events.

### UML class diagram
![event store](event%20store.JPG)
--- 
### Dependencies
- #### Jackson: JSON processing library for Java that provides a way to convert Java objects to JSON format and vice versa.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).

--- 

# Principles of design
## Single Responsibility Principle (SRP):
- ### Classes have unique and well-defined responsibilities. For example, WeatherControl handles control logic, OpenWeatherMapSupplier fetches data, and JMSWeatherStore handles the storage of the events created.
## Dependency Inversion Principle (DIP):
- ### Classes depend on abstractions (interfaces) rather than concrete implementations. WeatherControl depends on the WeatherSupplier and WeatherStore interfaces, allowing changes to implementations without affecting WeatherControl.
## Principle of Composition over Inheritance:
- ### Composition is favoured over inheritance. For example, instead of inheriting from Location in the Weather class, an instance of Location is composed within Weather.

---