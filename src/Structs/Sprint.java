package Structs;

import java.util.Date;

/**
 * Created by Eugene Berlizov on 03.12.2014.
 */
public class Sprint {
    private Date start;
    private Date end;

    public Sprint(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Sprint() {}

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
