#include <iostream>
#include <vector>

#include <algorithm>

using namespace std;

static const double EPS = 1e-9;

typedef vector<int> vi;
typedef vector<vi> vvi;

bool CheckPermutation(int a, int b) {
    vi mA, mB;
    while (a > 0 && b > 0) {
        mA.push_back(a%10);
        mB.push_back(b%10);
        a /= 10;
        b /= 10;
    }
    if (a + b > 0) {
        return false;
    }
    sort(mA.begin(), mA.end());
    sort(mB.begin(), mB.end());
    return mA == mB;
}

int main() {
    int MAX = 10000000;
    vi e(MAX);
    vvi t(MAX);
    vi f(MAX);

    int ans = 87109;
    double r = 87109./79180;
    for (int i = 2; i < MAX; ++i) {
        if (!e[i]) {
            for (int j = i << 1; j < MAX; j += i) {
                e[j] = 1;
                t[j].push_back(i);
            }
            f[i] = i;
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
            if ((double)i/f[i] < r - EPS && CheckPermutation(i, f[i])) {
                ans = i;
                r = (double)i/f[i];
            }
        }
    }
    cout << ans << endl;
    return 0;
}
