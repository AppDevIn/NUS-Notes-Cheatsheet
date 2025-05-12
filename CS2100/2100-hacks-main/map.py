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

# Test input
print("SOP")
print("sop(4, (1, 2), (0, 5, 6))")



