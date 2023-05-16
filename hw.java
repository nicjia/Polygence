import java.util.Comparator;

public class hw {
    private String name;
    private int work; //amount of work for assignment
    private int workDone; //how many hours
    private int daysLeft;

    public hw(String n, int a, int b, int c){
        name = n;
        work = a;
        workDone = b;
        daysLeft = c;
    }

    //public static final Comparator<hw> hwComparator = (hw a, hw b) -> hw.name.compareTo(o2.name);

    public int compareTo(hw a) {
        double w = this.totalWork();
        if(a.totalWork() > w){
            return -1;
        } else if(a.totalWork() < w){
            return 1;
        }
        else {
            return 0;
        }
    }

    public double totalWork(){
        if(workDone == 100){
            return 0;
        } else if(this.daysLeft == 0){
            return this.work*(100-this.workDone)/0.01;
        }
        double w = this.work*(100-this.workDone)/this.daysLeft;
        return w;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public int getWorkDone() {
        return workDone;
    }

    public int getWork() {
        return work;
    }

    public String getName() {
        return name;
    }

    public void setWorkDone(int workDone) {
        this.workDone = workDone;
    }



    public String toString(){
        return name + ", " + work + ", " + workDone + ", " + daysLeft;
    }
}
