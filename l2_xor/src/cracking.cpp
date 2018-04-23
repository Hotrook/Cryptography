#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

void crack( string & c, vector<vector<int>> & possibleKeys, int start, int stop );
void checkKey( string & c, vector<vector<int>> & possibleKeys, int current, int start, int stop, vector<int>& key );
void printText( string & c, int start, int stop, vector<int> & key );
int convertToNumber( string & c, int position );

int main(){
    int n;
    int start = 0;
    int stop = 170;

    vector<string> ciphertexts = vector<string>(20);

    for( int i = 0 ; i < 20 ; ++i )
    {
        cin >> ciphertexts[ i ] ;
    }


    vector<vector<int>> possibleKeys = vector<vector<int>>(183, vector<int>());

    ifstream myFile;

    myFile.open("keys.txt");
    for( int i = 0 ; i < 183 ; ++i )
    {
        int size;
        myFile >> size;
        for( int j = 0 ; j < size ; ++j )
        {
            int element;
            myFile >> element;
            possibleKeys[ i ].push_back( element );
        }
    }
    myFile.close();

    for( int i = 19 ; i <= 19 ; ++i )
    {
        crack( ciphertexts[i], possibleKeys, start, stop );
    }
}

void crack( string & c, vector<vector<int>> & possibleKeys, int start, int stop )
{
    vector<int> key = vector<int>(stop - start + 1);
    checkKey( c, possibleKeys, start, start, stop, key );
}

void checkKey( string & c, vector<vector<int>> & possibleKeys, int current, int start, int stop, vector<int> & key )
{
    if( current > stop )
    {
        printText( c, start, stop,key );
        return;
    }
    for( auto k : possibleKeys[ current ] )
    {
        key[ current - start ] = k;
        checkKey( c, possibleKeys, current + 1, start, stop, key );
    }
}

void printText( string & c, int start, int stop, vector<int> & key )
{
    for( int i = start ; i < stop ; ++i ){
        if( i < c.length( ) )
        {
            int position = i * 8;
            int number = convertToNumber(c, position);
            int xorNumber = number ^ key[ i - start ];
            cout << char(xorNumber);
        }
    }

    cout << endl;
}

int convertToNumber( string & c, int position )
{
    int m = 1;
    int result = 0;

    for( int pos = position + 7 ; pos >= position ; --pos )
    {
        if( c[ pos ] == '1' )
        {
            result += m;
        }
        m *= 2;
    }

    return result;
}
