#include <iostream>
#include <map>

using namespace std;

struct triple {
    int a,b,c;

    triple(int na, int nb, int nc) {
        a = na;
        b = nb;
        c = nc;
    }

    bool operator<(const triple& t) const {
        if (a != t.a)
            return a < t.a? true: false;
        if (b != t.b)
            return b < t.b? true: false;
        return c < t.c;
    }
};

typedef long long LL;

typedef map<triple, LL> ml; 

int main(void) {
    ml cur;
    for (int i1=1; i1 < 10; ++i1)
        for (int i2=0; i2<10; ++i2)
            for (int i3=0;i3<10;++i3)
                if(i1 + i2 +i3 <= 9)
                    cur[triple(i1,i2,i3)] = 1;

    for (int i = 4; i <= 20; ++i) {
        ml nw;
        for (ml::const_iterator it = cur.begin(); it != cur.end(); ++it) {
            triple k = it->first;
            for (int d = 0; d < 10; ++d) {
                if (k.b + k.c + d <= 9) {
                    nw[triple(k.b, k.c, d)] += it->second;
                }
            }
        }
        cur = nw;
    }
    LL total = 0;
    for (ml::const_iterator it = cur.begin(); it != cur.end(); ++it)
        total += it->second;
    cout << total << endl;
    return 0;
}
