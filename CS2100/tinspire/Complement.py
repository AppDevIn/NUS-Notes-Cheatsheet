def ones_complement_add(a_str, b_str):
    # Make sure both inputs have same length
    max_len = max(len(a_str), len(b_str))
    a_str = a_str.zfill(max_len)
    b_str = b_str.zfill(max_len)

    result = ""
    carry = 0

    # Add from right to left
    for i in range(max_len - 1, -1, -1):
        bit_a = int(a_str[i])
        bit_b = int(b_str[i])
        total = bit_a + bit_b + carry
        if total == 0:
            result = "0" + result
            carry = 0
        elif total == 1:
            result = "1" + result
            carry = 0
        elif total == 2:
            result = "0" + result
            carry = 1
        else:  # total == 3
            result = "1" + result
            carry = 1

    # Handle end-around carry
    if carry == 1:
        # Add 1 to the result
        result = ones_complement_add(result, "1".zfill(max_len))

    return result

def ones_complement(number_str):
    complement = ""
    for bit in number_str:
        if bit == "0":
            complement += "1"
        else:
            complement += "0"
    return complement

# Main
print("1's Complement Arithmetic")
a = input("Enter first binary number: ")
b = input("Enter second binary number: ")

sum_result = ones_complement_add(a, b)
print("1's complement sum:")
print(sum_result)
