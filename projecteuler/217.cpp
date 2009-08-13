#include <iostream>

using namespace std;

typedef long long i64;

inline int Power(int a, int b, int base = 1000000000) {
    int r = 1;
    for (int i=0; i < b; ++i) {
        r *= a;
        r %= base;
    }
    return r;
}

int Lucky(int s) {
    if (s == 1)
        return 45;
    int base = Power(3, 15);
    int c[2][9*47];
    int cc[2][9*47];
    int m[2][9*47];
    int im[2][9*47];
    for (int i=0; i < 2; ++i)
        for(int j=0; j < 9*47; ++j) {
            m[i][j] = 0;
            im[i][j] = 0;
            c[i][j] = 0;
            cc[i][j] = 0;
        }

    cc[0][0] = 1;
    for (int i=1; i< 10; i++) {
        m[0][i] = i;
        im[0][i] = i;
        c[0][i] = 1;
        cc[0][i] = 1;
    }
    for (int i = 1; i < s/2; ++i) {
        for (int j = 0; j < 9*46; j++) {
            m[i%2][j] = 0;
            im[i%2][j] = 0;
            c[i%2][j] = 0;
            cc[i%2][j] = 0;
        }
        for (int j = 0; j < 9*46; j++)
            for (int d = 0; d < 10; ++d) {
                c[i%2][j+d] += c[(i+1)%2][j];
                c[i%2][j+d] %= base;
                cc[i%2][j+d] += cc[(i+1)%2][j];
                cc[i%2][j+d] %= base;
                if (c[(i+1)%2][j]) {
                    m[i%2][j+d] += m[(i+1)%2][j]*10 + d*c[(i+1)%2][j];
                    m[i%2][j+d] %= base;
                }
                if (cc[(i+1)%2][j]) {
                    im[i%2][j+d] += im[(i+1)%2][j]*10 + d*cc[(i+1)%2][j];
                    im[i%2][j+d] %= base;
                }
            }
    }
    int t = 0;
    for (int i=0; i < 9*47;++i) {
        i64 k = c[(s/2+1)%2][i];
        i64 ck = cc[(s/2+1)%2][i];
        i64 x = m[(s/2+1)%2][i];
        i64 ix = im[(s/2+1)%2][i];

        i64 sm = 0;
        if (s%2) {
            sm = x*10*ck*10;
            sm += 45*k*ck;
        } else
            sm = x * ck;
        sm %= base;
        sm = (sm * Power(10, s/2, base))%base;
        if (s%2)
            sm += ix * k * 10;
        else
            sm += ix * k;
        sm %= base;
        t += sm;
        t %= base;
    }
    return t;
}

int main(void) {
    int t = 0;
    for (int i=1; i <=47; ++i) {
        t += Lucky(i);
        t %= Power(3, 15);
    }
    cout << t << endl;
    return 0;
}
