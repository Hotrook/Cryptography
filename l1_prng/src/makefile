CC = g++
CFLAGS= --std=c++11 -g
IDIR = ../include
ODIR = obj

_DEPS = LCGCracker.hpp
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

_OBJ1 = z1.o LCGCracker.o
OBJ1 = $(patsubst %,$(ODIR)/%,$(_OBJ1))

_OBJ2 = z2.o LCGCracker.o
OBJ2 = $(patsubst %,$(ODIR)/%,$(_OBJ2))


$(ODIR)/%.o: %.cpp $(DEPS)
	g++ -c -o $@ $< $(CFLAGS)


all: z1 z2



z1: $(OBJ1)
	$(CC) -o ../$@ $^ $(CFLAGS)

z2: $(OBJ2)
	$(CC) -o ../$@ $^ $(CFLAGS)
