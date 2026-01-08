#include <stdio.h>
#include <string.h>

char a[32];
char b[32];
int x;
int y;
int z;
char ausgabe[64];
char c[1];

char* mix(char a[],char b[]) {
    if (strlen(a) > strlen(b)) {
        x = strlen(b);
        y = strlen(a) - strlen(b);
        z = 1;
    } else if (strlen(a) < strlen(b)) {
        x = strlen(a);
        y = strlen(b) - strlen(a);
        z = 0;
    } else{
        x = strlen(a);
        y = 0;
        z = 2;
            }
    for (int i = 0; i < x; i++) {
    	c[0] = a[i];
    	strcat(ausgabe, c);
    	c[0] = b[i];
    	strcat(ausgabe, c);
    }
    if (z == 1) {
        for (int i = 0; i <= y; i++) {
        	c[0] = a[i + x];
        	strcat(ausgabe, c);
        }
    }
    if (z == 0) {
        for (int i = 0; i <= y; i++) {
        	c[0] = b[i + x];
        	strcat(ausgabe, c);
        }
    }
    return ausgabe;
}

int main() {
    scanf("%s", a);
    scanf("%s", b);
    printf("%s", mix(a, b));
    return 0;
}
