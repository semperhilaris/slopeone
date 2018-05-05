# Slope One Webservice

Kotlin implementation of the [Slope One](https://en.wikipedia.org/wiki/Slope_One) collaborative filtering algorithm.

The Application comes with a built-in web server based on [Spark](http://sparkjava.com/).

## Requirements

Java 7+

## Building

An executable JAR can be built by running the shadowJar Gradle task, either from your IDE or command line:

```sh
$ ./gradlew shadowJar
```

## Running

Once the JAR has been built, the app is started using:

```sh
$ java -jar build/libs/slopeone-1.1-SNAPSHOT-all.jar
```

The web server will be running on `http://localhost:4567`

## Webservice

The web service provides the following API:

### Status

Used to test if the application is running.

Request:
````
GET /status
````

Response:
````
"OK"
````

### Clear

Clears the rating data.

Request:
````
GET /clear
````

Response:
````
"Data cleared."
````

### Ratings

Adds rating data.

Request:
````
PUT /ratings
Content-Type: application/json
Payload:
{
  "entries": [
    {
      "item1": 1,
      "item2": 0.5,
      "item3": 0.2
    },
    {
      "item1": 1,
      "item3": 0.5,
      "item4": 0.2
    },
    {
      "item1": 0.2,
      "item2": 0.4,
      "item3": 1,
      "item4": 0.4
    },
    {
      "item2": 0.9,
      "item3": 0.4,
      "item4": 0.5
    }
  ]
}
````

Response:
````
"Ratings added."
````

### Predict

Returns the predictions based on provided ratings.

Request:
````
POST /predict
Content-Type: application/json
Payload:
{
  "entries":
    {
      "item1": 0.4
    }
}
````

Response:
````
{
  "predictions": {
    "item2": 0.25,
    "item3": 0.23333333333333336,
    "item4": 0.09999999999999998
  }
}
````

### Predict with ratings

Combines /ratings and /predict into a single request.
Uses a separate instance of the engine to avoid interfering with other requests.

Request:
````
POST /predict-with-ratings
Content-Type: application/json
Payload:
{
  "ratings": {
    "entries": [
      {
        "item1": 1,
        "item2": 0.5,
        "item3": 0.2
      },
      {
        "item1": 1,
        "item3": 0.5,
        "item4": 0.2
      },
      {
        "item1": 0.2,
        "item2": 0.4,
        "item3": 1,
        "item4": 0.4
      },
      {
        "item2": 0.9,
        "item3": 0.4,
        "item4": 0.5
      }
    ]
  },
  "predict": {
    "entries":
      {
        "item1": 0.4
      }
  }
}
````

Response:
````
{
  "predictions": {
    "item2": 0.25,
    "item3": 0.23333333333333336,
    "item4": 0.09999999999999998
  }
}
````