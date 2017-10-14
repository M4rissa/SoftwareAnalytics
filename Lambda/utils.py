def years():
    return [("2013", 1356998400000), ("2014", 1388534400000),
            ("2015", 1420070400000), ("2016", 1451606400000),
            ("2017", 1483228800000)]


class Modification:

    def __init__(self, date, mtype, old_path, new_path, lambdas):
        self.d = date
        self.t = mtype
        self.op = old_path
        self.np = new_path
        self.n = lambdas


class RepoProcessing:

    def __init__(self, repo_csv):
        self.repo_csv = open(repo_csv, "r")
        self.repo_state = dict()
        self.modifications = dict()
        self.lambdas_per_time = dict()
        self.dates = []

    def calculate_current_lambdas(self, dic):
        return sum(dic.values())

    def process(self):
        for l in self.repo_csv.readlines():
            line = l.strip().split(",")
            date, mtype, old_path, new_path, nl = line[1:]
            date = int(date)
            self.dates.append(date)
            nl = int(nl)
            if date not in self.modifications:
                self.modifications[date] = []
            self.modifications[date].append(Modification(
                date, mtype, old_path, new_path, nl))
        self.dates.sort()
        return None

    def get_plot_info(self):
        for date in self.dates:
            for mod in self.modifications[date]:
                if mod.t == 'ADD' or mod.t == 'MODIFY' or mod.t == 'COPY':
                    self.repo_state[mod.np] = mod.n
                elif mod.t == 'RENAME':
                    self.repo_state.pop(mod.op, "rename")
                    self.repo_state[mod.np] = mod.n
                elif mod.t == 'DELETE':
                    self.repo_state.pop(mod.op, "delete")
            self.lambdas_per_time[
                date] = self.calculate_current_lambdas(self.repo_state)
        data = list(zip(*filter(lambda x: x[0] >= years()[
            0][1], sorted(self.lambdas_per_time.items(), key=lambda x: x[0]))))
        if not data:
            return [], []
        return data
