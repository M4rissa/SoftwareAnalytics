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


# LINES FOR THE 20 MOST POP
repo_names = "top.csv"
repos_x, repos_y, repos_names = get_plotting_info(repo_names)
l = 0
r = 20
repos_x = repos_x[l:r]
repos_y = repos_y[l:r]
repos_names = repos_names[l:r] 
filtered = {"querydsl", "voldemort", "cSploit__android", "jitsi", "Hystrix", "DroidPlugin", "atmosphere", "Mybatis-PageHelper", "SmartCropper"  }
los_true = ['zxing', 'SmartRefreshLayout', 'RxPermissions', 'easypermissions', 'android-classyshark', 'robolectric', 'NoHttp', 'AndPermission', 'sugar', 'MaterialIntroView', 'aesthetic']
los_false = ['RxJava', 'ansj_seg', 'alluxio', 'Activiti', 'SwipeRecyclerView', 'hazelcast', 'PhotoPicker', 'SearchView', 'java-algorithms-implementation', 'jmonkeyengine', 'jeromq']
colors = ["black", "gray", "rosybrown", "firebrick", "red", "darksalmon", "sienna", "sandybrown", "tan", "gold", "darkkhaki", "olivedrab", "chartreuse", "darkgreen", "mediumspringgreen", "lightseagreen", "deepskyblue", "navy", "blue", "mediumpurple", "darkorchid", "m", "mediumvioletred", "palevioletred"]
c = 0
for i in range(len(repos_names)):
    if repos_names[i] in filtered:
        print("problem")
    plt.plot(repos_x[i], repos_y[i], colors[i], label=repos_names[i])
plt.plot([1394755200000], [0], marker='x', markersize=5, color='gray')
plt.axes().set_xticks([tick[1] for tick in years()])
plt.axes().set_xticklabels([label[0] for label in years()])
plt.xlabel('Year')
plt.ylabel('Amount of Lambdas')
plt.legend(loc='upper left', labelspacing=0)
plt.title('Lambda usage evolution in the top 20 repos that still use lambdas')
plt.show()







# REPOS THAT HAVE LAMBDAS ON THEIR CURRENT STATE TIME OF INTRO
# x = [1397208828000, 1398736874000, 1398895381000, 1399028152000, 1399472235000, 1399740176000, 1400591967000, 1402380076000, 1409590486000, 1410113076000, 1410221490000, 1411561393000, 1412959208000, 1413826371000, 1415350132000, 1417682560000, 1418388516000, 1418509886000, 1418847981000, 1420651158000, 1424557065000, 1425990741000, 1426109739000, 1426687867000, 1427415225000, 1427839787000, 1428686656000, 1428737717000, 1430474465000, 1430657527000, 1434836671000, 1435186081000, 1435625821000, 1435813216000, 1436321285000, 1436502433000, 1438095405000, 1439133951000, 1439912331000, 1441832069000, 1442345177000, 1443190289000, 1445819396000, 1446249592000, 1448250820000, 1448612637000, 1448720072000, 1449839382000, 1450956417000, 1452068737000, 1453454713000, 1453871412000, 1454365636000, 1454461720000, 1454965610000, 1455203047000, 1460234423000, 1460532042000, 1461119147000, 1461300052000, 1462623493000, 1462954701000, 1463571063000, 1463680376000, 1464337689000, 1466170298000, 1467204341000, 1467844974000, 1467893367000, 1468598044000, 1470732213000, 1470780058000, 1473122795000, 1473805926000, 1475305760000, 1477606557000, 1477902141000, 1480276723000, 1480779502000, 1481451194000, 1482542748000, 1483487885000, 1485421426000, 1486032739000, 1486552513000, 1486955778000, 1488024724000, 1489856493000, 1490041088000, 1490109893000, 1490214116000, 1490554792000, 1491559465000, 1492124276000, 1492732328000, 1492961745000, 1494688761000, 1496393368000, 1496996760000, 1497460496000, 1499448170000, 1501084414000, 1505868622000, 1506658945000, 1507357820000]
# y = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105]
# plt.axes().set_xticks([tick[1] for tick in years()])
# plt.axes().set_xticklabels([label[0] for label in years()])
# plt.plot(x, y,'black')
# #plt.tight_layout()
# plt.plot([1394755200000], [0], marker='x', markersize=5, color='gray')
# plt.xlabel('Time')
# plt.ylabel('Amount of projects that introduced lambdas')
# plt.title('Time of lambdas introduction into the project')
# plt.show()
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
