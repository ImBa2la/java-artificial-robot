#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>

using namespace std;

typedef long long LL;

typedef vector<int> vi;
typedef vector<vi> vvi;

int main() {
    int MAX = 20000000 + 1;
    vi a;
    a.push_back(20000000);
    a.push_back(15000000);
    a.push_back(20000000-15000000);
    vi p;
    vvi ap(a.size());
    vi e(MAX);
    for (int i = 2; i < MAX; ++i) {
        if (!e[i]) {
            p.push_back(i);
            for (int j = 0; j < a.size(); ++j) {
                ap[j].push_back(i <= a[j]? 1 : 0);
            }
            int cp = p.size() - 1;

            vi cpw((int)(log(MAX)/log(i)) + 1);
            cpw[0] = 1;
            for (int j = i << 1; j < MAX; j += i) {
                int k = 0;
                ++cpw[k];
                while (cpw[k] == i) {
                    cpw[k] = 0;
                    ++k;
                    ++cpw[k];
                }
                e[j] = 1;
                for (int l = 0; l < a.size(); ++l) {
                    if (j <= a[l]) {
                        ap[l][cp] += (k + 1);
                    }
                }
            }
        }
    }
    LL ans = 0;
    for (int i = 0; i < p.size(); ++i) {
        LL c = ap[0][i] - ap[1][i] - ap[2][i];
        ans += c * p[i];
    }
    cout << ans << endl;
    return 0;
}
