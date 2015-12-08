Description
--------------------

Physics simulation of a multiple pendulum setup often seen in physics exhibitions, science fairs, and youtube videos.
Pendulum lengths are chosen so that their periods of oscillations are integer multiples of some fundamental period, so that they seem to come back into phase after some integer number of these fundamental periods.


Compile
------------------------

To compile using the make system, simply run in the project directory:

  make

If the make system is not installed, you can use the JDK directly:

  javac Pendulums.javac
  

Launch
------------------------

The easiest way to launch the applet is to use a compatible browser and open the "RunPendulumDemo.html" file.
Otherwise, if your system's PATH variable points to the JDK binaries, then you can simply launch using:

  appletviewer RunPendulumDemo.html