# SimpleBanking

* SimpleBanking starts from command line by executing: mvn spring-boot:run
* Applications address: http://localhost:8080/simplebanking
* Application has REST endpoints:
    * GET /accounts - returns list of user accounts 
    * GET /accounts/{accountNo}/balance - returns account balance
    * GET /accounts/{accountNo}/operations - returns List of account operations
    * POST /accounts/{accountNo}/operations - post new account operations (DEPOSIT and WITHDRAW)
* REST endpoints defined in swagger: http://localhost:8080/simplebanking/swagger/ui
* All endpoints are secured with Basic Auth authorization type and user needs to pass 
Header 'Authorization: Basic <base64 of user:pass>' in order to access them. 
The application has these users/passwords:
    * john/john123
    * frank/frank123
    * linda/linda123
* All users have some preloaded accounts and user able to access just his own accounts and post operations to them.

##### Known issues:
* swagger requires login