from csv import reader

# name, lambdas, lambdas_per_loc, android, loc, url, percentage, stars, android_in_url, android_in_description, size, description



class RepoInfo:

    def __init__(self):
        self.name = None
        self.lambdas = None
        self.url = None
        self.percentage = None
        self.stars = None
        self.size = None
        self.description = None
        self.android_in_url = False
        self.android_in_description = False
        self.android = False
        self.loc = None
        self.lambdas_per_loc = None

def import_repos(names):
    repos = []
    repo_metrics_csv = reader(open('repo_metrics.csv', "r", encoding='utf-8'), delimiter='\t')
    for l in repo_metrics_csv:
        r = RepoInfo()
        r.name, r.lambdas, r.lambdas_per_loc, r.android, r.loc, r.url, r.percentage, r.stars, r.android_in_url, r.android_in_description, r.size, r.description = l
        if r.name in names:
            repos.append(r)
    return repos 

def main():

    repos = []

    new_repo_metrics_csv = open('current_lambdas.csv', 'w', encoding="utf-8")
    repo_metrics_csv = reader(
        open('repo_metrics.csv', "r", encoding='utf-8'), delimiter='\t')

    n = 0
    for l in repo_metrics_csv:
        r = RepoInfo()
        r.name, r.lambdas, r.lambdas_per_loc, r.android, r.loc, r.url, r.percentage, r.stars, r.android_in_url, r.android_in_description, r.size, r.description = l
        if int(r.lambdas) > 0:
            n += 1
            new_repo_metrics_csv.write("{}\t\n".format(r.name))
    print(n)

    new_repo_metrics_csv.close()

#main()
