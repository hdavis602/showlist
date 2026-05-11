# CSC435 Final Project: Spring Boot App

## Running the Showlist application

- URL: http://localhost:8080/showlist

--------

## Endpoints

### GET '/shows'
Returns the list of shows associated with the current user.

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
>   "error" : "Invalid credentials to access resource."  
> }

### GET '/shows/{showId}'
Returns the information for the show of the associated showId.

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
>   "error" : "Invalid credentials to access resource."  
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
>   "error" : "Invalid credentials to access resource."  
> }

### PATCH '/shows/{showId}'
Updates the status or rating of a show of the associated showId.

Request Body:
> {
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
>   "error" : "Invalid credentials to access resource."  
> }
---
> 404 NOT FOUND  
> {  
>   "error" : "Show not found."  
> }

### DELETE '/shows/{showId}'
Deletes the show of the associated showId.

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
>   "error" : "Invalid credentials to access resource."  
> }
---
> 404 NOT FOUND  
> {  
>   "error" : "Show not found."  
> }