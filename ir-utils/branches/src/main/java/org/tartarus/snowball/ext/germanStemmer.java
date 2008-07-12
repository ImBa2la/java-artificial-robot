// This file was generated automatically by the Snowball to Java compiler

package org.tartarus.snowball.ext;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.Among;

/**
 * Generated class implementing code defined by a snowball script.
 */
public class germanStemmer extends SnowballProgram {

        private Among a_0[] = {
            new Among ( "", -1, 6, "", this),
            new Among ( "U", 0, 2, "", this),
            new Among ( "Y", 0, 1, "", this),
            new Among ( "\u00E4", 0, 3, "", this),
            new Among ( "\u00F6", 0, 4, "", this),
            new Among ( "\u00FC", 0, 5, "", this)
        };

        private Among a_1[] = {
            new Among ( "e", -1, 1, "", this),
            new Among ( "em", -1, 1, "", this),
            new Among ( "en", -1, 1, "", this),
            new Among ( "ern", -1, 1, "", this),
            new Among ( "er", -1, 1, "", this),
            new Among ( "s", -1, 2, "", this),
            new Among ( "es", 5, 1, "", this)
        };

        private Among a_2[] = {
            new Among ( "en", -1, 1, "", this),
            new Among ( "er", -1, 1, "", this),
            new Among ( "st", -1, 2, "", this),
            new Among ( "est", 2, 1, "", this)
        };

        private Among a_3[] = {
            new Among ( "ig", -1, 1, "", this),
            new Among ( "lich", -1, 1, "", this)
        };

        private Among a_4[] = {
            new Among ( "end", -1, 1, "", this),
            new Among ( "ig", -1, 2, "", this),
            new Among ( "ung", -1, 1, "", this),
            new Among ( "lich", -1, 3, "", this),
            new Among ( "isch", -1, 2, "", this),
            new Among ( "ik", -1, 2, "", this),
            new Among ( "heit", -1, 3, "", this),
            new Among ( "keit", -1, 4, "", this)
        };

        private static final char g_v[] = {17, 65, 16, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 32, 8 };

        private static final char g_s_ending[] = {117, 30, 5 };

        private static final char g_st_ending[] = {117, 30, 4 };

        private int I_x;
        private int I_p2;
        private int I_p1;

        private void copy_from(germanStemmer other) {
            I_x = other.I_x;
            I_p2 = other.I_p2;
            I_p1 = other.I_p1;
            super.copy_from(other);
        }

        private boolean r_prelude() {
            int v_1;
            int v_2;
            int v_3;
            int v_4;
            int v_5;
            int v_6;
            // (, line 28
            // test, line 30
            v_1 = cursor;
            // repeat, line 30
            replab0: while(true)
            {
                v_2 = cursor;
                lab1: do {
                    // (, line 30
                    // or, line 33
                    lab2: do {
                        v_3 = cursor;
                        lab3: do {
                            // (, line 31
                            // [, line 32
                            bra = cursor;
                            // literal, line 32
                            if (!(eq_s(1, "\u00DF")))
                            {
                                break lab3;
                            }
                            // ], line 32
                            ket = cursor;
                            // <-, line 32
                            slice_from("ss");
                            break lab2;
                        } while (false);
                        cursor = v_3;
                        // next, line 33
                        if (cursor >= limit)
                        {
                            break lab1;
                        }
                        cursor++;
                    } while (false);
                    continue replab0;
                } while (false);
                cursor = v_2;
                break replab0;
            }
            cursor = v_1;
            // repeat, line 36
            replab4: while(true)
            {
                v_4 = cursor;
                lab5: do {
                    // goto, line 36
                    golab6: while(true)
                    {
                        v_5 = cursor;
                        lab7: do {
                            // (, line 36
                            if (!(in_grouping(g_v, 97, 252)))
                            {
                                break lab7;
                            }
                            // [, line 37
                            bra = cursor;
                            // or, line 37
                            lab8: do {
                                v_6 = cursor;
                                lab9: do {
                                    // (, line 37
                                    // literal, line 37
                                    if (!(eq_s(1, "u")))
                                    {
                                        break lab9;
                                    }
                                    // ], line 37
                                    ket = cursor;
                                    if (!(in_grouping(g_v, 97, 252)))
                                    {
                                        break lab9;
                                    }
                                    // <-, line 37
                                    slice_from("U");
                                    break lab8;
                                } while (false);
                                cursor = v_6;
                                // (, line 38
                                // literal, line 38
                                if (!(eq_s(1, "y")))
                                {
                                    break lab7;
                                }
                                // ], line 38
                                ket = cursor;
                                if (!(in_grouping(g_v, 97, 252)))
                                {
                                    break lab7;
                                }
                                // <-, line 38
                                slice_from("Y");
                            } while (false);
                            cursor = v_5;
                            break golab6;
                        } while (false);
                        cursor = v_5;
                        if (cursor >= limit)
                        {
                            break lab5;
                        }
                        cursor++;
                    }
                    continue replab4;
                } while (false);
                cursor = v_4;
                break replab4;
            }
            return true;
        }

