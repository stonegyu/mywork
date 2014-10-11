#include<stdio.h>
int main(){
        double a;
        double sum=0;
        double i=1;
        scanf("%lf",&a);
        while(a>=sum){
                sum+=(1/(i+1));
                i++;
        }
        printf("%.0lf card(s)",i-1);
        return 0;
}
