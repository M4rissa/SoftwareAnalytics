from git import Repo
from shutil import copyfile
from os import remove

repo_paths = []  # path of the repos, gets filled when downloading

# CLONE MAIN BRANCH OF REPOSITORIES

with open("repo_links.csv", "r") as f:
    for l in f.readlines():
        url = l.strip()
        path = "./repos/{}".format(url.split("/")[-1])
        repo_paths.append(path)
        r = Repo.clone_from(url, path)  # clones main branch

# CREATE BASH SCRIPT TO GET BRANCHES FOR EVERY REPOSITORY

with open("permit_and_branches.sh", "w") as f:
    for path in repo_paths:
        repo_name = path.split("/")[-1]
        script_path = "{}/branches.sh".format(path)
        copyfile("branches.sh", script_path)
        f.write("cd repos\n")
        f.write("cd {}\n".format(repo_name))
        f.write("chmod +x branches.sh\n")
        f.write("echo DESCARGANDO {}\n".format(repo_name))
        f.write("sh branches.sh\n")
        f.write("cd ..\n")
        f.write("cd ..\n")

# WAIT FOR USER TO EXECUTE BASH SCRIPT

wait = input(
    "execute sh permit_and_branches.sh on ur bash to download all branches")


# REMOVE BASH FILES

remove("permit_and_branches.sh")
for path in repo_paths:
    script_path = "{}/branches.sh".format(path)
    remove(script_path)
