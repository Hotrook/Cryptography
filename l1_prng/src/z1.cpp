#include <iostream>
#include <vector>
#include "../include/LCGCracker.hpp"

using namespace std;

class LCG
{
private:
    long long a;
    long long m;
    long long n;
    long long prev;

public:
    LCG(long long a, long long m, long long n, long long seed):a(a), m(m), n(n), prev(seed){}
    long long rand()
    {
        long long result = ( a * prev + m ) % n;
        prev = result;
        return result;
    }
};

int main()
{

    LCG lcg(65793, 4282663, 1 << 23, 1);
    vector<long long> rands;

    for( int i = 0 ; i < 10 ; ++i )
    {
        rands.push_back( lcg.rand() );
    }

    LCGCracker cracker;
    LCGParams lcgParams = cracker.crack(rands);

    cout << lcgParams.m << endl;
    cout << lcgParams.c << endl;
    cout << lcgParams.n << endl;

    return 0;
}
