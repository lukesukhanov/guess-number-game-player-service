<a name="readme-top"></a>

# About the application

This application provides REST API for the 'Guess my number' game.
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
