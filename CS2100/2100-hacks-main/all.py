DIGITS = "0123456789ABCDEF"

def _char_val(ch):
    """Return numeric value of ch (0-15)."""
    return DIGITS.index(ch)

def _str_to_dec_int(s, base):
    """Integer part: <string, base>  →  decimal int."""
    acc = 0
    for ch in s:
        acc = acc * base + _char_val(ch)
    return acc

def _str_to_dec_frac(s, base):
    """Fractional part: <string, base>  →  decimal float."""
    frac = 0.0
    denom = base
    for ch in s:
        frac += _char_val(ch) / denom
        denom *= base
    return frac

def _dec_int_to_base(n, base):
    """Decimal int → digits list (MSD first) in <base>."""
    if n == 0:
        return [0]
    out = []
    while n:
        out.append(n % base)
        n //= base
    return out[::-1]

def _round_frac_digits(digits_lst, base):
    """Round LSD, return possibly-updated integer carry."""
    carry = 0
    if digits_lst and digits_lst[-1] >= base // 2:
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

def hex_to_bin(hex_str):
    """Convert 8-char hex to 32-bit binary string."""
    hex_str = hex_str.upper()
    if len(hex_str) != 8:
        return "Error: Hex string must be 8 characters"
    bin_str = bin(int(hex_str, 16))[2:]
    while len(bin_str) < 32:
        bin_str = "0" + bin_str
    return bin_str

def bin_to_hex(bin_str):
    """Convert 32-bit binary to 8-char hex string."""
    if len(bin_str) != 32:
        return "Error: Binary string must be 32 bits"
    hex_str = hex(int(bin_str, 2))[2:].upper()
    while len(hex_str) < 8:
        hex_str = "0" + hex_str
    return hex_str

def i2b(ieee_hex_str):
    """Convert IEEE hex string to decimal and binary."""
    bin_str = hex_to_bin(ieee_hex_str)
    if "Error" in bin_str:
        return bin_str
    
    # Extract components
    sign = int(bin_str[0])  # 1 bit
    exponent = int(bin_str[1:9], 2)  # 8 bits
    mantissa = bin_str[9:]  # 23 bits
    
    # Calculate decimal value
    if exponent == 0 and int(mantissa, 2) == 0:
        decimal = 0.0
    else:
        unbiased_exp = exponent - 127
        mantissa_value = 1.0  # Implicit leading 1
        for i in range(len(mantissa)):
            if mantissa[i] == "1":
                mantissa_value += 2 ** -(i + 1)
        decimal = (-1) ** sign * mantissa_value * (2 ** unbiased_exp)
    
    # Convert to binary with decimal point
    if decimal == 0:
        binary_with_decimal = "0.0"
    else:
        sign_str = "-" if sign else ""
        int_part = int(abs(decimal))
        frac_part = abs(decimal) - int_part
        
        # Integer part to binary
        int_binary = ""
        temp = int_part
        while temp > 0:
            int_binary = str(temp % 2) + int_binary
            temp //= 2
        if not int_binary:
            int_binary = "0"
        
        # Fractional part to binary (limit to 10 bits)
        frac_binary = ""
        temp = frac_part
        for _ in range(10):
            temp *= 2
            bit = int(temp)
            frac_binary += str(bit)
            temp -= bit
            if temp == 0:
                break
        
        binary_with_decimal = sign_str + int_binary + "." + frac_binary
    
    return "decimal " + str(decimal) + ", binary " + binary_with_decimal

