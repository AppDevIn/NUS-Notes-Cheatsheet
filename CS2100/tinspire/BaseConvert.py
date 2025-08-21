def base_convert(number_str, from_base, to_base):
    # Step 1: Convert from any base to decimal
    decimal_value = 0
    power = 0
    for digit in number_str[::-1]:
        if '0' <= digit <= '9':
            value = ord(digit) - ord('0')
        else:
            value = ord(digit.upper()) - ord('A') + 10
        decimal_value += value * (from_base ** power)
        power += 1

    # Step 2: Convert decimal to target base
    if decimal_value == 0:
        return "0"

    digits = ""
    while decimal_value > 0:
        remainder = decimal_value % to_base
        if remainder < 10:
            digits = str(remainder) + digits
        else:
            digits = chr(ord('A') + remainder - 10) + digits
        decimal_value //= to_base

    return digits

# Example usage
num_str = input("Enter the number: ")
from_base = int(input("Enter the current base (e.g., 2 for binary): "))
to_base = int(input("Enter the target base (e.g., 10 for decimal): "))

result = base_convert(num_str, from_base, to_base)
print("Converted number:")
print(result)
