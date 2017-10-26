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


repos = []

new_repo_metrics_csv = open('l.csv', 'w', encoding="utf-8")
repo_metrics_csv = reader(
    open('repo_metrics.csv', "r", encoding='utf-8'), delimiter='\t')

for l in repo_metrics_csv:
    r = RepoInfo()
    r.name, r.lambdas, r.lambdas_per_loc, r.android, r.loc, r.url, r.percentage, r.stars, r.android_in_url, r.android_in_description, r.size, r.description = l
    if float(r.lambdas) > 0:
        new_repo_metrics_csv.write(
            "{}\t{}\t{}\n".format(r.name, r.lambdas, r.url))


new_repo_metrics_csv.close()
