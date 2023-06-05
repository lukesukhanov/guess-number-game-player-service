<a name="readme-top"></a>

# About the application

This application provides a simple REST API for the 'Guess my number' game.
<p>
The game boils down to guessing a number from 1 to 50 trying to do it with minimal count of attempts.
<p>
Information about players can be saved for the future.<br />
Each player has the id, the username and the count of best attempts.<br />
  
The API user can:
<ul>
<li>register, login and logout using basic authentication</li>
<li>receive and save information about himself</li>
<li>receive information about players with best result</li>
</ul>

Context path: /api

Built with:
<ul>
<li>PostgreSQL</li>
<li>Maven</li>
<li>Spring Boot</li>
<li>Spring Security (basic authentication, CORS for http://127.0.0.1:5500, defence from CSRF)</li>
</ul>

See also <a href="https://github.com/lukesukhanov/guess-number-game-frontend">the frontend part of this application</a>

# REST API specification

## Register a new user

### Request

POST /register

Authorization: username:password

(The 'username:password' part must be encoded with Base64.)

### Response (normal)

Status: 201

{
  "id": 1,
  "username": "boris",
  "bestAttemptsCount": null
}

### Response (the user with this username already exists)

Status: 400

{"error": "Duplicate"}

## Login

### Request

POST /login

Authorization: Basic username:password

(The 'username:password' part must be encoded with Base64.)

### Response (normal)

Status: 200<br />
Set-Cookie: JSESSIONID=...

{
  "username": "boris"
}

### Response (authentication failed)
Status: 401

## Get CSRF token (after authentication)

### Request

POST /csrfToken

Cookie: JSESSIONID=...

### Response (normal)

Status: 200

X-CSRF-TOKEN: ...

### Response (authentication failed)

Status: 401

## Logout

### Request

POST /logout

Cookie: JSESSIONID=...<br />
X-CSRF-TOKEN: ...

### Response

Status: 200<br />
Set-Cookie: JSESSIONID=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:10 GMT; Path=/api; SameSite=Strict

## Get all players

### Request

GET /players

### Response
Status: 200

[
  {
    "id": 1,
   "username": "boris",
    "bestAttemptsCount": 6
  },
  {
    "id": 2,
   "username": "ivan",
    "bestAttemptsCount": 5
  }
]
  
## Get player by id

### Request

GET /players/1

### Response (normal)
Status: 200

{
  "id": 1,
  "username": "boris",
  "bestAttemptsCount": 6
}

### Response (player not found)
Status: 404

{
  "error": "Can't find player with id = 1"
}

## Get player by username

### Request

GET /players?username=boris

### Response (normal)
Status: 200

{
  "id": 1,
  "username": "boris",
  "bestAttemptsCount": 6
}

### Response (player not found)
Status: 404

{
  "error": "Can't find player with username 'boris'"
}

## Get players with best result

### Request

GET /players/withBestResult

### Response (normal)
Status: 200

[
  {
    "id": 2,
    "username": "ivan",
    "bestAttemptsCount": 5
  }
]

### Response (empty list)
Status: 200

[]

## Update the player by id

### Request

PUT /players/1

Cookie: JSESSIONID=...<br />
X-CSRF-TOKEN: ...

{
  "id": 1,
  "username": "boris",
  "bestAttemptsCount": 4
}

### Response
Status: 204
