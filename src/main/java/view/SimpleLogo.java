package view;

import controler.Controller;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by theo on 12/04/17.
 */
public class SimpleLogo extends JFrame implements ActionListener {
    public static final Dimension VGAP = new Dimension(1,5);
    public static final Dimension HGAP = new Dimension(5,1);

    private DrawSheet sheet;
    private JTextField inputValue;
    private Controller controller;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){

                SimpleLogo fenetre = new SimpleLogo();
                fenetre.setVisible(true);
            }
        });
    }

    public SimpleLogo() {
        super("un logo tout simple");
        logoInit();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                super.windowClosing(arg0);
                System.exit(0);
            }
        });
    }

    public void logoInit() {
        getContentPane().setLayout(new BorderLayout(10,10));

        // Boutons
        JToolBar toolBar = new JToolBar();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(toolBar);

        getContentPane().add(buttonPanel,"North");

        addButton(toolBar,"Effacer","Nouveau dessin","/icons/index.png");

        toolBar.add(Box.createRigidArea(this.HGAP));
        this.inputValue=new JTextField("45",5);
        toolBar.add(this.inputValue);
        addButton(toolBar, "Avancer", "Avancer 50", null);
        addButton(toolBar, "Droite", "Droite 45", null);
        addButton(toolBar, "Gauche", "Gauche 45", null);
        addButton(toolBar, "Lever", "Lever Crayon", null);
        addButton(toolBar, "Baisser", "Baisser Crayon", null);
        addButton(toolBar,"Ajouter","Ajouter une tortue",null);

        String[] colorStrings = {"noir", "bleu", "cyan","gris fonce","rouge",
                "vert", "gris clair", "magenta", "orange",
                "gris", "rose", "jaune"};

        // Create the combo box
        toolBar.add(Box.createRigidArea(this.HGAP));
        JLabel colorLabel = new JLabel("   Couleur: ");
        toolBar.add(colorLabel);
        JComboBox colorList = new JComboBox(colorStrings);
        toolBar.add(colorList);

        colorList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                int n = cb.getSelectedIndex();
                controller.setColor(n);
            }
        });


        // Menus
        JMenuBar menubar=new JMenuBar();
        setJMenuBar(menubar);	// on installe le menu bar
        JMenu menuFile=new JMenu("File"); // on installe le premier menu
        menubar.add(menuFile);

        addMenuItem(menuFile, "Effacer", "Effacer", KeyEvent.VK_N);
        addMenuItem(menuFile, "Quitter", "Quitter", KeyEvent.VK_Q);

        JMenu menuCommandes=new JMenu("Commandes"); // on installe le premier menu
        menubar.add(menuCommandes);
        addMenuItem(menuCommandes, "Avancer", "Avancer", -1);
        addMenuItem(menuCommandes, "Droite", "Droite", -1);
        addMenuItem(menuCommandes, "Gauche", "Gauche", -1);
        addMenuItem(menuCommandes, "Lever Crayon", "Lever", -1);
        addMenuItem(menuCommandes, "Baisser Crayon", "Baisser", -1);

        JMenu menuHelp=new JMenu("Aide"); // on installe le premier menu
        menubar.add(menuHelp);
        addMenuItem(menuHelp, "Aide", "Help", -1);
        addMenuItem(menuHelp, "A propos", "About", -1);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // les boutons du bas
        JPanel p2 = new JPanel(new GridLayout());
        JButton b20 = new JButton("Proc1");
        p2.add(b20);
        b20.addActionListener(this);
        JButton b21 = new JButton("Proc2");
        p2.add(b21);
        b21.addActionListener(this);
        JButton b22 = new JButton("Proc3");
        p2.add(b22);
        b22.addActionListener(this);

        getContentPane().add(p2,"South");

        // Création de la feuille de dessin
        this.sheet = new DrawSheet();
        this.sheet.setBackground(Color.white);
        this.sheet.setSize(new Dimension(600,400));
        this.sheet.setPreferredSize(new Dimension(600,400));

        getContentPane().add(this.sheet,"Center");

        // Creation du model
        Dimension size = sheet.getSize();
        Model model = new Model((size.getWidth()+45)/2,size.getHeight()/2);
        model.addObserver(this.sheet);
        this.sheet.update(model,model.getCurrentTurtle());

        // On créé le controleur des tortues
        this.controller = new Controller(model);

        pack();
        setVisible(true);
    }

    /** la gestion des actions des boutons */
    public void actionPerformed(ActionEvent e)
    {
        Dimension size = sheet.getSize();
        System.out.println(e.getActionCommand());
        controller.handleAction(e.getActionCommand(),inputValue.getText(),size.getWidth(),size.getHeight());
//        this.sheet.repaint();
    }


    //utilitaires pour installer des boutons et des menus
    public void addButton(JComponent p, String name, String tooltiptext, String imageName) {
        JButton b;
        if ((imageName == null) || (imageName.equals(""))) {
            b = (JButton)p.add(new JButton(name));
        }
        else {
            java.net.URL u = this.getClass().getResource(imageName);
            if (u != null) {
                ImageIcon im = new ImageIcon (u);
                b = (JButton) p.add(new JButton(im));
            }
            else
                b = (JButton) p.add(new JButton(name));
            b.setActionCommand(name);
        }

        b.setToolTipText(tooltiptext);
        b.setBorder(BorderFactory.createRaisedBevelBorder());
        b.setMargin(new Insets(0,0,0,0));
        b.addActionListener(this);
    }

    public void addMenuItem(JMenu m, String label, String command, int key) {
        JMenuItem menuItem;
        menuItem = new JMenuItem(label);
        m.add(menuItem);

        menuItem.setActionCommand(command);
        menuItem.addActionListener(this);
        if (key > 0) {
            if (key != KeyEvent.VK_DELETE)
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK, false));
            else
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0, false));
        }
    }
}