def b2i(binary_str):
    """Convert binary string to IEEE 754 hex string."""
    is_negative = binary_str[0] == "-"
    if is_negative:
        binary_str = binary_str[1:]
    
    # Split integer and fractional parts
    parts = binary_str.split(".")
    int_part = parts[0]
    frac_part = parts[1] if len(parts) > 1 else "0"
    
    # Convert to decimal first
    decimal = 0.0
    for i in range(len(int_part)):
        if int_part[i] == "1":
            decimal += 2 ** (len(int_part) - 1 - i)
    for i in range(len(frac_part)):
        if frac_part[i] == "1":
            decimal += 2 ** -(i + 1)
    if is_negative:
        decimal = -decimal
    
    # Convert to IEEE binary
    if decimal == 0:
        bin_str = "0" * 32
    else:
        sign = "1" if decimal < 0 else "0"
        abs_decimal = abs(decimal)
        
        # Normalize to 1.xxxx * 2^exp
        exponent = 0
        normalized = abs_decimal
        if normalized >= 2:
            while normalized >= 2:
                normalized /= 2
                exponent += 1
        elif normalized < 1:
            while normalized < 1:
                normalized *= 2
                exponent -= 1
        
        # Bias exponent (127 for single precision)
        biased_exp = exponent + 127
        exp_str = bin(biased_exp)[2:]
        while len(exp_str) < 8:
            exp_str = "0" + exp_str
        
        # Mantissa (fractional part after 1.)
        mantissa = ""
        temp = normalized - 1
        for _ in range(23):
            temp *= 2
            bit = int(temp)
            mantissa += str(bit)
            temp -= bit
            if temp == 0:
                break
        while len(mantissa) < 23:
            mantissa += "0"
        
        bin_str = sign + exp_str + mantissa
    
    # Convert to hex
    return bin_to_hex(bin_str)

def d2i(decimal):
    """Convert decimal number to IEEE 754 hex string."""
    if decimal == 0:
        return "00000000"
    
    sign = "1" if decimal < 0 else "0"
    abs_decimal = abs(float(decimal))
    
    # Normalize to 1.xxxx * 2^exp
    exponent = 0
    normalized = abs_decimal
    if normalized >= 2:
        while normalized >= 2:
            normalized /= 2
            exponent += 1
    elif normalized < 1:
        while normalized < 1:
            normalized *= 2
            exponent -= 1
    
    # Bias exponent (127 for single precision)
    biased_exp = exponent + 127
    exp_str = bin(biased_exp)[2:]
    while len(exp_str) < 8:
        exp_str = "0" + exp_str
    
    # Mantissa (fractional part after 1.)
    mantissa = ""
    temp = normalized - 1
    for _ in range(23):
        temp *= 2
        bit = int(temp)
        mantissa += str(bit)
        temp -= bit
        if temp == 0:
            break
    while len(mantissa) < 23:
        mantissa += "0"
    
    # Convert to hex
    bin_str = sign + exp_str + mantissa
    return bin_to_hex(bin_str)

