def to_bytes(value, unit):
    unit = unit.upper()
    if unit == "KB":
        return value * 1024
    elif unit == "MB":
        return value * 1024  2
    elif unit == "GB":
        return value * 1024  3
    elif unit == "TB":
        return value * 1024 ** 4
    else:
        return "Invalid unit"

# Example usage
val = 1.5
unit = "MB"
result = to_bytes(val, unit)
print(f"{val} {unit} = {result} bytes")
