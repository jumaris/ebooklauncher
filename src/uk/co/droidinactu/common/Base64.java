/*<p>
 * Copyright 2012 Andy Aspell-Clark
 *</p><p>
 * This file is part of eBookLauncher.
 * </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * </p><p>
 * eBookLauncher is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 * </p><p>
 * You should have received a copy of the GNU General Public License along
 * with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *</p>
 */
package uk.co.droidinactu.common;

public class Base64 {

    static private final Base64 BASE64 = new Base64();
    static private final int[] DECODE = new int[256];
    static private final char[] ENCODE = new char[64];

    static private final int IGNORE = -1;
    static private final int PAD = -2;

    static {
        for (int xa = 0; xa <= 25; xa++) {
            Base64.ENCODE[xa] = (char) ('A' + xa);
        } // 0..25 -> 'A'..'Z'
        for (int xa = 0; xa <= 25; xa++) {
            Base64.ENCODE[xa + 26] = (char) ('a' + xa);
        } // 26..51 -> 'a'..'z'
        for (int xa = 0; xa <= 9; xa++) {
            Base64.ENCODE[xa + 52] = (char) ('0' + xa);
        } // 52..61 -> '0'..'9'
        Base64.ENCODE[62] = '+';
        Base64.ENCODE[63] = '/';

        for (int xa = 0; xa < 256; xa++) {
            Base64.DECODE[xa] = Base64.IGNORE;
        } // set all chars to IGNORE, first
        for (int xa = 0; xa < 64; xa++) {
            Base64.DECODE[Base64.ENCODE[xa]] = xa;
        } // set the Base 64 chars to their integer byte values
        Base64.DECODE['='] = Base64.PAD;
    }

    static public byte[] toBytes(final String b64) {
        return Base64.BASE64.decode(b64, 0, b64.length());
    }

    static public byte[] toBytes(final String b64, final int str, final int len) {
        return Base64.BASE64.decode(b64, str, len);
    }

    static public String toString(final byte[] dta) {
        return Base64.BASE64.encode(dta);
    }

    static public String toString(final byte[] dta, final int str, final int len) {
        return Base64.BASE64.encode(dta, str, len);
    }

    private int lineLength;

    private String lineSeparator;

    public Base64() {
        this.lineSeparator = System.getProperty("line.separator");
        this.lineLength = 0;
    }

    public byte[] decode(final String b64) {
        return this.decode(b64, 0, b64.length());
    }

    /**
     * Decode a Base64 string to an array of bytes. The string must have a
     * length evenly divisible by 4 (not counting line separators and other
     * ignorable characters, like whitespace).
     */
    public byte[] decode(final String b64, final int str, final int len) {
        byte[] ba; // target byte array
        int dc; // decode cycle (within sequence of 4 input chars).
        int rv; // reconstituted value
        int ol; // output length
        int pc; // padding count

        ba = new byte[(len / 4) * 3]; // create array to largest possible size.
        dc = 0;
        rv = 0;
        ol = 0;
        pc = 0;

        for (int xa = 0; xa < len; xa++) {
            final int ch = b64.charAt(xa + str);
            int value = ch <= 255 ? Base64.DECODE[ch] : Base64.IGNORE;
            if (value != Base64.IGNORE) {
                if (value == Base64.PAD) {
                    value = 0;
                    pc++;
                }
                switch (dc) {
                case 0: {
                    rv = value;
                    dc = 1;
                }
                    break;

                case 1: {
                    rv <<= 6;
                    rv |= value;
                    dc = 2;
                }
                    break;

                case 2: {
                    rv <<= 6;
                    rv |= value;
                    dc = 3;
                }
                    break;

                case 3: {
                    rv <<= 6;
                    rv |= value;

                    // Completed a cycle of 4 chars, so recombine the four 6-bit
                    // values in big-endian order
                    ba[ol + 2] = (byte) rv;
                    rv >>>= 8;
                    ba[ol + 1] = (byte) rv;
                    rv >>>= 8;
                    ba[ol] = (byte) rv;
                    ol += 3;
                    dc = 0;
                }
                    break;
                }
            }
        }
        if (dc != 0) { throw new ArrayIndexOutOfBoundsException(
                "Base64 data given as input was not an even multiple of 4 characters (should be padded with '=' characters)."); }
        ol -= pc;
        if (ba.length != ol) {
            final byte[] b2 = new byte[ol];
            System.arraycopy(ba, 0, b2, 0, ol);
            ba = b2;
        }
        return ba;
    }

