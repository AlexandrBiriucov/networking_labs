# Lab 2 - A coffee shop server (task 4, includes tasks 1-3)

Java 17+, only `Socket` / `ServerSocket`.

## Build (from the repository root)

    rm -rf out && mkdir out
    javac -d out $(find lab2/task4 -name '*.java')

## Run (three roles, each in its own terminal)

    java -cp out lab2.task4.Main server 6666
    java -cp out lab2.task4.Main client 127.0.0.1 6666
    java -cp out lab2.task4.Main provider 127.0.0.1 6666

Two computers: start the server on one, use its IP address instead of 127.0.0.1 on the other.

## Protocol (one text line per message, fields separated by `|`, max 256 bytes)

Client -> server:

    MENU
    ORDER|coffee|2|tea|1
    QUIT                      (local, closes the program)

Provider -> server:

    PROVIDER                  (sent automatically by the provider program)
    DELIVER|coffee|20|croissant|10
    MENU

Server -> client / provider:

    coffee | 4 sec | 12 available ; ...
    ACCEPTED|<id>|<seconds>
    READY|<id>
    Not enough <product>. Available: <n>
    PROVIDER|OK
    DELIVERED|{coffee=20, croissant=10}
    ALERT|LOW_STOCK|<product>|<left>
    ERROR|<CODE>              (UNKNOWN_REQUEST, WRONG_PARAMETERS, MESSAGE_TOO_LONG, INVALID_CHARACTERS,
                               ONLY_PROVIDER_CAN_DELIVER, PROVIDER_CANNOT_ORDER,
                               PROVIDER_ALREADY_CONNECTED, STOCK_LIMIT, INTERNAL)

## Checks

    kill -9 of a client while its orders are prepared: log shows "order N cancelled", MENU shows the stock restored
    random bytes / a line over 256 bytes: ERROR|..., the server keeps serving everybody
    stop the server: client and provider print "Server disconnected." and exit
