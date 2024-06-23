import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.Engineering;
public class javaLine {
    public static void main(String[] args)
    {
        ArrayList<String> courses = new ArrayList<String>();
        courses.add("ACES Undeclared");
        courses.add("Accountancy, BS");
        courses.add("Accountancy + Data Science, BS");
        courses.add("Actuarial Science, BSLAS");
        courses.add("Advertising, BS");
        courses.add("Aerospace Engineering, BS");
        courses.add("African American Studies, BALAS");
        courses.add("Agricultural & Biological Engineering, BS");
        courses.add("Agricultural Engineering");
        courses.add("Biological Engineering");
        courses.add("Agricultural & Biological Engineering, BS and Agricultural & Biological Engineering, BSAG (Dual Degree Program)");
        System.out.println(courses);
    }
}
