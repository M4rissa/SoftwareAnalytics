import requests
import time
from password import get_pass

USERNAME = 'tamycova'
PASSWORD = get_pass()


def calculate_java_percentage(dic):
    total = sum(dic.values())
    return round((dic["Java"] * 100) / total, 2)


def get(link):
    r = requests.get(link, auth=(USERNAME, PASSWORD))
    r.encoding = 'utf-8'
    return r


def get_next_page(r):
    ls = r.headers['Link']
    links = []
    in_link = False
    current_link = []
    for c in ls:
        if in_link:
            if c == ">":
                in_link = not in_link
                links.append("".join(current_link))
                current_link = []
            else:
                current_link.append(c)
        else:
            if c == "<":
                in_link = True
    return links[0]

# GET ALL REPOS FROM INITIAL REQUEST
# "html_url", "languages_url", "stargazers_count"

# counter = 0
# with open("repos.csv", "w") as f:
#     repos = set()
#     initial_request = 'https://api.github.com/search/repositories?q=language:java&sort=stars&order=desc&created:<2014-03-18&pushed>2016-03-18'
#     r = get(initial_request)
#     for i in r.json()["items"]:
#         repos.add(i["html_url"])
#         f.write("{0},{1},{2}\n".format(i["html_url"], i[
#                 "languages_url"], i["stargazers_count"]))
#     r_url = get_next_page(r)
#     in_loop = True
#     while in_loop:
#         time.sleep(5)
#         counter += 1
#         print(counter)
#         r = get(r_url)
#         if "items" in r.json():
#             for i in r.json()["items"]:
#                 if i["html_url"] in repos:
#                     in_loop = False
#                     break
#                 else:
#                     repos.add(i["html_url"])
#                     f.write("{0},{1},{2}\n".format(i["html_url"], i[
#                             "languages_url"], i["stargazers_count"]))
#         else:
#             print("error")
#             print(r)
#             print(r.headers)
#         r_url = get_next_page(r)


# FILTER BY JAVA LANGUAGE DATA
# 1020 repos, info of all repos given by init req
# all_repos = open("repos.csv", "r")
# #  instead of language link it has java % if > 90, 821
# filtered_repos = open("repos_requirements.csv", "w")
# # url links of repos
# to_clone = open("repo_links_all.csv", "w")
# # names of repos
# to_process = open("repos_names_all.csv", "w")
# p = open("p", "w")
# for line in all_repos.readlines():
#     path, languages, stars = line.strip().split(",")
#     percentage = calculate_java_percentage(get(languages).json())
#     p.write(str(percentage) + "\n")
#     if percentage >= 90:
#         name = path.split("/")[-1]
#         filtered_repos.write("{0},{1},{2}\n".format(path, percentage, stars))
#         to_clone.write("{}\n".format(path))
#         to_process.write("{}\n".format(name))
# all_repos.close()
# filtered_repos.close()
# to_clone.close()
# to_process.close()

# GET INFO THAT I WAS MISSING
# all repos
all_repos = open("repo_links_all.csv", "r")
# file where ill put missing info
missing_info = open("size_description.csv", "w",encoding="utf-8")
for line in all_repos.readlines():
    path = line.strip().split("/")
    link = "{0}/{1}".format(path[-2], path[-1])
    api_path = 'https://api.github.com/repos/{}'.format(link)
    j = get(api_path).json()
    missing_info.write("{0}\t{1}\t{2}\n".format(
        link, j["size"], j["description"]))
all_repos.close()
missing_info.close()