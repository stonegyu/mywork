#include<stdio.h>
int main(){
        double a, d, an;
        scanf("%lf %lf %lf",&a, &d, &an);
        double s1 = (an-a)/d+1;
        int s2 = s1;
        if(s1 == s2){
                printf("%d",s2);
        }else{
                printf("X");
        }
        return 0;
}
