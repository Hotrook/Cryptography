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

    cout << "m: " << lcgParams.m + lcgParams.n << endl;
    cout << "c: " << lcgParams.c << endl;
    cout << "n: " << lcgParams.n << endl;

    cout << "Next rand using calculated arguments: " << ( rands[ 9 ] * lcgParams.m + lcgParams.c ) % lcgParams.n << endl;
    cout << "Next rand using rand(): " << rand() << endl;

    return 0;
}
