# Implementation of mini-project "Guess the number" 
# input will come from buttons and an input field
# all output for the game will be printed in the console

import simplegui
import random

num_range = 100
count = 7

# helper function to start and restart the game
def new_game():
    # initialize global variables used in your code here
    global num_range, count, number, count_left
    count_left = count
    number = random.randrange(0, num_range)
    print ""
    print "New Game. Range is from 0 to", str(num_range)
    print "Number of remaining guesses is", str(count) 
    

# define event handlers for control panel
def range100():
    # button that changes the range to [0,100) and starts a new game 
    global num_range, count
    num_range = 100
    count = 7
    new_game()
    

def range1000():
    # button that changes the range to [0,1000) and starts a new game     
    global num_range, count
    num_range = 1000
    count = 10
    new_game()
    
def input_guess(guess):
    # main game logic goes here	
    
    # take a guess from input handler, and count minus one
    global count_left, number
    count_left -= 1
    print ""
    print "Guess was", guess
    print "Number of remaining guesses is", str(count_left)
    
    # compare guess and number
    guess_number = int(guess)
    # print out 'Correct!' if guess equals number
    if guess_number == number:
        print "Correct!"
        new_game()
    # print out the number and starts a new game if no count left
    elif count_left == 0:
        print "You ran out of guesses. The number is", str(number)
        new_game()
    # print out 'Higher' if number is larger than guess
    elif guess_number < number:
        print "Higher!"
    # print out 'Lower' if number is smaller than guess
    else:
        print "Lower!"

    
# create frame
f = simplegui.create_frame("Guess the number", 200, 200)

# register event handlers for control elements and start frame
f.add_button("Range is [0, 100)", range100, 200)
f.add_button("Range is [0, 1000)", range1000, 200)
f.add_input("Enter a guess", input_guess, 200)

# call new_game 
new_game()

# always remember to check your completed program against the grading rubric