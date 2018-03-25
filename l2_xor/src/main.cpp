#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

bool checkKey( vector<string> & ciphertexts, int position, int key);
bool checkCiphertext( string & c, int position, int key );
int convertToNumber( string & c, int position );

int main(){
    int n;
    int maxLength = 0 ;
    int minLength = 1 << 30;

    vector<string> ciphertexts = vector<string>(20);

    for( int i = 0 ; i < 20 ; ++i )
    {
        cin >> ciphertexts[ i ] ;
        if( ciphertexts[ i ].length() > maxLength )
        {
            maxLength = ciphertexts[ i ].length();
        }
        if( ciphertexts[ i ].length() < minLength )
        {
            minLength = ciphertexts[ i ].length();
        }
    }

    cout << maxLength << endl;

    vector<vector<int>> possibleKeys = vector<vector<int>>(maxLength / 8, vector<int>());

    for( int position = 0 ; position < maxLength ; position += 8 )
    {
        for( int key = 0 ; key < 256 ; ++key )
        {
            if( checkKey( ciphertexts, position, key) ){
                possibleKeys[ position/8 ].push_back(key);
            }
            printf("Position: %10d, Key: %10d\r", position, key );
        }

    }

    ofstream myFile;

    myFile.open("keys.txt");
    for( auto keys : possibleKeys )
    {
        myFile << keys.size() << " ";
        for( auto key : keys )
        {
            myFile << key << " ";
        }
        myFile << "\n";
    }
    myFile.close();

    return 0;
}

bool checkKey( vector<string> & ciphertexts, int position, int key)
{
    bool result = true;
    for( auto c : ciphertexts )
    {
        result = result && checkCiphertext( c, position, key );
    }
    return result;
}

bool checkCiphertext( string & c, int position, int key )
{
    if( position + 8 > c.length() )
        return true;

    int number = convertToNumber( c, position );
    int xorNumber = number ^ key;

    if(
        (xorNumber >= 48 && xorNumber <= 57 )
        || ( xorNumber >= 32 && xorNumber <= 95 )
        || ( xorNumber >= 97 && xorNumber <= 127 )
        || ( xorNumber == 32 || xorNumber == 33 ) //  space and !
        || ( xorNumber >= 44 && xorNumber <= 46 ) // , - .
        || ( xorNumber == 58 ) // :
        || ( xorNumber == 59 ) // :
        || ( xorNumber == 39 ) // '
        || ( xorNumber == 63 )
        || ( xorNumber == 96 )
        || ( xorNumber == 34 )
        || ( xorNumber == 40 || xorNumber == 41 ) // braces
        )
    {
        return true;
    }
    return false;
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
