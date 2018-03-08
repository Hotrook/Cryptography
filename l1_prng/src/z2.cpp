#include <iostream>
#include <cstdlib>
#include <vector>
#include "../include/LCGCracker.hpp"

using namespace std;

int main(){

    srand( 12323 );

    vector< long long > rands;

    for( int i = 0 ; i < 10 ; ++i )
    {
        rands.push_back( rand() );
    }

    LCGCracker cracker;
    LCGParams lcgParams = cracker.crack(rands);

    cout << lcgParams.m + lcgParams.n << endl;
    cout << lcgParams.c << endl;
    cout << lcgParams.n << endl;

    cout << ( rands[ 9 ] * lcgParams.m + lcgParams.c ) % lcgParams.n << endl;
    cout << rand() << endl;

    return 0;
}
