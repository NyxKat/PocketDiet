package Model;

public class MonthAndYear {
    private String month;
    private int year;


    public MonthAndYear(String month,int year){
        this.month=month;
        this.year=year;
    }

    public String getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if(o==null) return false;
        if(!(o instanceof  MonthAndYear)) return false;

        MonthAndYear other=(MonthAndYear) o;
        return this.month==other.month && this.year==other.year;
    }

    @Override
    public int hashCode() {
        return (int) year *this.month.hashCode();
    }
}
