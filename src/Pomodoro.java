import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Pomodoro implements ActionListener {
    JFrame frame, editor;
    JPanel panel, picPanel, timePanel, buttonPanel;
    Buttons manage, notes, edit, start, reset, add;
    Buttons pomodorobut, shortbut, longbut, saveEdited;
    ImageIcon gif;
    JLabel clock, header, pomodoroinEditor, shortbinEditor, longbinEditor, minutesEditor;
    JTextArea quote;
    ArrayList<String> quotes;
    int pomo = 25 * 60;
    int blong = 15 * 60;
    int bshort =  10 * 60;
    Timer timer;
    boolean started = false;
    String startLabel = "Start";
    String pauseLabel = "Pause";
    int current, currents;
    Music music;
    Random random;
    JSpinner pomoSpinner, shortbSpinner, longbSpinner;

    public Pomodoro() {
        frame = new JFrame("Sage's Study");
        frame.setSize(425, 585);
        frame.setLayout(null);
        frame.setLocation(300, 100);

        //lower part of the UI
        //This is for our bg img
        ImageIcon originalIcon = new ImageIcon("src/backg.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(425, 585, Image.SCALE_SMOOTH);
        ImageIcon bgimg = new ImageIcon(scaledImage);
        JLabel bg = new JLabel(bgimg);
        bg.setBounds(0, 0, 425, 585);
        frame.add(bg);

        //backround color
        //Container content = frame.getContentPane();
        //content.setBackground(new Color(34, 17, 7));

        //Now this will be for the cutie quotes
        quotes = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/OMG!.txt"));
            quotes = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                quotes.add(line);
            }

        } catch (IOException e) {
            System.out.print("An error has occured: " + e.getMessage());
        }

        random = new Random();
        quote = new JTextArea(changeQuote());
        quote.setLineWrap(true);
        quote.setWrapStyleWord(true);
        quote.setFocusable(false);
        quote.setEditable(false);
        quote.setOpaque(false);
        quote.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quote.setSize(120, 100);

        Timer quoteTimer = new Timer(10000, e ->{
            quote.setText(changeQuote());
        });
        quoteTimer.start();

        //next let's see if we can organize the design with panels
        panel = new JPanel(new GridBagLayout());
        panel.setBounds(252,320,140, 70);
        //panel.setOpaque(false);
        panel.setBackground(Color.white);
        panel.add(quote);

        //now for the pictures
        picPanel = new JPanel(new GridBagLayout());
        picPanel.setBounds(30,320,200, 200);

        gif = new ImageIcon("src/icons/sec.gif");
        JLabel Gif = new JLabel(gif);
        picPanel.add(Gif);

        //buttons sa side ng pic
        manage = new Buttons("Music");
        manage.setBackground(Color.orange);
        manage.setBounds(252,410, 140, 30);
        manage.addActionListener(this);

        notes = new Buttons("Notes/Tasks");
        notes.setBackground(Color.orange);
        notes.setBounds(252,450, 140, 30);

        edit = new Buttons("Edit Timer");
        edit.setBackground(Color.orange);
        edit.setBounds(252,490, 140, 30);
        edit.addActionListener(this);
        //End of the lower part

        //title panel
        header = new JLabel("Pomodoro Timer");
        header.setBounds(105, 35, 330, 50);
        frame.add(header);

        ImageIcon title = new ImageIcon("src/title.png");
        Image scaledTitle = title.getImage().getScaledInstance(325, 59, Image.SCALE_SMOOTH);
        ImageIcon ftitle = new ImageIcon(scaledTitle);
        JLabel titles = new JLabel(ftitle);
        titles.setBounds(20, 20, 380, 80);
        frame.add(titles);

        //music
        music = new Music();

        //Now for the clock
        timePanel = new JPanel();
        timePanel.setBackground(Color.white);
        timePanel.setBounds(38, 137, 340, 120);

        clock = new JLabel(TFormat(pomo));
        clock.setBounds(10, 40, 320, 100);
        clock.setBackground(Color.black);

        current = pomo;
        currents = pomo;
        clock.setText(TFormat(current));
        timer = new Timer(1000, _ -> {
            if (current > 0) {
                current--;
                clock.setText(TFormat(current));
            }else{
                timer.stop();
            }
            currents = pomo;
        });

        //this is for the font
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/LoveYaLikeASister-Regular.ttf"));
            font = font.deriveFont(85f); // font size
            clock.setFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            clock.setFont(new Font("Serif", Font.BOLD, 28)); // fallback
        }
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/LoveYaLikeASister-Regular.ttf"));
            font = font.deriveFont(30f); // font size
            header.setFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            header.setFont(new Font("Serif", Font.BOLD, 28)); // fallback
        }
        //end of font code

        //for the function of the timer
        //timer buttons, yung maliliit sa baba

        buttonPanel = new JPanel();
        buttonPanel.setBounds(40, 265, 340, 40);
        buttonPanel.setOpaque(false);
        // end of maliliit na buttons

        //mode buttons
        pomodorobut = new Buttons("Pomodoro");
        pomodorobut.setBackground(new Color(255, 255, 255));
        pomodorobut.setBounds(38, 105, 110, 25);
        pomodorobut.addActionListener(this);
        frame.add(pomodorobut);

        shortbut = new Buttons("Short Break");
        shortbut.setBounds(153, 105, 110, 25);
        shortbut.setBackground(Color.orange);
        shortbut.addActionListener(this);
        frame.add(shortbut);

        longbut = new Buttons("Long Break");
        longbut.setBounds(267, 105, 110, 25);
        longbut.setBackground(Color.orange);
        longbut.addActionListener(this);
        frame.add(longbut);

        start = new Buttons(startLabel);
        start.setBackground(Color.orange);
        start.setBounds(80,265, 30, 30);
        start.addActionListener(e -> {
            if(!started){
                timer.start();
                started = true;
                start.setText(pauseLabel);
            }else if(started){
                timer.stop();
                started = false;
                start.setText("Start");
            }
        });
        buttonPanel.add(start);

        //start function
        reset = new Buttons("Reset");
        reset.setBackground(Color.orange);
        reset.setBounds(50,265, 30, 30);
        reset.addActionListener(this);
        buttonPanel.add(reset);

        add = new Buttons("Add");
        add.setBackground(Color.orange);
        add.setBounds(110,265, 30, 30);
        buttonPanel.add(add);
        add.addActionListener(this);
        frame.add(buttonPanel);


        //end of timer function
        timePanel.add(clock);
        frame.add(timePanel);
        //end of the clock

        //bro this is where you add and edit your frame alright!? This should be at the last part.
        frame.add(panel);
        frame.add(manage);
        frame.add(notes);
        frame.add(edit);
        frame.add(picPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(bg);
        frame.setVisible(true);

    }

    public String changeQuote() {
        if (quotes.isEmpty()) return "No quotes available!";
        return quotes.get(random.nextInt(quotes.size()));
    }

    public String TFormat(int seconds){
        int hour = seconds / 3600;
        int minute = seconds / 60;
        int second = seconds % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == reset) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
                start.setText("Start");
                started = false;
                current = currents;
            }
            clock.setText(TFormat(currents));
            current = currents;
            return;
        }

        if (timer != null && timer.isRunning()) {
            timer.stop();
            start.setText("Start");
            started = false;
        }

        if (e.getSource() == pomodorobut){
            pomodorobut.setBackground(new Color(255, 255, 255));
            shortbut.setBackground(Color.orange);
            longbut.setBackground(Color.orange);
            current = pomo;
            picPanel.removeAll();
            gif = new ImageIcon("src/icons/sec.gif");
            JLabel Gif = new JLabel(gif);
            picPanel.add(Gif);
                clock.setText(TFormat(current));
                timer = new Timer(1000, _ -> {
                    if (current > 0) {
                        current--;
                        clock.setText(TFormat(current));
                    }else{
                        timer.stop();
                    }
                    currents = pomo;
                });
        }
        if (e.getSource() == shortbut){
            shortbut.setBackground(new Color(255, 255, 255));
            pomodorobut.setBackground(Color.orange);
            longbut.setBackground(Color.orange);
            current = bshort;
            picPanel.removeAll();
            gif = new ImageIcon("src/icons/sleep.gif");
            JLabel Gif = new JLabel(gif);
            picPanel.add(Gif);
                clock.setText(TFormat(current));
                timer = new Timer(1000, _ -> {
                    if (current > 0) {
                        current--;
                        clock.setText(TFormat(current));
                    }else{
                        timer.stop();
                    }
                    currents = bshort;
                });
        }
        if (e.getSource() == longbut){
            longbut.setBackground(new Color(255, 255, 255));
            shortbut.setBackground(Color.orange);
            pomodorobut.setBackground(Color.orange);
            current = blong;
            picPanel.removeAll();
            gif = new ImageIcon("src/icons/dancecat.gif");
            JLabel Gif = new JLabel(gif);
            picPanel.add(Gif);
            clock.setText(TFormat(current));
            timer = new Timer(1000, _ -> {
                if (current > 0) {
                    current--;
                    clock.setText(TFormat(current));
                }else{
                    timer.stop();
                }
                currents = blong;
            });
        }
        if (e.getSource() == edit){
            editor = new JFrame("Edit Timer");
            editor.setSize(260, 200);
            editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editor.setLocation(400, 470);
            editor.setLayout(null);

            Font customFont = new Font("Serif", Font.PLAIN, 25);

            pomodoroinEditor = new JLabel("Pomodoro: ");
            pomodoroinEditor.setBounds(10, 10, 130, 25);
            pomodoroinEditor.setFont(customFont);
            editor.add(pomodoroinEditor);

            SpinnerNumberModel pomoModel = new SpinnerNumberModel(25, 0, 1000, 1);
            SpinnerNumberModel shortbModel = new SpinnerNumberModel(10, 0, 1000, 1);
            SpinnerNumberModel longbModel = new SpinnerNumberModel(15, 0, 1000, 1);

            pomoSpinner = new JSpinner(pomoModel);
            pomoSpinner.setBounds(130, 10, 40, 25);
            editor.add(pomoSpinner);

            minutesEditor = new JLabel("minutes");
            minutesEditor.setBounds(170, 10, 400, 25);
            editor.add(minutesEditor);

            shortbinEditor = new JLabel("Short Break: ");
            shortbinEditor.setBounds(10, 50, 140, 25);
            shortbinEditor.setFont(customFont);
            editor.add(shortbinEditor);

            shortbSpinner = new JSpinner(shortbModel);
            shortbSpinner.setBounds(150, 55, 40, 25);
            editor.add(shortbSpinner);

            minutesEditor = new JLabel("minutes");
            minutesEditor.setBounds(190, 55, 400, 25);
            editor.add(minutesEditor);

            longbinEditor = new JLabel("Long Break: ");
            longbinEditor.setBounds(10, 90, 140, 25);
            longbinEditor.setFont(customFont);
            editor.add(longbinEditor);

            longbSpinner = new JSpinner(longbModel);
            longbSpinner.setBounds(150, 95, 40, 25);
            editor.add(longbSpinner);

            minutesEditor = new JLabel("minutes");
            minutesEditor.setBounds(190, 95, 400, 25);
            editor.add(minutesEditor);

            saveEdited = new Buttons("Save");
            saveEdited.setBackground(Color.orange);
            saveEdited.setBounds(80,130, 80, 25);
            saveEdited.addActionListener(a -> {
                pomo = 60 * (Integer) pomoSpinner.getValue();
                bshort = 60 * (Integer) shortbSpinner.getValue();
                blong = 60 * (Integer) longbSpinner.getValue();
                if (!started) {
                    if (pomodorobut.getBackground().equals(new Color(255, 255, 255))) {
                        current = pomo;
                    } else if (shortbut.getBackground().equals(new Color(255, 255, 255))) {
                        current = bshort;
                    } else if (longbut.getBackground().equals(new Color(255, 255, 255))) {
                        current = blong;
                    }
                    clock.setText(TFormat(current));
                    currents = current;
                }
            });
            editor.add(saveEdited);

            editor.setVisible(true);
        }
        if (e.getSource() == manage){
            music.Manage();
        }
        if(e.getSource() == add){
            current += (5 * 60);
            clock.setText(TFormat(current));
        }
    }

    public static void main(String[]args){
        Pomodoro time = new Pomodoro();
    }
}
