# Parallel-Heat-Propagation-Fork-Join

This project is a second iteration of a project I completed this past Fall.

I was unsatisfied with the performance of my Parallel-Heat-Propagation project
and as a result decided to tackle a few key issues. The first issue being the
display, it was missing pixels and didn't allow for resizing. The second issue
being parallel task size, I made the mistake of making the tasks too granular.
The third issue consisted of my phases having too much overhead, I was shutting
down an executor service and waiting for all the threads to be shutdown.

In order to combat the second and third issue I decided to utilize a fork join
pool with recursive actions. The metal alloy is now chunked into quarters(sometimes
halves) until it hits an optimal region size, then the calculations are
performed.
