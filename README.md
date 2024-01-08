# HotelRecommenderApp

## Subject: DACD
### Curse: 2º

#### Degree: Data Science and Engineering 
#### School: Computer School
#### University: ULPGC

---

# Summary of the application:
### The aim of this application is to periodically collect weather information using an API called Open Weather Map, and the availability and prices for different hotels using the API Xotelo. 
### Once the application has that information, it generates an event of each prediction and hotel that will be stored in the activemq broker, and then these events will be read by another module that will store these events in a file. 
### Moreover, this application will offer a hotel recommendation system to users depending on the date they want to go and the weather preferences of the user.

---
# How to use
### 1º Connect to the broker. 
### 2º Run the Main.java of the datalake_builder module, passing as argument the path to the folder where you want the datalake to be generated. The folder must exist.
### 3º Run the Main.java of the hotel_recommender module. A window will open in which you can choose which temperature you prefer, and when you choose the option you will get another window showing the recommendations. The first time it is executed it will not show the recommendations directly, but will have to wait 15-20 minutes for the Xotelo API requests to finish. Once the datamart has all the data, the application will work correctly.
### 4º Run the Main.java of the hotel_supplier module. The first run will take about 15-20 minutes to perform all API requests.
### 5º Run the Main.java of the weather_supplier module, passing as argument the OpenWeatherMap apikey to be able to make requests to the API.

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

### Dependencies
- #### Gson: Library for JSON data manipulation in Java.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).

---
- ## Module hotel_supplier:
#### This module is in charge of obtaining the availability and prices of different hotels from the Xotelo API and generates an event for each hotel at different check in check out times that will be stored in the data.Hotel topic of the activemq broker. It consists of the following classes and interfaces. Generates new events every 6 hours.
### Main.java:
- #### Entry point of the module.
- #### Loads the hotels data and starts a timer to periodically generate the hotel data.

### XoteloSupplier.java:
- #### Implements the HotelSupplier interface.
- #### Gets hotel data, specially the prices and web pages, from the Xotelo API.

### JMSHotelStore.java:
- #### Implements the HotelStore interface.
- #### Has the method to create the connection to the activemq broker and store the events of each hotel.

### HotelControl.java:
- #### Controls the main logic of the application.
- #### Gets hotel data and saves it.

### HotelStore.java:
- #### Defines the interface for storing and retrieving hotel data.

### HotelSupplier.java:
- #### Defines the interface for obtaining hotel data.

### Hotel.java:
- #### Model to represent a hotel.

### Rate.java:
- #### Model to represent the price and the web page that offer the price.

### UML class diagram
![hotel supplier](hotel%20supplier.JPG)

### Dependencies
- #### Gson: Library for JSON data manipulation in Java.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).

---
- ## Module datalake_builder
#### This module subscribes to the prediction.Weather and data.Hotel topics to read incoming events and stores them in a file with a specific directory.
### Main.java:
- #### Entry point of the module.
- #### Executes the method that reads the events.

### JMSEventSupplier.java:
- #### Implements the EventSupplier interface.
- #### Connects to the broker, subscribes to the topics and reads the incoming events.

### FileEventStore.java:
- #### Implements the EventStore interface.
- #### Writes each event it receives to a file in a specific directory.

### EventStore.java:
- #### Defines the interface for storing events.

### EventSupplier.java:
- #### Defines the interface for obtaining events.

### UML class diagram
![event store](event%20store.JPG)

### Dependencies
- #### Jackson: Provides functionality for mapping Java objects to JSON formats and vice versa.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).

---
- ## Module hotel_recommender
#### this module uses sensor data to provide the user with hotel recommendations according to their climatic preferences.
### Main.java:
- #### Entry point of the module.
- #### Executes the methods that read the events from the broker and that execute the GUI of the application.

### View.java:
- #### Defines the interface for the view

### JMSEventSupplier.java:
- #### Implements the EventSupplier interface.
- #### Connects to the broker, subscribes to the topics and reads the incoming events.

### SQLiteEventStore.java:
- #### Implements the EventStore interface.
- #### Writes the selected data from the events it receives to a SQLite database.

### SQLiteDataProvider.java:
- #### Implements the DataProcessedProvider  interface.
- #### The class encapsulates the logic for retrieving hotel and weather information from the SQLite database, creating corresponding Java objects, and processing the data for further use in the application.

### EventStore.java:
- #### Defines the interface for storing events.

### EventSupplier.java:
- #### Defines the interface for obtaining events.

### DataProvider.java:
- #### Defines the interface for retrieving hotel and weather information.

### Hotel.java:
- #### Model to represent a hotel.

### Rate.java:
- #### Model to represent the price and the web page that offer the price.

### Prediction.java
- #### Model to represent the weather forecast for a place.

### SwingHotelRecommendationView.java
- #### Overall, this class provides a simple and interactive GUI for users to input their preferences, search for available hotels, and view recommendations based on temperature preferences.
- #### It makes use of Swing components and interacts with the BusinessLogic class to perform the underlying business logic operations.

### UML class diagram
![hotel recommender](hotel%20recommender.JPG)

### Dependencies
- #### Gson: Library for JSON data manipulation in Java.
- #### Jackson: Provides functionality for mapping Java objects to JSON formats and vice versa.
- #### Apache ActiveMQ: It allows interaction with an Apache ActiveMQ server for the exchange of messages in a messaging system based on JMS (Java Message Service).
- #### SQLite-jdbc: Allows you to interact with a SQLite database from your Java application using JDBC.

---

# Principles of design
## Single Responsibility Principle (SRP):
- ### Classes have unique and well-defined responsibilities. For example, WeatherControl handles control logic, OpenWeatherMapSupplier fetches data, and JMSWeatherStore handles the storage of the events created. Moreover, The BusinessLogic, SQLiteDataProcessedProvider, SQLiteEventStore classes have specific responsibilities related to business logic, fetching processed data and storing events, respectively.
## Dependency Inversion Principle (DIP):
- ### Classes depend on abstractions (interfaces) rather than concrete implementations. HotelControl depends on the HotelSupplier and HotelStore interfaces, allowing changes to implementations without affecting HotelControl.
## Principle of Composition over Inheritance:
- ### Composition is favoured over inheritance. For example, instead of inheriting from Location in the Weather class, an instance of Location is composed within Weather.

---
