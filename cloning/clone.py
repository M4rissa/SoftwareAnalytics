from git import Repo
from shutil import copyfile
from os import remove, listdir

# FROM THE LIST, THERE ARE 13 THAT ARE A REPETITION SO I MANUALLY CLEANED
# THEM, NEXT TIME USE URL AS PATH NOT NAME
# IN THE NAMES THERE ARE 184 MATCHES TO ANDROID

# names = set()
# with open("repo_links_all.csv", "r") as f:
#     bad = set()
#     for l in f.readlines():
#         l = l.strip().split("/")[-1].lower()
#         if l not in names:
#             names.add(l)
#         else:
#             bad.add(l)
# print(bad)

# with open("repo_links_all.csv", "r") as f:
#     c = 0
#     for l in f.readlines():
#         c += 1
#         url = l.strip()
#         name = l.strip().split("/")[-1].lower()
#         if name in bad:
#             new_path = l.strip().split("/")[-2] + "/" + l.strip().split("/")[-1]
#             print(c, new_path)
#             try:
#                 # clones main branch
#                 r = Repo.clone_from(url, "./repos_all/{}".format(new_path))
#             except Exception as e:
#                 print(e)
#                 print("error with", "./repos_all/{}".format(new_path))


# CLONE MAIN BRANCH OF REPOSITORIES
# repo_paths = []  # path of the repos, gets filled when downloading

# with open("repo_links_all.csv", "r") as f:
#     for l in f.readlines():
#         url = l.strip()
#         repo_name = url.split("/")[-1]
#         path = "./repos_all/{}".format(repo_name)
#         repo_paths.append(path)
#         try:
#             r = Repo.clone_from(url, path)  # clones main branch
#         except Exception as e:
#             print(e)
#             print("error with", path)

# CREATE BASH SCRIPT TO GET BRANCHES FOR EVERY REPOSITORY

# with open("permit_and_branches.sh", "w") as f:
#     for path in repo_paths:
#         repo_name = path.split("/")[-1]
#         script_path = "{}/branches.sh".format(path)
#         copyfile("branches.sh", script_path)
#         f.write("cd repos\n")
#         f.write("cd {}\n".format(repo_name))
#         f.write("chmod +x branches.sh\n")
#         f.write("echo DESCARGANDO {}\n".format(repo_name))
#         f.write("sh branches.sh\n")
#         f.write("cd ..\n")
#         f.write("cd ..\n")

# WAIT FOR USER TO EXECUTE BASH SCRIPT

# wait = input(
#     "execute sh permit_and_branches.sh on ur bash to download all branches")


# REMOVE BASH FILES

# remove("permit_and_branches.sh")
# for path in repo_paths:
#     script_path = "{}/branches.sh".format(path)
#     remove(script_path)
