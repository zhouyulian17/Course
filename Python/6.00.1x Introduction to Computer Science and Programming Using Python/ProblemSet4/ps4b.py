from ps4a import *
import time


#
#
# Problem #6: Computer chooses a word
#
#
def compChooseWord(hand, wordList, n):
    """
    Given a hand and a wordList, find the word that gives 
    the maximum value score, and return it.

    This word should be calculated by considering all the words
    in the wordList.

    If no words in the wordList can be made from the hand, return None.

    hand: dictionary (string -> int)
    wordList: list (string)
    returns: string or None
    """
    maxword = ""
    maxscore = 0
        
    for word in wordList:
        if isValidWord(word, hand, wordList):
            score = getWordScore(word, n)
            if score > maxscore:
                maxscore = score
                maxword = word
    if maxscore == 0:
        return
    return maxword

            
wordList = loadWords()
print compChooseWord({'a': 2, 'c': 1, 'b': 1, 't': 1}, wordList, 5)


#
# Problem #7: Computer plays a hand
#
def compPlayHand(hand, wordList, n):
    """
    Allows the computer to play the given hand, following the same procedure
    as playHand, except instead of the user choosing a word, the computer 
    chooses it.

    1) The hand is displayed.
    2) The computer chooses a word.
    3) After every valid word: the word and the score for that word is 
    displayed, the remaining letters in the hand are displayed, and the 
    computer chooses another word.
    4)  The sum of the word scores is displayed when the hand finishes.
    5)  The hand finishes when the computer has exhausted its possible
    choices (i.e. compChooseWord returns None).
 
    hand: dictionary (string -> int)
    wordList: list (string)
    n: integer (HAND_SIZE; i.e., hand size required for additional points)
    """
    total = 0
    print
    while True:
        if calculateHandlen(hand) == 0:
            print "Run out of letters.",
            break
        print "Current Hand: ",
        displayHand(hand)
        word = compChooseWord(hand, wordList, n)
        if word is None: 
            print "No more word can be chosen.",
            break
        score = getWordScore(word, n)
        total += score
        hand = updateHand(hand, word)
        print '"' + word + '"' + " earned " + str(score) + " points. Total: "\
              + str(total) + " points"
        print
    print "Total score: " + str(total) + " points."  
    

#
# Problem #8: Playing a game
#
#
def playGame(wordList):
    """
    Allow the user to play an arbitrary number of hands.
 
    1) Asks the user to input 'n' or 'r' or 'e'.
        * If the user inputs 'e', immediately exit the game.
        * If the user inputs anything that's not 'n', 'r', or 'e', keep asking them again.

    2) Asks the user to input a 'u' or a 'c'.
        * If the user inputs anything that's not 'c' or 'u', keep asking them again.

    3) Switch functionality based on the above choices:
        * If the user inputted 'n', play a new (random) hand.
        * Else, if the user inputted 'r', play the last hand again.
      
        * If the user inputted 'u', let the user play the game
          with the selected hand, using playHand.
        * If the user inputted 'c', let the computer play the 
          game with the selected hand, using compPlayHand.

    4) After the computer or user has played the hand, repeat from step 1

    wordList: list (string)
    """
    hand = {}
    while True:
        begin = raw_input("Enter n to deal a new hand, r to replay the last hand, or e to end game: ") 
        if   begin == "e": break
        elif begin == "n":
            hand = dealHand(HAND_SIZE)
            playChooseHand(hand, wordList, HAND_SIZE)
        elif begin == "r":
            if hand != {}:
                playChooseHand(hand, wordList, HAND_SIZE)
            else:
                print "You have not played a hand yet. Please play a new hand first!"
        else: 
            print "Invalid command."

def playChooseHand(hand, wordList, HAND_SIZE):
    choice = raw_input("Enter u to have yourself play, c to have the computer play: ")
    if choice == "u":
        playHand(hand, wordList, HAND_SIZE)
    elif choice == "c":
        compPlayHand(hand, wordList, HAND_SIZE) 
    else:
        print "Invalid command."
        playChooseHand(hand, wordList, HAND_SIZE)

        
#
# Build data structures used for entire session and play game
#
if __name__ == '__main__':
    wordList = loadWords()
    playGame(wordList)


