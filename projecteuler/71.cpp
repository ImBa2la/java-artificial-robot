#include <iostream>
#include <vector>

using namespace std;

typedef long long LL;

void Sub(LL a, LL b, LL c, LL d, LL& x, LL& y) {
    x = a*d - b*c;
    y = c * d;
}

bool Less(LL a, LL b, LL c, LL d) {
    return a*d < b*c;
}

int main() {
    int MAX = 1000000;
    LL a = 1;
    LL b = 1;

    LL n = 1;
    for (int i = 1; i <= MAX; ++i) {
            LL x = (i*3)/7;
            if (i*3 % 7 == 0)
                --x;
            LL ta, tb;
            Sub(3, 7, x, i, ta, tb);
            if (Less(ta, tb, a, b)) {
                n = x;
                a = ta;
                b = tb;
            }
    }
    cout << n << endl;
    return 0;
}
