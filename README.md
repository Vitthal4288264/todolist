# To-Do List

Mitigant coding task.

### Assumptions

1. For due_date we are using LocatDateTime
   and expecting the input in this format ``2023-03-11T23:57:00.00`` ([SwaggerAPI](http://localhost:8000/swagger-ui/index.html))
2. When you add new to-do by default we are marking the status as NOT_DONE.
3. And 'Due_date' has to be greater than now.
4. To update the 'PAST_DUE' status we using scheduler for simplicity.
5. We expect description will unique(but we are allowing for duplicate).

For downloading and testing locally use the source code from git repository

``
$mvn clean install
``
### Run Application

For running the application:

``
$ docker-compose build && docker-compose up -d
``
or can run the command separately.

### Swagger API doc
To explore the API's you can visit the below link
[Swagger API](http://localhost:8000/swagger-ui/index.html)

# Testing app via rest api

Sample "Mitigant.postman_collection.json" is added for reference.

### addItem -> POST

Request
``  localhost:8000/api/v1/todo/addItem ``

Response -
````
{
"id": 19,
"description": "New Task14",
"creationDate": "2023-03-11T20:46:35.911548",
"dueDate": "2023-03-11T20:57:00",
"lastUpdatedAt": "2023-03-11T20:46:35.911548",
"status": "NOTDONE"
}
````

### getItem by Id -> GET

Request
`` localhost:8000/api/v1/todo/get/1 ``

Response -
````
{
    "id": 1,
    "description": "New Task",
    "creationDate": "2023-03-11T20:45:05.722231",
    "dueDate": "2023-03-11T20:48:00",
    "lastUpdatedAt": "2023-03-11T20:45:05.722231",
    "status": "DONE"
}
````

### getItem by 'Not Done' status

Request -
``localhost:8000/api/v1/todo/get?pageNumber=0&pageSize=5``

Response -
````
{
    "content": [
        {
            "id": 1,
            "description": "New Task14",
            "creationDate": "2023-03-11T23:00:27.678823",
            "dueDate": "2023-03-11T23:57:00",
            "lastUpdatedAt": "2023-03-11T23:00:27.678823",
            "status": "NOT_DONE"
        },
        {
            "id": 2,
            "description": "New Task14",
            "creationDate": "2023-03-11T23:00:29.623452",
            "dueDate": "2023-03-11T23:57:00",
            "lastUpdatedAt": "2023-03-11T23:00:29.623452",
            "status": "NOT_DONE"
        },
        {
            "id": 3,
            "description": "New Task14",
            "creationDate": "2023-03-11T23:00:31.110508",
            "dueDate": "2023-03-11T23:57:00",
            "lastUpdatedAt": "2023-03-11T23:00:31.110508",
            "status": "NOT_DONE"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 5,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "size": 5,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}
````

### changeDescription -> PATCH

``
localhost:8000/api/v1/todo/:id/changeDescription?description=efffef
``

````
{
    "id": 2,
    "description": "efffef",
    "creationDate": "2023-03-12T00:24:35.155066",
    "dueDate": "2023-03-12T00:57:00",
    "lastUpdatedAt": "2023-03-12T00:24:35.155066",
    "status": "NOT_DONE"
}
````


### changeStatus -> PATCH

``
localhost:8000/api/v1/todo/:id/changeStatus?status=DONE
``

````
{
    "id": 2,
    "description": "efffef",
    "creationDate": "2023-03-12T00:24:35.155066",
    "dueDate": "2023-03-12T00:57:00",
    "lastUpdatedAt": "2023-03-12T00:24:35.155066",
    "status": "DONE"
}
````