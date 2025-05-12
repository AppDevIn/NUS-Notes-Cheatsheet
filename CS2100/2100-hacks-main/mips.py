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
        elif parts[0] in ['beq', 'bne']:          # <── NEW branch
            rs, rt, imm = registers[parts[1]], \
                          registers[parts[2]], int(parts[3])
        else:                                     # addi, andi, ori
            rt, rs, imm = registers[parts[1]], \
                          registers[parts[2]], int(parts[3])
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
        
        elif parts[0] in ['beq', 'bne']:          # <── NEW branch
            rs, rt, imm = int(parts[1]), int(parts[2]), int(parts[3])
       
        else:                                     # addi, andi, ori
            rt, rs, imm = int(parts[1]), int(parts[2]), int(parts[3])
            
        imm_bin = '{:016b}'.format(imm & 0xFFFF)
        bin_val = '{:06b}{:05b}{:05b}{}'.format(opcode, rs, rt, imm_bin)
        hex_val = '{:08x}'.format(int(bin_val, 2))
        return (hex_val, bin_val)
    except:
        return "invalid I-type instruction"

print("MIPS")
print("h2m('00108102')")
print("m2h('srl s0 s0 4')")
print("mn2h('srl 16 16 4')")