    public String encode(final byte[] bin) {
        return this.encode(bin, 0, bin.length);
    }

    /**
     * Encode an array of bytes as Base64. It will be broken into lines if the
     * line length is not 0. If broken into lines, the last line is not
     * terminated with a line separator.
     * 
     * param ba The byte array to encode.
     */
    public String encode(final byte[] bin, final int str, final int len) {
        int ol; // output length
        StringBuffer sb; // string buffer for output(must be local for recursion
                         // to work).
        int lp; // line position(must be local for recursion to work).
        int el; // even multiple of 3 length
        int ll; // leftover length

        // CREATE OUTPUT BUFFER
        ol = ((len + 2) / 3) * 4;
        if (this.lineLength != 0) {
            final int lines = (((ol + this.lineLength) - 1) / this.lineLength) - 1;
            if (lines > 0) {
                ol += lines * this.lineSeparator.length();
            }
        }
        sb = new StringBuffer(ol);
        lp = 0;

        // EVEN MULTIPLES OF 3
        el = (len / 3) * 3;
        ll = len - el;
        for (int xa = 0; xa < el; xa += 3) {
            int cv;
            int c0, c1, c2, c3;

            if (this.lineLength != 0) {
                lp += 4;
                if (lp > this.lineLength) {
                    sb.append(this.lineSeparator);
                    lp = 4;
                }
            }

            // get next three bytes in unsigned form lined up, in big-endian
            // order
            cv = bin[xa + str + 0] & 0xFF;
            cv <<= 8;
            cv |= bin[xa + str + 1] & 0xFF;
            cv <<= 8;
            cv |= bin[xa + str + 2] & 0xFF;

            // break those 24 bits into a 4 groups of 6 bits, working LSB to
            // MSB.
            c3 = cv & 0x3F;
            cv >>>= 6;
            c2 = cv & 0x3F;
            cv >>>= 6;
            c1 = cv & 0x3F;
            cv >>>= 6;
            c0 = cv & 0x3F;

            // Translate into the equivalent alpha character emitting them in
            // big-endian order.
            sb.append(Base64.ENCODE[c0]);
            sb.append(Base64.ENCODE[c1]);
            sb.append(Base64.ENCODE[c2]);
            sb.append(Base64.ENCODE[c3]);
        }

        // LEFTOVERS
        if ((this.lineLength != 0) && (ll > 0)) {
            lp += 4;
            if (lp > this.lineLength) {
                sb.append(this.lineSeparator);
                lp = 4;
            }
        }
        if (ll == 1) {
            sb.append(this.encode(new byte[] { bin[el + str], 0, 0 }).substring(0, 2)).append("=="); // Use
                                                                                                     // recursion
                                                                                                     // so
                                                                                                     // escaping
                                                                                                     // logic
                                                                                                     // is
                                                                                                     // not
                                                                                                     // repeated,
                                                                                                     // replacing
                                                                                                     // last
                                                                                                     // 2
                                                                                                     // chars
                                                                                                     // with
                                                                                                     // "==".
        } else if (ll == 2) {
            sb.append(this.encode(new byte[] { bin[el + str], bin[el + str + 1], 0 }).substring(0, 3)).append("="); // Use
                                                                                                                    // recursion
                                                                                                                    // so
                                                                                                                    // escaping
                                                                                                                    // logic
                                                                                                                    // is
                                                                                                                    // not
                                                                                                                    // repeated,
                                                                                                                    // replacing
                                                                                                                    // last
                                                                                                                    // char
                                                                                                                    // and
                                                                                                                    // "=".
        }
        if (ol != sb.length()) { throw new RuntimeException(
                "Error in Base64 encoding method: Calculated output length of " + ol
                        + " did not match actual length of "
                        + sb.length()); }
        return sb.toString();
    }

    /**
     * Set maximum line length for encoded lines. Ignored by decode.
     * 
     * @param len
     *            Length of each line. 0 means no newlines inserted. Must be a
     *            multiple of 4.
     */
    public void setLineLength(final int len) {
        this.lineLength = (len / 4) * 4;
    }

    /**
     * Set the line separator sequence for encoded lines. Ignored by decode.
     * Usually contains only a combination of chars \n and \r, but could be any
     * chars except 'A'-'Z', 'a'-'z', '0'-'9', '+' and '/'.
     * 
     * @param linsep
     *            Line separator - may be "" but not null.
     */
    public void setLineSeparator(final String linsep) {
        this.lineSeparator = linsep;
    }

}