def sop(num_vars, minterms, dont_cares=()):
    def to_bin(n):
        s = ''
        for i in range(num_vars - 1, -1, -1):
            s += '1' if n & (1 << i) else '0'
        return s

    def combine(a, b):
        res = ''
        diff = 0
        for i in range(len(a)):
            if a[i] != b[i]:
                res += '-'
                diff += 1
            else:
                res += a[i]
        if diff == 1:
            return res
        return None

    def covered_by(bin_term, minterm):
        for i in range(len(bin_term)):
            if bin_term[i] != '-' and bin_term[i] != minterm[i]:
                return False
        return True

    def pi_str(bits):
        s = ''
        for i in range(len(bits)):
            if bits[i] == '1':
                s += chr(65 + i)
            elif bits[i] == '0':
                s += chr(65 + i) + "'"
        return s if s != '' else '1'

    def combinations(iterable, r):
        # Generate all combinations of length r from iterable
        pool = tuple(iterable)
        n = len(pool)
        if r > n:
            return
        indices = list(range(r))
        yield tuple(pool[i] for i in indices)
        while True:
            for i in reversed(range(r)):
                if indices[i] != i + n - r:
                    break
            else:
                return
            indices[i] += 1
            for j in range(i + 1, r):
                indices[j] = indices[j - 1] + 1
            yield tuple(pool[i] for i in indices)

    all_terms = list(minterms) + list(dont_cares)
    groups = {}
    for t in all_terms:
        b = to_bin(t)
        ones = b.count('1')
        groups.setdefault(ones, []).append((b, (t,)))

    prime_implicants = []
    while True:
        new_groups = {}
        used = set()
        keys = sorted(groups.keys())
        for i in range(len(keys) - 1):
            g1 = groups[keys[i]]
            g2 = groups[keys[i + 1]]
            for a in g1:
                for b in g2:
                    c = combine(a[0], b[0])
                    if c:
                        used.add(a)
                        used.add(b)
                        combined = tuple(sorted(set(a[1] + b[1])))
                        new_groups.setdefault(c.count('1'), []).append((c, combined))
        for g in groups.values():
            for item in g:
                if item not in used and item not in prime_implicants:
                    prime_implicants.append(item)
        if not new_groups:
            break
        groups = {}
        for v in new_groups.values():
            for item in v:
                if item not in groups.get(item[0].count('1'), []):
                    groups.setdefault(item[0].count('1'), []).append(item)

    bin_minterms = [to_bin(m) for m in minterms]
    filtered_pis = []
    for pi in prime_implicants:
        for m in minterms:
            if covered_by(pi[0], to_bin(m)):
                filtered_pis.append(pi)
                break

    pi_chart = {}
    for m in minterms:
        pi_chart[m] = []
        bm = to_bin(m)
        for pi in filtered_pis:
            if covered_by(pi[0], bm):
                pi_chart[m].append(pi)

    epis = []
    for m in pi_chart:
        if len(pi_chart[m]) == 1:
            epi = pi_chart[m][0]
            if epi not in epis:
                epis.append(epi)

    print("(PIs):")
    print(', '.join(pi_str(pi[0]) for pi in filtered_pis))

    print("(EPIs):")
    print(', '.join(pi_str(epi[0]) for epi in epis))

    essential_covered = set()
    for epi in epis:
        for m in minterms:
            if covered_by(epi[0], to_bin(m)):
                essential_covered.add(m)

    remaining_minterms = set(minterms) - essential_covered
    remaining_pis = [pi for pi in filtered_pis if pi not in epis]

    def get_simple_cover(minterms_left, pis_left):
        # Find the smallest combination of PIs that covers the remaining minterms
        for r in range(1, len(pis_left) + 1):
            for combo in combinations(pis_left, r):
                covered = set()
                for pi in combo:
                    for m in minterms:
                        if covered_by(pi[0], to_bin(m)):
                            covered.add(m)
                if minterms_left <= covered:
                    return combo  # Return the smallest cover
        return []  # Return empty if no valid cover found

    covers = get_simple_cover(remaining_minterms, remaining_pis)
    if not covers:
        print("No additional SOP expressions beyond EPIs.")
    else:
        print("SOP:")
        full = epis + list(covers)
        sop_expr = ' + '.join(sorted([pi_str(p[0]) for p in full]))
        print(sop_expr)

# Reverse MIPS Decoder in MicroPython for Casio fx-CG50

# 1) Dictionaries for Reverse Lookup
funct_table = {
    0x20: 'add', 0x00: 'sll', 0x24: 'and', 0x27: 'nor',
    0x25: 'or', 0x2a: 'slt', 0x02: 'srl', 0x22: 'sub'
}

i_table = {
    0x8: 'addi', 0x23: 'lw', 0x4: 'beq', 0x2b: 'sw',
    0xc: 'andi', 0x5: 'bne', 0xd: 'ori'
}

j_table = {
    0x2: 'j'
}

revRegisters = {
    0: 'zero', 1: 'at', 2: 'v0', 3: 'v1', 4: 'a0', 5: 'a1', 6: 'a2', 7: 'a3',
    8: 't0', 9: 't1', 10: 't2', 11: 't3', 12: 't4', 13: 't5', 14: 't6', 15: 't7',
    16: 's0', 17: 's1', 18: 's2', 19: 's3', 20: 's4', 21: 's5', 22: 's6', 23: 's7',
    24: 't8', 25: 't9', 26: 'k0', 27: 'k1', 28: 'gp', 29: 'sp', 30: 'fp', 31: 'ra'
}

registers = {v: k for k, v in revRegisters.items()}  # Reverse lookup for encoding

