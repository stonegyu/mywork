#include<stdio.h>
int main(){
        int a;
        int aa=0, b=0, c=0, d=0, f=0;
        while(a!=-1){
                scanf("%d",&a);
                if(a==-1)break;
                if(a<=100 && a>=90) aa++;
                else if(a<90 && a>= 80) b++;
                else if(a<80 && a>= 70) c++; 
                else if(a<70 && a>= 60) d++;
                else f++; 
        }
        printf("%d\n",aa+b+c+d+f);
        printf("%d\n",aa);
        printf("%d\n",b);
        printf("%d\n",c);
        printf("%d\n",d);
        printf("%d\n",f);
        return 0;
}
