#include <iostream>
#include <vector>

using namespace std;

typedef vector<int> vi;

int main() {
    int MAX_C = 1000000;
    vi v(MAX_C);
    vi primes;
    for (int i=2; i < v.size(); ++i) {
        if (!v[i]) {
            primes.push_back(i);
            for (int j = i << 1; j < v.size(); j += i)
                v[j] = 1;
        }
    }

    int size = primes.size();
    int maxc = 0;
    int maxv = 0;
    for (int i = 0; i < size; ++i) {
        int sum = 0;
        for (int j = i; j < size; ++j) {
            sum += primes[j];
            if (sum > MAX_C)
                break;
            if (!v[sum] && j - i + 1 > maxc) {
                maxc = j - i + 1;
                maxv = sum; 
            }                
        }
    }
    cout << maxv << "\t" << maxc << endl;
    return 0;
}
