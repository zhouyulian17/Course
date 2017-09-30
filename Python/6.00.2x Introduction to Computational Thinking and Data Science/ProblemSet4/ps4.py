# 6.00.2x Problem Set 4

import numpy
import random
import pylab
from ps3b import *

#
# PROBLEM 1
#        
def simulationDelayedTreatment(numTrials):
    """
    Runs simulations and make histograms for problem 1.

    Runs numTrials simulations to show the relationship between delayed
    treatment and patient outcome using a histogram.

    Histograms of final total virus populations are displayed for delays of 300,
    150, 75, 0 timesteps (followed by an additional 150 timesteps of
    simulation).

    numTrials: number of simulation runs to execute (an integer)
    """
    final = [0 for trial in range(numTrials)]
    numViruses = 100
    maxPop = 1000
    maxBirthProb = 0.1
    clearProb = 0.05
    resistances = {'guttagonol': False}
    mutProb = 0.005
    
    for trial in range(numTrials):
        viruses = [ResistantVirus(maxBirthProb, clearProb, resistances, mutProb) 
                   for i in range(numViruses)]
        p = TreatedPatient(viruses, maxPop)
        for timeStep in range(delay):
            final[trial] = p.update()
            
        p.addPrescription('guttagonol')
        for timeStep in range(150):
            final[trial] = p.update()
   
    cured = 0
    for trial in final:
        if trial <= 50:
            cured += 1
    
    pylab.figure()
    pylab.hist(final)
    pylab.title("ResistantVirus simulation")
    pylab.show()
    return float(cured) / numTrials


#
# PROBLEM 2
#
def simulationTwoDrugsDelayedTreatment(numTrials):
    """
    Runs simulations and make histograms for problem 2.

    Runs numTrials simulations to show the relationship between administration
    of multiple drugs and patient outcome.

    Histograms of final total virus populations are displayed for lag times of
    300, 150, 75, 0 timesteps between adding drugs (followed by an additional
    150 timesteps of simulation).

    numTrials: number of simulation runs to execute (an integer)
    """
    final = [0 for trial in range(numTrials)]
    numViruses = 100
    maxPop = 1000
    maxBirthProb = 0.1
    clearProb = 0.05
    resistances = {'guttagonol': False, 'grimpex': False}
    mutProb = 0.005
    
    for trial in range(numTrials):
        viruses = [ResistantVirus(maxBirthProb, clearProb, resistances, mutProb) 
                   for i in range(numViruses)]
        p = TreatedPatient(viruses, maxPop)
        for timeStep in range(150):
            final[trial] = p.update()
                        
        p.addPrescription('guttagonol')
        for timeStep in range(delay):
            final[trial] = p.update()

        p.addPrescription('grimpex')
        for timeStep in range(150):
            final[trial] = p.update()
   
    cured = 0
    for trial in final:
        if trial <= 50:
            cured += 1
    
    pylab.figure()
    pylab.hist(final)
    pylab.title("ResistantVirus simulation")
    pylab.show()
    return float(cured) / numTrials
    

delays = [300, 150, 75, 0]
for delay in delays:
    print simulationDelayedTreatment(100)
    print simulationTwoDrugsDelayedTreatment(100)