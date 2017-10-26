from csv import reader

# name, lambdas, lambdas_per_loc, android, loc, url, percentage, stars, android_in_url, android_in_description, size, description
 
# size represents in kb, I will need to calculate loc with repodriller once the other thing finishes running


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

    def calculate_metrics(self):
        if "android" in self.url.lower():
            self.android_in_url = True
        if "android" in self.description.lower():
            self.android_in_description = True
        if self.android_in_url or self.android_in_description:
            self.android = True
        self.lambdas_per_loc = round(self.lambdas / self.loc, 3)

    def get_line(self):
        data = (self.name, self.lambdas, self.lambdas_per_loc, self.android, self.loc, self.url, self.percentage,
                self.stars, self.android_in_url, self.android_in_description, self.size, self.description)
        return "{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(*data)


repos = []

repo_metrics = open('repo_metrics.csv', 'w', encoding="utf-8")
names_csv = reader(open('repo_names.csv', "r", encoding='utf-8'))
names_lambdas_loc_csv = reader(
    open('name_lambda_loc.csv', "r", encoding='utf-8'))
links_percentage_stars_csv = reader(
    open('repos_requirements.csv', "r", encoding='utf-8'))
size_description_csv = reader(
    open('size_description.csv', "r", encoding='utf-8'), delimiter='\t')

for l in names_csv:
    repo = RepoInfo()
    repo.name = l[0]
    repos.append(repo)

c = 0
for l in names_lambdas_loc_csv:
    repo = repos[c]
    c += 1
    name, lambdas, loc = l
    repo.lambdas = int(lambdas)
    repo.loc = int(loc)
c = 0
for l in links_percentage_stars_csv:
    repo = repos[c]
    c += 1
    link, percentage, stars = l
    repo.url = link
    repo.percentage = float(percentage)
    repo.stars = int(stars)

c = 0
for l in size_description_csv:
    repo = repos[c]
    c += 1
    name, size, description = l
    repo.size = int(size)
    repo.description = description

for repo in repos:
    repo.calculate_metrics()
    repo_metrics.write(repo.get_line())

repo_metrics.close()
