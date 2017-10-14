import matplotlib.pyplot as plt
import re
#  types 'RENAME', 'COPY', 'MODIFY', 'DELETE', 'ADD'

REPO_NAME = 'RxJava'


class Modification:

    def __init__(self, date, mtype, old_path, new_path, lambdas):
        self.d = date  # millis
        self.t = mtype
        self.op = old_path
        self.np = new_path
        self.n = lambdas


def calculate_current_lambdas(dic):
    # calculates the amount of lambdas currently in the project
    return sum(dic.values())


repo_state = dict()  # path -> amount of lambdas in file
modifications = dict()  # t in millis -> list of Modifications
updates = dict()  # t in millis -> dict of files and their lambdas
lambdas_per_time = dict()  # t in millis -> lambdas on current state
dates = []  # list with the commits times, just an aux

with open("{}_commits.csv".format(REPO_NAME), "r") as f:
    for l in f.readlines():
        line = l.strip().split(",")
        if line[0] == "MOD":
            date, mtype, old_path, new_path, nl = line[1:]
            date = int(date)
            dates.append(date)
            nl = int(nl)
            if date not in modifications:
                modifications[date] = []
            modifications[date].append(Modification(
                date, mtype, old_path, new_path, nl))
        elif line[0] == "UPD":
            date, absolute_path, nl = line[1:4]
            date = int(date)
            dates.append(date)
            nl = int(nl)
            path = re.split('\\{}/\\b'.format(REPO_NAME), absolute_path)[-1]
            if date not in updates:
                updates[date] = dict()
            updates[date][path] = nl
        else:
            print("Problem in parsing")

dates = sorted(dates)

for date in dates:
    if date in modifications:
        for mod in modifications[date]:
            if mod.t == 'ADD' or mod.t == 'MODIFY' or mod.t == 'COPY':
                repo_state[mod.np] = mod.n
            elif mod.t == 'RENAME':
                del_status = repo_state.pop(mod.op, "rename")
                print(del_status, mod.op, mod.d)
                repo_state[mod.np] = mod.n
            elif mod.t == 'DELETE':
                del_status = repo_state.pop(mod.op, "delete")
                print(del_status, mod.op, mod.d)
    elif date in updates:
        repo_state = updates[date]
    else:
        print("Problem with dates")
    lambdas_per_time[date] = calculate_current_lambdas(repo_state)

x_times, y_values = zip(*sorted(lambdas_per_time.items(), key=lambda x: x[0]))
plt.plot(x_times, y_values)
plt.plot([1394755200000], [0], 'ro')  #  release date
plt.xlabel('Java milliseconds')
plt.ylabel('Amount of Lambdas')
plt.title('Lambda usage evolution in RxJava')

# COMPARATION WITH JGIT ANALYSIS

x_times_jgit = []
y_values_jgit = []
with open("{}_jgit.csv".format(REPO_NAME), "r") as f:
    for l in f.readlines():
        h, n, t = l.strip().split(",")
        x_times_jgit.append(int(t))
        y_values_jgit.append(int(n))
plt.plot(x_times_jgit, y_values_jgit, 'k')

# TO DO: CONVERT TO YEAR LABELS

plt.show()
