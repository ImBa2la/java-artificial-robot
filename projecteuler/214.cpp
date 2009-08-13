#include <iostream>
#include <vector>

#include <algorithm>

using namespace std;

typedef long long LL;

typedef vector<int> vi;
typedef vector<vi> vvi;

int main() {
    int MAX = 40000000;
    int size = 24;

    vi e(MAX);
    vvi t(MAX);
    vi f(MAX);
    vi ff(MAX); 

    LL ans = 0;
    ff[1] = 1;
    for (int i = 2; i < MAX; ++i) {
        if (!e[i]) {
            for (int j = i << 1; j < MAX; j += i) {
                e[j] = 1;
                t[j].push_back(i);
            }
            f[i] = i - 1;
            if (ff[f[i]] == size) {
                ans += i;
            }
        } else {
            vi c;
            c.push_back(t[i][0]);
            for (int j = 1; j < t[i].size(); ++j) {
                int curSize = c.size();
                c.push_back(t[i][j]);
                for (int l = 0; l < curSize; ++l) {
                    c.push_back(-t[i][j]*c[l]);
                }
            }
            f[i] = i;
            for (int j = 0; j < c.size(); ++j) {
                f[i] -= i/c[j];
            }
            t[i].clear();
        }
        ff[i] = 1 + ff[f[i]];
    }
    cout << ans << endl;
    return 0;
}
