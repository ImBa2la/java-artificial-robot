#include <iostream>
#include <set>

using namespace std;

typedef pair<int, int> pii;
typedef set<pii> sii;

int main() {
    int MAX = 10000;

    sii m;
    int res = 0;
    for (int i = 1; i <= MAX; ++i) {
        int s = 2*i;
        int f = min(3*i-1, MAX);
        for (int j = s+1; j <= f; ++j) 
            if (m.find(pii(i, j)) == m.end()) {
                res += 1;
                for (int kj = j << 1, ki = i << 1; kj <= MAX; kj += j, ki += i)
                    m.insert(pii(ki, kj));
            }
                
        
    }
    cout << res << endl;
    return 0;
}
