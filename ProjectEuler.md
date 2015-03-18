# Solutions of Euler problems


# Task 1 #
```
Plus @@ Select[Range[999], (Mod[#, 3] == 0 || Mod[#, 5] == 0) &]
```
233168

# Task 2 #
```
Fib = {1, 1};
T = 0;
For[i = 2, i < 1000, i++, 
 Fib[[Mod[i + 1, 2] + 1]] = Fib[[1]] + Fib[[2]]; 
 x = Fib[[Mod[i + 1, 2] + 1]];
 If[x >= 4000000, Break[]];
 If[Mod[x, 2] == 0, T += x]]
T
```
4613732

# Task 3 #
```
n = 600851475143;
x = IntegerPart[Sqrt[n]];
While[Mod[n, x] > 0, x = NextPrime[x, -1]];
x
```
6857

# Task 4 #
```
p = 0;
For[i = 10, i < 1000, i++,
  For[j = 10, j < 1000, j++, x = i*j; 
   If[x > p && ToString[x] == StringReverse[ToString[x]], p = x]]];
p
```

# Task 5 #
```
LCM @@ Range[20]
```
# Task 6 #
```
Total[Range[100]]^2 - Total[Range[100]^2]
```

# Task 7 #
```
Prime[10001]
```

# Task 8 #
```
Max @@ Map[Apply[Times, #] &, 
  Partition[
   Map[ToExpression, 
    StringSplit[
     "7316717653133062491922511967442657474235534919493496983520312774\
5063262395783180169848018694788518438586156078911294949545950173795833\
1952853208805511125406987471585238630507156932909632952274430435576689\
6648950445244523161731856403098711121722383113622298934233803081353362\
7661428280644448664523874930358907296290491560440772390713810515859307\
9608667017242712188399879790879227492190169972088809377665727333001053\
3678812202354218097512545405947522435258490771167055601360483958644670\
6324415722155397536978179778461740649551492908625693219784686224828397\
2241375657056057490261407972968652414535100474821663704844031998900088\
9524345065854122758866688116427171479924442928230863465674813919123162\
8245861786645835912456652947654568284891288314260769004224219022671055\
6263211111093705442175069416589604080719840385096245544436298123098787\
9927244284909188845801561660979191338754992005240636899125607176060588\
6116467109405077541002256983155200055935729725716362695618826704282524\
83600823257530420752963450", ""]], 5, 1]]
```

# Task 9 #
```
For[a = 1, a < 1000, a++,
 For[b = a, b < 1000, b++,
  c = IntegerPart[Sqrt[a^2 + b^2]];
  If[a + b + c == 1000 && c^2 == a^2 + b^2, Print[a*b*c]]
  ]]
```

# Task 10 #
```
x = 2;
t = 0;
While[x < 2000000, t += x; x = NextPrime[x]];
t
```

# Task 39 #
```
SortBy[ Tally[Map[Total, Select[Join @@ Map[Map[{Last[#]^2 - First[#]^2, 2*First[#]*Last[#], Last[#]^2 + First[#]^2} &, Select[Join @@ Table[{i, j}, {i, 1, 25}, {j, i + 1, 25}], CoprimeQ @@ # &]]*# &, Range[83]], Total[#] <= 1000 &]]], Last]
```

# Task 47 #
```
n = 4;
For[i = 10, True, i++,
 If[And @@ 
   Map[Length[FactorInteger[#]] == n &, Table[i + j - 1, {j, n}]], 
  Print[i]; Break[]]
 ]
```

# Task 48 #
```
Mod[Total[Table[PowerMod[i, i, 10^11], {i, 1000}]], 10^10]
```

# Task 119 #
```
Union[First /@  Select[Join @@ Table[{i^j, i}, {i, 1, 9*20}, {j, 20}],  Last[#] == Total[IntegerDigits[ First[#]]] && First[#] > 10 &]][[30]]
```

# Task 204 #
```
#include <util/stream/ios.h>
#include <util/stream/ios.h>
#include <util/generic/pair.h>
#include <util/generic/set.h>
#include <util/generic/vector.h>



typedef ypair<size_t, size_t> pii;
typedef yvector<size_t> vi;
typedef yset<size_t> si;

void CalcPrimes(int n, vi& primes) {
    yvector<int> m(n); // shoutl be bitset
    for (int i = 2; i < n; ++i) {
        if (m[i] == 0) {
            primes.push_back(i);
            int x = i << 1;
            while (x < n) {
                m[x] = 1;
                x += i;
            }
        }
    }
}

int main(int argc, char** argv) {
    vi primes;
    CalcPrimes(100, primes);
    vi max_powers;
    size_t MAXN = 1000000000;
    si numbers;
    numbers.insert(1);
    size_t count = 0;
    while (!numbers.empty()) {
        size_t x = *numbers.begin();
        numbers.erase(numbers.begin());
        for (vi::const_iterator it = primes.begin(); it != primes.end(); ++it) {
            if (*it * x <= MAXN) {
                numbers.insert(*it * x);
            }
        }
        ++ count;
    }
    Cout << count << Endl;
    return 0;
}
```

# Task 203 #
```
Total[Select[Union[Join @@ Table[Binomial[n, k], {n, 0, 50}, {k, 0, n}]], Max[Last /@ FactorInteger[#]] < 2 &]]
```