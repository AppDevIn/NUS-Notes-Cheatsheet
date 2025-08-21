def decimal_to_ieee754(num):
    if num == 0:
        return "0" * 32

    sign_bit = "0"
    if num < 0:
        sign_bit = "1"
        num = -num

    integer_part = int(num)
    fractional_part = num - integer_part

    integer_binary = ""
    if integer_part == 0:
        integer_binary = "0"
    else:
        while integer_part > 0:
            integer_binary = str(integer_part % 2) + integer_binary
            integer_part = integer_part // 2

    fractional_binary = ""
    count = 0
    while fractional_part != 0 and count < 30:
        fractional_part *= 2
        bit = int(fractional_part)
        fractional_binary += str(bit)
        fractional_part -= bit
        count += 1

    if integer_binary != "0":
        exponent = len(integer_binary) - 1
        mantissa = integer_binary[1:] + fractional_binary
    else:
        exponent = -1
        for c in fractional_binary:
            exponent -= 1
            if c == '1':
                break
        mantissa = fractional_binary[-exponent:]

    exponent += 127
    exponent_bits = ""
    for i in range(8):
        exponent_bits = str(exponent % 2) + exponent_bits
        exponent = exponent // 2

    if len(mantissa) < 23:
        mantissa = mantissa + "0" * (23 - len(mantissa))
    mantissa_bits = mantissa[:23]

    ieee754 = sign_bit + exponent_bits + mantissa_bits
    return ieee754

def binary_to_hex(binary_str):
    hex_str = ""
    for i in range(0, 32, 4):
        four_bits = binary_str[i:i+4]
        decimal_value = int(four_bits, 2)
        if decimal_value < 10:
            hex_str += str(decimal_value)
        else:
            hex_str += chr(ord('A') + decimal_value - 10)
    return hex_str

number = float(input("Enter a decimal number: "))
ieee754_binary = decimal_to_ieee754(number)
ieee754_hex = binary_to_hex(ieee754_binary)

print("IEEE-754 Representation (32-bit):")
print(ieee754_binary)
print("Hexadecimal:")
print(ieee754_hex)
