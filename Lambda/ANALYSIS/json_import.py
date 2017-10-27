import json
from data_import import import_repos


def get_jsons():
    with open("repo_names.csv", "r") as f:
        for name in f.readlines():
            name = name.strip()
            info = open("./postprocess/{}.json".format(name), "r")
            data = json.load(info)
            yield (name, data)


# CURRENT PROJECTS WITH LAMBDAS

names = set()
with open("current_lambdas.csv", "r") as f:
    for l in f.readlines():
        name = l.strip()
        names.add(name)
repos = import_repos(names)  # contains the Repos
json_studied = []
for j in get_jsons():
    name, data = j
    if name in names:
        json_studied.append(data)
in_dates = []
for j in json_studied:
    dates = list(sorted(j.keys()))
    for d in dates:
        if int(j[d]) > 0:
            in_dates.append(d)
            break
print(len(in_dates))
print(in_dates)
mapp = {0: "2014-2015", 1: "2015-2016", 2: "2016-2017", 3: "2017-"}
dic = {i: 0 for i in range(4)}
ranges = [[1394755200000, 1426291200000], [1426291200000, 1457913600000], [
    1457913600000, 1489449600000], [1489449600000, 1509494400000]]
c = 0
for d in in_dates:
    d = int(d)
    for i in range(4):
        if d >= ranges[i][0] and d < ranges[i][1]:
            dic[i] += 1 
    if d < ranges[0][0]:
            c+= 1
print(dic)
dic_ = {mapp[i]:j*105/100 for i,j in dic.items()}
print(dic_)
print(sum(dic.values()))


# LAMBDAS PER LOC ANALYSIS
# lambdas_per_loc = [(float(r.lambdas_per_loc)*200, r.name) for r in repos]
# lambdas_per_loc.sort(key=lambda x: x[0], reverse=True)
# print(lambdas_per_loc)


# DROPPED
# names = set()
# with open("no_lambdas.csv", "r") as f:
#     for l in f.readlines():
#         name = l.strip()
#         names.add(name)
# total = 0
# repo_studied = set()
# for j in get_jsons():
#     name, data = j
#     if name in names:
#         dates = list(sorted(data.keys()))
#         for d in dates:
#             if data[d] != 0:
#                 total += 1
#                 repo_studied.add(name)
#                 break
# print(total)
# repos = import_repos(repo_studied)
# print(len([r for r in repos if r.android_in_description=="True"]))

# filtered = {"querydsl", "voldemort", "cSploit__android", "jitsi", "Hystrix", "DroidPlugin", "atmosphere", "Mybatis-PageHelper", "SmartCropper"  }
# final_plot_no_lambdas_names = [n for n in repos if n.name not in filtered]
# print(final_plot_no_lambdas_names[0].loc)
# print(sum((int(l.loc) for l in final_plot_no_lambdas_names))/len(final_plot_no_lambdas_names))
# print(len([r for r in final_plot_no_lambdas_names if r.android =="True"]))
# print([r.name for r in final_plot_no_lambdas_names if r.android=="True"])
# print([r.name for r in final_plot_no_lambdas_names if r.android=="False"])


# names = set()
# with open("l.csv", "r") as f:
#     for l in f.readlines():
#         name, lambdas = l.strip().split("\t")
#         names.add(name)
# total = 0
# for j in get_jsons():
#     name, data = j
#     dates = list(sorted(data.keys()))[-1]
#     if int(data[dates]) > 0:
#         total += 1
#         if name not in names:
#             print(name)
#     else:
#         if name in names:
#             pass
# print(total)

# 118 projects with lambdas


#     largo = len(data)
#     total += largo
# print("TOTAL " + str(total))
