#include <iostream>

using namespace std;

int Calc(int a) {
    int m = 2;
    int mod = a*a;
    int ap = a - 1;
    int an = a + 1;
    int x = 1, y = 1;
    for (int i = 0; i < 1000000; ++i) {
        x *= ap; x %= mod;
        y *= an; y %= mod;
        m = max(m, (x+y) % mod);
    }
    return m;
}

int main() {
    int res = 0;
    for (int a = 3; a <= 1000; ++a)
        res += Calc(a);
    cout << res << endl;
    return 0;
}
