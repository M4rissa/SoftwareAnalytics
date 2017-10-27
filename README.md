# David

1. Clone:
https://github.com/NLPchina/ansj_seg
https://github.com/hankcs/HanLP
https://github.com/aosp-mirror/platform_frameworks_base

2. Create directory inside Lambda called repos_all and put the three repos in
3. Create directory inside Lambda called preprocess
4. Run Main.java

[Warning: This may take forever]

### Directories

Cloning has the script to clone the repos, Lambda to preprocess the data with java and then process it with python.

### Cloning

You have to run clone.py on the terminal (see dependencies like the git module), because after the repos (stated in repo_links) are cloned the user is prompt to enter an input (and execute the .sh if wants the branches, but not needed), this is important to eliminate the branches.sh file created in every repo with the script. After cloning the folder repo should be moved inside of Lambda. 

### Lambda

- /preprocess: thats where the java program stores the preprocessed information for every repo.
- /repos: the repos (IMPORTAN THAT THIS DIR IS HERE HAHA)
- /src/ModificationsMain.java: running this generates the info for every repo, to try it its better to uncomment the lines where the date is set so it only runs from june 2017, but for real it has to be from the beggining. Invoking ModificationsVisitor used the method of going to every commit modification and only checking those files (I called it method "mod" for csv identification, ModificationsVisitorNew also every 100 commits checks out the whole project and updates state (method "udp"), the results with the latter are weird so I used the other one. 
- /manual: has some files I used to test the methods and the python script with which you can play around. 

First you run the Java Main, this creates the files in preprocess. Then you run the python main, this first calls generate_postprocess_info() which processes the data in preprocess and generates a json per repo in the postprocess folder, then generate_plot_info() gets executed and using the jsons generates the lists with the x and y data for plotting, then the plot gets done in the script.  Theres a folder in preprocess called 2017 hat can be used to test this only with the commits from 2017 of every repo.


platform, junit5, hanlp, java8tutorial, mockito, store(esta en w/lambdas), powermock (esta en w/lambdas)