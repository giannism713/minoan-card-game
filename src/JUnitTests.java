import Model.Card.*;
import Model.Collection.CollectionCards;
import Model.Finding.*;
import Model.Paths.Path;
import Model.Pawn.Archaiologos;
import Model.Pawn.Thiseas;
import Model.Players.Player;
import Model.Priority.Turn;
import Model.Square.FindingSquare;
import Model.Square.SimpleSquare;
import Model.Square.Square;
import Model.TableOfGame.Table;
import junit.framework.*;

import java.util.ArrayList;

public class JUnitTests extends TestCase{
    // Card Package
    public void test_card_init() {
        // Test correct number and anaktoro placement for Numbered Cards
        for(int i = 0; i < 10; i++) {
            NumberCard card = new NumberCard(i, AnaktoroEnum.KNOSSOS);
            assertEquals(i, card.getNumber());
            assertEquals(AnaktoroEnum.KNOSSOS, card.getAnaktoro());

            card = new NumberCard(i, AnaktoroEnum.MALIA);
            assertEquals(i, card.getNumber());
            assertEquals(AnaktoroEnum.MALIA, card.getAnaktoro());

            card = new NumberCard(i, AnaktoroEnum.FAISTOS);
            assertEquals(i, card.getNumber());
            assertEquals(AnaktoroEnum.FAISTOS, card.getAnaktoro());

            card = new NumberCard(i, AnaktoroEnum.ZAKROS);
            assertEquals(i, card.getNumber());
            assertEquals(AnaktoroEnum.ZAKROS, card.getAnaktoro());
        }
        // Test correct special card init
        Ariadne ariadne = new Ariadne(AnaktoroEnum.KNOSSOS);
        assertEquals(AnaktoroEnum.KNOSSOS, ariadne.getAnaktoro());
        Minotaur minotaur = new Minotaur(AnaktoroEnum.MALIA);
        assertEquals(AnaktoroEnum.MALIA, minotaur.getAnaktoro());
    }
    // Collection Package
    public void test_collection_init() {
        CollectionCards collection = new CollectionCards();
        assertEquals(0, collection.getAll_cards().size());
        // Now set the cards and shuffle them
        collection.set_shuffle_cards();
        assertEquals(100, collection.getAll_cards().size());
        // Check minotaur and ariadne count
        int minotaur_count = 0;
        int ariadne_count = 0;
        for(int i = 0; i < 100; i++) {
            if(collection.getAll_cards().get(i) instanceof Minotaur) {
                minotaur_count++;
            } else if(collection.getAll_cards().get(i) instanceof Ariadne) {
                ariadne_count++;
            }
        }
        assertEquals(8, minotaur_count);
        assertEquals(12, ariadne_count);
        // Get 8 cards test if they are really 8
        ArrayList<Card> cards_set1 = collection.getRandom8();
        assertEquals(8, cards_set1.size());
    }
    // Finding Package
    public void test_finding_init() {
        // Check agalmataki functionality
        Player player = new Player();
        Agalmataki agalmataki = new Agalmataki();
        agalmataki.addToSyllogi(player);
        assertEquals(1, player.get_player_agalmatakia_count());
        // Check DaxtylidiMinoa
        DaxtylidiMinoa daxtylidiMinoa = new DaxtylidiMinoa(25, AnaktoroEnum.KNOSSOS);
        assertEquals(25, daxtylidiMinoa.getPoints());
        assertEquals(AnaktoroEnum.KNOSSOS, daxtylidiMinoa.getAnaktoro());
        daxtylidiMinoa.addToSyllogi(player);
        assertEquals(1, player.get_rare_holdings()[0]);
        // Check DiskosFaistou
        DiskosFaistou diskosFaistou = new DiskosFaistou(35, AnaktoroEnum.FAISTOS);
        assertEquals(35, diskosFaistou.getPoints());
        assertEquals(AnaktoroEnum.FAISTOS, diskosFaistou.getAnaktoro());
        diskosFaistou.addToSyllogi(player);
        assertEquals(1, player.get_rare_holdings()[2]);
        // Check KosmimaMaliwn
        KosmimaMaliwn kosmimaMaliwn = new KosmimaMaliwn(25, AnaktoroEnum.MALIA);
        assertEquals(25, kosmimaMaliwn.getPoints());
        assertEquals(AnaktoroEnum.MALIA, kosmimaMaliwn.getAnaktoro());
        kosmimaMaliwn.addToSyllogi(player);
        assertEquals(1, player.get_rare_holdings()[1]);
        // Check RytoZakrou
        RytoZakrou rytoZakrou = new RytoZakrou(25, AnaktoroEnum.ZAKROS);
        assertEquals(25, rytoZakrou.getPoints());
        assertEquals(AnaktoroEnum.ZAKROS, rytoZakrou.getAnaktoro());
        rytoZakrou.addToSyllogi(player);
        assertEquals(1, player.get_rare_holdings()[3]);
        // Check Toixografies
        Toixografies toixografia = new Toixografies(15, 5);
        assertEquals(15, toixografia.getPoints());
        assertEquals(5, toixografia.get_id());
        toixografia.addToSyllogi(player);
        assertEquals(1, player.get_toixografies_photographed_ids()[4]); // id - 1
        toixografia.setPlayers_photographed(0, 1); // player0 just photographed this toixografia
        assertEquals(Integer.valueOf(1), toixografia.get_players_photographed().get(0));
    }
    // Paths Package
    public void test_paths_init() {
        // Knossos Path
        Path path_knossos = new Path(AnaktoroEnum.KNOSSOS);
        // Take the squares
        Square[] squares = path_knossos.get_squares_array();
        // Check the points
        assertEquals(-20, squares[0].getPoints());
        assertEquals(-15, squares[1].getPoints());
        assertEquals(-10, squares[2].getPoints());
        assertEquals(5, squares[3].getPoints());
        assertEquals(10, squares[4].getPoints());
        assertEquals(15, squares[5].getPoints());
        assertEquals(30, squares[6].getPoints());
        assertEquals(35, squares[7].getPoints());
        assertEquals(50, squares[8].getPoints());
        // Check if the rare finding has been put
        int flag = 0;
        for(int i = 0; i < 9; i++) {
            if (squares[i] instanceof FindingSquare) {
                flag = 1;
                break;
            }
        }
        assertEquals(1, flag);
        // Malia Path
        Path path_malia = new Path(AnaktoroEnum.MALIA);
        // Take the squares
        squares = path_malia.get_squares_array();
        // Check the points
        assertEquals(-20, squares[0].getPoints());
        assertEquals(-15, squares[1].getPoints());
        assertEquals(-10, squares[2].getPoints());
        assertEquals(5, squares[3].getPoints());
        assertEquals(10, squares[4].getPoints());
        assertEquals(15, squares[5].getPoints());
        assertEquals(30, squares[6].getPoints());
        assertEquals(35, squares[7].getPoints());
        assertEquals(50, squares[8].getPoints());
        // Check if the rare finding has been put
        flag = 0;
        for(int i = 0; i < 9; i++) {
            if (squares[i] instanceof FindingSquare) {
                flag = 1;
                break;
            }
        }
        assertEquals(1, flag);
        // Faistos Path
        Path path_faistos = new Path(AnaktoroEnum.FAISTOS);
        // Take the squares
        squares = path_faistos.get_squares_array();
        // Check the points
        assertEquals(-20, squares[0].getPoints());
        assertEquals(-15, squares[1].getPoints());
        assertEquals(-10, squares[2].getPoints());
        assertEquals(5, squares[3].getPoints());
        assertEquals(10, squares[4].getPoints());
        assertEquals(15, squares[5].getPoints());
        assertEquals(30, squares[6].getPoints());
        assertEquals(35, squares[7].getPoints());
        assertEquals(50, squares[8].getPoints());
        // Check if the rare finding has been put
        flag = 0;
        for(int i = 0; i < 9; i++) {
            if (squares[i] instanceof FindingSquare) {
                flag = 1;
                break;
            }
        }
        assertEquals(1, flag);
        // Zakros Path
        Path path_zakros = new Path(AnaktoroEnum.KNOSSOS);
        // Take the squares
        squares = path_zakros.get_squares_array();
        // Check the points
        assertEquals(-20, squares[0].getPoints());
        assertEquals(-15, squares[1].getPoints());
        assertEquals(-10, squares[2].getPoints());
        assertEquals(5, squares[3].getPoints());
        assertEquals(10, squares[4].getPoints());
        assertEquals(15, squares[5].getPoints());
        assertEquals(30, squares[6].getPoints());
        assertEquals(35, squares[7].getPoints());
        assertEquals(50, squares[8].getPoints());
        // Check if the rare finding has been put
        flag = 0;
        for(int i = 0; i < 9; i++) {
            if (squares[i] instanceof FindingSquare) {
                flag = 1;
                break;
            }
        }
        assertEquals(1, flag);
    }
    // Pawn Package
    public void test_pawn_init() {
        // Archaiologos anaktoro and position and visibility and uses
        Archaiologos archaiologos = new Archaiologos(AnaktoroEnum.KNOSSOS);
        assertEquals(AnaktoroEnum.KNOSSOS, archaiologos.get_anaktoro());
        assertEquals(-1, archaiologos.get_position());
        archaiologos.set_position(archaiologos.get_position() + 1);
        assertEquals(0, archaiologos.get_position());
        assertFalse(archaiologos.get_visibility());
        archaiologos.set_visibility(true);
        assertTrue(archaiologos.get_visibility());
        // Thiseas anaktoro and position and visibility and uses
        Thiseas thiseas = new Thiseas(AnaktoroEnum.KNOSSOS);
        assertEquals(AnaktoroEnum.KNOSSOS, thiseas.get_anaktoro());
        assertEquals(-1, thiseas.get_position());
        thiseas.set_position(thiseas.get_position() + 1);
        assertEquals(0, thiseas.get_position());
        assertFalse(thiseas.get_visibility());
        thiseas.set_visibility(true);
        assertTrue(thiseas.get_visibility());
        assertEquals(0, thiseas.getUses());
        thiseas.setUses(thiseas.getUses() + 1);
        assertEquals(1, thiseas.getUses());
    }
    // Priority Package
    public void test_turn() {
        Turn turn = new Turn();
        int turn1 = turn.get_turn();
        turn.update_turn();
        int turn2 = turn.get_turn();
        assertNotSame(turn1, turn2);
    }
    // Square Package
    public void test_squares_init() {
        // Test simple square
        SimpleSquare square = new SimpleSquare(30);
        assertEquals(30, square.getPoints());
        RytoZakrou rytoZakrou = new RytoZakrou(25, AnaktoroEnum.ZAKROS);
        // Test Finding Square
        FindingSquare f_square = new FindingSquare(35, rytoZakrou);
        assertEquals(35, f_square.getPoints());
        assertEquals(rytoZakrou, f_square.getFinding());
    }
    // Table Package
    public void test_table_init() {
        Path path_knossos = new Path(AnaktoroEnum.KNOSSOS);
        Path path_malia = new Path(AnaktoroEnum.MALIA);
        Path path_faistos = new Path(AnaktoroEnum.FAISTOS);
        Path path_zakros = new Path(AnaktoroEnum.ZAKROS);
        // This is the order of the constructor below
        Table table_of_game = new Table(path_faistos, path_knossos, path_malia, path_zakros);
        assertEquals(path_knossos, table_of_game.getPath_knossos());
        assertEquals(path_malia, table_of_game.getPath_malia());
        assertEquals(path_zakros, table_of_game.getPath_zakros());
        assertEquals(path_faistos, table_of_game.getPath_faistos());
        // Test record of holdings
        int flag = 1;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 9; j++) {
                // Everything must be -1
                if(table_of_game.get_record_of_holding(i, j) != -1) { flag = 0; }
             }
        }
        assertEquals(1, flag);
        int path_index = 2;
        int position = 3;
        int new_value = 0; // Belongs to player1
        table_of_game.change_record_of_holding(path_index, position, new_value);
        assertEquals(new_value, table_of_game.get_record_of_holding(path_index, position));
        for(int i = 0; i < 4; i++) {
            assertNull(table_of_game.get_last_played_cards().get(i));
        }
    }
    // Player Package
    public void test_player_init() {
        Player player = new Player();
        assertEquals(0, player.get_player_agalmatakia_count()); // Test agalmatakia count
        assertEquals(0, player.get_player_score()); // Test score
        assertNull(player.get_card_index(0)); // Test cards assure they are null at the beginning
        for(int i = 0; i < 8; i++) {
            assertNull(player.get_card_index(i)); // Test cards assure they are null at the beginning
        }
        for(int i = 0; i < 4; i++) {
            assertNull(player.get_pawns().get(i).get_anaktoro()); // Test that the anaktora of the Players are null
        }
        assertEquals(3,player.get_available_pawns()[0]); // Test correct pawn availability
        assertEquals(1,player.get_available_pawns()[1]);
        for(int i = 0; i < 4; i++) {
            assertEquals(0,player.get_rare_holdings()[i]); // Test that player doesn't have any rare finding at the beginning
        }
        for(int i = 0; i < 6; i++) {
           assertEquals(-1, player.get_toixografies_photographed_ids()[i]); // Test that player doesn't have toixografies
        }
    }
}
