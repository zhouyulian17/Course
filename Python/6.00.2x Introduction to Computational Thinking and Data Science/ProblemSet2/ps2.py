# 6.00.2x Problem Set 2: Simulating robots

import math
import random

import ps2_visualize
import pylab

# For Python 2.7:
from ps2_verify_movement27 import testRobotMovement

# If you get a "Bad magic number" ImportError, you are not using 
# Python 2.7 and using most likely Python 2.6:


# === Provided class Position
class Position(object):
    """
    A Position represents a location in a two-dimensional room.
    """
    def __init__(self, x, y):
        """
        Initializes a position with coordinates (x, y).
        """
        self.x = x
        self.y = y
        
    def getX(self):
        return self.x
    
    def getY(self):
        return self.y
    
    def getNewPosition(self, angle, speed):
        """
        Computes and returns the new Position after a single clock-tick has
        passed, with this object as the current position, and with the
        specified angle and speed.

        Does NOT test whether the returned position fits inside the room.

        angle: number representing angle in degrees, 0 <= angle < 360
        speed: positive float representing speed

        Returns: a Position object representing the new position.
        """
        old_x, old_y = self.getX(), self.getY()
        angle = float(angle)
        # Compute the change in position
        delta_y = speed * math.cos(math.radians(angle))
        delta_x = speed * math.sin(math.radians(angle))
        # Add that to the existing position
        new_x = old_x + delta_x
        new_y = old_y + delta_y
        return Position(new_x, new_y)

    def __str__(self):  
        return "(%0.2f, %0.2f)" % (self.x, self.y)


# === Problem 1
class RectangularRoom(object):
    """
    A RectangularRoom represents a rectangular region containing clean or dirty
    tiles.

    A room has a width and a height and contains (width * height) tiles. At any
    particular time, each of these tiles is either clean or dirty.
    """
    def __init__(self, width, height):
        """
        Initializes a rectangular room with the specified width and height.

        Initially, no tiles in the room have been cleaned.

        width: an integer > 0
        height: an integer > 0
        """
        self.width = width
        self.height = height
        self.tiles = [[False for i in range(height)] for j in range(width)]
        self.cleaned = 0
    
    def cleanTileAtPosition(self, pos):
        """
        Mark the tile under the position POS as cleaned.

        Assumes that POS represents a valid position inside this room.

        pos: a Position
        """
        m = int(pos.getX())
        n = int(pos.getY())
        if not self.isTileCleaned(m, n):
            self.cleaned += 1
            self.tiles[m][n] = True

    def isTileCleaned(self, m, n):
        """
        Return True if the tile (m, n) has been cleaned.

        Assumes that (m, n) represents a valid tile inside the room.

        m: an integer
        n: an integer
        returns: True if (m, n) is cleaned, False otherwise
        """
        return self.tiles[m][n]
    
    def getNumTiles(self):
        """
        Return the total number of tiles in the room.

        returns: an integer
        """
        return self.width * self.height

    def getNumCleanedTiles(self):
        """
        Return the total number of clean tiles in the room.

        returns: an integer
        """
        return self.cleaned

    def getRandomPosition(self):
        """
        Return a random position inside the room.

        returns: a Position object.
        """
        m = random.randrange(self.width)
        n = random.randrange(self.height)
        return Position(m, n)

    def isPositionInRoom(self, pos):
        """
        Return True if pos is inside the room.

        pos: a Position object.
        returns: True if pos is in the room, False otherwise.
        """
        if pos.getX() >= self.width or pos.getX() < 0:
            return False
        if pos.getY() >= self.height or pos.getY() < 0:
            return False
        return True


class Robot(object):
    """
    Represents a robot cleaning a particular room.

    At all times the robot has a particular position and direction in the room.
    The robot also has a fixed speed.

    Subclasses of Robot should provide movement strategies by implementing
    updatePositionAndClean(), which simulates a single time-step.
    """
    def __init__(self, room, speed):
        """
        Initializes a Robot with the given speed in the specified room. The
        robot initially has a random direction and a random position in the
        room. The robot cleans the tile it is on.

        room:  a RectangularRoom object.
        speed: a float (speed > 0)
        """
        self.room = room
        self.speed = speed
        self.direction = random.randrange(360)
        x = random.random()
        y = random.random()
        self.pos = Position(x * room.width, y * room.height)
        room.cleanTileAtPosition(self.pos)

    def getRobotPosition(self):
        """
        Return the position of the robot.

        returns: a Position object giving the robot's position.
        """
        return self.pos
    
    def getRobotDirection(self):
        """
        Return the direction of the robot.

        returns: an integer d giving the direction of the robot as an angle in
        degrees, 0 <= d < 360.
        """
        return self.direction

    def setRobotPosition(self, position):
        """
        Set the position of the robot to POSITION.

        position: a Position object.
        """
        self.pos = position

    def setRobotDirection(self, direction):
        """
        Set the direction of the robot to DIRECTION.

        direction: integer representing an angle in degrees
        """
        self.direction = direction

    def updatePositionAndClean(self):
        """
        Simulate the raise passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        raise NotImplementedError # don't change this!


# === Problem 2
class StandardRobot(Robot):
    """
    A StandardRobot is a Robot with the standard movement strategy.

    At each time-step, a StandardRobot attempts to move in its current
    direction; when it would hit a wall, it *instead* chooses a new direction
    randomly.
    """
    def updatePositionAndClean(self):
        """
        Simulate the raise passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        newpos = self.pos.getNewPosition(self.direction, self.speed)
        while not self.room.isPositionInRoom(newpos):
            newdirection = random.randrange(360)
            self.setRobotDirection(newdirection)
            newpos = self.pos.getNewPosition(self.direction, self.speed)
        self.setRobotPosition(newpos)
        self.room.cleanTileAtPosition(newpos)

