#include <iostream>
#include <vector>

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

struct Result{
    int a;
    int b;
    int c;
    Result( int a, int b, int c) : a(a), b(b), c(c) {}
};

Result egcd(int a, int b)
{
    if( a == 0 )
    {
        return Result(b, 0, 1);
    }
    Result temp = egcd( b%a, a );
    return Result( temp.a, temp.c - ( b / a ) * temp.b, temp.b );
}

int modinv(int b, int n)
{
    Result temp = egcd( b, n );
    if( temp.a == 1 )
    {
        return temp.b % n;
    }
}

long long gcd( long long a, long long b)
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

long long gcd( vector< long long > & multiples )
{
    long long result = multiples[ 0 ];
    for( int i = 1 ; i < multiples.size() ; ++i )
    {
        result = gcd( multiples[ i ], result );
    }
    return result;
}

long long findN( vector< long long > & rands)
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

int main()
{

    LCG lcg(65793, 4282663, 1 << 23, 1);
    vector<long long> rands;

    for( int i = 0 ; i < 10 ; ++i )
    {
        rands.push_back( lcg.rand() );
    }

    long long modulus = findN( rands );
    cout << findN( rands ) << endl;
    cout << (1<<23) << endl;
    int m = (rands[ 2 ] - rands[ 1 ] ) * modinv( rands[ 1 ] - rands[ 0 ], modulus ) % modulus;
    int a = (rands[ 1 ] - rands[ 0 ] * m )% modulus;
    cout << m << endl;
    cout << a + modulus << endl;



    return 0;
}
