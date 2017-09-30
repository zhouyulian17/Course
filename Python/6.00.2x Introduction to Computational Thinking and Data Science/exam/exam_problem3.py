import random
import pylab

# Global Variables
MAXRABBITPOP = 1000
CURRENTRABBITPOP = 500
CURRENTFOXPOP = 30

def rabbitGrowth():
    """ 
    rabbitGrowth is called once at the beginning of each time step.

    It makes use of the global variables: CURRENTRABBITPOP and MAXRABBITPOP.

    The global variable CURRENTRABBITPOP is modified by this procedure.

    For each rabbit, based on the probabilities in the problem set write-up, 
      a new rabbit may be born.
    Nothing is returned.
    """
    global CURRENTRABBITPOP

    if CURRENTRABBITPOP < 10:
        return
    rabbit_rep_prob = 1.0 - float(CURRENTRABBITPOP) / MAXRABBITPOP
    for rab in range(CURRENTRABBITPOP):
        if random.random() < rabbit_rep_prob:
            CURRENTRABBITPOP += 1


def foxGrowth():
    """ 
    foxGrowth is called once at the end of each time step.

    It makes use of the global variables: CURRENTFOXPOP and CURRENTRABBITPOP,
        and both may be modified by this procedure.

    Each fox, based on the probabilities in the problem statement, may eat 
      one rabbit (but only if there are more than 10 rabbits).

    If it eats a rabbit, then with a 1/3 prob it gives birth to a new fox.

    If it does not eat a rabbit, then with a 1/10 prob it dies.

    Nothing is returned.
    """
    global CURRENTRABBITPOP
    global CURRENTFOXPOP

    if CURRENTRABBITPOP < 10 or CURRENTFOXPOP < 10:
        return
    fox_eat_rabbit = float(CURRENTRABBITPOP) / MAXRABBITPOP
    for fox in range(CURRENTFOXPOP):
        if random.random() < fox_eat_rabbit:
            if random.random() < 1.0 / 3:
                CURRENTFOXPOP += 1
            CURRENTRABBITPOP -= 1
            if CURRENTRABBITPOP < 10:
                CURRENTRABBITPOP = 10
        else:
            if random.random() < 0.9:
                CURRENTFOXPOP -= 1
                if CURRENTFOXPOP < 10:
                    CURRENTFOXPOP = 10  

            
def runSimulation(numSteps):
    """
    Runs the simulation for `numSteps` time steps.

    Returns a tuple of two lists: (rabbit_populations, fox_populations)
      where rabbit_populations is a record of the rabbit population at the 
      END of each time step, and fox_populations is a record of the fox population
      at the END of each time step.

    Both lists should be `numSteps` items long.
    """
    rabbit_populations = []
    fox_populations = []
    for i in range(numSteps):
        rabbitGrowth()
        foxGrowth()
        rabbit_populations.append(CURRENTRABBITPOP)
        fox_populations.append(CURRENTFOXPOP)
        
    return (rabbit_populations, fox_populations)

(rabbitPopulationOverTime, foxPopulationOverTime) = runSimulation(200)

#pylab.plot(rabbitPopulationOverTime)
#pylab.plot(y)
#pylab.show()

coeff = pylab.polyfit(range(len(rabbitPopulationOverTime)), rabbitPopulationOverTime, 2)
pylab.plot(pylab.polyval(coeff, range(len(rabbitPopulationOverTime))))

#coeff = pylab.polyfit(range(len(foxPopulationOverTime)), foxPopulationOverTime, 2)
#pylab.plot(pylab.polyval(coeff, range(len(foxPopulationOverTime))))
pylab.show()