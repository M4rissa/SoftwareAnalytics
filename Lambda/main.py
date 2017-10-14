from utils import RepoProcessing, years
import matplotlib.pyplot as plt
from matplotlib import colors


repos_names = []
repos_x = []
repos_y = []
with open("repo_names.csv", "r") as f:
    for l in f.readlines():
        repo_name = l.strip()
        repos_names.append(repo_name)
        repo_csv = "./preprocess/{}_mod.csv".format(repo_name)
        repo = RepoProcessing(repo_csv)
        repo.process()
        x, y = repo.get_plot_info()
        repos_x.append(x)
        repos_y.append(y)

colors = list(colors.cnames)
for i in range(len(repos_names)):
    plt.plot(repos_x[i], repos_y[i], colors[i], label=repos_names[i])
plt.plot([1394755200000], [0], colors[i + 1])  #  release date
plt.axes().set_xticks([tick[1] for tick in years()])
plt.axes().set_xticklabels([label[0] for label in years()])
plt.xlabel('Year')
plt.ylabel('Amount of Lambdas')
plt.title('Lambda usage evolution in the 20 most popular Java projects')
plt.legend()
plt.show()
