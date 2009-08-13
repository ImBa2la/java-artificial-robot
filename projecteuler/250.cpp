#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

typedef vector<int> vi;
typedef long long i64;


inline int Sqr(int x) {
    return x * x;
}

int PowerBase(int x, int p, int b) {
    int r = 1;
    if (p & 1) {
        r *= x;
        r %= b;
    }
    int prev = x;
    for (int i = 1;(1 << i) <= p; ++i) {
        prev = Sqr(prev)%b;
        if (p & (1 << i)) {
            r *= prev;
            r %= b;
        }
    }
    return r;
}


int main(void) {
    i64 BASE = 10000000000000000LL;
    int P_BASE = 250;
    int COUNT = 250250;

    i64 M[2][P_BASE];
    for (int i = 0; i < 2; ++i)
        for (int j = 0; j < P_BASE; ++j)
            M[i][j] = 0;

    M[0][0] = 1;
    for (int i = 1; i <= COUNT; i++) {
        for (int j=0; j < P_BASE; ++j)
            M[i%2][j] = M[(i+1)%2][j];
        int pw = PowerBase(i%P_BASE,i, P_BASE); 
        for (int j=0; j < P_BASE; ++j) {
            int p = (j + pw)%P_BASE;
            M[i%2][p] += M[(i+1)%2][j];
            if (M[i%2][p] > BASE)
                M[i%2][p] -= BASE;
        }
    }

    cout << (M[COUNT%2][0]-1) << endl;
}
