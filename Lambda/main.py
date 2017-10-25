import json
from utils import RepoProcessing, years
import matplotlib.pyplot as plt
from matplotlib import colors


def generate_postprocess_info(repo_names):
    with open(repo_names, "r") as f:
        for l in f.readlines():
            repo_name = l.strip()
            if repo_name not in ["ansj_seg", "HanLP", "platform_frameworks_base"]:
                repo_csv = "./preprocess/{}_mod.csv".format(repo_name)
                repo_output = "./postprocess/{}.json".format(repo_name)
                repo = RepoProcessing(repo_csv, repo_output)
                repo.process()
                repo.generate_plot_info()


def get_plotting_info(repo_names):
    repos_names = []
    repos_x = []
    repos_y = []
    with open(repo_names, "r") as f:
        for l in f.readlines():
            repo_name = l.strip()
            repos_names.append(repo_name)
            repo_output = "./postprocess/{}.json".format(repo_name)
            with open(repo_output) as jf:
                data = json.load(jf)
                data = list(zip(*filter(lambda x: int(x[0]) >= years()[
                    0][1], sorted(data.items(), key=lambda x: int(x[0])))))
            if data:
                repo_x, repo_y = data
                repo_x = [int(x) for x in repo_x]
                repos_x.append(repo_x)
                repos_y.append(repo_y)
            else:
                repos_x.append([])
                repos_y.append([])
        return repos_x, repos_y, repos_names


repo_names = "repo_names.csv"
generate_postprocess_info(repo_names)

# repos_x, repos_y, repos_names = get_plotting_info(repo_names)

# good_colors = ['r', 'b', 'g', 'm', 'c']
# colors = list(colors.cnames)
# # c = 0
# for i in range(len(repos_names)):
#     # if repos_names[i] in ["RxJava", "elasticsearch", "spring-framework", "spring-boot", "PocketHub"]:
#     #     color = good_colors[c]
#     #     c += 1
#     # else:
#     #     color = colors[i]
#     color = good_colors[i]
#     plt.plot(repos_x[i], repos_y[i], color, label=repos_names[i])
# plt.plot([1394755200000], [0], 'g')  #  release date
# plt.axes().set_xticks([tick[1] for tick in years()])
# plt.axes().set_xticklabels([label[0] for label in years()])
# plt.xlabel('Year')
# plt.ylabel('Amount of Lambdas')
# # plt.title('Lambda usage evolution in 22 popular Java projects')
# plt.legend()
# plt.show()
