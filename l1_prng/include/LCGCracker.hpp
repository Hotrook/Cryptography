#ifndef L1_PRNG_LCG_CRACKER
#define L1_PRNG_LCG_CRACKER

#include <vector>

using namespace std;

struct Result{
    int a;
    int b;
    int c;
    Result( int a, int b, int c) : a(a), b(b), c(c) {}
};

struct LCGParams{
    long long m;
    long long c;
    long long n;
    LCGParams( long long a, long long b, long long c ) : m(a), c(b), n(c){}
};

class LCGCracker
{
private:
    Result egcd(int a, int b);
    int modinv(int b, int n);
    long long gcd( long long a, long long b);
    long long gcd( vector< long long > & multiples );

public:
    long long findN( vector< long long > & rands);
    LCGParams crack( vector< long long > & rands);
};


#endif
