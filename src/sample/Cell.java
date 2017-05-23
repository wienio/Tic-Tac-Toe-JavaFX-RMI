package sample;

import java.util.EventObject;

/**
 * Created by Wienio on 2017-05-18.
 */
public class Cell extends EventObject {
    // 1 to status dla X
    // 2 to status dla O

    private int status;
    private int index;

    public Cell(Object source, int status, int index) {
        super(source);
        this.status = status;
        this.index = index;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getIndex() {
        return index;
    }
}
