"""
Radix-to-radix converter with rounding
100 % MicroPython-compatible
Author: <you>
"""

DIGITS = "0123456789ABCDEF"

# ---------------------------------------------------------------------
def _char_val(ch):
    """Return numeric value of ch (0-15)."""
    return DIGITS.index(ch)

# ---------------------------------------------------------------------
def _str_to_dec_int(s, base):
    """Integer part: <string, base>  →  decimal int."""
    acc = 0
    for ch in s:
        acc = acc * base + _char_val(ch)
    return acc

# ---------------------------------------------------------------------
def _str_to_dec_frac(s, base):
    """Fractional part: <string, base>  →  decimal float."""
    frac = 0.0
    denom = base
    for ch in s:
        frac += _char_val(ch) / denom
        denom *= base
    return frac

# ---------------------------------------------------------------------
def _dec_int_to_base(n, base):
    """Decimal int → digits list (MSD first) in <base>."""
    if n == 0:
        return [0]
    out = []
    while n:
        out.append(n % base)
        n //= base
    return out[::-1]

# ---------------------------------------------------------------------
def _round_frac_digits(digits_lst, base):
    """Round LSD, return possibly-updated integer carry."""
    carry = 0
    if digits_lst and digits_lst[-1] >= base // 2:
        # propagate
        idx = len(digits_lst) - 2
        while idx >= 0:
            if digits_lst[idx] + 1 < base:
                digits_lst[idx] += 1
                break
            digits_lst[idx] = 0
            idx -= 1
        else:
            carry = 1          # overflow into integer part
    return carry

# ---------------------------------------------------------------------
def r(r1, r2, num_str, precision, bits=8):
    """
    Convert <num_str> from base r1 to base r2 with <precision> fractional
    digits.  If r2==2 and the number is integral, pad to <bits> bits.
    """
    num_str = num_str.upper()

    if not (2 <= r1 <= 16 and 2 <= r2 <= 16):
        return "Error: Bases must be between 2 and 16"

    # ---------------- split integer / fractional ---------------------
    if '.' in num_str:
        int_s, frac_s = num_str.split('.')
    else:
        int_s, frac_s = num_str, ''

    # ---------------- validate digits --------------------------------
    valid = set(DIGITS[:r1])
    for ch in int_s + frac_s:
        if ch not in valid:
            return "Error: Invalid digit '{}' for base {}".format(ch, r1)

    # ---------------- r1  →  decimal ---------------------------------
    dec_int  = _str_to_dec_int(int_s, r1)
    dec_frac = _str_to_dec_frac(frac_s, r1)
    dec_val  = dec_int + dec_frac

    # ---------------- integer part to r2 -----------------------------
    int_digits = _dec_int_to_base(int(dec_val), r2)

    # ---------------- fractional part to r2 --------------------------
    frac_digits = []
    frac = dec_val - int(dec_val)
    for _ in range(precision + 1):      # +1 for rounding digit
        frac *= r2
        digit = int(frac)
        frac_digits.append(digit)
        frac -= digit

    # ---------------- rounding ---------------------------------------
    carry = _round_frac_digits(frac_digits, r2)
    frac_digits = frac_digits[:precision]  # trim extra digit

    # propagate any carry into integer digits
    if carry:
        idx = len(int_digits) - 1
        while idx >= 0:
            if int_digits[idx] + 1 < r2:
                int_digits[idx] += 1
                break
            int_digits[idx] = 0
            idx -= 1
        else:
            int_digits.insert(0, 1)

    # ---------------- stringify --------------------------------------
    int_part  = ''.join(DIGITS[d] for d in int_digits)
    frac_part = ''.join(DIGITS[d] for d in frac_digits)

    # special binary-padding case
    if r2 == 2 and not frac_s:
        return int_part.zfill(bits)
    if not frac_part:
        return int_part
    return "{}.{}".format(int_part, frac_part)

# ---------------------------------------------------------------------
# quick demo (same call as you gave)
print("NUM")
print("r(10, 5, '0.2425', 3)")

