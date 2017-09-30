# Implementation of card game - Memory

import simplegui
import random

lst = [x for x in range(16)]
expose = []
state = 0
turn = 0
pair = [0, 0, 0]

# helper function to initialize globals
def new_game():
    # start a new game with all card hiden
    global expose, state, turn
    random.shuffle(lst)
    expose = [False for x in lst]
    state = 0
    turn = 0
    pair = [0, 0, 0]
    label.set_text("Turns = 0")

# define event handlers
def mouseclick(pos):
    global state, turn, label
    # when one card has been exposed (state1) and the click exposes a new card
    # increase the turn by one
    # go to state2
    if   state == 1:
        pair[1] = pos[0]/50
        if not expose[pair[1]] and pair[1] != pair[0]:
            expose[pair[1]] = True
            state = 2
            turn += 1  
            label.set_text("Turns = " + str(turn))
    # when two cards have been exposed (state2) and the click exposes a new card
    # if the previously exposed cards are not paired, hide them
    # go to state1
    elif state == 2:
        pair[2] = pos[0]/50
        if not expose[pair[2]] and pair[2] != pair[0] and pair[2] != pair[1]:
            expose[pair[2]] = True
            if lst[pair[0]] % 8 != lst[pair[1]] % 8:
                expose[pair[0]] = False
                expose[pair[1]] = False
            pair[0] = pair[2]        
            state = 1
    # when no card is exposed (state0) and the click exposes a new card
    # go to state1
    else:
        state = 1
        pair[0] = pos[0]/50
        expose[pair[0]] = True
                   
# cards are logically 50x100 pixels in size    
def draw(canvas):
    # draw the card number when it is exposed, otherwise draw a green card
    for i in range(16):
        if expose[i]: 
            canvas.draw_text(str(lst[i] % 8), [i * 50 + 12, 66], 50, "White")
        else:
            canvas.draw_polygon(([i * 50, 0], [i * 50 + 50, 0], 
                                 [i * 50 + 50 , 100], [i * 50, 100]), 
                                1, "Yellow", "Green")
        
# create frame and add a button and labels
frame = simplegui.create_frame("Memory", 800, 100)
frame.add_button("Reset", new_game)
label = frame.add_label("Turns = 0")

# register event handlers
frame.set_mouseclick_handler(mouseclick)
frame.set_draw_handler(draw)

# get things rolling
new_game()
frame.start()

# Always remember to review the grading rubric