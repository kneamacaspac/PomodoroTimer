import javax.swing.*;
import java.awt.*;

public class Entry {
    public Entry(){
        JFrame frame = new JFrame("Hatdog");
        frame.setSize(300, 300);
        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.BLUE);

        frame.setVisible(true);
    }

    public static void main(String[] args){
        Entry enter = new Entry();
    }
}
