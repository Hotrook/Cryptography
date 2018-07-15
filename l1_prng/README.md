# LCG cracker

This is a simple app that lets you crack LCG pseudorandom generator(you can read about LCG [here](https://en.wikipedia.org/wiki/Linear_congruential_generator).

App is based on algorithm described [here](https://tailcall.net/blog/cracking-randomness-lcgs/)


# Usage

After compiling this app by:
```console
foo@bar:~$ cd src
foo@bar:src~$ make
```

Two exucatables will be produced:
- `app1` - it cracks LCG implemented in `LCGCracker`
- `app2` - it cracks `rand()` from `glibc`

To run this apps type:
```console
foo@bar:~$ ./app1
foo@bar:~$ ./app2
```
