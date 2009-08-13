#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

#define MAX 10000002
vector<int> f(MAX);

int Set(int p) {
    int pp = p;
    if (pp < MAX && f[pp] != 0)
        return f[pp];
    int d = 0;
    while (p > 0) {
        d += (p % 10) * (p % 10);
        p /= 10;
    }
    int x = Set(d);
    if (pp < MAX)
        f[pp] = x;
    return x;
}

int main() {
    f[1] = 1;
    f[89] = 2;
    for (int i = 1; i < 10000000; ++i) 
        Set(i);
    cout << count(f.begin(), f.end(), 2) << endl;
    return 0;
}
