package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CheckStyle {
    public static int inputCheck(){
        int n =0;
        do {
            try {
                n = new Scanner(System.in).nextInt();  //nhap so sinh vien
            } catch (InputMismatchException e) {
                System.out.print("Bạn cần nhập vào là số thực, không được nhập chữ: ");
            }
            if (n > 0) break;
            System.out.print("Bạn cần nhập vào là số thực, lớn hơn 0: ");
        } while (true);
        return n;
    }
}
