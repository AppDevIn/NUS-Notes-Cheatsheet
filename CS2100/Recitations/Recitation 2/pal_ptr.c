#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define BUFLEN 1024

int is_palindrome(char *start, char *end) {
    while(end > start) {
        if(tolower(*start) != tolower(*end))
            return 0;
        start++;
        end--;
    }
    return 1;
}

int main() {
    char str[BUFLEN];
    int len;
    printf("Enter string: ");
    fgets(str, BUFLEN, stdin);

    len = strlen(str);

    if (*(str + len - 1) == '\n') {
        *(str + len - 1) = '\0';
        len--;
    }

    if(is_palindrome(str, str + len - 1))
        printf("YES!\n");
    else
        printf("NO!\n");
}
