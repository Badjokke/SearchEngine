call cd src/ui/bcc_information_retrieval
ECHO instaluju node packages
call npm install
ECHO spoustim klientskou aplikaci
call npm start
ECHO klient spusten, bezim na portu 3000