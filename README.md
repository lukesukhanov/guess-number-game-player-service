<a name="readme-top"></a>

# About the application

This application provides the REST API for the 'Guess my number' game.
<p>
The game is simple - the player has to guess a number from 1 to 50 with minimal count of attempts.<br />
Information about players can be saved for the future.<br />
Each player has the id, the username and the count of his best attempts.<br />
  
The API allows user:
<ul>
<li>to register, to login and to logout using basic authentication;</li>
<li>to receive and to save information about himself;</li>
<li>to receive information about players with the best result.</li>
</ul>

Built with:
<ul>
  <li>Java</li>
  <li>Spring Boot</li>
  <li>Spring Security (basic authentication, defence from CSRF)</li>
  <li>Maven</li>
  <li>PostgreSQL</li>
</ul>

See also <a href="https://github.com/lukesukhanov/guess-number-game-web">the frontend part of this application</a>

# Usage examples
## Authentication
### Registration
<b>Request</b>
<p>
POST /register<br />
Authorization: [username]:[password]<br />
<p>
The '[username]:[password]' part must be encoded with the Base64 algorithm.
<p>  
<b>Normal response</b>
<p>
Status: 201<br />
Location: /players/[id]<br />
Body: {"id": "[id]", "username": "[username]", "bestAttemptsCount": "null"}
<p>
<b>Response in case the user with the given username already exists</b>
<p>
Status: 400<br />
Body: {"error": "Duplicating username"}

### Login
<b>Request</b>
<p>
POST /login<br />
Authorization: Basic [username]:[password]
<p>
The '[username]:[password]' part must be encoded with the Base64 algorithm.
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Set-Cookie: JSESSIONID=[JSESSIONID]<br />
Body: {"username": "[username]"}
<p>
<b>Response in case authentication has failed</b>
<p>
Status: 401

### Receiving CSRF token (after authentication)
<b>Request</b>
<p>
POST /csrfToken<br />
Cookie: JSESSIONID=[JSESSIONID]
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
X-CSRF-TOKEN: [CSRF token]
<p>
<b>Response in case authentication has failed</b>
<p>
Status: 401
  
### Logout
<b>Request</b>
<p>
POST /logout<br />
Cookie: JSESSIONID=[JSESSIONID]<br />
X-CSRF-TOKEN: [CSRF token]
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Set-Cookie: JSESSIONID=<br />
<p>
<b>Response in case authentication has failed</b>
<p>
Status: 401
  
## Players

### Get all players
<b>Request</b>
<p>
GET /players<br />
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Body: [{"id": "[id]", "username": "[username]", "bestAttemptsCount": "[bestAttemptsCount]"}, ...]
  
### Get player by id
<b>Request</b>
<p>
GET /players/[id]<br />
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Body: {"id": "[id]", "username": "[username]", "bestAttemptsCount": "[bestAttemptsCount]"}
<p>  
<b>Response in case the player not found</b>
<p>
Status: 404<br />
Body: {"error": "Can't find player with id = [id]"}
  
### Get player by username
<b>Request</b>
<p>
GET /players?username=[username]<br />
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Body: [{"id": "[id]", "username": "[username]", "bestAttemptsCount": "[bestAttemptsCount]"}, ...]
<p>  
<b>Response in case the player not found</b>
<p>
Status: 404<br />
Body: {"error": "Can't find player with username '[username]'"}
  
### Get players with the best result
<b>Request</b>
<p>
GET /players/withBestResult<br />
<p>  
<b>Normal response</b>
<p>
Status: 200<br />
Body: [{"id": "[id]", "username": "[username]", "bestAttemptsCount": "[bestAttemptsCount]"}, ...]
<p>  
<b>Response in case the players not found</b>
<p>
Status: 200<br />
Body: []
  
### Update the player by id
<b>Request</b>
<p>
PUT /players/[id]<br />
Cookie: JSESSIONID=[JSESSIONID]<br />
X-CSRF-TOKEN: [CSRF token]<br />
Body: {"id": "null", "username": "[username]", "bestAttemptsCount": "[bestAttemptsCount]"}
<p>  
<b>Normal response</b>
<p>
Status: 204
<p>  
<b>Response in case the player not found</b>
<p>
Status: 404<br />
Body: {"error": "Can't find player with id = [id]"}
