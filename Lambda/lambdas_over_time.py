import matplotlib.pyplot as plt
import re
#  types 'RENAME', 'COPY', 'MODIFY', 'DELETE', 'ADD'
# sin UPD se demora 6 minutos,  rename errors 647015, delete_errors 1443687
# con UPD se demora 11 minutos, rename errors 647556, delete errors 1443376
# 1 hora en spring-framework

REPO_NAME = 'spring-framework'
METHOD = 'mod'


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

rename_errors = 0
delete_errors = 0

with open("{}_commits_{}.csv".format(REPO_NAME, METHOD), "r") as f:
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
            path = re.split('\\{}/\\b'.format(REPO_NAME),
                            absolute_path.strip())[-1]
            if date not in updates:
                updates[date] = dict()
            updates[date][path] = nl
        else:
            print("Problem in parsing")

dates.sort()

for date in dates:
    if date in modifications:
        for mod in modifications[date]:
            if mod.t == 'ADD' or mod.t == 'MODIFY' or mod.t == 'COPY':
                repo_state[mod.np] = mod.n
            elif mod.t == 'RENAME':
                del_status = repo_state.pop(mod.op, "rename")
                # print(del_status, mod.op, mod.d)
                if del_status != 0:
                    rename_errors += 1
                repo_state[mod.np] = mod.n
            elif mod.t == 'DELETE':
                del_status = repo_state.pop(mod.op, "delete")
                # print(del_status, mod.op, mod.d)
                if del_status != 0:
                    delete_errors += 1
    elif date in updates:
        repo_state = updates[date]
    else:
        print("Problem with dates")
    lambdas_per_time[date] = calculate_current_lambdas(repo_state)

# print("RENAME ERRORS", rename_errors)
# print("DELETE ERRORS", delete_errors)

x_times, y_values = zip(*sorted(lambdas_per_time.items(), key=lambda x: x[0]))

plt.plot(x_times, y_values)
plt.plot([1394755200000], [0], 'ro')  #  release date
years = [("2013", 1356998400000), ("2014", 1388534400000),
         ("2015", 1420070400000), ("2016", 1451606400000),
         ("2017", 1483228800000)]
plt.axes().set_xticks([x[1] for x in years])
plt.axes().set_xticklabels([x[0] for x in years])
plt.xlabel('Year')
plt.ylabel('Amount of Lambdas')
plt.title('Lambda usage evolution in {}'.format(REPO_NAME))

# COMPARATION WITH JGIT ANALYSIS

# x_times_jgit = []
# y_values_jgit = []
# with open("{}_jgit.csv".format(REPO_NAME), "r") as f:
#     for l in f.readlines():
#         h, n, t = l.strip().split(",")
#         x_times_jgit.append(int(t))
#         y_values_jgit.append(int(n))
# plt.plot(x_times_jgit, y_values_jgit, 'k')


plt.show()

print("ACTUAL LAMBDAS = ", lambdas_per_time[dates[len(dates) - 1]])
