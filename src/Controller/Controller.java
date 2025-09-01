package Controller;

import Model.Card.*;
import Model.Collection.CollectionCards;
import Model.Finding.*;
import Model.Paths.Path;
import Model.Pawn.Archaiologos;
import Model.Pawn.Pawn;
import Model.Pawn.Thiseas;
import Model.Players.Player;
import Model.Square.FindingSquare;
import Model.Square.Square;
import Model.TableOfGame.Table;
import Model.Priority.Turn;
import View.View;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Controller {
    private View game_view;
    private Player Player1, Player2;
    private Path path_faistos, path_knossos, path_malia, path_zakros;
    private Table table_of_game;
    private CollectionCards cards_of_game;
    private Turn turn;
    private Clip clip;
    private boolean thiseas_can_be_used = true;
    private int thiseas_used_player = -1;

    /**
     * <b>Constructor</b>
     * creates an instance of our controller that manages the MVC architecture
     *
     * @param view is the view of our MVC architecture
     * @Precondition view is not null
     * @Postcondition constructs a new Controller with 2 players
     */
    public Controller(View view) {
        this.game_view = view;
        game_init(); // Initialize the game
    }

    /**
     * <b>Transformer</b>
     * initializes the game with the basic things
     * @Precondition none
     * @Postcondition every player has now 8 cards from the shuffled array the paths are initialized with the findings etc.
     */
    public void game_init() {
        Player1 = new Player();
        Player2 = new Player(); // thiseas is staying alone and then is being chosen by gui
        /* Creating the cards, shuffling them, giving 8 random of these in 2 players */
        cards_of_game = new CollectionCards();
        cards_of_game.set_shuffle_cards();
        Player1.setCards(cards_of_game.getRandom8());
        Player2.setCards(cards_of_game.getRandom8());
        /* Creating the different paths and adding them the random findings */
        path_faistos = new Path(AnaktoroEnum.FAISTOS);
        path_knossos = new Path(AnaktoroEnum.KNOSSOS);
        path_malia = new Path(AnaktoroEnum.MALIA);
        path_zakros = new Path(AnaktoroEnum.ZAKROS);

        table_of_game = new Table(path_faistos, path_knossos, path_malia, path_zakros);
        table_of_game.setRestFindings(); // set the rest of the findings too
        /* Managing each turn */
        turn = new Turn(); // class that determines who plays next random 0 1 bit?
    }

    /**
     * <b>Accessor</b>
     * access the cards of the two players as strings
     * @Precondition the player's cards are initialized
     * @return a string array that contains the cards of the player as "knossos1"
     */
    public String[][] get_cards_as_string() {
        String[][] array = new String[2][8];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0) {
                    array[i][j] = Player1.get_card_index(j).getCardString();
                } else {
                    array[i][j] = Player2.get_card_index(j).getCardString();
                }
            }
        }
        return array;
    }

    /**
     * <b>Accessor</b>
     * access the last played cards of the two players as strings
     * @Precondition the player's cards are initialized
     * @return the 8 cards (4 for each player) as strings
     */
    public String[][] get_last_cards_as_string() {
        String[][] array = new String[2][4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {
                    try {
                        array[i][j] = Player1.get_last_card_index(j).getCardString();
                    } catch (Exception e) {
                        array[i][j] = null; // case where player hasn't played anything in this path yet
                    }
                } else {
                    try {
                        array[i][j] = Player2.get_last_card_index(j).getCardString();
                    } catch (Exception e) {
                        array[i][j] = null; // case where player hasn't played anything in this path yet
                    }
                }
            }
        }
        return array;
    }

    /**
     * <b>Accessor</b>
     * access the paths in an array if they have rare finding or not
     * @Precondition the paths are initialized
     * @return a 4x9 with integers that contains 1 if finding square 0 if not
     */
    public int[][] get_path_state() {
        int[][] array = new int[4][9];
        Square[] f = table_of_game.getPath_faistos().get_squares_array();
        Square[] k = table_of_game.getPath_knossos().get_squares_array();
        Square[] m = table_of_game.getPath_malia().get_squares_array();
        Square[] z = table_of_game.getPath_zakros().get_squares_array();
        for (int i = 0; i < 9; i++) {
            if (f[i] instanceof FindingSquare) {
                array[0][i] = 1;
            } else {
                array[0][i] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (k[i] instanceof FindingSquare) {
                array[1][i] = 1;
            } else {
                array[1][i] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (m[i] instanceof FindingSquare) {
                array[2][i] = 1;
            } else {
                array[2][i] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (z[i] instanceof FindingSquare) {
                array[3][i] = 1;
            } else {
                array[3][i] = 0;
            }
        }
        return array;
    }

    /**
     * <b>Accessor</b>
     * access the current card stack count of cards of the game
     * @Precondition cards have been initialized and shuffled
     * @return the count of the cards of the stack
     */
    public int get_stack_count() {
        return cards_of_game.getAll_cards().size();
    }

    /**
     * <b>Accessor</b>
     * return the score details and agalmatakia of the player in 2x2 array
     * @return the 2x2 array that contains (0,0) score of the player1, (0,1) agalmatakia of the player 1 etc...
     */
    public int[][] get_detailed_score_players() {
        int[][] score = new int[2][2];
        score[0][0] = Player1.get_player_score();
        score[0][1] = Player1.get_player_agalmatakia_count();
        score[1][0] = Player2.get_player_score();
        score[1][1] = Player2.get_player_agalmatakia_count();
        return score;
    }

    /**
     * <b>Accessor</b>
     * return the rare holdings of both the players in an array 2x4 k,m,f,z
     * @Precondition player has his holdings initialized
     * @return the 2x2 array that contains 1 and 0 if the holding is held or not
     */
    public int[][] get_holdings_both_players() {
        int[][] holdings = new int[2][4];
        holdings[0] = Player1.get_rare_holdings();
        holdings[1] = Player2.get_rare_holdings();
        return holdings;
    }

    /**
     * <b>Accessor</b>
     * get the availability or not of the pawns for each player
     * @Precondition players has his pawn initialized not allocated particularly
     * @return a 2x2 array with the details of the pawns if they are available or not (1,0)
     */
    public int[][] get_pawn_availability_players() {
        int[][] availability = new int[2][2];
        availability[0] = Player1.get_available_pawns();
        availability[1] = Player2.get_available_pawns();
        return availability;
    }

    /**
     * <b>Transformer</b>
     * checks if a card can be played by a certain player if the card to play is bigger or equal than the last played card of the anaktoro
     * @param player     is the player 0,1
     * @param card_index is the index of the card to be played
     * @Precondition player is [0,1] and card_index [0-7]
     * @return true or false whether a card that wants to be played from the player can be played
     */
    public boolean check_if_card_playable(int player, int card_index) { // this is equivalent to the MatchCard of the exercise instructions
        Card card_to_play = null;
        Card last_played_card_path = null; // this contains the last played card of the player at the path of the card he wants to play
        if (player == 0) {
            card_to_play = Player1.get_card_index(card_index);
            if (card_to_play.getAnaktoro() == AnaktoroEnum.KNOSSOS) {
                last_played_card_path = Player1.get_last_card_index(0);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player1.get_prev_last_card_index(0);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.MALIA) {
                last_played_card_path = Player1.get_last_card_index(1);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player1.get_prev_last_card_index(1);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.FAISTOS) {
                last_played_card_path = Player1.get_last_card_index(2);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player1.get_prev_last_card_index(2);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.ZAKROS) {
                last_played_card_path = Player1.get_last_card_index(3);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player1.get_prev_last_card_index(3);
                }
            }
        } else {
            card_to_play = Player2.get_card_index(card_index);
            if (card_to_play.getAnaktoro() == AnaktoroEnum.KNOSSOS) {
                last_played_card_path = Player2.get_last_card_index(0);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player2.get_prev_last_card_index(0);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.MALIA) {
                last_played_card_path = Player2.get_last_card_index(1);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player2.get_prev_last_card_index(1);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.FAISTOS) {
                last_played_card_path = Player2.get_last_card_index(2);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player2.get_prev_last_card_index(2);
                }
            } else if (card_to_play.getAnaktoro() == AnaktoroEnum.ZAKROS) {
                last_played_card_path = Player2.get_last_card_index(3);
                if (last_played_card_path instanceof Ariadne || last_played_card_path instanceof Minotaur) {
                    // then we need to check with the card before the ariadne
                    last_played_card_path = Player1.get_prev_last_card_index(3);
                }
            }
        }
        if (card_to_play instanceof Ariadne || card_to_play instanceof Minotaur) {
            if (last_played_card_path == null) { // tries to start the path with a MitoAriadne or Minotaur
                return false;
            }
        }
        if (card_to_play instanceof NumberCard && last_played_card_path instanceof NumberCard) {
            NumberCard number_card_to_play = (NumberCard) card_to_play;
            NumberCard last_played_number_card_path = (NumberCard) last_played_card_path;
            if (number_card_to_play.getNumber() < last_played_number_card_path.getNumber()) {
                return false;
            }

        }
        return true;
    }

    /**
     * <b>Accessor</b>
     * returns the turn as an integer with the id of the player that plays
     * @Precondition the game has started and the turn has been initialized
     * @return 0 is player1 plays, 1 if player2 plays
     */
    public int get_turn() {
        return turn.get_turn();
    }

    /**
     * <b>Accessor</b>
     * return the photographed photos of both players in an array 2x2 with the ids being the indexes and 1 if photographed -1 if not
     * @Precondition toixografies of the player array is initialized
     * @return a 2x2 array that contains the ids of the photographed toixografies
     */
    public int[][] get_photographed_both_players() {
        int[][] photos = new int[2][7];
        photos[0] = Player1.get_toixografies_photographed_ids();
        photos[1] = Player2.get_toixografies_photographed_ids();
        return photos;
    }

    /**
     * <b>Accessor</b>
     * return the location and type of pawns in a 4x9 array 4 pawns 9 positions -1 position is invalid, 0 is hidden, 1 is archaio, 2 is thiseas
     * @Precondition pawns and anaktora are initialized
     * @return the 2x2 array that contains -1,0,1,2 for the pawns (-1 means pawn must not be shown, 0 pawn is invisible, 1 pawn is archaiologos, 2 pawn is thiseas)
     */
    public int[][] get_pawns_of_board_player1() {
        int[][] pawns = new int[4][9];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                pawns[i][j] = -1; // at first fill everything with -1
            }
        }

        ArrayList<Pawn> pawns1 = Player1.get_pawns();

        ArrayList<AnaktoroEnum> anaktora_of_pawns = new ArrayList<>();
        anaktora_of_pawns.add(pawns1.get(0).get_anaktoro());
        anaktora_of_pawns.add(pawns1.get(1).get_anaktoro());
        anaktora_of_pawns.add(pawns1.get(2).get_anaktoro());
        anaktora_of_pawns.add(pawns1.get(3).get_anaktoro());

        for (int i = 0; i < 4; i++) {
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && !pawns1.get(i).get_visibility() && pawns1.get(i).get_position() != -1) {
                pawns[0][pawns1.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Archaiologos && pawns1.get(i).get_position() != -1) {
                pawns[0][pawns1.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Thiseas && pawns1.get(i).get_position() != -1) {
                pawns[0][pawns1.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && !pawns1.get(i).get_visibility() && pawns1.get(i).get_position() != -1) {
                pawns[1][pawns1.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Archaiologos && pawns1.get(i).get_position() != -1) {
                pawns[1][pawns1.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Thiseas && pawns1.get(i).get_position() != -1) {
                pawns[1][pawns1.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && !pawns1.get(i).get_visibility() && pawns1.get(i).get_position() != -1) {
                pawns[2][pawns1.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Archaiologos && pawns1.get(i).get_position() != -1) {
                pawns[2][pawns1.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Thiseas && pawns1.get(i).get_position() != -1) {
                pawns[2][pawns1.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && !pawns1.get(i).get_visibility() && pawns1.get(i).get_position() != -1) {
                pawns[3][pawns1.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Archaiologos && pawns1.get(i).get_position() != -1) {
                pawns[3][pawns1.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && (pawns1.get(i).get_visibility() || get_turn() == 0) && pawns1.get(i) instanceof Thiseas && pawns1.get(i).get_position() != -1) {
                pawns[3][pawns1.get(i).get_position()] = 2;
            }
        }

        return pawns;
    }

    /**
     * <b>Accessor</b>
     * return the location and type of pawns in a 4x9 array 4 pawns 9 positions -1 position is invalid, 0 is hidden, 1 is archaio, 2 is thiseas
     * @Precondition pawns and anaktora are initialized
     * @return the 2x2 array that contains -1,0,1,2 for the pawns (-1 means pawn must not be shown, 0 pawn is invisible, 1 pawn is archaiologos, 2 pawn is thiseas)
     */
    public int[][] get_pawns_of_board_player2() {
        int[][] pawns = new int[4][9];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                pawns[i][j] = -1; // at first fill everything with -1
            }
        }

        ArrayList<Pawn> pawns2 = Player2.get_pawns();

        ArrayList<AnaktoroEnum> anaktora_of_pawns = new ArrayList<>();
        anaktora_of_pawns.add(pawns2.get(0).get_anaktoro());
        anaktora_of_pawns.add(pawns2.get(1).get_anaktoro());
        anaktora_of_pawns.add(pawns2.get(2).get_anaktoro());
        anaktora_of_pawns.add(pawns2.get(3).get_anaktoro());

        for (int i = 0; i < 4; i++) {
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && !pawns2.get(i).get_visibility() && pawns2.get(i).get_position() != -1) {
                pawns[0][pawns2.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Archaiologos && pawns2.get(i).get_position() != -1) {
                pawns[0][pawns2.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.KNOSSOS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Thiseas && pawns2.get(i).get_position() != -1) {
                pawns[0][pawns2.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && !pawns2.get(i).get_visibility() && pawns2.get(i).get_position() != -1) {
                pawns[1][pawns2.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Archaiologos && pawns2.get(i).get_position() != -1) {
                pawns[1][pawns2.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.MALIA && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Thiseas && pawns2.get(i).get_position() != -1) {
                pawns[1][pawns2.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && !pawns2.get(i).get_visibility() && pawns2.get(i).get_position() != -1) {
                pawns[2][pawns2.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Archaiologos && pawns2.get(i).get_position() != -1) {
                pawns[2][pawns2.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.FAISTOS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Thiseas && pawns2.get(i).get_position() != -1) {
                pawns[2][pawns2.get(i).get_position()] = 2;
            }

            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && !pawns2.get(i).get_visibility() && pawns2.get(i).get_position() != -1) {
                pawns[3][pawns2.get(i).get_position()] = 0;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Archaiologos && pawns2.get(i).get_position() != -1) {
                pawns[3][pawns2.get(i).get_position()] = 1;
            }
            if (anaktora_of_pawns.get(i) == AnaktoroEnum.ZAKROS && (pawns2.get(i).get_visibility() || get_turn() == 1) && pawns2.get(i) instanceof Thiseas && pawns2.get(i).get_position() != -1) {
                pawns[3][pawns2.get(i).get_position()] = 2;
            }
        }

        return pawns;
    }

    /**
     * <b>Transformer</b>
     * the player has played a valid card and now its time to take one from the stack at the top and also the length is automatically decreasec
     * @param player            is the player to take the card from the stack
     * @param card_index_to_add is the card index to be replaced on the player's hand
     * @Precondition player is [0,1] and card_index_to_add is [0-7]
     * @return true or false whether taking from the stack happened successfully or not
     */
    public boolean take_card_from_stack(int player, int card_index_to_add) {
        Card card_for_player = cards_of_game.get_top_card();
        if (card_index_to_add == -1) {
            game_view.print_information_string("Η στοίβα του παιχνιδιού άδειασε");
            return false;
        }
        if (player == 0) {
            Player1.set_card_at_index(card_index_to_add, card_for_player); // just set it will replace the old card
        } else {
            Player2.set_card_at_index(card_index_to_add, card_for_player);
        }
        turn.update_turn(); // Better to update turn here
        return true;
    }


    /**
     * <b>Observer</b>
     * checks whether a pawn has been placed in this path
     * @param player     player that plays the card
     * @param card_index the index of the card that the player wants to play and can play
     * @Precondition player is [0,1] and card_index_to_add is [0-7]
     * @return true if the path already has a pawn of that player else return false
     */
    public boolean check_if_pawn_exists(int player, int card_index) {
        Card card_to_play = null;
        ArrayList<Pawn> pawns_player = null;
        AnaktoroEnum path_of_card = null;
        if (player == 0) {
            card_to_play = Player1.get_card_index(card_index);
            pawns_player = Player1.get_pawns();

        } else {
            card_to_play = Player2.get_card_index(card_index);
            pawns_player = Player2.get_pawns();
        }
        // get the anaktoro of the card
        path_of_card = card_to_play.getAnaktoro();

        // look if i have a pawn at this anaktoro
        for (int i = 0; i < 4; i++) {
            if (pawns_player.get(i).get_position() != -1 && pawns_player.get(i).get_anaktoro() == path_of_card) {
                return true;
            }
        }
        return false;
    }

    /**
     * <b>Transformer</b>
     * initializes a pawn for the certain path and then places it correctly
     * @param player     player that is eligible to collect(syllogi) the finding
     * @param card_index is index of the card we want to play
     * @param choice     0 is archaiologos for new pawn, 1 is thiseas for new pawn
     * @Precondition player is [0,1] and card_index_to_add is [0-7] and choice [0,1]
     * @Postcondition initializes the pawn to the played path
     */
    public void set_pawn_for_player_play(int player, int card_index, int choice) {
        Card card_to_play = null;
        ArrayList<Pawn> pawns_player = null;
        AnaktoroEnum path_of_card = null;
        if (player == 0) {
            card_to_play = Player1.get_card_index(card_index);
            path_of_card = card_to_play.getAnaktoro();
            pawns_player = Player1.get_pawns();
        } else {
            card_to_play = Player2.get_card_index(card_index);
            path_of_card = card_to_play.getAnaktoro();
            pawns_player = Player2.get_pawns();
        }
        int path_index;
        if (path_of_card == AnaktoroEnum.KNOSSOS) {
            path_index = 0;
        } else if (path_of_card == AnaktoroEnum.MALIA) {
            path_index = 1;
        } else if (path_of_card == AnaktoroEnum.FAISTOS) {
            path_index = 2;
        } else {
            path_index = 3;
        }
        // do the changes with the data chosen
        if (choice == 0) {
            // look for first empty archaiologos
            for (int i = 0; i < 4; i++) {
                if (pawns_player.get(i).get_position() == -1 && pawns_player.get(i) instanceof Archaiologos) {
                    pawns_player.get(i).set_position(0);
                    pawns_player.get(i).set_anaktoro(path_of_card);
                    break; // do it only once
                }
            }
        } else if (choice == 1) {
            // adjust thiseas accordingly
            pawns_player.get(3).set_position(0);
            pawns_player.get(3).set_anaktoro(path_of_card);
        }
        // update at the correct player array also update last played card
        // also set the last played card for this player at the path index
        update_pawns_and_last_cards(player, card_index, card_to_play, pawns_player, path_index);
    }

    /**
     * <b>Transformer</b>
     * handles how a card is played for each Player
     * @param player     player that plays the card
     * @param card_index the index of the card that the player wants to play and can play
     * @Precondition player is [0,1] and card_index is [0-7]
     * @Postcondition appropriate moves are done depending on the card and the player played
     */
    public void play_card(int player, int card_index) {
        Card card_to_play = null;
        ArrayList<Pawn> pawns_player = null;
        Path current_path = null;
        if (player == 0) {
            card_to_play = Player1.get_card_index(card_index);
            pawns_player = Player1.get_pawns();
        } else {
            card_to_play = Player2.get_card_index(card_index);
            pawns_player = Player2.get_pawns();
        }

        int path_index = 0;
        if (card_to_play.getAnaktoro() == AnaktoroEnum.KNOSSOS) {
            path_index = 0;
            current_path = table_of_game.getPath_knossos();
        } else if (card_to_play.getAnaktoro() == AnaktoroEnum.MALIA) {
            path_index = 1;
            current_path = table_of_game.getPath_malia();
        } else if (card_to_play.getAnaktoro() == AnaktoroEnum.FAISTOS) {
            path_index = 2;
            current_path = table_of_game.getPath_faistos();
        } else if (card_to_play.getAnaktoro() == AnaktoroEnum.ZAKROS) {
            path_index = 3;
            current_path = table_of_game.getPath_zakros();
        }

        // Check what pawn exists in the path of the card's anaktoro
        int index_of_pawn = -1;
        Pawn target_pawn = null;
        for (int i = 0; i < 4; i++) {
            if (pawns_player.get(i).get_anaktoro() == card_to_play.getAnaktoro()) {
                target_pawn = pawns_player.get(i);
                index_of_pawn = i;
                break;
            }
        }

        // Check if it is Minotaur
        if (card_to_play instanceof Minotaur) {
            ArrayList<Pawn> opponent_pawns;
            Pawn pawn_to_move = null;
            int index_opponent_pawn = -1;
            // Check if there is a opponent pawn in this path
            int opponent;
            if (player == 0) {
                opponent = 1;
                opponent_pawns = Player2.get_pawns();
            } else {
                opponent = 0;
                opponent_pawns = Player1.get_pawns();
            }
            // Look for the same anaktoro pawn
            for (int i = 0; i < 4; i++) {
                if (opponent_pawns.get(i).get_anaktoro() == card_to_play.getAnaktoro()) {
                    pawn_to_move = opponent_pawns.get(i);
                    index_opponent_pawn = i;
                    break;
                }
            }

            if (pawn_to_move == null) {
                game_view.print_information_string("Ο αντίπαλος δεν έχει κάποιο πιόνι σε αυτό το μονοπάτι, η επίθεση σου δεν κάνει κάτι");
                return;
            }

            if (pawn_to_move instanceof Thiseas) {
                game_view.print_information_string("Ο αντίπαλος έχει πιόνι Θησέα άρα αποκρούει την επίθεση σου");
                thiseas_can_be_used = false;
                if(player == 0) {
                    thiseas_used_player = 1;
                } else {
                    thiseas_used_player = 0;
                }
                return;
            }
            if (pawn_to_move.get_position() >= 6) { // Case where minotauro can't set back the opponent pawn
                game_view.print_information_string("Ο αντίπαλος έχει περάσει το check point, η επίθεση σου δεν κάνει κάτι");
                return;
            }
            int prev_position_opponent = pawn_to_move.get_position();
            int new_position_opponent;
            if (prev_position_opponent == 1 || prev_position_opponent == 0) {
                new_position_opponent = 0;
            } else {
                new_position_opponent = prev_position_opponent - 2;
            }
            pawn_to_move.set_position(new_position_opponent);
            opponent_pawns.set(index_opponent_pawn, pawn_to_move);
            if (opponent == 0) {
                Player1.set_pawns(opponent_pawns);
            } else {
                Player2.set_pawns(opponent_pawns);
            }
            // Also set the last played card for this player at the path index
            update_pawns_and_last_cards(player, card_index, card_to_play, pawns_player, path_index);
            return;
        }

        assert target_pawn != null;
        int prev_position = target_pawn.get_position();
        int new_position;

        if(!thiseas_can_be_used && thiseas_used_player == player && target_pawn instanceof Thiseas) { // Only disallow movement if it is a Thiseas that wants to move
            new_position = prev_position;
            game_view.print_information_string("Ο θησέας σου ξεκουράζεται και δεν μπορεί να κουνήσει");
        } else {
            new_position = prev_position + 1;
        }


        if (new_position >= 8) {
            new_position = 8; // Dont surpass the limits
        }
        if (new_position == 8 && Player1.get_anaktoro_visited_at_index(path_index) == 0 && player == 0) {
            if (path_index == 0) {
                game_view.print_information("knossosPalace");
            } else if (path_index == 1) {
                game_view.print_information("maliaPalace");
            } else if (path_index == 2) {
                game_view.print_information("phaistosPalace");
            } else {
                game_view.print_information("zakrosPalace");
            }
            Player1.set_anaktoro_visited(path_index, 1);
        } else if (new_position == 8 && Player2.get_anaktoro_visited_at_index(path_index) == 0 && player == 1) {
            if (path_index == 0) {
                game_view.print_information("knossosPalace");
            } else if (path_index == 1) {
                game_view.print_information("maliaPalace");
            } else if (path_index == 2) {
                game_view.print_information("phaistosPalace");
            } else {
                game_view.print_information("zakrosPalace");
            }
            Player2.set_anaktoro_visited(path_index, 1);
        }
        target_pawn.set_position(new_position);
        // Setback to the array the upgraded pawn
        pawns_player.set(index_of_pawn, target_pawn);

        // Check for findings in the new position by getting the squares array
        assert current_path != null;
        Square[] squares_of_path = current_path.get_squares_array();
        // Take the square of the new position
        Square current_square = squares_of_path[new_position];

        handle_finding(player, path_index, target_pawn, new_position, current_square);


// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Do the same if this was an Ariadne card
        if (card_to_play instanceof Ariadne) {
            assert target_pawn != null;
            prev_position = target_pawn.get_position();
            new_position = prev_position + 1;
            if (new_position >= 8) {
                new_position = 8; // Dont surpass the limits
            }
            target_pawn.set_position(new_position);
            // Setback to the array the upgraded pawn
            pawns_player.set(index_of_pawn, target_pawn);

            // Check for findings in the new position by getting the squares array
            assert current_path != null;
            squares_of_path = current_path.get_squares_array();
            // Take the square of the new position
            current_square = squares_of_path[new_position];
            handle_finding(player, path_index, target_pawn, new_position, current_square);
        }
// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Also set the last played card for this player at the path index
        update_pawns_and_last_cards(player, card_index, card_to_play, pawns_player, path_index);

        if(player == thiseas_used_player && !thiseas_can_be_used) {
            thiseas_can_be_used = true;
            thiseas_used_player = -1; // Now for the next round he can be used
        }

    }

    /**
     * <b>Transformer</b>
     * Updates the pawns and last played cards of the pawn
     * @param player player that needs update
     * @param card_index index of the card played
     * @param card_to_play card played
     * @param pawns_player pawns of the player
     * @param path_index path of the card
     * @Precondition all indexes are [0-7] except path_index [0-3]
     * @Postcondition the player just played has everything updated
     */
    private void update_pawns_and_last_cards(int player, int card_index, Card card_to_play, ArrayList<Pawn> pawns_player, int path_index) {
        if (player == 0) {
            Player1.set_pawns(pawns_player);
            if (card_to_play instanceof NumberCard) {
                Player1.set_last_played_card_for_path(path_index, card_to_play);
            }
        } else {
            Player2.set_pawns(pawns_player);
            if (card_to_play instanceof NumberCard) {
                Player2.set_last_played_card_for_path(path_index, card_to_play);
            }
        }
        table_of_game.set_last_played_cards_at_index(path_index, card_to_play);
    }

    private void handle_finding(int player, int path_index, Pawn target_pawn, int new_position, Square current_square) {
        if (current_square instanceof FindingSquare) {
            // Check whether the pawn now is arxaio, or thiseas
            Finding f = ((FindingSquare) current_square).getFinding();
            int pawn;
            if (target_pawn instanceof Archaiologos) {
                pawn = 0;
            } else {
                pawn = 1;
            }
            int decision = -1;
            if (table_of_game.get_record_of_holding(path_index, new_position) == -1) {
                if (f instanceof Toixografies && ((Toixografies) f).get_players_photographed().get(player) == 1) { // This player already has photographed the photo so no need to do it again
                    decision = -1;
                } else {
                    if(player == thiseas_used_player && !thiseas_can_be_used && target_pawn instanceof Thiseas) {
                        decision = -1; // Do nothing
                    } else {
                        decision = game_view.ask_for_decision(pawn);
                    }
                }
            }
            if (pawn == 0 && decision == 0 && table_of_game.get_record_of_holding(path_index, new_position) == -1) { // arxaio yes available
                if (f instanceof Agalmataki) {
                    game_view.print_information("snakes");
                } else if (f instanceof DaxtylidiMinoa) {
                    game_view.print_information("ring");
                } else if (f instanceof DiskosFaistou) {
                    game_view.print_information("diskos");
                } else if (f instanceof KosmimaMaliwn) {
                    game_view.print_information("kosmima");
                } else if (f instanceof RytoZakrou) {
                    game_view.print_information("ruto");
                } else if (f instanceof Toixografies) {
                    game_view.print_information("fresco" + ((Toixografies) f).get_id());
                }
                if (player == 0) {
                    f.addToSyllogi(Player1);
                    if (f instanceof Toixografies) {
                        table_of_game.change_record_of_holding(path_index, new_position, -1); // Opponent can take it too
                        ((Toixografies) f).setPlayers_photographed(player, 1);
                    } else {
                        table_of_game.change_record_of_holding(path_index, new_position, 0); // Belongs to player 1
                    }
                } else {
                    f.addToSyllogi(Player2);
                    if (f instanceof Toixografies) {
                        table_of_game.change_record_of_holding(path_index, new_position, -1); // Opponent can take it too
                        ((Toixografies) f).setPlayers_photographed(player, 1);
                    } else {
                        table_of_game.change_record_of_holding(path_index, new_position, 1); // Belongs to player 2
                    }
                }
                target_pawn.set_visibility(true);
            } else if (pawn == 1 && decision == 0 && table_of_game.get_record_of_holding(path_index, new_position) != -2 && thiseas_can_be_used && thiseas_used_player != player && target_pawn.getUses() < 3) { // Thiseas destroy logic
                table_of_game.change_record_of_holding(path_index, new_position, -2);
                target_pawn.set_visibility(true);
                target_pawn.setUses(target_pawn.getUses() + 1); // Increment the uses of thiseas so it can be used maximum 3 times
            } else if(pawn == 1 && decision == 0 && table_of_game.get_record_of_holding(path_index, new_position) != -2 && !thiseas_can_be_used && thiseas_used_player == player && target_pawn.getUses() < 3) {
                game_view.print_information_string("Ο θησέας σου ξεκουράζεται");
            } else if(pawn == 1 && decision == 0 && table_of_game.get_record_of_holding(path_index, new_position) != -2 && thiseas_can_be_used && thiseas_used_player != player && target_pawn.getUses() >= 3) {
                game_view.print_information_string("Ο θησέας σου δεν έχει άλλες χρήσεις");
            }
        } else {
            // Its a simple square case
//            System.out.println("HTAN SIMPLE SQUARE");
        }
    }

    /**
     * <b>Transformer</b>
     * plays music according to the turn of the player
     * @Precondition turn has been initialized
     * @Postcondition the music of each player is played
     */
    public void play_music() {
        File wav1 = new File("src/project_assets/music/Player1.wav");
        File wav2 = new File("src/project_assets/music/Player2.wav");

        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }

        AudioInputStream audio_input1 = null;
        AudioInputStream audio_input2 = null;
        try {
            audio_input1 = AudioSystem.getAudioInputStream(wav1);
            audio_input2 = AudioSystem.getAudioInputStream(wav2);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (get_turn() == 0) {
            try {
                clip = AudioSystem.getClip();
                clip.open(audio_input1);
                clip.start();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                clip = AudioSystem.getClip();
                clip.open(audio_input2);
                clip.start();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * <b>Accessor</b>
     * calculates how many pawns have surpassed the checkpoint which is (index6) 7th square
     * @Precondition pawns are initialized not allocated specifically
     * @return the number of the pawns that have surpassed the checkpoint
     */
    public int get_surpassed_check_point_pawns() {
        int count_pawns = 0;
        ArrayList<Pawn> pawns1 = Player1.get_pawns();
        ArrayList<Pawn> pawns2 = Player2.get_pawns();
        for (int i = 0; i < pawns1.size(); i++) {
            if (pawns1.get(i).get_position() >= 6) {
                count_pawns++;
            }
            if (pawns2.get(i).get_position() >= 6) {
                count_pawns++;
            }
        }
        return count_pawns;
    }

    /**
     * <b>Observer</b>
     * check for a game winner
     * @Precondition the game has started players are initialized
     * @return if someone won return the id of the player that won
     */
    public int check_get_winner() {
        int winner = -1;

        if (get_stack_count() == 0 || get_surpassed_check_point_pawns() >= 4) {
            if (Player1.get_player_score() > Player2.get_player_score()) {
                winner = 0;
            } else if (Player2.get_player_score() > Player1.get_player_score()) {
                winner = 1;
            } else if (Player1.get_player_score() == Player2.get_player_score()) {
                // get the count of rare holdings
                int[] rare_holdings1 = Player1.get_rare_holdings();
                int[] rare_holdings2 = Player2.get_rare_holdings();
                int count_rare1 = 0;
                int count_rare2 = 0;
                for (int i = 0; i < 4; i++) {
                    if (rare_holdings1[i] == 1) {
                        count_rare1++;
                    }
                    if (rare_holdings2[i] == 1) {
                        count_rare2++;
                    }
                }
                if (count_rare1 > count_rare2) {
                    winner = 0;
                } else if (count_rare2 > count_rare1) {
                    winner = 1;
                } else if (count_rare1 == count_rare2) {
                    int[] toixografies1 = Player1.get_toixografies_photographed_ids();
                    int[] toixografies2 = Player2.get_toixografies_photographed_ids();
                    int count_f1 = 0;
                    int count_f2 = 0;
                    for (int i = 0; i < 4; i++) {
                        if (toixografies1[i] == 1) {
                            count_f1++;
                        }
                        if (toixografies2[i] == 1) {
                            count_f2++;
                        }
                    }
                    if (count_f1 > count_f2) {
                        winner = 0;
                    } else if (count_f2 > count_f1) {
                        winner = 1;
                    } else if (count_f1 == count_f2) {
                        if (Player1.get_player_agalmatakia_count() > Player2.get_player_agalmatakia_count()) {
                            winner = 0;
                        } else if (Player2.get_player_agalmatakia_count() > Player1.get_player_agalmatakia_count()) {
                            winner = 1;
                        } else {
                            winner = -2; // TIE
                        }
                    }
                }
            }
        }
        return winner;
    }

    /**
     * <b>Transformer</b>
     * updates the turn accordingly
     * @Precondition game has started
     * @Postcondition the turn has been changed to the other player
     */
    public void update_turn() {
        turn.update_turn();
    }

    /**
     * <b>Transformer</b>
     * saves the game state
     * @Precondition none
     * @Postcondition a txt file is made that contains the details of the current game state
     */
    public void save_game() {
        File file_to_save = null;
        JFileChooser file_manager = new JFileChooser();
        file_manager.setDialogTitle("Select or Create a File to save the game");
        file_manager.setApproveButtonText("Select");
        int selection = file_manager.showOpenDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            file_to_save = file_manager.getSelectedFile();
            if (!file_to_save.exists()) {
                try {
                    if (file_to_save.createNewFile()) {
                        System.out.println("New file was created");
                    } else {
                        System.out.println("New file creation failed");
                        file_to_save = null;
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    file_to_save = null;
                }
            }
        } else {
            return;
        }
        Player1.save_details(file_to_save);
        Player2.save_details(file_to_save);
        cards_of_game.save_details(file_to_save);
        table_of_game.save_details(file_to_save);
        turn.save_details(file_to_save);
        try {
            assert file_to_save != null;
            try (BufferedReader reader = new BufferedReader(new FileReader(file_to_save));
                 PrintWriter writer = new PrintWriter(new FileWriter(file_to_save, true))) {
                String line;
                // Find the next empty line
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        break;
                    }
                }
                writer.println("------------------------------------------------------------");

                // Write player details to the file
                writer.println(thiseas_can_be_used);
                writer.println(thiseas_used_player);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * continues a saved game from a txt
     * @Precondition txt file is written the same way it should from the save_game() function
     * @Postcondition the game continues from where the txt says
     */
    public void continue_saved_game() {
        File file_to_read = null;
        JFileChooser file_manager = new JFileChooser();
        file_manager.setDialogTitle("Select or Create a File to load the game");
        file_manager.setApproveButtonText("Select");
        int selection = file_manager.showOpenDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            file_to_read = file_manager.getSelectedFile();
            if (!file_to_read.exists()) {
                System.out.println("This file does't exist.");
            }
        } else {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file_to_read))) {
            Player1.load_details(reader);
            Player2.load_details(reader);
            cards_of_game.load_details(reader);
            table_of_game.load_details(reader);
            turn.load_details(reader);
            String line;
            line = reader.readLine(); // Ignore --
            line = reader.readLine();
            thiseas_can_be_used = Objects.equals(line, "true");
            line = reader.readLine();
            thiseas_used_player = Integer.parseInt(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}