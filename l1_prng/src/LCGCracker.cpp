#include "../include/LCGCracker.hpp"

Result LCGCracker::egcd(int a, int b)
{
    if( a == 0 )
    {
        return Result(b, 0, 1);
    }
    Result temp = egcd( b%a, a );
    return Result( temp.a, temp.c - ( b / a ) * temp.b, temp.b );
}

int LCGCracker::modinv(int b, int n)
{
    Result temp = egcd( b, n );
    if( temp.a == 1 )
    {
        return temp.b % n;
    }
    return 0;
}

long long LCGCracker::gcd( long long a, long long b)
{
    if( b == 0 )
    {
        return a;
    }
    if( b > a )
    {
        return gcd( b, a );
    }
    return gcd( b, a%b);
}

long long LCGCracker::gcd( vector< long long > & multiples )
{
    long long result = multiples[ 0 ];
    for( int i = 1 ; i < multiples.size() ; ++i )
    {
        result = gcd( multiples[ i ], result );
    }
    return result;
}

long long LCGCracker::findN( vector< long long > & rands)
{
    vector<long long> t;
    for( int i = 1 ; i < rands.size() ; ++i )
    {
        t.push_back( rands[ i ] - rands[ i - 1 ] );
    }
    vector<long long> multiples;

    for( int i = 2 ; i < t.size() ; ++i )
    {
        multiples.push_back( abs(t[ i ] * t[ i - 2 ] - t[ i - 1 ] * t[ i - 1 ]) );
    }

    return gcd( multiples );
}

LCGParams LCGCracker::crack( vector< long long > & rands)
{
    long long n = findN( rands );
    int m = (rands[ 2 ] - rands[ 1 ] ) * modinv( rands[ 1 ] - rands[ 0 ], n ) % n;
    if( m < 0 )
    {
        m += n;
    }
    int c = (rands[ 1 ] - rands[ 0 ] * m )% n;
    if( c < 0 )
    {
        c += n;
    }
    return LCGParams(m, c, n);
}
