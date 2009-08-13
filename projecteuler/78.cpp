#include <iostream>
#include <vector>

using namespace std;

typedef int LL;

typedef vector<LL> vl;
typedef vector<vl> vvl;
typedef vector<vvl> vvvl;


void Calc() { 
    int MOD = 1000000;
    int MAX = 6;
    vl R(MAX);
    vvvl B(2, vvl(MAX, vl(MAX)));
    for (int i = 1; i < MAX; ++i) {
        for (int j = i; j < MAX; ++j)
            B[0][i][j] = 1;
        R[i] += 1;
    }
    for (int i = 1; i < MAX; ++i) {
        int cur = i%2;
        int prev = (i-1)%2;
        for (int n = 1; n < MAX; ++n) {
            B[cur][n][1] = B[prev][n-1][1];
            for (int m = 1; m < MAX; ++m) {
                B[cur][n][m] = B[cur][n][m-1] + (n>m?B[prev][n-m][m]:0);
                B[cur][n][m] %= MOD;
            }
            R[n] += B[cur][n][n];
            R[n] %= MOD;
        }
        cout << i << " " << R[i] << endl;
    }
}

int main() {
    //Calc();
    int MAX = 100000;
    int MOD = 1000000;
    vvl B(2, vl(MAX));
    for (int n = 1; n < MAX; ++n)
        B[1][n] = 1;
    for (int m = 2; m < MAX; ++m) {
        int cur = m%2;
        int prev = (m-1)%2;
        B[cur][m] = B[prev][m] + 1;
        B[cur][m] %= MOD;
        for (int n = m+1; n < MAX; ++n) {
            if (n-m < m)
                B[cur][n-m] = B[prev][n-m];
            B[cur][n] = B[prev][n] + B[cur][n-m];
            B[cur][n] %= MOD;
        }
        //cout  << m << " " << B[cur][m] << endl;
        if (B[cur][m] == 0) {
            cout << m << endl;
            break;
        }
    }
    return 0;
}
