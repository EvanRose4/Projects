import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class SpaceInvaders extends JFrame {
    private SIPanel siPanel;
    private JMenuItem resume, pause, newGame, quit;
    private boolean paused = false;
    
    public static void main(String[]args) {
        JFrame si = new SpaceInvaders();
        si.setVisible(true);
    }
    public SpaceInvaders() {
        super("Space Invaders");
        setPreferredSize(new Dimension(500, 450));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addCloseListener();// sets the close button to doExit();
        setResizable(false);
        ////Add the MenuBar and Menus
        addMenuBar();
        
        //Add SIPanel
        siPanel = new SIPanel();
        add( siPanel );
        
        //Pack and Center the Frame
        pack();
        setLocationRelativeTo(null);
    }
    /**
     * Creates a Window Listener which will call doExit() when the Close Button
     * is Clicked
     */
    private void addCloseListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }
        });
    }
    private void addMenuBar() {
        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(addGameMenu());// method returns JMenu
        mainMenuBar.add(Box.createHorizontalGlue());
        mainMenuBar.add(addHelpMenu());// method returns JMenu
        setJMenuBar(mainMenuBar);
    }
    private JMenu addGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        
        newGame = new JMenuItem("New Game");
        gameMenu.add(newGame);
        
        gameMenu.addSeparator();//Separate New Game from Pause and Resume
        
        pause = new JMenuItem("Pause");
        gameMenu.add(pause);
        pause.setEnabled(false);
        
        resume = new JMenuItem("Resume");
        gameMenu.add(resume);
        resume.setEnabled(false);
        
        gameMenu.addSeparator();//Separate Pause and Resume from Quit
        
        quit = new JMenuItem("Quit");
        gameMenu.add(quit);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });
        
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siPanel.resume();
                resume.setEnabled(false);
                pause.setEnabled(true);
                paused = false;
            }
        });
        
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siPanel.pause();
                pause.setEnabled(false);
                resume.setEnabled(true);
                paused = true;
            }
        });
        
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGameDialog();
            }
        });
        gameMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuCanceled(MenuEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void menuDeselected(MenuEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void menuSelected(MenuEvent arg0) {
                if(siPanel.isRunning()) {
                    newGame.setEnabled(true);
                    if(!paused) {
                    pause.setEnabled(true);
                    }
                }
            }
            
        });
        setHotKeys();
        return gameMenu;
    }
    private JMenu addHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About...");
        helpMenu.add(about);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame("About"),
                        "Space Invaders\n by Evan Rose");
            }
        });
        return helpMenu;
    }
    private void newGameDialog() {
        int result = JOptionPane.showConfirmDialog(new JFrame("Exit Menu"),
                "Start a new game?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if(paused) {
                siPanel.resume();
            }
            siPanel.resetQueue();
            pause.setEnabled(false);
            resume.setEnabled(false);
        }
    }
    private void setHotKeys() {
        KeyStroke newGameKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        newGame.setAccelerator(newGameKey);
        KeyStroke pauseKey = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        pause.setAccelerator(pauseKey);
        KeyStroke resumeKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        resume.setAccelerator(resumeKey);
        KeyStroke quitKey = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
        quit.setAccelerator(quitKey);
    }
    private void doExit() {
        int result = JOptionPane.showConfirmDialog(new JFrame("Exit Menu"),
                "Dare to Quit", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
