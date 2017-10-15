class RepoInfo:

    def __init__(self, name, lambdas):
        self.name = name
        self.lambdas = lambdas
        self.url = None
        self.javapercentage = None
        self.stars = None


data = []

with open("lambda_count_broad_mining.csv", "r") as f:
    for l in f.readlines():
        name, lambdas = l.strip().split(",")
        data.append(RepoInfo(name, int(lambdas)))

with open("repos_requirements.csv", "r") as f:
    i = 0
    for l in f.readlines():
        repo = data[i]
        i += 1
        url, percentage, stars = l.strip().split(",")
        repo.url = url
        repo.javapercentage = float(percentage)
        repo.stars = int(stars)

with open("BROAD_FINAL_RESULT.txt", "w") as f:
    f.write("TOTAL REPOS {}, DESC ORDER BY STAR GH JAVA PROJECTS\n".format(len(data)))
    android_mentions = 0
    repos_with_lambdas = []
    for r in data:
        if r.lambdas != 0:
            repos_with_lambdas.append(r)
        if "android" in r.url.lower():
            android_mentions += 1
    f.write("TOTAL ANDROID MENTIONS {}\n".format(android_mentions))
    f.write("TOTAL REPOS WITHOUT LAMBDAS {}\n".format(
        len(data)-len(repos_with_lambdas)))
    f.write("TOTAL REPOS WITH LAMBDAS {}\n".format(
        len(repos_with_lambdas)))
    f.write("{},{},{}\n".format("NAME","STARS","LAMBDAS"))
    for r in repos_with_lambdas:
        f.write("{},{},{}\n".format(r.name, r.stars, r.lambdas))



