
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FraGui3 extends JFrame {

    private BlackjackHand playerHand;
    private JLabel[] arLblPlayer;
    private BlackjackHand dealerHand;
    private JLabel[] arLblDealer;
    private Deck deck;
    private JPanel panBoard;
    private JPanel panTitle;
    private JPanel panStart;
    private JTextField txtMoney;
    private int nTotal = 1000;
    private int nBet = 0;
    private JPanel panEast;
    private JPanel panInput; // hit, stand, new, etc.
    private JLabel lblStatus; // status sMessage label
    private String sMessage = "Please place your bet.";
    private String sMoney;// Money update?
    private String sBet;
    private JLabel lblMoney;
    private JLabel lblBet;
    private JLabel lblMenu;
    private JButton btnHit;
    private JButton btnStand;
    private JButton btnNewGame;
    private JButton btnBet;
    private JButton btnStart;
    private boolean bInGame = false;

    public static void main(String[] args) {
        FraGui3 myFrame = new FraGui3();
        myFrame.setPreferredSize(new Dimension(1000, 400));
        myFrame.pack();
        myFrame.getContentPane().setBackground(Color.green);
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        myFrame.setTitle("Black Jack");
    }

    public FraGui3() {
        // to initalize the GUI
        menu();
    }//constructor

    // initialize GUI components
    public void menu() {
        panTitle = new JPanel();
        panStart = new JPanel();
        add(panTitle, BorderLayout.CENTER);
        add(panStart, BorderLayout.SOUTH);
        panTitle.setBackground(Color.black);
        panStart.setBackground(Color.black);
        lblMenu = new JLabel("Black Jack");
        btnStart = new JButton("Play Game!");
        Font menufont = new Font("Copperplate Gothic Bold", Font.PLAIN, 28);
        lblMenu.setFont(menufont);
        lblMenu.setForeground(Color.red);
        panTitle.add(lblMenu);
        panStart.add(btnStart);
        btnStart.addActionListener(new PlayActionListener());
    }

    class PlayActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            {
                
                panTitle.remove(lblMenu);
                panStart.remove(btnStart);
                init();
                newGame();
            }
        }
    }

    public void init() {
        setSize(1000, 400);
        setLayout(new BorderLayout()); // JFrame layout
        lblStatus = new JLabel(sMessage);
        lblMoney = new JLabel(sMoney);
        lblBet = new JLabel(sBet);
        txtMoney = new JTextField("", 4);
        arLblPlayer = new JLabel[6];
        arLblDealer = new JLabel[6];
        panBoard = new JPanel(new GridLayout(2, 6)); // 2 players - up to 6 cards each
        panInput = new JPanel(); // defaults to FlowLayout
        panEast = new JPanel();
        //panSEast = new JPanel();
        add(lblStatus, BorderLayout.NORTH);
        add(panEast, BorderLayout.EAST);
        add(panBoard, BorderLayout.CENTER);
        add(panInput, BorderLayout.SOUTH);
        Font font = new Font("Copperplate Gothic Bold", Font.PLAIN, 16);
        panBoard.setBackground(Color.green);
        panEast.setBackground(Color.green);
        panInput.setBackground(Color.green);
        lblStatus.setFont(font);
        lblStatus.setForeground(Color.BLACK);


        for (int i = 0; i < 6; i++) {
            arLblPlayer[i] = new JLabel();
            arLblDealer[i] = new JLabel();
        }

        for (int i = 0; i < 6; i++) {
            panBoard.add(arLblDealer[i]);
        }

        for (int i = 0; i < 6; i++) {
            panBoard.add(arLblPlayer[i]);
        }


        btnHit = new JButton("Hit!");
        btnHit.addActionListener(new HitActionListener());


        btnStand = new JButton("Stand!");
        btnStand.addActionListener(new StandActionListener());


        btnNewGame = new JButton("New game");
        btnNewGame.addActionListener(new NewgameActionListener());


        btnBet = new JButton("Bet");
        btnBet.addActionListener(new BetActionListener());
        panInput.add(btnBet);

        panEast.add(txtMoney);
        panEast.add(lblMoney);
        panEast.add(lblBet);
        sBet = "0";
        sMoney = "Money: $" + nTotal;


    }

    public void newGame() {
        // clear the board

        for (int i = 0; i < 6; i++) {
            arLblPlayer[i].setIcon(null);
            arLblDealer[i].setIcon(null);
        }

        // initialize the deck and hands (or do this in a newGame method)
        arLblDealer[0].setText("Dealer's Cards");
        arLblPlayer[0].setText("Your Cards");

        bInGame = true;
        deck = new Deck();
        playerHand = new BlackjackHand();
        dealerHand = new BlackjackHand();
        deck.shuffle();

        //deals cards
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());

        if (dealerHand.getBlackjackValue() == 21) {
            sMessage = "Sorry, you lose.  Dealer has Blackjack.";
            bInGame = false;
        } else if (playerHand.getBlackjackValue() == 21) {
            sMessage = "You win!  You have Blackjack.";
            bInGame = false;
        } else {
            sMessage = "You must place a bet first.";
            bInGame = true;
        }
        redraw();
    }

    public void redraw() {
        // update playing area
        // 1) look at cards in playerHand and dealerHand and add/update labes in the "Grid"
        for (int i = 1; i < dealerHand.getCardCount() + 1; i++) {
            Card dealerCard = dealerHand.getCard(i - 1);
            arLblDealer[i].setIcon(new ImageIcon(GetFileName(dealerCard)));
        }
        for (int i = 1; i < playerHand.getCardCount() + 1; i++) {
            Card playerCard = playerHand.getCard(i - 1);
            arLblPlayer[i].setIcon(new ImageIcon(GetFileName(playerCard)));
        }

        if (bInGame) {
            arLblDealer[1].setIcon(new ImageIcon("back-blue-75-2.png"));
        }

        // 2) update any status messages
        lblStatus.setText(sMessage);
        lblMoney.setText(sMoney);

    }

    public static String GetFileName(Card thisCard) {
        String sSuit = thisCard.getSuitAsString();
        String sValue = thisCard.getValueAsString();
        String FileName = sSuit + "-" + sValue + "-75.png";
        return FileName;
    }

    // actionListeners for buttons!
    class HitActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            {
                if (nBet == 0) {
                    sMessage = "Must place a bet first";
                    redraw();
                    return;
                }
                if (bInGame == false) {
                    sMessage = "Click \"New Game\" to start a new game.";
                    redraw();
                    return;
                }
                playerHand.addCard(deck.dealCard());
                if (playerHand.getBlackjackValue() > 21) {
                    sMessage = "You've busted!  Sorry, you lose.";
                    bInGame = false;
                    nTotal = nTotal - nBet;
                    nBet = 0;
                } else if (playerHand.getCardCount() == 5) {
                    sMessage = "You win by taking 5 cards without going over 21.";
                    bInGame = false;
                    nTotal = nTotal + nBet;
                    nBet = 0;
                } else {
                    sMessage = "You have " + playerHand.getBlackjackValue() + ".  Hit or Stand?";
                }

                redraw();
            }
        }
    }

    class StandActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (nBet == 0) {
                sMessage = "Must place a bet first";
                redraw();
                return;
            }
            if (bInGame == false) {
                sMessage = "Click \"New Game\" to start a new game.";
                redraw();
                return;
            }
            bInGame = false;
            while (dealerHand.getBlackjackValue() <= 16 && dealerHand.getCardCount() < 5) {
                dealerHand.addCard(deck.dealCard());
            }
            if (dealerHand.getBlackjackValue() > 21) {
                sMessage = "You win!  Dealer has busted with " + dealerHand.getBlackjackValue() + ".";
                nTotal = nTotal + nBet;
                nBet = 0;
            } else if (dealerHand.getCardCount() == 5) {
                sMessage = "Sorry, you lose.  Dealer took 5 cards without going over 21.";
                nTotal = nTotal - nBet;
                nBet = 0;
            } else if (dealerHand.getBlackjackValue() > playerHand.getBlackjackValue()) {
                sMessage = "Sorry, you lose, " + dealerHand.getBlackjackValue()
                        + " to " + playerHand.getBlackjackValue() + ".";
                nTotal = nTotal - nBet;
                nBet = 0;
            } else if (dealerHand.getBlackjackValue() == playerHand.getBlackjackValue()) {
                sMessage = "Sorry, you lose.  Dealer wins on a tie.";
                nTotal = nTotal - nBet;
                nBet = 0;
            } else {
                sMessage = "You win, " + playerHand.getBlackjackValue()
                        + " to " + dealerHand.getBlackjackValue() + "!";
                nTotal = nTotal + nBet;
                nBet = 0;

            }
            redraw();
        }
    }

    class NewgameActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            txtMoney.enable();
            panInput.remove(btnHit);
            panInput.remove(btnStand);
            panInput.remove(btnNewGame);
            txtMoney.setText("");
            sBet = " ";
            lblBet.setText(sBet);
            sMoney = "Money: $" + nTotal;
            lblMoney.setText(sMoney);
            if (bInGame == true) {
                sMessage = "You still have to finish this game!";
                redraw();
                return;
            } else {
                newGame();
            }
        }
    }

    class BetActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnBet) {
                sBet = txtMoney.getText();
                nBet = Integer.parseInt(sBet);

                if (nTotal == 0) {
                    sMessage = "Sorry game over, you're all out of money.";
                    lblStatus.setText(sMessage);
                    return;
                } else if (nBet > nTotal) {
                    sMessage = "Sorry insufficent funds";
                    lblStatus.setText(sMessage);

                } else {
                    panInput.add(btnHit);
                    panInput.add(btnStand);
                    panInput.add(btnNewGame);
                    txtMoney.disable();
                    lblBet.setText("Your bet is $" + nBet);
                    sMessage = "You have " + playerHand.getBlackjackValue() + ".  Hit or Stand?";
                    lblStatus.setText(sMessage);
                }
            }
        }
    }
}