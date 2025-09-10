import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

public class Music implements ActionListener {
    static Clip clip;
    JFrame frame;
    JPanel panel, browsePanel;
    Buttons playButton, nextButton, previousButton, browse;
    boolean playing = true;
    JLabel album, songTitle, singer, by;
    ArrayList musicArray, musicTitlesArray;
    int musicNum;

    public Music(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/musiclist.txt"));
            musicArray = new ArrayList<>();

        } catch (Exception e) {
            System.out.print("Error: " + e.getMessage());
        }

        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/musictitles.txt"));
            musicTitlesArray = new ArrayList<>();

        } catch (Exception e) {
            System.out.print("Error: " + e.getMessage());
        }
        musicTitleListing("src/musictitles.txt");
        musicArrayListing("src/musiclist.txt");
        String filepath = String.valueOf(musicArray.get(musicNum));
        PlayMusic(filepath);
    }

    private void musicTitleListing(String musicTitles){
        try (BufferedReader br = new BufferedReader(new FileReader(musicTitles))) {
            String line;
            while ((line = br.readLine()) != null) {
                musicTitlesArray.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void musicArrayListing(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                musicArray.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void PlayMusic(String path) {
        try {
            File musicpath = new File(path);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void Manage(){
        frame = new JFrame("Music");
        frame.setSize(450, 250);
        frame.setBackground(new Color(255, 241, 137));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocation(90, 90);

        playButton = new Buttons("Pause");
        playButton.setBounds(105, 160, 80, 30);
        playButton.setBackground(Color.orange);
        playButton.addActionListener(this);

        nextButton = new Buttons("Next");
        nextButton.setBounds(190, 160, 80, 30);
        nextButton.setBackground(Color.orange);
        nextButton.addActionListener(this);

        previousButton = new Buttons("Previous");
        previousButton.setBounds(20, 160, 80, 30);
        previousButton.setBackground(Color.orange);
        previousButton.addActionListener(this);
        previousButton.addActionListener(this);

        browse = new Buttons("Browse Music");
        browse.setBounds(280, 160, 130, 30);
        browse.setBackground(Color.orange);
        browse.addActionListener(this);

        panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBounds(20, 20, 400, 100);
        panel.setLayout(null);
        frame.add(panel);

        ImageIcon albumIcon = new ImageIcon("src/icons/wut.jpg");
        Image scaledAlbum = albumIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon albumFinal = new ImageIcon(scaledAlbum);
        album = new JLabel(albumFinal);
        album.setBounds(10,10, 80, 80);
        panel.add(album);

        songTitle = new JLabel(String.valueOf(musicTitlesArray.get(musicNum)));
        songTitle.setBounds(105, 28, 300, 20);
        songTitle.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(songTitle);

        by = new JLabel("by");
        by.setBounds(105, 52, 300, 14);
        by.setFont(new Font("Serif", Font.BOLD, 12));
        panel.add(by);

        singer = new JLabel("Cutie Cattos");
        singer.setBounds(120, 52, 300, 12);
        singer.setFont(new Font("Serif", Font.BOLD, 12));
        panel.add(singer);

        frame.add(previousButton);
        frame.add(nextButton);
        frame.add(playButton);
        frame.add(browse);

        frame.setVisible(true);
    }

    public static void main(String[] args){
        Music music = new Music();
        music.Manage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton && playing){
            clip.stop();
            playing = false;
            playButton.setText("Play");
        }else{
            clip.start();
            playing = true;
            playButton.setText("Pause");
        }
        if (e.getSource() == nextButton){
            musicNum++;
            if (musicNum >= musicArray.size()) {
                musicNum = 0;
            }

            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            songTitle.setText(String.valueOf(musicTitlesArray.get(musicNum)));
            String filepath = String.valueOf(musicArray.get(musicNum));
            PlayMusic(filepath);
            playing = true;
            playButton.setText("Pause");
        }
        if (e.getSource() == previousButton){
            musicNum--;
            if (musicNum <= musicArray.size()) {
                musicNum = 0;
            }

            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            songTitle.setText(String.valueOf(musicTitlesArray.get(musicNum)));
            String filepath = String.valueOf(musicArray.get(musicNum));
            PlayMusic(filepath);
            playing = true;
            playButton.setText("Pause");
        }
        if(e.getSource() == browse){
        }
    }
}