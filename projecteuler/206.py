#!/usr/local/bin/python

def Calc(x, t):
    x = x * x
    s = str(x)
    if len(s) < len(t):
        return -1
    if len(s) > len(t):
        return 1
    for i in xrange(0, len(t)):
        if t[i] == '_':
            continue
        if s[i] < t[i]:
            return -1
        if s[i] > t[i]:
            return 1
    return 0

def Check(t):
    a = 1
    b = 111111111111
    while b-a>1:
        c = (a+b)/2
        cm = Calc(c, t)
        if cm < 0:
            a = c
        elif cm > 0:
            b = c
        else:
            break
    print t
    print a
    ca = Calc(a, t)
    cb = Calc(b, t)
    if ca == 0:
        print a
    if cb == 0:
        print b


def Rec(templ, ind):
    if ind > 13:
        for i in xrange(0, 10):
            Rec(templ[0:ind] + chr(ord('0') + i) + templ[ind+1:], ind-2)
    else:
        Check(templ)
            
Rec("1_2_3_4_5_6_7_8_9_0", 17)

