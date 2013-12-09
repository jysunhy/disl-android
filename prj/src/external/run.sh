#!/bin/bash
rm Socket.o
rm test
g++ -g -c Socket.cpp -o Socket.o
g++ -g Socket.o test.cpp -I/usr/lib/jvm/java-6-oracle/include -I/usr/lib/jvm/java-6-oracle/include/linux -lpthread -o test
./test
