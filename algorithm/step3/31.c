#include<stdio.h>
int main(){
   double input, before=-1;
        int i;
        while(1){
                scanf("%lf",&input);
                if(before!=-1 && input != 999)
                         printf("%.2lf\n",input-before);
                else if(input==999){
                   printf("End of Output");
					break;
				}
                before=input;
        }

        return 0;
}
