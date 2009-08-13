#include <iostream>
#include <cmath>

using namespace std;

inline bool Check(int x) {
    int i = 1;
    int c = 0;
    while (true) {
        int d = x/i;
        if (i > d)
            break;
        if (i * d == x && (i + d) % 4 == 0) {
            ++c;
            if (i != d && 3*i > d)
                return false;
        }
        if (c > 1)
            return false;
        ++i;
    }
    return c == 1;
}

int main() {
    int MAX = 50000000;
    int res = 0;
    for (int i = 1; i < MAX; ++i) 
        if (Check(i)) {
            ++res;
        }
    cout << res << endl;
    return 0;
}
