from shutil import rmtree

keep = set()
to_keep = "with_lambdas.csv"
population = "repo_names.csv"
with open(to_keep, "r") as f:
    for l in f.readlines():
        keep.add(l.strip())
with open(population, "r") as f:
    for l in f.readlines():
        line = l.strip()
        if line not in keep:
            try:
                rmtree(line)
            except:
                print(line)