insCodes = {
    'add': (0, 0x20), 'sll': (0, 0x00), 'and': (0, 0x24), 'nor': (0, 0x27),
    'or': (0, 0x25), 'slt': (0, 0x2a), 'srl': (0, 0x02), 'sub': (0, 0x22),
    'addi': (0x8, 0), 'lw': (0x23, 0), 'beq': (0x4, 0), 'sw': (0x2b, 0),
    'andi': (0xc, 0), 'bne': (0x5, 0), 'j': (0x2, 0), 'ori': (0xd, 0)
}

# 2) Helper for Sign-Extending 16-bit Fields
def sign_extend_16bit(imm16):
    if imm16 & 0x8000:  # Simplified condition
        return imm16 - 0x10000
    return imm16

# 3) Decode Function (h2m) - Hex to Assembly
def h2m(hex_str):
    try:
        if hex_str.startswith('0x'):
            hex_str = hex_str[2:]
        val = int(hex_str, 16)  # Parse hex
        opcode = (val >> 26) & 0x3F  # Top 6 bits
    except:
        return ("invalid hex input", "invalid hex input")

    if opcode == 0:  # R-type
        rs = (val >> 21) & 0x1F
        rt = (val >> 16) & 0x1F
        rd = (val >> 11) & 0x1F
        shamt = (val >> 6) & 0x1F
        funct = val & 0x3F

        if funct in funct_table:
            instr_name = funct_table[funct]
            if instr_name in ['sll', 'srl']:
                name_str = '{} {} {} {}'.format(instr_name, revRegisters[rd], revRegisters[rt], shamt)
                num_str = '{} {} {} {}'.format(instr_name, rd, rt, shamt)
            else:
                name_str = '{} {} {} {}'.format(instr_name, revRegisters[rd], revRegisters[rs], revRegisters[rt])
                num_str = '{} {} {} {}'.format(instr_name, rd, rs, rt)
            return (name_str, num_str)
        return ('unknown R-type (funct=0x{:x})'.format(funct), 'unknown R-type (funct=0x{:x})'.format(funct))

    elif opcode in j_table:  # J-type
        instr_name = j_table[opcode]
        address = (val & 0x03FFFFFF) << 2
        name_str = '{} {}'.format(instr_name, address)
        return (name_str, name_str)

    elif opcode in i_table:  # I-type
        instr_name = i_table[opcode]
        rs = (val >> 21) & 0x1F
        rt = (val >> 16) & 0x1F
        imm = val & 0xFFFF

        if instr_name in ['addi', 'beq', 'bne', 'lw', 'sw']:
            imm_signed = sign_extend_16bit(imm)
            if instr_name == 'addi':
                name_str = '{} {} {} {}'.format(instr_name, revRegisters[rt], revRegisters[rs], imm_signed)
                num_str = '{} {} {} {}'.format(instr_name, rt, rs, imm_signed)
            elif instr_name in ['beq', 'bne']:
                name_str = '{} {} {} {}'.format(instr_name, revRegisters[rs], revRegisters[rt], imm_signed)
                num_str = '{} {} {} {}'.format(instr_name, rs, rt, imm_signed)
            elif instr_name in ['lw', 'sw']:
                name_str = '{} {} {}({})'.format(instr_name, revRegisters[rt], imm_signed, revRegisters[rs])
                num_str = '{} {} {}({})'.format(instr_name, rt, imm_signed, rs)
            return (name_str, num_str)
        elif instr_name in ['andi', 'ori']:
            name_str = '{} {} {} {}'.format(instr_name, revRegisters[rt], revRegisters[rs], imm)
            num_str = '{} {} {} {}'.format(instr_name, rt, rs, imm)
            return (name_str, num_str)
        return ('unknown I-type (opcode=0x{:x})'.format(opcode), 'unknown I-type (opcode=0x{:x})'.format(opcode))

    return ('unknown instruction (opcode=0x{:x})'.format(opcode), 'unknown instruction (opcode=0x{:x})'.format(opcode))

