from csv import reader

# name, lambdas, android, size, url, percentage, stars, android_in_url, android_in_description, description

# size represents in kb, I will need to calculate loc with repodriller
# once the other thing finishes


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


repos = []

new_repo_metrics_csv = open('info_rq2.csv', 'w', encoding="utf-8")
repo_metrics_csv = reader(
    open('repo_metrics.csv', "r", encoding='utf-8'), delimiter='\t')

for l in repo_metrics_csv:
    r = RepoInfo()
    r.name, r.lambdas, r.android, r.size, r.url, r.percentage, r.stars, r.android_in_url, r.android_in_description, r.description = l
    # repos.append(r)
    if int(r.lambdas) > 0:
        new_repo_metrics_csv.write("{}\t{}\t{}\t{}\n".format(r.name, r.lambdas, r.size, r.url))
    

# print(repos[100].get_line())

new_repo_metrics_csv.close()

