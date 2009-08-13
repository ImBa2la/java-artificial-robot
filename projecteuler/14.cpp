#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;

typedef long long LL;
typedef vector<int> vi;

int MAX = 1000000;
vi M(MAX);

int Down(LL i) {
    if (i < MAX && M[i] > 0)
        return M[i];
    int mx = (i & 1? Down(3*i+1): Down(i>>1)) + 1;
    if (i < MAX)
        M[i] = mx;
    return mx;    
}

int main() {
    M[1] = 1;
    for (int i = 2; i < MAX; ++i) {
        Down(i);
    cout << (max_element(M.begin(), M.end()) - M.begin());
    return 0;
}
