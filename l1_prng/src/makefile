CC = g++
CFLAGS= --std=c++11 -g
IDIR = ../include
ODIR = obj

_DEPS = LCGCracker.hpp
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

_OBJ1 = app1.o LCGCracker.o
OBJ1 = $(patsubst %,$(ODIR)/%,$(_OBJ1))

_OBJ2 = app1.o LCGCracker.o
OBJ2 = $(patsubst %,$(ODIR)/%,$(_OBJ2))


$(ODIR)/%.o: %.cpp $(DEPS)
	g++ -c -o $@ $< $(CFLAGS)

all: app1 app2

app1: $(OBJ1)
	$(CC) -o ../$@ $^ $(CFLAGS)

app2: $(OBJ2)
	$(CC) -o ../$@ $^ $(CFLAGS)
