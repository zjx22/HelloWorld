import java.io.File;
import java.util.ArrayList;

public class readfiles {
    public static void traverseFolder2(String path) {
        ArrayList arr=new ArrayList();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return ;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                    arr.add(file2.getAbsolutePath());

                    }
                }

            }
        } else {
            System.out.println("文件不存在!");
        }
        System.out.println(arr.toString());
    }
    public static void main(String[] args) {
        String path="E:\\test";
        traverseFolder2(path);
        File f=new File("");



    }
}
