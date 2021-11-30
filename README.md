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

## Endpoints

### User
#### Sign Up

**POST `/users?uid=1234567890`**

Add user to database after signing up with Firebase

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": "2021-11-21T23:50:30.223001000Z"
}
```

#### Find User

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

Update chatbot accuracy after user finish a test

Example request body:
```json
{
  "name": "dialogflow",
  "result": "true"
}
```

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": "0.6666666667"
}
```

#### Get Stats

**POST `/chatbots/stats`**

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
Clear stats of a chatbot (use with caution)

### Dialogue
#### Start Search

**POST `/dialogues/search?uid=123456`**

Start searching for opponents

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

Example response body (matched):
```json
{
  "code": "000",
  "msg": "success",
  "data": "888888"
}
```

#### ~~Start Dialogue~~ (Probably unnecessary)

**POST `/dialogues`**

Start a new dialogue

#### Continue Dialogue

**PATCH `/dialogues`**

Get response from an ongoing dialogue

Example request body **(Dialogflow)**:
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

#### End Dialogue

**GET `/dialogues?uid=123456`**

End the current dialogue **(must call after a dialogue completes)**

Example response body:
```json
{
  "code": "000",
  "msg": "success",
  "data": true
}
```

## Deployment (TBD)
Hosting on Heroku/Firebase

## Credit
**Yibo Wen** /
**Tom Zhou** /
**Shawn Liao**
