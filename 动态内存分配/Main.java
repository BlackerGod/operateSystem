package Experiment;

import java.util.Scanner;

public class Main {
    public static Test2 user; //声明一个用户;
    public static void Init(){ //初始化内存大小
        System.out.println("请输入内存大小(如果输入0，则按默认400分配)");
        Scanner scanner=new Scanner(System.in);
        int choose=scanner.nextInt();
        if(choose==0){
            user=new Test2();
        }else {
            user=new Test2(choose);
        }
    }
    public static void mainMenu(){
        System.out.println("***********************");
        System.out.println("*     1.输入作业      *");
        System.out.println("*     2.回收内存块    *");
        System.out.println("*     3.展示分区情况  *");
        System.out.println("*     4.退出系统      *");
        System.out.println("***********************");
        System.out.println("请输入你要进行的操作:");
    }
    public static void algorithmMenu(int size){
        System.out.println("请选择分配算法:");
        System.out.println("1.首次适应算法");
        System.out.println("2.循环首次算法");
        System.out.println("3.最佳适应算法");
        System.out.println("4.最坏适应算法");
        Scanner in = new Scanner(System.in);
        int choose = in.nextInt();
        switch (choose){
            case 1:
                user.FirstFit(size);break;
            case 2:
                user.NextFit(size);break;
            case 3:
                user.BestFit(size);break;
            case 4:
                user.WorstFit(size);break;
            default:
                System.out.println("请重新选择!");
        }
    }
    public static void main(String[] args) {
        Init();     //初始化内存条大小
        Scanner sc=new Scanner(System.in);

        while(true){
            mainMenu();             //主菜单
            int choice=sc.nextInt();
            if(choice == 4){
               return;
            }else if(choice == 1){
               System.out.println("请输入你要插入的内存块大小");
               int length=sc.nextInt();
               algorithmMenu(length);
            }else if(choice==2){
                System.out.println("请输入你要回收的分区号码");
                int ID=sc.nextInt();
                user.collection(ID);
            }else if(choice == 3){
                user.showZones();
            }else{
               System.out.println("输入错误，请检查");
           }
        }
    }

}
