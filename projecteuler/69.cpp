#include <iostream>
#include <vector>

#include <algorithm>

using namespace std;

static const double EPS = 1e-9;

typedef vector<int> vi;
typedef vector<vi> vvi;

int main() {
    int MAX = 1000001;
    vi e(MAX);
    vvi t(MAX);
    vi f(MAX);

    int ans = 6;
    double r = 3.;
    for (int i = 2; i < MAX; ++i) {
        if (!e[i]) {
            for (int j = i << 1; j < MAX; j += i) {
                e[j] = 1;
                t[j].push_back(i);
            }
            f[i] = i - 1;
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
        }
        if ((double)i/f[i] > r + EPS) {
            ans = i;
            r = (double)i/f[i];
        }
    }
    cout << ans << endl;
    return 0;
}