        private boolean r_mark_regions() {
            int v_1;
            // (, line 42
            I_p1 = limit;
            I_p2 = limit;
            // test, line 47
            v_1 = cursor;
            // (, line 47
            // hop, line 47
            {
                int c = cursor + 3;
                if (0 > c || c > limit)
                {
                    return false;
                }
                cursor = c;
            }
            // setmark x, line 47
            I_x = cursor;
            cursor = v_1;
            // gopast, line 49
            golab0: while(true)
            {
                lab1: do {
                    if (!(in_grouping(g_v, 97, 252)))
                    {
                        break lab1;
                    }
                    break golab0;
                } while (false);
                if (cursor >= limit)
                {
                    return false;
                }
                cursor++;
            }
            // gopast, line 49
            golab2: while(true)
            {
                lab3: do {
                    if (!(out_grouping(g_v, 97, 252)))
                    {
                        break lab3;
                    }
                    break golab2;
                } while (false);
                if (cursor >= limit)
                {
                    return false;
                }
                cursor++;
            }
            // setmark p1, line 49
            I_p1 = cursor;
            // try, line 50
            lab4: do {
                // (, line 50
                if (!(I_p1 < I_x))
                {
                    break lab4;
                }
                I_p1 = I_x;
            } while (false);
            // gopast, line 51
            golab5: while(true)
            {
                lab6: do {
                    if (!(in_grouping(g_v, 97, 252)))
                    {
                        break lab6;
                    }
                    break golab5;
                } while (false);
                if (cursor >= limit)
                {
                    return false;
                }
                cursor++;
            }
            // gopast, line 51
            golab7: while(true)
            {
                lab8: do {
                    if (!(out_grouping(g_v, 97, 252)))
                    {
                        break lab8;
                    }
                    break golab7;
                } while (false);
                if (cursor >= limit)
                {
                    return false;
                }
                cursor++;
            }
            // setmark p2, line 51
            I_p2 = cursor;
            return true;
        }

        private boolean r_postlude() {
            int among_var;
            int v_1;
            // repeat, line 55
            replab0: while(true)
            {
                v_1 = cursor;
                lab1: do {
                    // (, line 55
                    // [, line 57
                    bra = cursor;
                    // substring, line 57
                    among_var = find_among(a_0, 6);
                    if (among_var == 0)
                    {
                        break lab1;
                    }
                    // ], line 57
                    ket = cursor;
                    switch(among_var) {
                        case 0:
                            break lab1;
                        case 1:
                            // (, line 58
                            // <-, line 58
                            slice_from("y");
                            break;
                        case 2:
                            // (, line 59
                            // <-, line 59
                            slice_from("u");
                            break;
                        case 3:
                            // (, line 60
                            // <-, line 60
                            slice_from("a");
                            break;
                        case 4:
                            // (, line 61
                            // <-, line 61
                            slice_from("o");
                            break;
                        case 5:
                            // (, line 62
                            // <-, line 62
                            slice_from("u");
                            break;
                        case 6:
                            // (, line 63
                            // next, line 63
                            if (cursor >= limit)
                            {
                                break lab1;
                            }
                            cursor++;
                            break;
                    }
                    continue replab0;
                } while (false);
                cursor = v_1;
                break replab0;
            }
            return true;
        }