# Uncomment this line to see your implementation of StandardRobot in action!
#testRobotMovement(StandardRobot, RectangularRoom)


# === Problem 3
def runSimulation(num_robots, speed, width, height, min_coverage, num_trials,
                  robot_type):
    """
    Runs NUM_TRIALS trials of the simulation and returns the mean number of
    time-steps needed to clean the fraction MIN_COVERAGE of the room.

    The simulation is run with NUM_ROBOTS robots of type ROBOT_TYPE, each with
    speed SPEED, in a room of dimensions WIDTH x HEIGHT.

    num_robots: an int (num_robots > 0)
    speed: a float (speed > 0)
    width: an int (width > 0)
    height: an int (height > 0)
    min_coverage: a float (0 <= min_coverage <= 1.0)
    num_trials: an int (num_trials > 0)
    robot_type: class of robot to be instantiated (e.g. StandardRobot or
                RandomWalkRobot)
    """
    timeSteps = 0
    for i in range(num_trials):
        room = RectangularRoom(width, height)
        robots = [robot_type(room, speed) for j in range(num_robots)]
        while float(room.getNumCleanedTiles()) / room.getNumTiles() < min_coverage:
            timeSteps += 1
            for rob in robots:
                rob.updatePositionAndClean()
    return float(timeSteps) / num_trials
    

# Uncomment this line to see how much your simulation takes on average
#print  runSimulation(1, 1.0, 20, 20, 1.0, 30, StandardRobot)

# === Problem 4
class RandomWalkRobot(Robot):
    """
    A RandomWalkRobot is a robot with the "random walk" movement strategy: it
    chooses a new direction at random at the end of each time-step.
    """
    def updatePositionAndClean(self):
        """
        Simulate the passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        newdirection = random.randrange(360)
        self.setRobotDirection(newdirection)
        newpos = self.pos.getNewPosition(self.direction, self.speed)
        while not self.room.isPositionInRoom(newpos):
            newdirection = random.randrange(360)
            self.setRobotDirection(newdirection)
            newpos = self.pos.getNewPosition(self.direction, self.speed)
        self.setRobotPosition(newpos)
        self.room.cleanTileAtPosition(newpos)


def showPlot1(title, x_label, y_label):
    """
    Runs simulation with different numbers of StandardRobot or RandomWalkRobot
    number of robots ranges from 1 to 10
    speed = 1.0
    room width = 20
    room height = 20
    minimal coverage = 0.8
    number of trials = 20
    plot the mean number of time-steps against numbers of robots
    """
    num_robot_range = range(1, 11)
    timeStepsS = []
    timeStepsRW = []
    for num_robots in num_robot_range:
        print "Plotting", num_robots, "robots..."
        timeStepsS.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, StandardRobot))
        timeStepsRW.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, RandomWalkRobot))
    
    pylab.plot(num_robot_range, timeStepsS)
    pylab.plot(num_robot_range, timeStepsRW)
    pylab.title(title)
    pylab.legend(('StandardRobot', 'RandomWalkRobot'))
    pylab.xlabel(x_label)
    pylab.ylabel(y_label)
    pylab.show()

    
def showPlot2(title, x_label, y_label):
    """
    Runs simulation of StandardRobot or RandomWalkRobot in rooms with different shapes
    number of robots = 2
    speed = 1.0
    room width * room height = 10*30, 20*15, 25*12, 50*6
    minimal coverage = 0.8
    number of trials = 200
    plot the mean number of time-steps against the ratio of room width / height
    """
    aspect_ratios = []
    timeStepsS = []
    timeStepsRW = []
    for width in [10, 20, 25, 50]:
        height = 300/width
        print "Plotting cleaning time for a room of width:", width, "by height:", height
        aspect_ratios.append(float(width) / height)
        timeStepsS.append(runSimulation(2, 1.0, width, height, 0.8, 200, StandardRobot))
        timeStepsRW.append(runSimulation(2, 1.0, width, height, 0.8, 200, RandomWalkRobot))
    
    pylab.plot(aspect_ratios, timeStepsS)
    pylab.plot(aspect_ratios, timeStepsRW)
    pylab.title(title)
    pylab.legend(('StandardRobot', 'RandomWalkRobot'))
    pylab.xlabel(x_label)
    pylab.ylabel(y_label)
    pylab.show()
    

title = 'title'
x_label = 'x'
y_label = 'y'
showPlot1(title, x_label, y_label)
#showPlot2(title, x_label, y_label)

# === Problem 5
#
# 1) Write a function call to showPlot1 that generates an appropriately-labeled
#     plot.
#
#       (... your call here ...)
#

#
# 2) Write a function call to showPlot2 that generates an appropriately-labeled
#     plot.
#
#       (... your call here ...)
#
