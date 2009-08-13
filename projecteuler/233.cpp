#include <iostream>

using namespace std;

typedef long long LL;

inline LL Sqr(LL a) {
    return a * a;
}

bool Check(LL x) {
    LL a = 1;
    LL b = 2;
    while (Sqr(a) + Sqr(b) < x)
        ++b;
    while (a < b) {
        cout << a << "\t" << b << endl;
        if (Sqr(a) + Sqr(b) == x) {
            cout << a << "\t" << b << "\t" << x << endl;
            return true;
        }
        ++a;
    }
    return false;
}

int main () {
    LL s = 0;
    cout << (int) Check(25) << endl;
    //for (LL i = 10; i < 300000; ++i)
        //if (Check(2*i*i)) 
            //s += i*i;
    //cout << s << endl;
    return 0;
}
