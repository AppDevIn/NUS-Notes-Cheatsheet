def to_bin(x, bits):
    result = ""
    for i in range(bits-1, -1, -1):
        result += "1" if (x >> i) & 1 else "0"
    return result

def bin_to_hex(binary_str):
    hex_str = ""
    for i in range(0, 32, 4):
        nibble = binary_str[i:i+4]
        hex_digit = int(nibble, 2)
        if hex_digit < 10:
            hex_str += str(hex_digit)
        else:
            hex_str += chr(ord('A') + hex_digit - 10)
    return hex_str

def r_format(rs, rt, rd, shamt, funct):
    opcode = 0
    binary = (
        to_bin(opcode, 6) +
        to_bin(rs, 5) +
        to_bin(rt, 5) +
        to_bin(rd, 5) +
        to_bin(shamt, 5) +
        to_bin(funct, 6)
    )
    return bin_to_hex(binary)

def i_format(opcode, rs, rt, imm):
    binary = (
        to_bin(opcode, 6) +
        to_bin(rs, 5) +
        to_bin(rt, 5) +
        to_bin(imm & 0xFFFF, 16)  # Immediate might be signed
    )
    return bin_to_hex(binary)

def j_format(opcode, address):
    binary = (
        to_bin(opcode, 6) +
        to_bin(address >> 2, 26)  # Address must be word-aligned (drop 2 bits)
    )
    return bin_to_hex(binary)

# Main
print("Choose instruction type:")
print("1: R-format")
print("2: I-format")
print("3: J-format")
choice = int(input("Enter choice: "))

if choice == 1:
    rs = int(input("Enter rs: "))
    rt = int(input("Enter rt: "))
    rd = int(input("Enter rd: "))
    shamt = int(input("Enter shamt: "))
    funct = int(input("Enter funct: "))
    print("Hex:", r_format(rs, rt, rd, shamt, funct))

elif choice == 2:
    opcode = int(input("Enter opcode: "))
    rs = int(input("Enter rs: "))
    rt = int(input("Enter rt: "))
    imm = int(input("Enter immediate: "))
    print("Hex:", i_format(opcode, rs, rt, imm))

elif choice == 3:
    opcode = int(input("Enter opcode: "))
    addr = int(input("Enter target address: "))
    print("Hex:", j_format(opcode, addr))

else:
    print("Invalid choice.")
