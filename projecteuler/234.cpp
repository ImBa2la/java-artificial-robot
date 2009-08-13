#include <iostream>
#include <vector>

using namespace std;

typedef long long LL;
typedef vector<LL> vi;

int main(void) {
    int MAXN = 1000000;
    vi primes(MAXN);
    vi pr;
    for (int i=2; i < MAXN; ++i) {
        if (!primes[i]) {
            pr.push_back(i);
            for (int j = i << 1; j < 1000000; j += i)
                primes[j] = 1;
        }
    }
    LL MX = 999966663333LL;
    LL sum = 0;
    for (int i = 1; i < pr.size(); ++i) {
        LL a = pr[i-1];
        LL b = pr[i];
        b = b * b;
        a = a * a;
        for (LL j = a + pr[i-1]; j < b; j += pr[i-1])
            if (j%pr[i] && j <= MX) {
                sum += j;
            }
        for (LL j = b - pr[i]; j > a; j -= pr[i])
            if (j%pr[i-1] && j <= MX) {
                sum += j;
            }
    }
    cout << sum << endl;
}
