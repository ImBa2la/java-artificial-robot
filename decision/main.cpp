#include <cmath>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <utility>

using namespace std;

static const double EPS = 1e-9;

typedef vector<double> TVD;
typedef vector<TVD> TM;
typedef pair<int, int> pii;

template <class T>
static inline T Abs(T a) {
    return a < 0 ? -a: a;
}

template <class T>
static inline T Sqr(T a) {
	return a * a;
}

template <class TCol>
void Print(const TCol& col) {
    bool first = true;
    for (typename TCol::const_iterator it = col.begin(); it != col.end(); ++it) {
        if (!first)
            cout << " ";
        cout << *it;
        first = false;
    }
    cout << endl;
}


struct TOption {
    TVD u;

    TOption(int size):
        u(size)
    {
    }

    friend istream &operator>>(istream &stream, TOption &ob);
};

struct TPlane {
    TVD norm;
    double k;
};

istream &operator>>(istream &stream, TOption &ob) {
  for (size_t i = 0; i < ob.u.size(); ++i)
      stream >> ob.u[i];
  return stream;
}

typedef vector<TOption> TOptions;
typedef vector<TPlane> TPlanes;
typedef vector<pii> TPrefs;

static inline double Mult(const TVD& a, const TVD& b) {
    double r = 0;
    for (size_t i = 0; i < a.size(); ++i)
        r += a[i] * b[i];
    return r;
}

static void Add(TVD& a, const TVD& b) {
    for (size_t i = 0; i < a.size(); ++i)
        a[i] += b[i];
}

static void Mult(TVD& a, const double m) {
    for (size_t i = 0; i < a.size(); ++i)
        a[i] *= m;
}


static inline double Norm(const TVD& v) {
    double d = 0;
    for (size_t i =0; i < v.size(); ++i)
        d += v[i] * v[i];
    return sqrt(d);
}

static inline double Dist(const TVD& a, const TVD& b) {
	double d = 0;
	for (size_t i = 0; i < a.size(); ++i)
		d += Sqr(a[i] - b[i]);
	return sqrt(d);
}

static inline double Dist(const TVD& point, const TPlane& plane) {
    double r = -plane.k;
    r += Mult(plane.norm, point);
    return Abs(r)/Norm(plane.norm);
}

static inline bool CheckUnequation(const TVD& point, const TPlane& plane) {
    return Mult(plane.norm, point) > plane.k;
}

struct TChoser {
    int N, M, P;
    TOptions options;
    TVD w;
    TPrefs b;

    TPlanes planes;

    void Init() {
        for (int i = 0; i < P; ++i) {
            planes.push_back(TPlane());
            TVD& current = planes.back().norm;
            double un = options[b[i].first-1].u[M-1] - options[b[i].second-1].u[M-1];
            for (int j=0; j < M - 1; ++j) {
                current.push_back(options[b[i].first-1].u[j] - options[b[i].second-1].u[j] - un);
            }
            planes.back().k = -un;
        }
        // Wi > 0 - border 
        for (int i = 0; i < M - 1; ++i) {
            planes.push_back(TPlane());
            TVD& current = planes.back().norm;
            current.resize(M-1);
            current[i] = 1;
        }
        
    }

    struct TCmp {
        TVD& p;

        TCmp(TVD& mp) :
            p(mp)
        {}

        bool operator() (const TPlane& a, const TPlane& b) {
            return Dist(p, a) < Dist(p, b);
        }
    };


    bool MakeCheck() {
        for (int iter = 0; iter < 100000; ++iter) {
            bool was = false;
            for (size_t i = 0; i < planes.size(); ++i) {
                bool ch = CheckUnequation(w, planes[i]);
                if (ch)
                    continue;
                was = true;
                double d = (Dist(w, planes[i]) + EPS)/Norm(planes[i].norm);
                for (int j = 0; j < M - 1; ++j) {
                    w[j] += d * planes[i].norm[j];
                }
                break;
            }
//            Print(w);
            if (!was)
                return true;
        }
        return false;
    }

    double Radius(TVD& point, TVD& n) {
        double md = 1e10;
        for (size_t i = 0; i < planes.size(); ++i) {
            if (Dist(point, planes[i]) < md) {
                md = Dist(point, planes[i]);
            }
        }
        n.clear();
        for (size_t i = 0; i < planes.size(); ++i) {
        	if (Abs(Dist(point, planes[i]) - md) < EPS) {
        		if (n.empty()) {
        			n = planes[i].norm;
        	        Mult(n, 1.0/Norm(n));        			
        		} else {
        			TVD x = planes[i].norm;
        	        Mult(x, 1.0/Norm(x));
        	        Add(n, x);
        		}        		
        	}
        }
        return md;
    }

    static void Point(TVD& spoint, const TVD& n, double t, TVD& p) {
        p = n;
        Mult(p, t);
        Add(p, spoint);
    }

    double Radius(TVD& spoint, const TVD& n, double t, TVD& r) {
        TVD x;
        Point(spoint, n, t, x);
        return Radius(x, r);
    }


    void Optimize() {
        // polygon is convex
        TVD n;
        Radius(w, n);
        for (int ind = 0; ind < 1000; ++ind) {
            double t = 1e9;
            for (size_t i = 0; i < planes.size(); ++i) {
                double tt = -(Mult(planes[i].norm, w) - planes[i].k)/Mult(planes[i].norm, n);
                if (tt > 2*EPS) {
                    t = min(t, tt);
                }
            }
            // Gold ratio method
            const double f = (sqrt(5.) + 1)/2;
            double a = 10*EPS, b = t - 10*EPS;
            // Can be optimized
            TVD nn;
            while (b - a > EPS) {
            	TVD n1, n2;
            	double x1 = b - (b-a)/f;
                double x2 = a + (b-a)/f;

                double r1 = Radius(w, n, x1, n1);
                double r2 = Radius(w, n, x2, n2);
                cout << "R: " << r1 << "\t" << r2 << endl;
                if (r1 > r2) {
                    b = x2;
                    nn = n2;
                } else {
                    a = x1;
                    nn = n1;
                }
            }
            TVD x;
            Point(w, n, a, x);
            Print(x);
            if (Dist(w, x) < EPS)
            	break;
            w = x;
            n = nn;
        }
    }

    bool CheckPrefs() {
        for (int i = 0; i < P; ++i) {
            if (!(Mult(planes[i].norm, w) > planes[i].k)) {
                return false;
            }
        }
        return true;
    }

    friend istream &operator>>(istream &stream, TChoser &ob);
};

istream &operator>>(istream &stream, TChoser &ob) {
	stream >> ob.N >> ob.M >> ob.P;
	ob.w.resize(ob.M, 1./ob.M);
	for (int i=0; i < ob.N; ++i) {
		ob.options.push_back(TOption(ob.M));
		stream >> ob.options.back();
	}
	for (int i = 0; i < ob.P; ++i) {
		ob.b.push_back(pii());
		stream >> ob.b.back().first >> ob.b.back().second;
	}
	return stream;
}


void LoadData(TChoser& input, const char* fileName) {
    ifstream in(fileName);
    in >> input;
}

int main(void) {
    TChoser input;
    LoadData(input, "input.in");
    input.Init();
    input.MakeCheck();
    Print(input.w);
    input.Optimize();
    Print(input.w);
}
