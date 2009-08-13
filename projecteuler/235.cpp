#include <iostream>

using namespace std;

double Calc(double r) {
    double s = 0;
    double b = 1;
    for (int i = 1; i <= 5000; ++i) {
        double a = 900 - 3*i;
        s += a*b;
        b *= r;
    }
    return s + 600000000000.0;
}

int main(void) {
    double a = 1, b = 1.1;
    cout << Calc(a) << "\t" << Calc(b) << endl;

    while (b - a > 1e-14) {
        double c = (a + b)/2;
        if (Calc(c) > 0)
            a = c;
        else
            b = c;
    }
    cout << a << endl;
    return 0;
}
