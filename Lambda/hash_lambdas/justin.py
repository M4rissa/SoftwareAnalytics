from json import load
repos_with_lambdas = []
with open("with_lambdas.csv", "r") as f:
    for l in f.readlines():
        repos_with_lambdas.append(l.strip())
for r in repos_with_lambdas:
    json_path = "./postprocess/{}.json".format(r)
    j = load(open(json_path, "r"))
    jj = {x: y for x, y in j.items() if int(x) >= 1394755200000}
    new_file = open("./hash_lambdas/{}_hl.csv".format(r), "w")
    with open("./hash_time/{}_hash.csv".format(r)) as f:
        for l in f.readlines():
            t, h = l.strip().split(",")
            if str(t) in jj:
                new_file.write("{},{}\n".format(h, jj[str(t)]))
    new_file.close()
