package View;

import Controller.Controller;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class View extends JFrame {

    private Controller controller;

    private JMenu top_menu;
    private JMenuBar top_menu_bar;

    private JLayeredPane player_area1;
    private JPanel player_area_cards1;
    private JPanel player_area_last_played_cards1;
    private JPanel player_area_rare_found1;
    private JPanel player_area_details1;

    private JLayeredPane player_area2;
    private JPanel player_area_cards2;
    private JPanel player_area_last_played_cards2;
    private JPanel player_area_rare_found2;
    private JPanel player_area_details2;

    private JLayeredPane whole_table;
    private JPanel whole_table_grid_paths;
    private JButton stack_button;
    private JLabel stack_label;

    private JButton[] cards1_array = new JButton[8];
    private JButton[] cards2_array = new JButton[8];

    private JLabel[] last_played1_array = new JLabel[4];
    private JLabel[] last_played2_array = new JLabel[4];

    private int[][] detailed_score;
    private int[][] rare_holdings;
    private int[][] path_state_findings;
    private int[][] available_pawns;
    private int[][] photographies;
    private Timer timer;


    /**
     * <b>Constructor</b>
     * makes a new Window and initializes buttons and paths
     * @Postcondition creates a new window with the two panels and the main table
     */
    public View() {
        controller = new Controller(this);


        setTitle("Αναζητώντας τα Χαμένα Μινωικά Ανάκτορα - csd5448");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1400, 800);


        top_menu_bar = MakeMenuBar();
        setJMenuBar(top_menu_bar);

        player_area1 = MakeArea1();
        add(player_area1, BorderLayout.NORTH);

        player_area2 = MakeArea2();
        add(player_area2, BorderLayout.SOUTH);

        whole_table = MakeTable();
        add(whole_table, BorderLayout.CENTER);
        setVisible(true);

        // pop up for turn ask appropriate player to play
        int turn = controller.get_turn() + 1;
        JOptionPane.showMessageDialog(null, "Το παιχνίδι ξεκίνησε και είναι η σειρά του παίκτη: " + turn, "Παράθυρο Έναρξης", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * <b>Transformer</b>
     * shows the actual dialog to the user
     * @param image_path is the path that contains the image of the finding
     * @param message is the message displayed
     * @param description is the description displayed
     * @Precondition image_path, message and description (parameters) are not null and valid
     * @Postcondition shows the appropriate information as a dialog
     */
    private static void show_dialog(String image_path, String message, String description) {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File(image_path)).getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            String text = "<html><h3>" + message + "</h3><p style='width:480px;'>" + description + "</p></html>";
            JOptionPane.showMessageDialog(null, text, "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * sets the menu bar on the cop that contains options for the client
     * @Precondition none
     * @return the top menu Bar
     */
    private JMenuBar MakeMenuBar() {
        /* Set the Menu on the top */
        top_menu_bar = new JMenuBar();
        top_menu = new JMenu("Menu");

        JMenuItem item0 = new JMenuItem("New Game");
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.game_init();
                update_all_components();
            }
        });
        JMenuItem item1 = new JMenuItem("Save Game");
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.save_game();
            }
        });
        JMenuItem item2 = new JMenuItem("Continue Saved Game");
        item2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.continue_saved_game();
                update_all_components();
            }
        });
        JMenuItem item3 = new JMenuItem("Exit Game");
        item3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        top_menu.add(item0);
        top_menu.add(item1);
        top_menu.add(item2);
        top_menu.add(item3);


        /* Add it to the bar */
        top_menu_bar.add(top_menu);
        return top_menu_bar;
    }

    /**
     * <b>Transformer</b>
     * creates a button array for cards of player1
     * @Precondition everything for player1 has been initialized
     * @Postcondition the array cards1_array has the cards of player1
     */
    void set_button_array1() {
        for (int i = 0; i < 8; i++) {
            String photo_to_look = controller.get_cards_as_string()[0][i] + ".jpg";
            JButton b = new JButton();

            ImageIcon image = new ImageIcon("src/project_assets/images/cards/" + photo_to_look);
            Image image_temp = image.getImage();
            Image scaled_image = image_temp.getScaledInstance(70, 100, Image.SCALE_SMOOTH); // Set dimensions as needed
            ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
            b.setIcon(scaled_image_icon);

            b.setBorderPainted(false);
            // If its not his turn make it look like disabled
            if (controller.get_turn() == 1) {
                b.setEnabled(false);
            }

            int card_index = i;
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (controller.get_turn() == 0) {
                        b.setBorderPainted(true);
                    }
                }

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (controller.get_turn() != 0) {
                        print_information_string("Δεν είναι η σειρά σου ακόμα");
                    } else if (e.getButton() == MouseEvent.BUTTON1) { // Discard
                        b.setVisible(false);
                        for (JButton button : cards1_array) {
                            for (MouseListener m : button.getMouseListeners()) {
                                button.removeMouseListener(m); // Remove all mouse listeners to make the completely disabled
                            }
                            button.setEnabled(false);
                        }
                        JOptionPane.showMessageDialog(null, "Τώρα πρέπει να πάρεις κάρτα από την στοίβα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                        stack_button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    controller.take_card_from_stack(0, card_index);
                                    update_all_components();
                                }
                            }
                        });
                    } else if (e.getButton() == MouseEvent.BUTTON3) { // play
                        if (!controller.check_if_card_playable(0, card_index)) {
                            JOptionPane.showMessageDialog(null, "Δεν μπορείς να παίξεις αυτήν την κάρτα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if (!controller.check_if_pawn_exists(0, card_index)) {
                                // pop up to make a new pawn for the player for this specific anaktoro
                                int result = JOptionPane.showOptionDialog(null, "Διάλεξε ένα πιόνι:", "Επιλογή πιονιού", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Αρχαιολόγος", "Θησέας"}, null);
                                while (result == -1 || available_pawns[0][result] == 0) { // check for availability
                                    result = JOptionPane.showOptionDialog(null, "Διάλεξε ένα πιόνι:", "Επιλογή πιονιού", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Αρχαιολόγος", "Θησέας"}, null);
                                }
                                controller.set_pawn_for_player_play(0, card_index, result); // 0 is first button pressed(archaiologos), 1 is second button pressed(thiseas)
                            } else {
                                controller.play_card(0, card_index);
                            }
                            b.setVisible(false); // set the button to invisible before playing
                            for (JButton button : cards1_array) {
                                for (MouseListener m : button.getMouseListeners()) {
                                    button.removeMouseListener(m); // Remove all mouse listeners to make the completely disabled
                                }
                                button.setEnabled(false);
                            }
                            JOptionPane.showMessageDialog(null, "Τώρα πρέπει να πάρεις κάρτα από την στοίβα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                            stack_button.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        controller.take_card_from_stack(0, card_index);
                                        update_all_components();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBorderPainted(false);
                }
            });
            cards1_array[i] = b;

        }
    }

    /**
     * <b>Transformer</b>
     * creates a button array for cards of player2
     * @Precondition everything for player2 has been initialized
     * @Postcondition the array cards2_array has the cards of player2
     */
    void set_button_array2() {
        for (int i = 0; i < 8; i++) {
            String photo_to_look = controller.get_cards_as_string()[1][i] + ".jpg";
            JButton b = new JButton();

            // Load the image
            ImageIcon image = new ImageIcon("src/project_assets/images/cards/" + photo_to_look);

            Image image_temp = image.getImage();
            Image scaled_image = image_temp.getScaledInstance(70, 100, Image.SCALE_SMOOTH); // Set dimensions as needed
            ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
            b.setIcon(scaled_image_icon);

            b.setBorderPainted(false);

            // If its not his turn make it look like disabled
            if (controller.get_turn() == 0) {
                b.setEnabled(false);
            }

            int card_index = i;
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (controller.get_turn() == 1) {
                        b.setBorderPainted(true);
                    }
                }

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (controller.get_turn() != 1) {
                        print_information_string("Δεν είναι η σειρά σου ακόμα");
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        b.setVisible(false);
                        for (JButton button : cards2_array) {
                            for (MouseListener m : button.getMouseListeners()) {
                                button.removeMouseListener(m); // Remove all mouse listeners to make the completely disabled
                            }
                            button.setEnabled(false);
                        }
                        JOptionPane.showMessageDialog(null, "Τώρα πρέπει να πάρεις κάρτα από την στοίβα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                        stack_button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    controller.take_card_from_stack(1, card_index);
                                    update_all_components();
                                }
                            }
                        });
                    } else if (e.getButton() == MouseEvent.BUTTON3) { // play
                        if (!controller.check_if_card_playable(1, card_index)) {
                            JOptionPane.showMessageDialog(null, "Δεν μπορείς να παίξεις αυτήν την κάρτα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if (!controller.check_if_pawn_exists(1, card_index)) {
                                // pop up to make a new pawn for the player for this specific anaktoro
                                int result = JOptionPane.showOptionDialog(null, "Διάλεξε ένα πιόνι:", "Επιλογή πιονιού", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Αρχαιολόγος", "Θησέας"}, null);
                                while (result == -1 || available_pawns[1][result] == 0) { // check for availability
                                    result = JOptionPane.showOptionDialog(null, "Διάλεξε ένα πιόνι:", "Επιλογή πιονιού", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Αρχαιολόγος", "Θησέας"}, null);
                                }
                                controller.set_pawn_for_player_play(1, card_index, result); // 0 is first button pressed(archaiologos), 1 is second button pressed(thiseas)
                            } else {
                                controller.play_card(1, card_index); // do the changes (move the pawns and check if we found a finding or not)
                            }
                            b.setVisible(false); // set the button to invisible before playing
                            for (JButton button : cards2_array) {
                                for (MouseListener m : button.getMouseListeners()) {
                                    button.removeMouseListener(m); // Remove all mouse listeners to make the completely disabled
                                }
                                button.setEnabled(false);
                            }
                            JOptionPane.showMessageDialog(null, "Τώρα πρέπει να πάρεις κάρτα από την στοίβα", "Παράθυρο Ενημέρωσης", JOptionPane.INFORMATION_MESSAGE);
                            stack_button.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        controller.take_card_from_stack(1, card_index);
                                        update_all_components();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBorderPainted(false);
                }
            });

            cards2_array[i] = b;

        }
    }

    /**
     * <b>Transformer</b>
     * sets the labels arrays for last played cards labels at player area for both players
     * @Precondition everything for the players has been initialized
     * @Postcondition the last_played_arrays contains the labels of the last played cards for both players
     */
    void set_last_played_arrays() {
        for (int i = 0; i < 4; i++) {
            JLabel l = new JLabel();
            l.setFont(new Font("Arial", Font.PLAIN, 10));
            if (i == 0) {
                Border border = BorderFactory.createLineBorder(Color.RED, 2);
                l.setBorder(border);
            } else if (i == 1) {
                Border border = BorderFactory.createLineBorder(Color.YELLOW, 2);
                l.setBorder(border);
            } else if (i == 2) {
                Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
                l.setBorder(border);
            } else {
                Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
                l.setBorder(border);
            }
            if (controller.get_last_cards_as_string()[0][i] == null) {
                if (i == 0) {
                    l.setText("Κνωσσός");
                } else if (i == 1) {
                    l.setText("Μάλια");
                } else if (i == 2) {
                    l.setText("Φαιστός");
                } else if (i == 3) {
                    l.setText("Ζάκρος");
                }
            } else {
                String photo_to_look = controller.get_last_cards_as_string()[0][i] + ".jpg";
                ImageIcon image = new ImageIcon("src/project_assets/images/cards/" + photo_to_look);

                Image image_temp = image.getImage();
                Image scaled_image = image_temp.getScaledInstance(55, 100, Image.SCALE_SMOOTH); // Set dimensions as needed
                ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
                l.setIcon(scaled_image_icon);
            }
            last_played1_array[i] = l;
        }
        for (int i = 0; i < 4; i++) {
            JLabel l = new JLabel();
            l.setFont(new Font("Arial", Font.PLAIN, 10));
            if (i == 0) {
                Border border = BorderFactory.createLineBorder(Color.RED, 2);
                l.setBorder(border);
            } else if (i == 1) {
                Border border = BorderFactory.createLineBorder(Color.YELLOW, 2);
                l.setBorder(border);
            } else if (i == 2) {
                Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
                l.setBorder(border);
            } else {
                Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
                l.setBorder(border);
            }
            if (controller.get_last_cards_as_string()[1][i] == null) {
                if (i == 0) {
                    l.setText("Κνωσσός");
                } else if (i == 1) {
                    l.setText("Μάλια");
                } else if (i == 2) {
                    l.setText("Φαιστός");
                } else if (i == 3) {
                    l.setText("Ζάκρος");
                }
            } else {
                String photo_to_look = controller.get_last_cards_as_string()[1][i] + ".jpg";
                ImageIcon image = new ImageIcon("src/project_assets/images/cards/" + photo_to_look);

                Image image_temp = image.getImage();
                Image scaled_image = image_temp.getScaledInstance(55, 100, Image.SCALE_SMOOTH); // Set dimensions as needed
                ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
                l.setIcon(scaled_image_icon);
            }
            last_played2_array[i] = l;
        }
    }

    /**
     * <b>Transformer</b>
     * sets up the Player1 area with his cards points and holdings
     * @Precondition everything for player1 is properly initialized
     * @return the player1 area
     */
    private JLayeredPane MakeArea1() {
        /* Create the player area */
        detailed_score = controller.get_detailed_score_players(); // check might need to be updated more often
        rare_holdings = controller.get_holdings_both_players();
        set_last_played_arrays();
        photographies = controller.get_photographed_both_players();
        available_pawns = controller.get_pawn_availability_players(); // update things

        player_area1 = new JLayeredPane();
        player_area1.setLayout(null);
        player_area1.setPreferredSize(new Dimension(900, 170));
        player_area1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.RED, 2),
                "Παίκτης 1 - Διαθέσιμα Πιόνια: " + available_pawns[0][0] + " Αρχαιολόγοι και " + available_pawns[0][1] + " Θησέας"
        ));
        // Player cards area
        player_area_cards1 = new JPanel(new GridLayout(1, 8, 5, 0));
        player_area_cards1.setBounds(10, 20, 610, 100); // Adjust position and size
        set_button_array1();
        for (int i = 0; i < 8; i++) {
            player_area_cards1.add(cards1_array[i]);
        }
        player_area1.add(player_area_cards1);

        // Last played cards area
        player_area_last_played_cards1 = new JPanel(new GridLayout(1, 4, 5, 0));
        player_area_last_played_cards1.setBounds(720, 20, 260, 100); // Adjust position and size
        for (int i = 0; i < 4; i++) {
            player_area_last_played_cards1.add(last_played1_array[i]);
        }
        player_area1.add(player_area_last_played_cards1);

        // Rare findings found labels
        player_area_rare_found1 = new JPanel(new GridLayout(1, 4, 10, 0));
        player_area_rare_found1.setBounds(720, 115, 260, 50);
        for (int i = 0; i < 4; i++) {
            JLabel rare_finding = new JLabel();
            ImageIcon image;
            rare_finding.setOpaque(true); // Make the label background visible
            rare_finding.setHorizontalAlignment(SwingConstants.CENTER);
            if (i == 0) {
                image = new ImageIcon("src/project_assets/images/findings/ring.jpg");
                if (rare_holdings[0][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else if (i == 1) {
                image = new ImageIcon("src/project_assets/images/findings/kosmima.jpg");
                if (rare_holdings[0][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else if (i == 2) {
                image = new ImageIcon("src/project_assets/images/findings/diskos.jpg");
                if (rare_holdings[0][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else {
                image = new ImageIcon("src/project_assets/images/findings/ruto.jpg");
                if (rare_holdings[0][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            }
            Image image_temp = image.getImage();
            Image scaled_image = image_temp.getScaledInstance(35, 35, Image.SCALE_SMOOTH); // Set dimensions as needed
            ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
            rare_finding.setIcon(scaled_image_icon);
            player_area_rare_found1.add(rare_finding);
        }
        player_area1.add(player_area_rare_found1);

        // Player details area (to the right of last played cards)
        player_area_details1 = new JPanel(new GridLayout(3, 1, -10, 0));
        player_area_details1.setBounds(1000, 50, 360, 100); // Position on the right of last played cards
        JLabel label_score = new JLabel("Το σκορ μου: " + detailed_score[0][0] + " πόντοι");
        player_area_details1.add(label_score);
        JButton toixografies_button = new JButton("Οι Τοιχογραφίες μου");
        toixografies_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Οι τοιχογραφίες του παίκτη 1");
                frame.setSize(500, 400);
                frame.setLayout(new GridLayout(2, 3, 0, 0)); // 2 rows, 3 columns, 10px gap
                for (int i = 1; i < 7; i++) {
                    String photo_to_look = "fresco" + i;
                    if (i == 3 || i > 4) {
                        photo_to_look = photo_to_look + "_15.jpg";
                    } else {
                        photo_to_look = photo_to_look + "_20.jpg";
                    }
                    ImageIcon imageIcon = new ImageIcon("src/project_assets/images/findings/" + photo_to_look); // Load image
                    JLabel image_label = new JLabel();
                    if (photographies[0][i - 1] == 1) {
                        image_label.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                    } else {
                        image_label.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                    }
                    image_label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(200, 220, Image.SCALE_SMOOTH))); // Resize image
                    frame.add(image_label); // Add to the frame
                }
                frame.setVisible(true);
            }
        });
        player_area_details1.add(toixografies_button);
        JLabel label_agalmatakia = new JLabel("Αγαλματάκια: " + detailed_score[0][1]);
        player_area_details1.add(label_agalmatakia);


        JLabel label_agalmataki_plain = new JLabel();
        ImageIcon image = new ImageIcon("src/project_assets/images/findings/snakes.jpg");
        Image image_temp = image.getImage();
        Image scaled_image = image_temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Set dimensions as needed
        ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
        label_agalmataki_plain.setIcon(scaled_image_icon);
        player_area_details1.add(label_agalmataki_plain);

        player_area1.add(player_area_details1);
        return player_area1;
    }

    /**
     * <b>Transformer</b>
     * sets up the Player2 area with his cards points and holdings
     * @Precondition everything for player2 is properly initialized
     * @return the player2 area
     */
    private JLayeredPane MakeArea2() {
        /* Create the player area */
        detailed_score = controller.get_detailed_score_players(); // check might need to be updated more often
        rare_holdings = controller.get_holdings_both_players();
        set_last_played_arrays();
        photographies = controller.get_photographed_both_players();
        available_pawns = controller.get_pawn_availability_players(); // update things

        player_area2 = new JLayeredPane();
        player_area2.setLayout(null);
        player_area2.setPreferredSize(new Dimension(900, 170));
        player_area2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GREEN, 2),
                "Παίκτης 2 - Διαθέσιμα Πιόνια: " + available_pawns[1][0] + " Αρχαιολόγοι και " + available_pawns[1][1] + " Θησέας"
        ));
        // Player cards area
        player_area_cards2 = new JPanel(new GridLayout(1, 8, 5, 0));
        player_area_cards2.setBounds(10, 20, 610, 100); // Adjust position and size
        set_button_array2();
        for (int i = 0; i < 8; i++) {
            player_area_cards2.add(cards2_array[i]);
        }
        player_area2.add(player_area_cards2);

        // Last played cards area
        player_area_last_played_cards2 = new JPanel(new GridLayout(1, 4, 5, 0));
        player_area_last_played_cards2.setBounds(720, 20, 260, 100); // Adjust position and size
        for (int i = 0; i < 4; i++) {
            player_area_last_played_cards2.add(last_played2_array[i]);
        }
        player_area2.add(player_area_last_played_cards2);

        // Rare findings found labels
        player_area_rare_found2 = new JPanel(new GridLayout(1, 4, 10, 0));
        player_area_rare_found2.setBounds(720, 115, 260, 50);
        for (int i = 0; i < 4; i++) {
            JLabel rare_finding = new JLabel();
            ImageIcon image;
            rare_finding.setOpaque(true); // Make the label background visible
            rare_finding.setHorizontalAlignment(SwingConstants.CENTER);
            if (i == 0) {
                image = new ImageIcon("src/project_assets/images/findings/ring.jpg");
                if (rare_holdings[1][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else if (i == 1) {
                image = new ImageIcon("src/project_assets/images/findings/kosmima.jpg");
                if (rare_holdings[1][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else if (i == 2) {
                image = new ImageIcon("src/project_assets/images/findings/diskos.jpg");
                if (rare_holdings[1][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            } else {
                image = new ImageIcon("src/project_assets/images/findings/ruto.jpg");
                if (rare_holdings[1][i] == 1) {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                } else {
                    rare_finding.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            }
            Image image_temp = image.getImage();
            Image scaled_image = image_temp.getScaledInstance(35, 35, Image.SCALE_SMOOTH); // Set dimensions as needed
            ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
            rare_finding.setIcon(scaled_image_icon);
            player_area_rare_found2.add(rare_finding);
        }
        player_area2.add(player_area_rare_found2);

        // Player details area (to the right of last played cards)
        player_area_details2 = new JPanel(new GridLayout(3, 1, -10, 0));
        player_area_details2.setBounds(1000, 50, 360, 100); // Position on the right of last played cards
        JLabel label_score = new JLabel("Το σκορ μου: " + detailed_score[1][0] + " πόντοι");
        player_area_details2.add(label_score);
        JButton toixografies_button = new JButton("Οι Τοιχογραφίες μου");
        toixografies_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Οι τοιχογραφίες του παίκτη 2");
                frame.setSize(500, 400);
                frame.setLayout(new GridLayout(2, 3, 0, 0)); // 2 rows, 3 columns, 10px gap
                for (int i = 1; i < 7; i++) {
                    String photo_to_look = "fresco" + i;
                    if (i == 3 || i > 4) {
                        photo_to_look = photo_to_look + "_15.jpg";
                    } else {
                        photo_to_look = photo_to_look + "_20.jpg";
                    }
                    ImageIcon imageIcon = new ImageIcon("src/project_assets/images/findings/" + photo_to_look); // Load image
                    JLabel image_label = new JLabel();
                    if (photographies[1][i - 1] == 1) {
                        image_label.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                    } else {
                        image_label.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                    }
                    image_label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(200, 220, Image.SCALE_SMOOTH))); // Resize image
                    frame.add(image_label); // Add to the frame
                }
                frame.setVisible(true);
            }
        });
        player_area_details2.add(toixografies_button);
        JLabel label_agalmatakia = new JLabel("Αγαλματάκια: " + detailed_score[1][1]);
        player_area_details2.add(label_agalmatakia);

        JLabel label_agalmataki_plain = new JLabel();
        ImageIcon image = new ImageIcon("src/project_assets/images/findings/snakes.jpg");
        Image image_temp = image.getImage();
        Image scaled_image = image_temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Set dimensions as needed
        ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
        label_agalmataki_plain.setIcon(scaled_image_icon);
        player_area_details2.add(label_agalmataki_plain);

        player_area2.add(player_area_details2);
        return player_area2;
    }

    /**
     * <b>Transformer</b>
     * makes the whole table that contains the 4 paths and the card stack button
     * @Precondition everything concerning the pawns and paths is properly initialized with the appropriate values
     * @return the center pane
     */
    public JLayeredPane MakeTable() {
        path_state_findings = controller.get_path_state();
        int[][] array_pawns1 = controller.get_pawns_of_board_player1();
        int[][] array_pawns2 = controller.get_pawns_of_board_player2();
        int winner = controller.check_get_winner();
        if (winner == 0) {
            JOptionPane.showMessageDialog(null, "ΝΊΚΗΣΕ Ο ΠΑΊΚΤΗΣ 1!", "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE, null);
            System.exit(0);
        } else if (winner == 1) {
            JOptionPane.showMessageDialog(null, "ΝΊΚΗΣΕ Ο ΠΑΊΚΤΗΣ 2!", "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE, null);
            System.exit(0);
        } else if (winner == -2) {
            JOptionPane.showMessageDialog(null, "ΟΛΙΚΗ ΙΣΟΠΑΛΊΑ!", "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE, null);
            System.exit(0);
        }
        controller.play_music(); // Setup
        // Create the main layered pane
        whole_table = new JLayeredPane() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File("src/project_assets/images/background.jpg")); // Replace with your image path
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);  // Scale the image to fill the pane
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        whole_table.setLayout(new BorderLayout());

        // Panel to hold the grid
        whole_table_grid_paths = new JPanel(); // Adjust spacing between grid cells
        whole_table_grid_paths.setOpaque(false); // Transparent background
        whole_table_grid_paths.setPreferredSize(new Dimension(900, 900));
        whole_table_grid_paths.setLayout(null);


        // Add points
        for (int i = 0; i < 9; i++) {
            JLabel label_points = new JLabel("");
            if (i == 0) {
                label_points.setText("-20 points");
            } else if (i == 1) {
                label_points.setText("-15 points");
            } else if (i == 2) {
                label_points.setText("-10 points");
            } else if (i == 3) {
                label_points.setText("5 points");
            } else if (i == 4) {
                label_points.setText("10 points");
            } else if (i == 5) {
                label_points.setText("15 points");
            } else if (i == 6) {
                label_points.setText("30 points");
                JLabel check_points = new JLabel("Check Point!");
                check_points.setOpaque(false);
                check_points.setBounds(5 + i * 100, -32, 100, 100);
                whole_table_grid_paths.add(check_points);
            } else if (i == 7) {
                label_points.setText("35 points");
            } else if (i == 8) {
                label_points.setText("50 points");
            }
            label_points.setOpaque(false);
            label_points.setFont(new Font("Arial", Font.BOLD, 12));
            label_points.setBounds(15 + i * 100, -45, 100, 100);
            whole_table_grid_paths.add(label_points);
        }

        // Scale down the grid by adjusting the size of each cell
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                JLabel square = new JLabel();
                ImageIcon image;
                if (j == 8) {
                    square.setBounds(10 + j * 98, 27 + i * 83, 105, 85);
                } else {
                    square.setBounds(10 + j * 98, 27 + i * 85, 80, 80);
                }
                if (i == 0) {
                    if (path_state_findings[i][j] == 1 && j == 8) {
                        image = new ImageIcon("src/project_assets/images/paths/knossosPalace.jpg");
                    } else if (path_state_findings[i][j] == 1) {
                        image = new ImageIcon("src/project_assets/images/paths/knossos2.jpg");
                    } else {
                        image = new ImageIcon("src/project_assets/images/paths/knossos.jpg");
                    }
                } else if (i == 1) {
                    if (path_state_findings[i][j] == 1 && j == 8) {
                        image = new ImageIcon("src/project_assets/images/paths/maliaPalace.jpg");
                    } else if (path_state_findings[i][j] == 1) {
                        image = new ImageIcon("src/project_assets/images/paths/malia2.jpg");
                    } else {
                        image = new ImageIcon("src/project_assets/images/paths/malia.jpg");
                    }
                } else if (i == 2) {
                    if (path_state_findings[i][j] == 1 && j == 8) {
                        image = new ImageIcon("src/project_assets/images/paths/phaistosPalace.jpg");
                    } else if (path_state_findings[i][j] == 1) {
                        image = new ImageIcon("src/project_assets/images/paths/phaistos2.jpg");
                    } else {
                        image = new ImageIcon("src/project_assets/images/paths/phaistos.jpg");
                    }
                } else {
                    if (path_state_findings[i][j] == 1 && j == 8) {
                        image = new ImageIcon("src/project_assets/images/paths/zakrosPalace.jpg");
                    } else if (path_state_findings[i][j] == 1) {
                        image = new ImageIcon("src/project_assets/images/paths/zakros2.jpg");
                    } else {
                        image = new ImageIcon("src/project_assets/images/paths/zakros.jpg");
                    }
                }

                JLabel pawn1 = new JLabel();
                pawn1.setBounds(15 + j * 98, 32 + i * 85, 35, 35);
                ImageIcon pawn_icon;
                if (array_pawns1[i][j] == 0) {
                    pawn_icon = new ImageIcon("src/project_assets/images/pionia/question.jpg");
                } else if (array_pawns1[i][j] == 1) {
                    pawn_icon = new ImageIcon("src/project_assets/images/pionia/arch.jpg");
                } else if (array_pawns1[i][j] == 2) {
                    pawn_icon = new ImageIcon("src/project_assets/images/pionia/theseus.jpg");
                } else {
                    pawn_icon = null;
                }
                if (pawn_icon != null) {
                    Image scaled_temp1 = pawn_icon.getImage();
                    Image scaled_image1 = scaled_temp1.getScaledInstance(pawn1.getWidth(), pawn1.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon scaled_image_icon1 = new ImageIcon(scaled_image1);
                    pawn1.setIcon(scaled_image_icon1);
                    pawn1.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                    whole_table_grid_paths.add(pawn1);
                }

                JLabel pawn2 = new JLabel();
                pawn2.setBounds(15 + j * 98, 68 + i * 85, 35, 35);
                ImageIcon pawn_icon2;
                if (array_pawns2[i][j] == 0) {
                    pawn_icon2 = new ImageIcon("src/project_assets/images/pionia/question.jpg");
                } else if (array_pawns2[i][j] == 1) {
                    pawn_icon2 = new ImageIcon("src/project_assets/images/pionia/arch.jpg");
                } else if (array_pawns2[i][j] == 2) {
                    pawn_icon2 = new ImageIcon("src/project_assets/images/pionia/theseus.jpg");
                } else {
                    pawn_icon2 = null;
                }
                if (pawn_icon2 != null) {
                    Image scaled_temp2 = pawn_icon2.getImage();
                    Image scaled_image2 = scaled_temp2.getScaledInstance(pawn2.getWidth(), pawn2.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon scaled_image_icon2 = new ImageIcon(scaled_image2);
                    pawn2.setBorder(BorderFactory.createLineBorder(Color.green, 3));
                    pawn2.setIcon(scaled_image_icon2);
                    whole_table_grid_paths.add(pawn2);
                }

                Image image_temp = image.getImage();
                Image scaled_image = image_temp.getScaledInstance(square.getWidth(), square.getHeight(), Image.SCALE_SMOOTH); // Set dimensions as needed
                ImageIcon scaled_image_icon = new ImageIcon(scaled_image);
                square.setIcon(scaled_image_icon);
                square.setHorizontalAlignment(SwingConstants.CENTER);
                whole_table_grid_paths.add(square);
            }
        }


        JPanel grid_panel = new JPanel();
        grid_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        grid_panel.add(whole_table_grid_paths);
        grid_panel.setOpaque(false); // Transparent background
        whole_table.add(grid_panel, BorderLayout.CENTER);

        // Create a smaller button
        JPanel button_stack_panel = new JPanel();
        button_stack_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        button_stack_panel.setOpaque(false);
        stack_button = new JButton();
        // Load the image
        ImageIcon image = new ImageIcon("src/project_assets/images/cards/backCard.jpg");
        stack_button.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        stack_button.setIcon(image);

        stack_label = new JLabel();
        stack_label.setHorizontalAlignment(SwingConstants.CENTER); // Center text
        stack_label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        stack_label.setOpaque(true); // Make the background visible
        stack_label.setBackground(Color.WHITE); // Set white background
        button_stack_panel.add(stack_label);

        int turn = controller.get_turn() + 1; // For id displays 1,2
        final int[] sec = {30};

        if (timer != null) {
            timer.cancel();  // Cancel the existing timer (very important)
        }
        timer = new Timer();
        TimerTask print_task = new TimerTask() {

            /**
             * The action to be performed by this timer task.
             */
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    stack_label.setText("<html>Available Cards:" + controller.get_stack_count() +
                            "<br>Check Points:" + controller.get_surpassed_check_point_pawns() +
                            "<br>Turn: Player " + turn +
                            "<br>Time left: " + sec[0] + " </html>");
                });
                sec[0]--;
                if(sec[0] == 0) {
                    print_information_string("Ο χρόνος σου τελείωσε και χάνεις την σειρά σου!");
                    controller.update_turn(); // Need the id of the players
                    update_all_components();
                }
            }
        };
        timer.scheduleAtFixedRate(print_task, 0, 1000);

        button_stack_panel.add(stack_button);
        whole_table.add(button_stack_panel, BorderLayout.WEST); // Add the button to the left

        return whole_table;
    }


    /**
     * <b>Transformer</b>
     * updates all the components of the view model to the new data
     * @Precondition none
     * @Postcondition updated view contains the update model data
     */
    public void update_all_components() {

        if (player_area1 != null) remove(player_area1);
        if (player_area2 != null) remove(player_area2);
        if (whole_table != null) remove(whole_table);

        top_menu_bar = MakeMenuBar();
        setJMenuBar(top_menu_bar);

        player_area1 = MakeArea1();
        add(player_area1, BorderLayout.NORTH);

        player_area2 = MakeArea2();
        add(player_area2, BorderLayout.SOUTH);

        whole_table = MakeTable();
        add(whole_table, BorderLayout.CENTER);

        // Repaint and revalidate the frame to reflect updates
        revalidate();
        repaint();
    }

    /**
     * <b>Transformer</b>
     * asks user what he wants to do with his pawn
     * @param pioni is the pioni that the player has in the certain path
     * @Precondition the player has initialized a valid movement with one of his pawns and pioni is 1 or 0
     * @return a valid integer 0 if yes 1 if no, integer is returned
     */
    public int ask_for_decision(int pioni) {
        int decision = 0;
        if (pioni == 0) {
            decision = JOptionPane.showOptionDialog(null, "Θέλεις ο αρχαιολόγος να ανοίξει το κουτάκι;", "ΒΡΗΚΕΣ ΣΠΑΝΙΟ ΕΎΡΗΜΑ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ναι", "Όχι"}, null);
            while (decision == -1) { // check for availability
                decision = JOptionPane.showOptionDialog(null, "Θέλεις ο αρχαιολόγος να ανοίξει το κουτάκι;", "ΒΡΗΚΕΣ ΣΠΑΝΙΟ ΕΎΡΗΜΑ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ναι", "Όχι"}, null);
            }
        } else {
            decision = JOptionPane.showOptionDialog(null, "Θέλεις ο Θησέας να καταστρέψει το κουτάκι;", "ΒΡΗΚΕΣ ΣΠΑΝΙΟ ΕΎΡΗΜΑ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ναι", "Όχι"}, null);
            while (decision == -1) { // check for availability
                decision = JOptionPane.showOptionDialog(null, "Θέλεις ο Θησέας να καταστρέψει το κουτάκι;", "ΒΡΗΚΕΣ ΣΠΑΝΙΟ ΕΎΡΗΜΑ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ναι", "Όχι"}, null);
            }
        }
        return decision;
    }

    /**
     * <b>Transformer</b>
     * outputs details for each finding of the square
     * @param f the string of the finding
     * @Precondition f is not null
     * @Postcondition it is shown to the user the details of the square
     */
    public void print_information(String f) {
        String csv_file_path = "src/project_assets/csvFiles/csv_greek.csv";
        try (Scanner scanner = new Scanner(new File(csv_file_path))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(";", 3);
                if (parts.length == 3) {
                    String image_path = "src/project_assets/images/" + parts[0].trim();
                    String message = parts[1].trim();
                    String description = parts[2].trim();

                    if (image_path.contains(f)) { // if the path contains the thing we search for
                        show_dialog(image_path, message, description);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * outputs a special string message
     * @param f the string that we want to be printed
     * @Precondition f is not null
     * @Postcondition the user is shown the special message
     */
    public void print_information_string(String f) {
        JOptionPane.showMessageDialog(null, f, "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE);
    }

}