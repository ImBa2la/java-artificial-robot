#!/usr/local/bin/python

total = 0
for i in xrange(1,1000000001):
    s = str(i)[::-1]
    if s[0] != '0':
        s = str(i + long(s))
        all = True
        for i in xrange(0, len(s)):
            if (ord(s[i]) - ord('0'))%2 == 0:
                all = False
                break
        if all:
            total += 1
print total
