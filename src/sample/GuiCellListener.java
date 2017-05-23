package sample;

import java.util.EventListener;

/**
 * Created by Wienio on 2017-05-22.
 */
public interface GuiCellListener extends EventListener {
    void writeCell (Cell event);
}
