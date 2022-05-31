# Wallet API

The Wallet API is a REST Service to manage users with different money accounts and save their transactions It implements
all the CRUD operations for each resource which can be accessed via HTTP and responds with JSON format, and it also uses
JWT Authentication to secure the resources attached to each user

This REST service is built on Java with the Spring Boot Framework

# Endpoints

## /login

``POST`` Authenticates user

- Content-type: application/json
    - Accepts:
      ```json
      {"username": $username,"password": $password}
    - Returns:
        - Headers:
            - Authorization: Contains the JWT to authenticate
            - Refresh-Token: Contains a refresh token to refresh the Authorization token
            - Expiration-Date: The expiration date of the Authorization token

## /users

``POST`` Creates a new user

- Content-type: application/json
    - Accepts:
      ```json
      {"username": $username,
       "password": $password,
       "email": $email}
    - Returns:
        - body:
          ```json
          {"username": $username}

``GET`` Gets the user credentials

- Content-type: application/json
    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - body:
          ```json
          {"username": $username,
           "email": $email}

``PATCH`` Updates user credentials

- Content-type: "application/json-patch+json"
    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - Body:
          ```json
          {"username": $new_username,
           "email": $new_email}

``DELETE`` Deletes the user

- Requires:
    - Headers:
        - Authorization
    - Returns:
        - Body: "User deleted successfully"

## /users/accounts

``POST`` Creates a new account

- Content-type: application/json
    - Accepts:
      ```json
      {"name": $name}
    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - URI: Returns the URI to access the created resource
        - Body:
          ```json
          {"username": $username,
           "email": $email,
           "account": {"name": $name}}

``GET`` Gets all the accounts

- Requires:
    - Headers:
        - Authorization
    - Returns:
        - Body:
        ```json
        {"username": $username,
         "email": $email,  
         "account": [
                     {"name": $account_name1} ,
                     {"name": $account_name2}
                    ]
         }

``PATCH`` /users/accounts/{$account_name} Updates the account

- Content-type: "application/json-patch+json"
    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - Body:
          ```json
          {"name": $new_name}

``DELETE`` /users/accounts/{account_name}

Deletes the account

- Requires:
    - Headers:
        - Authorization
    - Returns:
        - Body: "Account deleted successfully"

## /users/accounts/{name}/transactions

``POST`` Creates a new transaction

- Content-type: application/json
    - Accepts:
      ```json
      {"amount": $amount,
       "description": $description}

    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - URI: Returns the URI to access the created resource
        - Body:
          ```json
          {"id": $id, 
          "amount": $amount, 
          "description": $description, 
          "date": $current_ISO_date
          }

``PATCH``/users/accounts/{$account_name}/transaction/{$id}

Updates the transaction by id

- Content-type: "application/json-patch+json"
    - Requires:
        - Headers:
            - Authorization
    - Returns:
        - Body:
          ```json
          {"id": $id, 
          "amount": $new_amount, 
          "description": $new_description, 
          "date": $new_ISO_date}

``DELETE`` /users/accounts/{$account_name}/transaction/{$id}

Deletes the transaction by id

- Requires:
    - Headers:
        - Authorization
    - Returns:
        - Body: "Transaction deleted"

``GET`` /users/accounts/{$account_name}/transaction/{$id}?page={$page}&size={$size}&from={$from}&to={$to}

Gets the specified ``$page`` with  ``$size`` with the transaction from the specified account between the two
dates ``$from - $to``.

- Requires:
    - Headers:
        - Authorization
    - Returns:
        - URI: Returns the URI to access the created resource
        - body:
          ```json
          {"account":
             {"name": $account_name,
              "transactions": [
                                {"id": $id, 
                                 "amount": $amount,
                                 "description": $description,
                                 "date": ISO_date}
                                 ]
             },
          "totalElements": $totalElements,
          "totalPages": $totalPages,
          "page": $page,
          "size": $size,
          "from": $from}

``$from`` and ``$to`` dates are optionals and need to be in ISO format

``$size`` limit is 1000, if not specified it will use that value

If ``$account_name = *`` then it returns an array with all the accounts and their transactions page

## /refresh

``GET`` Refreshes the JWT authentication token

- Requires:
    - Headers:
        - Refresh-Token
    - Returns:
        - Headers:
            - Authorization: Contains a new JWT to authenticate
            - Refresh-Token: Contains a new refresh token to refresh the new Authorization token
            - Expiration-Date: The expiration date of the new Authorization token
        



