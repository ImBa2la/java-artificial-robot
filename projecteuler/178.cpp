#include <iostream>
#include <vector>

#include <algorithm>

using namespace std;

typedef long long LL;

#define DC 40

int main() {
    int S = 1 << 10;
    int FS = S - 1;
    LL M[DC][10][S];
    for (int i = 0; i < DC; ++i)
        for (int j = 0; j < 10; ++j)
            for (int k = 0; k < S; ++k)
                M[i][j][k] = 0;

    for (int i = 1; i < 10; ++i)
        M[0][i][1 << i] = 1;

    for (int i = 1; i < DC; ++i) {
        for (int d = 0; d < 10; ++d) {
            if (d - 1 >= 0) 
                for (int k = 0; k < S; ++k)
                    M[i][d-1][k | (1 << (d-1))] += M[i-1][d][k];
            if (d + 1 < 10) 
                for (int k = 0; k < S; ++k) 
                    M[i][d+1][k | (1 << (d+1))] += M[i-1][d][k];
                
        }
    }
    LL res = 0;
    for (int i = 0; i < DC; ++i)
        for (int d = 0; d < 10; ++d) 
            res += M[i][d][FS];
    cout << res << endl;

    return 0;
}
