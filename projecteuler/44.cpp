#include <iostream>
#include <set>
#include <vector>
#include <map>
#include <queue>
#include <algorithm>

using namespace std;


typedef long long LL;

typedef pair<int, int> pii;
typedef pair<LL, pii > pp;

inline bool CheckPentagonal(const vector<LL>& pent, int i, int j) {
    LL sum = pent[i] + pent[i+j];
    LL sub = pent[i+j] - pent[i];
    //cout << i << " " << j << "\t" <<  (int)binary_search(pent.begin(), pent.end(), sub) << (int) binary_search(pent.begin(), pent.end(), sum) << endl;
    return binary_search(pent.begin(), pent.end(), sub) && binary_search(pent.begin(), pent.end(), sum);
}

int main(void) {
    vector<LL> pentagonal;
    for (LL i = 1; i < 1000000; ++i)
        pentagonal.push_back(i*(3*i-1)/2);
    priority_queue<pp> q;
    q.push(pp(-5+1, pii(0, 1)));
    int mP = 0;
    while (!q.empty()) {
        pp t = q.top();
        //cout << t.first << "\t" << t.second.first << " " << t.second.second << endl;
        q.pop();
        if (CheckPentagonal(pentagonal, t.second.first, t.second.second)) {
            cout << -t.first << endl;
            break;
        }
        if (t.second.first == mP) {
            ++mP;
            q.push(pp(-pentagonal[mP+1]+pentagonal[mP], pii(mP, 1)));
        }
        q.push(pp(-pentagonal[t.second.first+t.second.second + 1] + pentagonal[t.second.first], pii(t.second.first, t.second.second + 1)));
    }
    return 0;
}
