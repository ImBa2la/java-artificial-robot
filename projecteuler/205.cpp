#include <iostream>
#include <vector>

using namespace std;

typedef vector<double> vd;

void Calc(vd& v, int c, int r) {
    v.clear();
    v.resize(c * r + 1);
    for (int i = 1; i <= c; ++i)
        v[i] = 1.0/c;
    for (int i=2; i <= r; ++i) {
        vd tmp(c*r + 1);
        for (int p=0; p < v.size() - c; ++p)
            for (int j=1; j <= c; ++j)
                tmp[p+j] += v[p]/c;
        v = tmp;
    }
}

template <class C>
void Print(C& c) {
    for (typename C::const_iterator it = c.begin(); it != c.end(); ++it)
        cout << *it << " ";
    cout << endl;
}

int main(void) {
    vd p, c;
    Calc(p, 4, 9);
    Calc(c, 6, 6);
    double pr = 0;
    for (int i = 2; i < p.size(); ++i)
        for (int j=1; j < i; ++j)
            pr += p[i]*c[j];
    cout.precision(9);
    cout << pr << endl;
        
    return 0;
}
