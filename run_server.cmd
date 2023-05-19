ECHO instaluju zavislosti pro server
call mvn clean install
ECHO spoustim spring server
call mvn spring-boot:run


