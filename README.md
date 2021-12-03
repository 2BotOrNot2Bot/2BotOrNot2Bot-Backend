# 2BotOrNot2Bot Backend

##  Table of Contents
- [Basics](#basics)
- [Configuration](#configuration)
- [Endpoints](#endpoints)
    - [User](#user)
    - [Chatbot Stats](#chatbot-stats)
    - [Dialogue](#dialogue)
- [Deployment (TBD)](#deployment-tbd)
- [Credit](#credit)

## Basics
The backend is written using Java [Spring Boot](https://spring.io/projects/spring-boot) framework.
It utilizes the Firebase database [Firestore](https://firebase.google.com/docs/firestore) for storage of data.
It achieves the stable matching mechanism with [Redis](https://redis.io/).

## Configuration
Please download the file from Firebase console and put it under the resource folder with name `serviceAccountKey.json`.

Please set relevant Redis configuration, name of dialogflow project id as `DIALOGFLOW_PROJECT_ID` and put it under the 
resource folder with name `application.properties`.

Please download the file from GCP console of your Dialogflow project and put it under the resource folder with 
name `dialogflowServiceAccountKey.json`.

Please reserve port 8080 for backend.

## Endpoints

### User
#### Sign Up

**POST `/users`**

Add user to database after signing up with Firebase

Example request body:
```json
{
  "uid": "1234567890"
}
```

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": "2021-11-21T23:50:30.223001000Z"
}
```

#### Find User - Login

**GET `/users?uid=1234567890`**

Retrieve user from database after signing in with Firebase

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": {
    "uid": "7867291389",
    "points": 0
  }
}
```
```json
{
  "code": "003",
  "msg": "didn't find the user",
  "data": null
}
```


### Chatbot Stats
#### Update Stats

**POST `/chatbots/stats`**

Update chatbot accuracy and personal score after user submit test answers

Example request body:

```json
{
  "name": "dialogflow",
  "result": true,
  "uid": "123456789"
}
```

Example response body:
Return user's new score if the input uid is not null
```json
{
  "code": "000",
  "msg": "success",
  "data": "62"
}
```

#### Get Stats

**GET `/chatbots/stats`**

Retrieve chatbot stats ordered by accuracy from database

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": [
    {
      "name": "testbot1",
      "testCount": 94,
      "successCount": 94,
      "percentage": 1
    },
    {
      "name": "testbot2",
      "testCount": 94,
      "successCount": 47,
      "percentage": 0.5
    },
    {
      "name": "testbot3",
      "testCount": 94,
      "successCount": 0,
      "percentage": 0
    }
  ]
}
```

#### Delete Stats
Clear stats of a chatbot **(use with caution)**

### Dialogue
#### Start Search

**POST `/dialogues/search`**

Start searching for opponents

Example request body:
```json
{
  "uid": "123456"
}
```

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": true
}
```

#### Get Opponent

**GET `/dialogues/search?uid=123456`**

Get available opponent **(must call after starting search)**

Example response body (no matching):
```json
{
  "code": "000",
  "msg": "success",
  "data": null
}
```

Example response body (matched): return opponent's user id and name of the chat bot

```json
{
  "code": "000",
  "msg": "success",
  "data": {
    "first": "88888888",
    "second": "dialogflow"
  }
}
```

[comment]: <> (#### ~~Start Dialogue~~ &#40;Probably unnecessary&#41;)

[comment]: <> (**POST `/dialogues`**)

[comment]: <> (Start a new dialogue)

#### Chat with Chat Bot

**PATCH `/dialogues`**

Get response from chat bot based on a unique id

Example request body:
```json
{
  "input": "Hello!",
  "chatbot": "dialogflow",
  "session": "1234567"
}
```

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": "Hi! I'm absolutely a human!"
}
```

## Deployment (TBD)
Hosting on Heroku/Firebase

## Credit
**Yibo Wen** /
**Tom Zhou** /
**Shawn Liao**
