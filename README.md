# CSC435 Homework 5: Spring Boot

## Running the Showlist application

- URL: http://localhost:8080/showlist
- NOTE: Since authentication is not a concern for this assignment, some endpoints work differently than the final design, most notably the authentication endpoints.

--------

## Endpoints

### POST '/auth/register'
Registers a user in the database.

Request Body:  
> {  
>   "username" : [username],  
>   "password" : [password]  
> }  

Possible Responses:  
> 200 OK  
> {  
>   "uid" : [uid]  
> }  
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 400 BAD REQUEST  
> {  
>   "error" : "Username already exists."  
> }

### POST '/auth/login'
Creates a session associated with the user, allowing them to access their shows.
CURRENTLY: returns the UID of a user if they are registered.

Request Body:  
> {  
>   "username" : [username],  
>   "password" : [password]  
> }

Possible Responses:  
> 200 OK  
> {  
>   "uid" : [uid]  
> }
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Invalid username or password."  
> }
---

### DELETE '/auth/logout'
Ends the user's session.
CURRENTLY: Does nothing.

Possible Responses:  
> 204 NO CONTENT

### GET '/shows'
Returns the list of shows associated with the user.

Request Body:  
> {  
>   "uid" : [uid]*  
> }

Possible Responses:  
> 200 OK  
> [  
>   {  
>     "showId" : [showId],  
>     "title" : [title],  
>     "status" : [status],  
>     "rating" : [rating]  
>   },  
>   ...  
> ]
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Invalid user id."  
> }

### GET '/shows/{showId}'
Returns the information for the show of the associated showId.

Request Body:
> {  
>   "uid" : [uid]*  
> }

Possible Responses:  
> 200 OK  
> {  
>   "showId" : [showId],  
>   "title" : [title],  
>   "status" : [status],  
>   "rating" : [rating]  
> }
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Unauthorized. Please log in."  
> }
---
> 404 NOT FOUND  
> {  
>   "error" : "Show not found."  
> }

### POST '/shows/addshow'
Adds a show associated with the user to the database.

Request Body:
> {  
>   "uid" : [uid]*,  
>   "title" : [title],  
>   "status" : [status]  
> }

Possible Responses:  
> 201 CREATED  
> {  
>   "showId" : [showId]  
> }
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Invalid user id."  
> }

### PATCH '/shows/{showId}'
Updates the status or rating of a show of the associated showId.

Request Body:
> {  
>   "uid" : [uid]*,  
>   "status" : [newStatus],  
>   "rating" : [newRating]  
> }

Possible Responses:  
> 200 OK  
> {  
>   "showId" : [showId],  
>   "title" : [title],  
>   "status" : [newStatus],  
>   "rating" : [newRating]  
> }
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Unauthorized. Please log in."  
> }
---
> 404 NOT FOUND  
> {  
>   "error" : "Show not found."  
> }

### DELETE '/shows/{showId}'
Deletes the show of the associated showId.

Request Body:
> {  
>   "uid" : [uid]*  
> }

Possible Responses:  
> 204 NO CONTENT
---
> 400 BAD REQUEST  
> {  
>   "error" : "Invalid input."  
> }
---
> 401 UNAUTHORIZED  
> {  
>   "error" : "Unauthorized. Please log in."  
> }
---
> 404 NOT FOUND  
> {  
>   "error" : "Show not found."  
> }

*The addition of a UID in the request body acts as access control before proper authentication is implemented. 