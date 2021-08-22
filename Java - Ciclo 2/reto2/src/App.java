public class App {
    public static void main(String[] args) {
        SchoolGradingSystem schoolGradingSystem = new SchoolGradingSystem();
        schoolGradingSystem.readData();
        System.out.printf("%.2f%n", schoolGradingSystem.question1());
        System.out.println(schoolGradingSystem.question2());
        System.out.println(schoolGradingSystem.question3());
        System.out.println(schoolGradingSystem.question4());
    }
}