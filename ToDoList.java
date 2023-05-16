import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ToDoList {

    public static void write(String file, ArrayList<hw> b){
        try {
            FileWriter fw = new FileWriter(file, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        } catch (Exception exception) {
            System.out.println("Exception has been caught");
        }
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            for(hw c: b){
                pw.println(c);
            }
            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        }
        catch (IOException io) {
        }
    }

    public static hw newHw(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Assignment name?");
        String name = scan.nextLine();
        System.out.println("How many hours will this assignment take?");
        int work = scan.nextInt();
        System.out.println("What percent of this assignment are you done with?");
        int workDone = scan.nextInt();
        scan.nextLine();
        System.out.println("When is this assignment due? (Month-Day) eg. 1/27");
        String day = scan.nextLine();
        String[] d = day.split("/");
        if(Integer.valueOf(d[0]) < 10){
            d[0] = "0"+ d[0];
        }
        if(Integer.valueOf(d[1]) < 10){
            d[1] = "0"+ d[1];
        }
        day = "2023-" + d[0]+ "-"+d[1];
        LocalDateTime ldt = LocalDateTime.now();
        String formattedDateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(ldt);
        LocalDate dateBefore = LocalDate.parse(formattedDateStr);
        LocalDate dateAfter = LocalDate.parse(day);
        //calculating number of days in between
        int noOfDaysBetween = (int) ChronoUnit.DAYS.between(dateBefore, dateAfter);
        hw a = new hw(name,work,workDone,noOfDaysBetween);
        return a;
    }

    public static void userPrint(ArrayList<hw> a){
        System.out.println("You have a total of " + a.size() + " assignments");
        Collections.sort(a, new Comparator<hw>() { //sorts homework in descending order
            @Override
            public int compare(hw o1, hw o2) {
                int a = o1.compareTo(o2);
                if(a==0){
                    return a;
                }
                return o1.totalWork() > o2.totalWork() ? 1: o1.totalWork()< o2.totalWork() ? -1 : 0;
            }
        });
        Collections.reverse(a);
        String reset = "\u001B[0m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String red = "\u001B[31m";
        for (hw x: a){
            if(x.totalWork() > 2){
                System.out.print(red);
            } else if(x.totalWork() > 1){
                System.out.print(yellow);
            } else{
                System.out.print(green);
            }
            System.out.println(x.getName() +": " + x.getWorkDone() + "% completed, due in " +x.getDaysLeft() + " days");
        }
        System.out.print(reset);
    }

    public static void progressCheck(ArrayList<hw> a){
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < a.size(); i++){
            System.out.println("What percent of " + a.get(i).getName() + " do you have left?");
            int percent = scan.nextInt();
            scan.nextLine();
            a.get(i).setWorkDone(100-percent);
            if(a.get(i).getDaysLeft() > 1){
                a.get(i).setDaysLeft(a.get(i).getDaysLeft()-1);
            }
        }

    }

    public static void Check(ArrayList<hw> a){
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < a.size(); i++){
            System.out.println("What percent of " + a.get(i).getName() + " do you have left?");
            int percent = scan.nextInt();
            scan.nextLine();
            a.get(i).setWorkDone(100-percent);
        }

    }

    public static void main(String[] args) {
        Calendar today = Calendar.getInstance();
        /*
        today.set(Calendar.HOUR_OF_DAY, 16);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 00);
         */
        Timer timer = new Timer();
        timer.schedule(taskNew(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)); // period: 1 day
        }

    public static TimerTask taskNew() {
        return new TimerTask() {
            public void run() {
                ArrayList<hw> homework = new ArrayList<>();
                String fileName = "/Users/tjf347/Downloads/test.txt";
                File file = new File(fileName);
                Scanner reader = null;
                try {
                    reader = new Scanner(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Scanner scan = new Scanner(System.in);
                while (reader.hasNextLine()){ //reads file and adds all assignments to arraylist
                    String b = reader.nextLine();
                    String[] stuff = b.split(", ");
                    String name = stuff[0];
                    int work = Integer.parseInt(stuff[1]);
                    int workDone = Integer.parseInt(stuff[2]);
                    int daysLeft = Integer.parseInt(stuff[3]);
                    hw a = new hw(name,work,workDone,daysLeft);
                    homework.add(a);
                }
                System.out.println("How many new assignments?");
                int newWork = scan.nextInt();
                scan.nextLine();
                for(int i = 0; i < newWork; i++){
                    homework.add(newHw());
                }
                userPrint(homework);
                //maybe add feature to be able to check homework every so often with a command
                int time = 25200000;
                while (time > 0){
                    if(scan.nextLine().equals("progress check")){
                        Check(homework);
                        userPrint(homework);
                    } else{
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time--;
                    }
                }
                try {
                    Thread.sleep(3000);
                    //Thread.sleep(25200000); //progress check again at 11
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressCheck(homework);
                for(int i = 0; i < homework.size();){
                    hw a = homework.get(i);
                    if(a.getWorkDone() == 100){
                        homework.remove(a);
                    } else{
                        i++;
                    }
                }
                userPrint(homework);
                write(fileName, homework);
            }
        };
    }
}
