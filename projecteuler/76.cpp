#include <iostream>

using namespace std;


#define MAX 101

int main() {
    int M[MAX][MAX][MAX];
    for (int i=0; i < MAX; ++i)
        for (int j=0; j < MAX; ++j)
            for (int k=0; k < MAX+1; ++k)
                M[i][j][k] = 0;

    for (int i=1; i < MAX; ++i)
        M[0][i][i] = 1;
    for (int i=1; i < MAX; ++i)
        for (int j = 1; j < MAX; ++j)
            for (int a = j; a < MAX; ++a)
                for (int k = 1; k < MAX; ++k)
                    if (k + a <= MAX)
                        M[i][a][k+a] += M[i-1][j][k];
    
    int res = -1;
    for (int i=0; i < MAX; ++i)
        for (int j=1; j < MAX; ++j) {
            res += M[i][j][MAX-1];
            //if (M[i][j][MAX-1])
                //cout << i << " " << j << " " << M[i][j][MAX-1] << endl;
        }
    cout << res << endl;
    return 0;
}
