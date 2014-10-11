#include<stdio.h>
int main(){
        int a, arr[3]={0};
        for(int i=0; i<7; i++){
                scanf("%d",&a);
                int temp=a;
                int j;
                for( j=0; j<3; j++){
                        temp/=10;
			if(temp==0) break;	
		}
                arr[j] += a;
        }
        printf("%d %d %d",arr[0],arr[1],arr[2]);
        return 0;
}
