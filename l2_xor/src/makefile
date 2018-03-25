CC = g++
CFLAGS= --std=c++11 -O3
DEBUGFLAGS= --std=c++11 -g

all: z1 debug crack

z1: main.cpp
	$(CC) -o ../$@ $^ $(CFLAGS)

crack: cracking.cpp
	$(CC) -o ../$@ $^ $(CFLAGS)

debug: main.cpp
	$(CC) -o ../$@ $^ $(DEBUGFLAGS)