# 4) Encode Function (m2h) - Assembly with Register Names to Hex/Binary
def m2h(instruction):
    try:
        parts = instruction.replace(',', ' ').split()  # Normalize input
        instr_name = parts[0]
    except:
        return "invalid instruction format"

    if instr_name not in insCodes:
        return "unknown instruction {}".format(instr_name)

    if instr_name == 'j':
        return jType(parts)
    elif instr_name in ['sll', 'srl', 'add', 'and', 'nor', 'or', 'slt', 'sub']:
        return rType(parts)
    else:
        return iType(parts)

def jType(parts):
    try:
        opcode = insCodes[parts[0]][0]
        loc = int(int(parts[1]) / 4)
        imm = loc & 0x03FFFFFF
        bin_val = '{:06b}{:026b}'.format(opcode, imm)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid J-type instruction"

def rType(parts):
    try:
        opcode = insCodes[parts[0]][0]
        shift = 0
        if parts[0] in ['sll', 'srl']:
            rd, rt, shift = registers[parts[1]], registers[parts[2]], int(parts[3])
            rs = 0
        else:
            rd, rs, rt = registers[parts[1]], registers[parts[2]], registers[parts[3]]
        func = insCodes[parts[0]][1]
        bin_val = '{:06b}{:05b}{:05b}{:05b}{:05b}{:06b}'.format(opcode, rs, rt, rd, shift, func)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid R-type instruction"

def iType(parts):
    try:
        opcode = insCodes[parts[0]][0]
        if parts[0] in ['lw', 'sw']:
            rt = registers[parts[1]]
            offset_base = parts[2].split('(')
            imm = int(offset_base[0])
            rs = registers[offset_base[1].rstrip(')')]
        else:  # addi, andi, ori, beq, bne
            rt, rs, imm = registers[parts[1]], registers[parts[2]], int(parts[3])
        imm_bin = '{:016b}'.format(imm & 0xFFFF)
        bin_val = '{:06b}{:05b}{:05b}{}'.format(opcode, rs, rt, imm_bin)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid I-type instruction"

# 5) Encode Function (mn2h) - Assembly with Register Numbers to Hex/Binary
def mn2h(instruction):
    try:
        parts = instruction.replace(',', ' ').split()  # Normalize input
        instr_name = parts[0]
    except:
        return "invalid instruction format"

    if instr_name not in insCodes:
        return "unknown instruction {}".format(instr_name)

    if instr_name == 'j':
        return jType(parts)  # Same as m2h
    elif instr_name in ['sll', 'srl', 'add', 'and', 'nor', 'or', 'slt', 'sub']:
        return rTypeNum(parts)
    else:
        return iTypeNum(parts)

def rTypeNum(parts):
    try:
        opcode = insCodes[parts[0]][0]
        shift = 0
        if parts[0] in ['sll', 'srl']:
            rd, rt, shift = int(parts[1]), int(parts[2]), int(parts[3])
            rs = 0
        else:
            rd, rs, rt = int(parts[1]), int(parts[2]), int(parts[3])
        func = insCodes[parts[0]][1]
        bin_val = '{:06b}{:05b}{:05b}{:05b}{:05b}{:06b}'.format(opcode, rs, rt, rd, shift, func)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid R-type instruction"

def iTypeNum(parts):
    try:
        opcode = insCodes[parts[0]][0]
        if parts[0] in ['lw', 'sw']:
            rt = int(parts[1])
            offset_base = parts[2].split('(')
            imm = int(offset_base[0])
            rs = int(offset_base[1].rstrip(')'))
        else:  # addi, andi, ori, beq, bne
            rt, rs, imm = int(parts[1]), int(parts[2]), int(parts[3])
        imm_bin = '{:016b}'.format(imm & 0xFFFF)
        bin_val = '{:06b}{:05b}{:05b}{}'.format(opcode, rs, rt, imm_bin)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid I-type instruction"
    
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

print("MIPS")
print("h2m('00108102')")
print("m2h('srl s0 s0 4')")
print("mn2h('srl 16 16 4')")

print("IEEE")
print("d2i(-1.875)")
print("i2b('BF600000')")
print("b2i('11000.110')")

print("SOP")
print("sop(4, (1, 2), (0, 5, 6))")

print("NUM")
print("r(10, 5, '0.2425', 3)")