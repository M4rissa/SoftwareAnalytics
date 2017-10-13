import matplotlib.pyplot as plt

# STORES EVERYTHING IN MEMORY, FOR NOW I DONT THINK THATS A PROBLEM
# WHEN I REFACTOR IT ILL DO IN PLACE PROCESSING
#  types 'RENAME', 'COPY', 'MODIFY', 'DELETE', 'ADD'


class Modification:

    def __init__(self, date, mtype, path, lambdas):
        self.d = date  # millis
        self.t = mtype
        self.p = path
        self.n = lambdas


def calculate_current_lambdas(dic):
    # calculates the amount of lambdas currently in the project
    return sum(dic.values())


repo_state = dict()  # path -> amount of lambdas in file
pre_process = dict()  # t in millis -> list of Modifications
lambdas_per_time = dict()  # t in millis -> lambdas on current state

with open("RxJava_m.csv", "r") as f:
    for l in f.readlines():
        date, mtype, path, nl = l.strip().split(",")
        if date not in pre_process:
            pre_process[int(date)] = []
        pre_process[int(date)].append(
            Modification(int(date), mtype, path, int(nl)))

order = sorted(pre_process.keys())

# I NEED TO IMPROVE THIS SO FAR IM ONLY GIVING ONE PATH SO RENAMES DONT WORK
for date in order:
    for mod in pre_process[date]:
        if mod.t == 'RENAME' or mod.t == 'ADD' or mod.t == 'MODIFY':
            repo_state[mod.p] = mod.n
        elif mod.t == 'DELETE':
            del_status = repo_state.pop(mod.p, "problem with deletion")
            print(del_status)
    lambdas_per_time[date] = calculate_current_lambdas(repo_state)

x_times, y_values = zip(*sorted(lambdas_per_time.items(), key=lambda x: x[0]))
plt.plot(x_times, y_values)
plt.plot([1394755200000], [0], 'ro')  #  release date
plt.show()
