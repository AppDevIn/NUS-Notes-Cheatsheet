
def simplify_sop(minterms, dont_cares, num_vars):
    """
    Simplifies a Boolean function using the Quine-McCluskey algorithm.
    
    Arguments:
      minterms:   List of minterm integers where the function is 1.
      dont_cares: List of integers representing don’t care conditions.
      num_vars:   Number of variables (bits) in the function.
      
    Returns:
      (prime_implicants, essential_prime_implicants, simplified_sop)
      
      - prime_implicants: List of tuples (implicant, set_of_covered_numbers)
        where implicant is a string (e.g., '01-0') and the set contains the original
        minterm/don’t care numbers that the implicant covers.
      - essential_prime_implicants: List of implicant strings that are essential.
      - simplified_sop: A string representing the simplified SOP expression.
    """
    
    # Combine minterms and don't cares for initial grouping.
    terms = sorted(set(minterms) | set(dont_cares))
    
    # Create initial groups based on count of ones in the binary representation.
    groups = {}  # key: number of ones, value: list of (binary_term, set of covered indices)
    for number in terms:
        b_str = format(number, '0{}b'.format(num_vars))
        groups.setdefault(b_str.count('1'), []).append((b_str, set([number])))
    
    # List to hold final prime implicants.
    prime_implicants = []
    
    # Begin combining adjacent groups.
    while True:
        new_groups = {}
        marked = set()  # To record which terms got merged.
        combined_any = False
        
        # Process groups in order of increasing ones.
        sorted_keys = sorted(groups.keys())
        for i in range(len(sorted_keys) - 1):
            group1 = groups[sorted_keys[i]]
            group2 = groups[sorted_keys[i+1]]
            for term1, covers1 in group1:
                for term2, covers2 in group2:
                    # Count differences between the two terms.
                    diff_count = 0
                    diff_index = -1
                    for pos in range(num_vars):
                        # Only compare if neither is already a don’t care.
                        if term1[pos] != term2[pos]:
                            # Allow combining only if both bits are definite ('0' or '1')
                            if term1[pos] in '01' and term2[pos] in '01':
                                diff_count += 1
                                diff_index = pos
                            else:
                                diff_count = 999
                                break
                    if diff_count == 1:
                        combined_any = True
                        # Create a new term with a '-' at the differing position.
                        new_term = list(term1)
                        new_term[diff_index] = '-'
                        new_term = ''.join(new_term)
                        new_cover = covers1 | covers2
                        key = new_term.count('1')  # count definite 1's (ignoring '-')
                        new_groups.setdefault(key, [])
                        candidate = (new_term, new_cover)
                        if candidate not in new_groups[key]:
                            new_groups[key].append(candidate)
                        marked.add((term1, tuple(sorted(covers1))))
                        marked.add((term2, tuple(sorted(covers2))))
        
        # Any term not combined becomes a prime implicant.
        for group in groups.values():
            for term, covers in group:
                if (term, tuple(sorted(covers))) not in marked:
                    if (term, covers) not in prime_implicants:
                        prime_implicants.append((term, covers))
        
        if not combined_any:
            break  # No further combining possible.
        groups = new_groups  # Continue with the newly combined terms.
    
    # ------- Prime Implicant Chart Construction -------
    # Build a chart mapping each minterm (not don't care) to a list of implicants (the string form)
    # that cover that minterm.
    pi_chart = {}  # key: minterm, value: list of implicant strings covering it.
    for m in minterms:
        pi_chart[m] = []
        m_bin = format(m, '0{}b'.format(num_vars))
        for implicant, covers in prime_implicants:
            # Check if the implicant covers this minterm.
            match = True
            for i in range(num_vars):
                if implicant[i] != '-' and implicant[i] != m_bin[i]:
                    match = False
                    break
            if match:
                pi_chart[m].append(implicant)
    
    # ------- Identify Essential Prime Implicants -------
    essential_pi = set()
    # A prime implicant is essential if it is the only one covering a particular minterm.
    for m, implicants in pi_chart.items():
        if len(implicants) == 1:
            essential_pi.add(implicants[0])
    
    # Remove minterms covered by essential prime implicants.
    remaining_minterms = set(minterms)
    for epi in essential_pi:
        for m in list(remaining_minterms):
            m_bin = format(m, '0{}b'.format(num_vars))
            match = True
            for i in range(num_vars):
                if epi[i] != '-' and epi[i] != m_bin[i]:
                    match = False
                    break
            if match:
                remaining_minterms.discard(m)
    
    # ------- Additional Coverage Using a Greedy Approach -------
    # For minterms not yet covered by an essential implicant, select additional PIs.
    selected_pi = set(essential_pi)
    while remaining_minterms:
        best_pi = None
        best_count = 0
        for implicant, covers in prime_implicants:
            if implicant in selected_pi:
                continue
            count = 0
            for m in remaining_minterms:
                m_bin = format(m, '0{}b'.format(num_vars))
                match = True
                for i in range(num_vars):
                    if implicant[i] != '-' and implicant[i] != m_bin[i]:
                        match = False
                        break
                if match:
                    count += 1
            if count > best_count:
                best_count = count
                best_pi = implicant
        if best_pi is None:
            break
        selected_pi.add(best_pi)
        for m in list(remaining_minterms):
            m_bin = format(m, '0{}b'.format(num_vars))
            match = True
            for i in range(num_vars):
                if best_pi[i] != '-' and best_pi[i] != m_bin[i]:
                    match = False
                    break
            if match:
                remaining_minterms.discard(m)
    
    # ------- Build the Simplified SOP Expression -------
    # Convert each implicant string to its algebraic expression.
    var_names = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    sop_terms = []
    for implicant in selected_pi:
        term_expr = ""
        for idx, ch in enumerate(implicant):
            if ch == '1':
                term_expr += var_names[idx]
            elif ch == '0':
                term_expr += var_names[idx] + "'"
            # For '-' we do not add any literal.
        if term_expr == "":
            term_expr = "1"  # Covers the always-true case.
        sop_terms.append(term_expr)
    simplified_sop = " + ".join(sop_terms)
    
    return prime_implicants, list(essential_pi), simplified_sop


# ===== Example Usage =====
# Suppose we want to simplify a function of 4 variables.
# Let minterms be [4,8,10,11,12,15] and don’t cares be [9,14]:
if __name__ == '__main__':
    minterms = [4, 8, 10, 11, 12, 15]
    dont_cares = [9, 14]
    num_vars = 4

    pis, epis, sop = simplify_sop(minterms, dont_cares, num_vars)
    print("Prime Implicants:")
    for pi, covers in pis:
        print("  {} covers {}".format(pi, sorted(covers)))
    print("\nEssential Prime Implicants:")
    for epi in epis:
        print("  {}".format(epi))
    print("\nSimplified SOP Expression:")
    print("  {}".format(sop))
