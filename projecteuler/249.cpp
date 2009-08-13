#include <iostream>
#include <map>
#include <vector>

using namespace std;

typedef long long i64;
typedef vector<int> vi;

i64 BASE = 10000000000000000LL;

int main(void) {
    int MAXN = 1600000;
    int primes[MAXN];
    vi pr;
    {
        primes[0] = 1;
        primes[1] = 1;
        for(int i = 2; i < MAXN; ++i) {
            if (!primes[i]) {
                pr.push_back(i);
                for (int j = i << 1; j < MAXN; j += i) {
                    primes[j] = 1;
                }
            }
        }
    }
    
    typedef vector<i64> mii;
    mii cur(MAXN);
    cur[0] = 1;
    for (int i = 0; pr[i] < 5000; ++i) {
        mii ne(cur.begin(), cur.end());
        for (int j = 0; j < MAXN - 5000; ++j) {
            ne[j + pr[i]] += cur[j];
            if (ne[j+pr[i]] > BASE)
                ne[j+pr[i]] -= BASE;
        }
        cur = ne;
    }
    i64 sum = 0;
    for (int i = 0; i < MAXN; ++i) {
        if (!primes[i]) {
            sum += cur[i];
            if (sum > BASE)
                sum -= BASE;
        }
    }
    cout << sum << endl;
    return 0;
}