        private boolean r_R1() {
            if (!(I_p1 <= cursor))
            {
                return false;
            }
            return true;
        }

        private boolean r_R2() {
            if (!(I_p2 <= cursor))
            {
                return false;
            }
            return true;
        }

        private boolean r_standard_suffix() {
            int among_var;
            int v_1;
            int v_2;
            int v_3;
            int v_4;
            int v_5;
            int v_6;
            int v_7;
            int v_8;
            int v_9;
            // (, line 73
            // do, line 74
            v_1 = limit - cursor;
            lab0: do {
                // (, line 74
                // [, line 75
                ket = cursor;
                // substring, line 75
                among_var = find_among_b(a_1, 7);
                if (among_var == 0)
                {
                    break lab0;
                }
                // ], line 75
                bra = cursor;
                // call R1, line 75
                if (!r_R1())
                {
                    break lab0;
                }
                switch(among_var) {
                    case 0:
                        break lab0;
                    case 1:
                        // (, line 77
                        // delete, line 77
                        slice_del();
                        break;
                    case 2:
                        // (, line 80
                        if (!(in_grouping_b(g_s_ending, 98, 116)))
                        {
                            break lab0;
                        }
                        // delete, line 80
                        slice_del();
                        break;
                }
            } while (false);
            cursor = limit - v_1;
            // do, line 84
            v_2 = limit - cursor;
            lab1: do {
                // (, line 84
                // [, line 85
                ket = cursor;
                // substring, line 85
                among_var = find_among_b(a_2, 4);
                if (among_var == 0)
                {
                    break lab1;
                }
                // ], line 85
                bra = cursor;
                // call R1, line 85
                if (!r_R1())
                {
                    break lab1;
                }
                switch(among_var) {
                    case 0:
                        break lab1;
                    case 1:
                        // (, line 87
                        // delete, line 87
                        slice_del();
                        break;
                    case 2:
                        // (, line 90
                        if (!(in_grouping_b(g_st_ending, 98, 116)))
                        {
                            break lab1;
                        }
                        // hop, line 90
                        {
                            int c = cursor - 3;
                            if (limit_backward > c || c > limit)
                            {
                                break lab1;
                            }
                            cursor = c;
                        }
                        // delete, line 90
                        slice_del();
                        break;
                }
            } while (false);
            cursor = limit - v_2;
            // do, line 94
            v_3 = limit - cursor;
            lab2: do {
                // (, line 94
                // [, line 95
                ket = cursor;
                // substring, line 95
                among_var = find_among_b(a_4, 8);
                if (among_var == 0)
                {
                    break lab2;
                }
                // ], line 95
                bra = cursor;
                // call R2, line 95
                if (!r_R2())
                {
                    break lab2;
                }
                switch(among_var) {
                    case 0:
                        break lab2;
                    case 1:
                        // (, line 97
                        // delete, line 97
                        slice_del();
                        // try, line 98
                        v_4 = limit - cursor;
                        lab3: do {
                            // (, line 98
                            // [, line 98
                            ket = cursor;
                            // literal, line 98
                            if (!(eq_s_b(2, "ig")))
                            {
                                cursor = limit - v_4;
                                break lab3;
                            }
                            // ], line 98
                            bra = cursor;
                            // not, line 98
                            {
                                v_5 = limit - cursor;
                                lab4: do {
                                    // literal, line 98
                                    if (!(eq_s_b(1, "e")))
                                    {
                                        break lab4;
                                    }
                                    cursor = limit - v_4;
                                    break lab3;
                                } while (false);
                                cursor = limit - v_5;
                            }
                            // call R2, line 98
                            if (!r_R2())
                            {
                                cursor = limit - v_4;
                                break lab3;
                            }
                            // delete, line 98
                            slice_del();
                        } while (false);
                        break;
                    case 2:
                        // (, line 101
                        // not, line 101
                        {
                            v_6 = limit - cursor;
                            lab5: do {
                                // literal, line 101
                                if (!(eq_s_b(1, "e")))
                                {
                                    break lab5;
                                }
                                break lab2;
                            } while (false);
                            cursor = limit - v_6;
                        }
                        // delete, line 101
                        slice_del();
                        break;
                    case 3:
                        // (, line 104
                        // delete, line 104
                        slice_del();
                        // try, line 105
                        v_7 = limit - cursor;
                        lab6: do {
                            // (, line 105
                            // [, line 106
                            ket = cursor;
                            // or, line 106
                            lab7: do {
                                v_8 = limit - cursor;
                                lab8: do {
                                    // literal, line 106
                                    if (!(eq_s_b(2, "er")))
                                    {
                                        break lab8;
                                    }
                                    break lab7;
                                } while (false);
                                cursor = limit - v_8;
                                // literal, line 106
                                if (!(eq_s_b(2, "en")))
                                {
                                    cursor = limit - v_7;
                                    break lab6;
                                }
                            } while (false);
                            // ], line 106
                            bra = cursor;
                            // call R1, line 106
                            if (!r_R1())
                            {
                                cursor = limit - v_7;
                                break lab6;
                            }
                            // delete, line 106
                            slice_del();
                        } while (false);
                        break;
                    case 4:
                        // (, line 110
                        // delete, line 110
                        slice_del();
                        // try, line 111
                        v_9 = limit - cursor;
                        lab9: do {
                            // (, line 111
                            // [, line 112
                            ket = cursor;
                            // substring, line 112
                            among_var = find_among_b(a_3, 2);
                            if (among_var == 0)
                            {
                                cursor = limit - v_9;
                                break lab9;
                            }
                            // ], line 112
                            bra = cursor;
                            // call R2, line 112
                            if (!r_R2())
                            {
                                cursor = limit - v_9;
                                break lab9;
                            }
                            switch(among_var) {
                                case 0:
                                    cursor = limit - v_9;
                                    break lab9;
                                case 1:
                                    // (, line 114
                                    // delete, line 114
                                    slice_del();
                                    break;
                            }
                        } while (false);
                        break;
                }
            } while (false);
            cursor = limit - v_3;
            return true;
        }

        public boolean stem() {
            int v_1;
            int v_2;
            int v_3;
            int v_4;
            // (, line 124
            // do, line 125
            v_1 = cursor;
            lab0: do {
                // call prelude, line 125
                if (!r_prelude())
                {
                    break lab0;
                }
            } while (false);
            cursor = v_1;
            // do, line 126
            v_2 = cursor;
            lab1: do {
                // call mark_regions, line 126
                if (!r_mark_regions())
                {
                    break lab1;
                }
            } while (false);
            cursor = v_2;
            // backwards, line 127
            limit_backward = cursor; cursor = limit;
            // do, line 128
            v_3 = limit - cursor;
            lab2: do {
                // call standard_suffix, line 128
                if (!r_standard_suffix())
                {
                    break lab2;
                }
            } while (false);
            cursor = limit - v_3;
            cursor = limit_backward;            // do, line 129
            v_4 = cursor;
            lab3: do {
                // call postlude, line 129
                if (!r_postlude())
                {
                    break lab3;
                }
            } while (false);
            cursor = v_4;
            return true;
        }

}

