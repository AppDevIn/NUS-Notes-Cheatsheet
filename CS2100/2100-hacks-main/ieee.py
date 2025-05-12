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

# Example
print("IEEE")
print("d2i(-1.875)")
print("i2b('BF600000')")
print("b2i('11000.110')")

