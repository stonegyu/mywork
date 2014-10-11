#include <stdio.h>
int main(){
        int a,b,c;
        int i;
        scanf("%d %d %d",&a,&b,&c);
        printf("%d.",a/b);
        
        for(i=0; i<c; i++){
            a%=b;
            a*=10;
            printf("%d",a/b);
            a-=(a/b)*b;
        }
        return 0;
}
