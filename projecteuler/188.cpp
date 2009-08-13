#include <iostream>
#include <map>

using namespace std;

typedef long long LL;

template <class T>
inline T Sqr(T a) {
    return a * a;
}

LL PowerBase(LL x, LL p, LL b) {
    LL r = 1;
    if (p & 1) {
        r *= x;
        r %= b;
    }
    LL prev = x;
    for (int i = 1;(1 << i) <= p; ++i) {
        prev = Sqr(prev)%b;
        if (p & (1 << i)) {
            r *= prev;
            r %= b;
        }
    }
    return r;
}

LL Hyper (LL a, LL p, LL b) {
    if (b == 1)
        return 0;
    if (p == 1)
        return a%b;
    map<LL, int> m;
    LL x = 1;
    int ind = 0;
    for (; m.find(x) == m.end(); ++ind) {
        m[x] = ind;
        x *= a;
        x %= b;
    }
    cout << ind << endl;
    return PowerBase(a, Hyper(a, p-1, ind), b);
}

int main() {
    cout << Hyper(1777, 1855, 100000000) << endl;
    return 0;
}
