import json
from utils import RepoProcessing, years
import matplotlib.pyplot as plt
from matplotlib import colors


def generate_postprocess_info(repo_names):
    with open(repo_names, "r") as f:
        for l in f.readlines():
            repo_name = l.strip()
            if repo_name in ["ansj_seg"]:
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

# REPOS THAT HAVE LAMBDAS ON THEIR CURRENT STATE

# LAMBDAS/LOC
# info = [(9.4, 'Intro-To-RxJava'), (5.6000000000000005, 'vavr'), (4.8, 'failsafe'), (4.2, 'vert.x'), (3.2, 'useful-java-links'), (3.2, 'JFoenix'), (2.8000000000000003, 'SeeWeather'), (2.6, 'exchange'), (2.1999999999999997, 'android-oss'), (2.0, 'caffeine'), (2.0, 'PhotoNoter'), (1.7999999999999998, 'gs-collections'), (1.7999999999999998, 'Shuttle'), (1.6, 'spark'), (1.6, 'u2020'), (1.6, 'javaparser'), (1.4000000000000001, 'PocketHub'), (1.4000000000000001, 'ZhihuDailyPurify'), (1.4000000000000001, 'retrolambda'), (1.4000000000000001, 'AntennaPod'), (1.2, 'elasticsearch'), (1.2, 'bilibili-android-client'), (1.2, 'reark'), (1.2, 'LQRWeChat'), (1.2, 'stampede'), (1.0, 'android-architecture-components'), (1.0, 'hsweb-framework'), (1.0, 'LeafPic'), (1.0, 'disunity'), (1.0, 'MaterialChipsInput'), (1.0, 'materialistic'), (0.8, 'material-dialogs'), (0.8, 'presto'), (0.8, 'metrics'), (0.8, 'Meizhi'), (0.8, 'blade'), (0.8, 'Carbon'), (0.8, 'spring-data-examples'), (0.6, 'spring-boot'), (0.6, 'async-http-client'), (0.6, 'keywhiz'), (0.6, 'crate'), (0.6, 'qualitymatters'), (0.4, 'java-design-patterns'), (0.4, 'spring-framework'), (0.4, 'dropwizard'), (0.4, 'che'), (0.4, 'mosby'), (0.4, 'cassandra'), (0.4, 'error-prone'), (0.4, 'hibernate-orm'), (0.4, 'requery'), (0.4, 'Terasology'), (0.4, 'ChipsLayoutManager'), (0.4, 'spring-cloud-microservice-example'), (0.4, 'nucleus'), (0.2, 'Android-CleanArchitecture'), (0.2, 'Signal-Android'), (0.2, 'bazel'), (0.2, 'agera'), (0.2, 'auto'), (0.2, 'druid-io__druid'), (0.2, 'HikariCP'), (0.2, 'lombok'), (0.2, 'scribejava'), (0.2, 'CoreNLP'), (0.2, 'redisson'), (0.2, 'MVPArms'), (0.2, 'orientdb'), (0.2, 'CtCI-6th-Edition'), (0.2, 'VirtualApp'), (0.2, 'AlgoDS'), (0.2, 'StickerCamera'), (0.2, 'checkstyle'), (0.2, 'jersey'), (0.2, 'MinecraftForge'), (0.2, 'spring-security'), (0.2, 'wildfly'), (0.2, 'springboot-learning-example'), (0.2, 'helios'), (0.2, 'rest.li'), (0.2, 'jetty.project'), (0.2, 'qksms'), (0.2, 'MovieGuide'), (0.2, 'hbase'), (0.2, 'RxGalleryFinal'), (0.2, 'camel'), (0.2, 'skywalking'), (0.2, 'ReactiveNetwork'), (0.0, 'guava'), (0.0, 'guice'), (0.0, 'j2objc'), (0.0, 'acra'), (0.0, 'hadoop'), (0.0, 'processing'), (0.0, 'dbeaver'), (0.0, 'openhab1-addons'), (0.0, 'quasar'), (0.0, 'elasticsearch-sql'), (0.0, 'feign'), (0.0, 'aws-sdk-java'), (0.0, 'twitter4j'), (0.0, 'jOOQ'), (0.0, 'javacv'), (0.0, 'GCViewer'), (0.0, 'spring-loaded'), (0.0, 'tomcat'), (0.0, 'ninja'), (0.0, 'byte-buddy'), (0.0, 'undertow'), (0.0, 'immutables'), (0.0, 'cucumber-jvm'), (0.0, 'hive'), (0.0, 'bitcoinj'), (0.0, 'spring-cloud-netflix'), (0.0, 'jackson-databind'), (0.0, 'nutch'), (0.0, 'syncany')]

# info = info[:20]
# x_data = [i+1 for i in range(len(info))]
# y_data = [x[0] for x in info]
# plt.axes().set_xticks(x_data)
# plt.axes().set_xticklabels([x[1] for x in info])
# for tick in plt.axes().get_xticklabels():
#     tick.set_rotation(90)
# plt.plot(x_data, y_data,'black')
# plt.tight_layout()
# plt.ylabel('Lambdas per 200 lines of code')
# plt.title('Lambdas per line of code')
# plt.show()

# REPOS THAT DROPPED
# repo_names = "eliminated.csv"
# repos_x, repos_y, repos_names = get_plotting_info(repo_names)
# l = 0
# r = 1
# repos_x = repos_x[l:r]
# repos_y = repos_y[l:r]
# repos_names = repos_names[l:r] 
# filtered = {"querydsl", "voldemort", "cSploit__android", "jitsi", "Hystrix", "DroidPlugin", "atmosphere", "Mybatis-PageHelper", "SmartCropper"  }
# los_true = ['zxing', 'SmartRefreshLayout', 'RxPermissions', 'easypermissions', 'android-classyshark', 'robolectric', 'NoHttp', 'AndPermission', 'sugar', 'MaterialIntroView', 'aesthetic']
# los_false = ['RxJava', 'ansj_seg', 'alluxio', 'Activiti', 'SwipeRecyclerView', 'hazelcast', 'PhotoPicker', 'SearchView', 'java-algorithms-implementation', 'jmonkeyengine', 'jeromq']
# colors = ["black", "gray", "rosybrown", "firebrick", "red", "darksalmon", "sienna", "sandybrown", "tan", "gold", "darkkhaki", "olivedrab", "chartreuse", "darkgreen", "mediumspringgreen", "lightseagreen", "deepskyblue", "navy", "blue", "mediumpurple", "darkorchid", "m", "mediumvioletred", "palevioletred"]
# c = 0
# for i in range(len(repos_names)):
#     if repos_names[i] not in filtered and repos_names[i] in los_true:
#         color = colors[c]
#         c += 1
#         plt.plot(repos_x[i], repos_y[i], color, label=repos_names[i] +" (A)")
#     elif repos_names[i] not in filtered and repos_names[i] in los_false:
#         color = colors[c]
#         c += 1
#         plt.plot(repos_x[i], repos_y[i], color, label=repos_names[i])
#   #  release date
# plt.plot([1394755200000], [0], marker='x', markersize=5, color='gray')
# plt.axes().set_xticks([tick[1] for tick in years()])
# plt.axes().set_xticklabels([label[0] for label in years()])
# plt.xlabel('Year')
# plt.ylabel('Amount of Lambdas')
# plt.legend(loc='upper left', labelspacing=0)
# plt.title('Lambda usage evolution in RxJava')
# plt.show()
