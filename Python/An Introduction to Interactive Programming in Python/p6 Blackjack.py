# Implementation of mini-project - Blackjack

import simplegui
import random

# load card sprite - 936x384 - source: dropbox.com
CARD_SIZE = (72, 96)
CARD_CENTER = (36, 48)
card_images = simplegui.load_image("https://dl.dropbox.com/s/gg2oly1b2cxwcpy/File%20Jul%2018%2C%2011%2056%2048%20PM.png?dl=0")

CARD_BACK_SIZE = (72, 96)
CARD_BACK_CENTER = (36, 48)
card_back = simplegui.load_image("https://dl.dropbox.com/s/xuv0tosqvnli5yc/File%20Jul%2018%2C%2011%2056%2034%20PM.png?dl=0")    

# initialize some useful global variables
in_play = False
outcome = ""
score = 0

# define globals for cards
SUITS = ('C', 'S', 'H', 'D')
RANKS = ('A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K')
VALUES = {'A':1, '2':2, '3':3, '4':4, '5':5, '6':6, '7':7, '8':8, '9':9, 'T':10, 
          'J':10, 'Q':10, 'K':10}


# define card class
class Card:
    def __init__(self, suit, rank):
        if (suit in SUITS) and (rank in RANKS):
            self.suit = suit
            self.rank = rank
        else:
            self.suit = None
            self.rank = None
            print "Invalid card: ", suit, rank

    def __str__(self):
        return self.suit + self.rank

    def get_suit(self):
        return self.suit

    def get_rank(self):
        return self.rank

    def draw(self, canvas, pos):
        card_loc = (CARD_CENTER[0] + CARD_SIZE[0] * RANKS.index(self.rank), 
                    CARD_CENTER[1] + CARD_SIZE[1] * SUITS.index(self.suit))
        canvas.draw_image(card_images, card_loc, CARD_SIZE, [pos[0] + CARD_CENTER[0], 
                          pos[1] + CARD_CENTER[1]], CARD_SIZE)
        
# define hand class
class Hand:
    def __init__(self):
        # create Hand object
        self.val = 0
        self.cards = []

    def __str__(self):
        # return a string representation of a hand
        s = "Hand contains"
        for card in self.cards:
            s += " " + card.suit + card.rank
        return s

    def add_card(self, card):
        # add a card object to a hand
        self.cards.append(card)

    def get_value(self):
        # count aces as 1, if the hand has an ace, then add 10 to hand value
        # if it doesn't bust
        # compute the value of the hand, see Blackjack video
        self.val = 0
        for card in self.cards:
            self.val += VALUES[card.rank]
        for card in self.cards:
            if card.rank == 'A':
                if self.val < 12:
                    self.val += 10
                    break
        return self.val
   
    def draw(self, canvas, pos):
        # draw a hand on the canvas, use the draw method for cards
        for card in self.cards:
            card.draw(canvas, pos)
            pos[0] += CARD_SIZE[0] + 10
        
# define deck class 
class Deck:
    def __init__(self):
        # create a Deck object
        self.deck = []
        for suit in SUITS:
            for rank in RANKS:
                self.deck.append(Card(suit, rank))

    def shuffle(self):
        # shuffle the deck 
        # use random.shuffle()
        random.shuffle(self.deck)

    def deal_card(self):
        # deal a card object from the deck
        return self.deck.pop()
    
    def __str__(self):
        # return a string representing the deck
        s = "Deck contains"
        for card in self.deck:
            s += " " + card.suit + card.rank
        return s

#define event handlers for buttons
def deal():
    # start a new deal
    global outcome, in_play, hand1, hand2, deck, score
    if in_play:
        score -= 1
    # create a new Deck object annd shuffle the deck
    deck = Deck()
    deck.shuffle()
    # create a player Hand object and add two cards to the player's hand
    hand1 = Hand()
    hand1.add_card(deck.deal_card())
    hand1.add_card(deck.deal_card())
    # create a dealer Hand object and add two cards to the dealer's hand
    hand2 = Hand()
    hand2.add_card(deck.deal_card())
    hand2.add_card(deck.deal_card())
    # start the play by updating in_play
    in_play = True

def hit():
    global score, in_play, msg
    # if the hand is in play, hit the player
    if in_play:
        hand1.add_card(deck.deal_card())
        # if busted, assign a message to outcome, update in_play and score
        if hand1.get_value() > 21:
            msg = "You went bust and lose"
            in_play = False
            score -= 1
            
def stand():
    global score, in_play, msg
    # if hand is in play, repeatedly hit dealer until his hand has value 17 or more
    if in_play:
        while hand2.get_value() < 17:
            hand2.add_card(deck.deal_card())
        # assign a message to outcome, update in_play and score
        in_play = False
        if hand2.get_value() > 21:
            msg = "Dealer went bust and you win"
            score += 1
        elif hand1.get_value() > hand2.get_value():
            msg = "You win"
            score += 1
        else:
            msg = "You lose"
            score -= 1  

# draw handler    
def draw(canvas):
    # draw the hands on the canvas
    global hand1, hand2, in_play, msg
    hand2.draw(canvas, [50, 150])
    hand1.draw(canvas, [50, 350])
    if in_play:
        canvas.draw_image(card_back, CARD_CENTER, CARD_SIZE, 
                          [50 + CARD_CENTER[0], 150 + CARD_CENTER[1]], CARD_SIZE)
        canvas.draw_text("Dealer", (50, 120), 24, "White") 
        canvas.draw_text("Player", (50, 320), 24, "White") 
        canvas.draw_text("Hit or Stand?", (200, 320), 24, "White") 
    else:
        canvas.draw_text("Dealer (" + str(hand2.get_value()) + ")", 
                         (50, 120), 24, "White") 
        canvas.draw_text(msg, (200, 120), 24, "White") 
        canvas.draw_text("Player (" + str(hand1.get_value()) + ")", 
                         (50, 320), 24, "White") 
        canvas.draw_text("New deal?", (200, 320), 24, "White") 
    canvas.draw_text("BlackJack", (50, 50), 36, "White") 
    canvas.draw_text("Score = " + str(score), (400, 50), 24, "White") 

# initialization frame
frame = simplegui.create_frame("Blackjack", 600, 600)
frame.set_canvas_background("Green")

#create buttons and canvas callback
frame.add_button("Deal", deal, 200)
frame.add_button("Hit",  hit, 200)
frame.add_button("Stand", stand, 200)
frame.set_draw_handler(draw)

# get things rolling
in_play = False
deal()
frame.start()

# remember to review the gradic rubric