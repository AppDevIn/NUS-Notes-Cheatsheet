def get_power_of_two(x):
    power = 0
    while 2**power < x:
        power += 1
    if 2**power != x:
        print(str(x) + " is NOT a power of 2!")
        return -1
    return power

def safe_int(prompt):
    raw = input(prompt)
    try:
        return int(raw.strip())
    except:
        print("Invalid input. Enter only an integer (e.g., 64, 128)")
        return -1

# Step 1: Get block size in bytes
block_size = safe_int("Memory block size (must be power of 2): ")
if block_size <= 0:
    print("Invalid block size. Restart.")
else:
    N = get_power_of_two(block_size)
    if N == -1:
        print("Restart with valid power-of-2 block size.")
    else:
        block_number_size = 32 - N

        # Step 2: Get number of cache blocks (or sets)
        print("For sets, remember to divide total cache blocks by the X-way")
        cache_blocks = safe_int("Cache blocks or sets (power of 2): ")
        if cache_blocks <= 0:
            print("Invalid cache block count. Restart.")
        else:
            M = get_power_of_two(cache_blocks)
            if M == -1:
                print("Restart with valid power-of-2 cache block count.")
            else:
                tag_size = 32 - M - N

                # Final Output
                print("")
                print("--------RESULTS--------")
                print("Mem Block Size: " + str(block_size) + " bytes (2^" + str(N) + ")")
                print("Offset (N): " + str(N) + " bits")
                print("Block Number Size: " + str(block_number_size) + " bits")

                print("Number of Cache Blocks or Sets: " + str(cache_blocks) + " = 2^" + str(M))
                print("Cache Index (M): " + str(M) + " bits")
                # Step 4: Cache tag
                tag_size = 32 - M - N
                print("Cache Tag: " + str(tag_size) + " bits")
