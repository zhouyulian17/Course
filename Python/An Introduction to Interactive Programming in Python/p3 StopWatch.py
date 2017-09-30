# Implementation of "Stopwatch: The Game"

# define global variables
# count: timer count; x: stops at whole second; y: total stops
import simplegui
count = 0
x = 0
y = 0

# define helper function format that converts time
# in tenths of seconds into formatted string A:BC.D
def format(n):
    min = int(n/600)
    sec = int(n - 600*min)
    if(sec >= 100):
        return str(min) + ":" + str(sec)[0:2] + "." + str(sec)[2]
    elif(sec >= 10):
        return str(min) + ":0" + str(sec)[0:1] + "." + str(sec)[1]
    else:
        return str(min) + ":00." + str(sec)[0]

    
# define event handlers for buttons; "Start", "Stop", "Reset"
def start():
    timer.start()

def stop():
    global x, y
    if(timer.is_running()):
        timer.stop()
        y += 1
        if(count % 10 ==0):
            x += 1

def reset():
    global count, x, y
    timer.stop()
    count = 0
    x = 0
    y = 0

# define event handler for timer with 0.1 sec interval
def clock():
    global count
    count += 1

# define draw handler
def draw(canvas):
    canvas.draw_text(format(count), (60, 90), 32, "White")
    canvas.draw_text(str(x) + "/" + str(y), (155, 30), 24, "Blue")
    
# create frame
frame = simplegui.create_frame("Stopwatch Game", 200, 160)
timer = simplegui.create_timer(100, clock)

# register event handlers
frame.set_draw_handler(draw)
frame.add_label("Try to stop the watch at a whole second!")
frame.add_button("Start", start, 100)
frame.add_button("Stop", stop, 100)
frame.add_button("Reset", reset, 100)

# start frame
frame.start()

# Please remember to review the grading rubric