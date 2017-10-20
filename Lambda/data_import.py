from csv import reader

# name, lambdas, android, url, percentage, stars, size, android_in_url, android_in_description, description

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

    def get_line(self):
        data = (self.name, self.lambdas, self.android, self.url, self.percentage,
                self.stars, self.size, self.android_in_url, self.android_in_description, self.description)
        return "{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(*data)

repos = []

# new_repo_metrics_csv = open('new_repo_metrics.csv', 'w', encoding="utf-8")
repo_metrics_csv = reader(
    open('repo_metrics.csv', "r", encoding='utf-8'), delimiter='\t')

for l in repo_metrics_csv:
    r = RepoInfo()
    r.name, r.lambdas, r.android, r.url, r.percentage, r.stars, r.size, r.android_in_url, r.android_in_description, r.description = l
    repos.append(r)

print(repos[100].get_line())

# new_repo_metrics_csv.close()

