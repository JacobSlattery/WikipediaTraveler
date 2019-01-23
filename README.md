## Installation:
When you download the repo, the first thing you will want to do is unzip the files and replace the .zip files with the .txt files that are extracted. The program is currently hardcoded to use those names, but can also be swapped for the "test*" version attached as well. 

## What You Need To Know:
### Files
The files store two different sets of data which correspond to one another. The ID file contains a list of every Wikipedia article name and an assigned integer to it in the format of "NAME:####" and neither side having a set length. The Links file is a list of integers, which are the article IDs, separated by a space. The file is organized so that the first integer is the ID of the current page with any trailing integers being the IDs of the pages linked in the source. The data is a little outdated currently so don't be surprised if it doesn't match current Wikipedia articles.

### Efficiency:
The code is designed in a pattern which minimizes the work necessary to load the information to a map of nodes for rapid traversal. On my current machine, which runs with 4 cores clocking 4.6GHz and 16GB RAM, the build time averages at 36 seconds and the searches take no more than 4 seconds. If it takes more than a minute, it is likely that the unlikely scenario of finding two unlinked points occurred and it is best to end the program.

## Special Thanks
Dr. Benzel - Without him, this project never would have happened and it also would not be reading as quick.
David Spurlock & James Looney - Numerous discussions about approaches for optimization
