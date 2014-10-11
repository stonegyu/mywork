#include<stdio.h>
int main(){
        int a;
        int i;
        int sum=0;
        int min=999999,max=-1;
        for(i=0; i<10; i++){
              scanf("%d",&a);
              max=(max>a)?max:a;
              min=(min<a)?min:a;
              sum+=a;
        }
        printf("%d %d %d\n",sum, max, min);
        return 0;
}
