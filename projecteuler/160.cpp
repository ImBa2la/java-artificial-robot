#include <iostream>

using namespace std;

typedef long long LL;

int Fac(int n, int mod) {
    LL r = 1;
    for (int i = 1; i <= n; ++i) {
        LL x = i;
        while (x%10 == 0)
            x /= 10;
        r *= x;
        while (r%10 == 0)
            r /= 10;
        r %= mod;
        //cout << "\t" << i << " " << r << endl;
    }
    return r;
}

int main() {
    int MOD = 10;
    int s = 5;
    LL x = Fac(100, MOD);

    LL r = 1;
    for (int i = 1; i <= 10; ++i) {
        r *= x;
        r *= i;
        r %= MOD;


        cout << r << " = " << Fac((i+1)*10, MOD) << endl;
        if (i > 4)
            break;
    }



    cout << x << endl;
    while (s < 12) {
        LL r = 1;
        for (int i = 0; i<= 9; ++i) {
            r *= x;
            r %= MOD;
        }
        x = r;
        cout << x << endl;
        ++s;
    }
    cout << x << endl;
    return 0;
}
