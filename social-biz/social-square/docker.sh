# 获取脚本所在目录
basedir=$(cd "$(dirname "$0")"; pwd)
# 进入项目根目录
cd "$basedir/../../"

# 打包指定工程, -am: 同时构建所有依赖的模块, 否则会报错, 因为social-common-core没有安装到本地
mvn clean package -Pprd -DskipTests -pl social-biz/social-square -am

cd "$basedir"
# 构建镜像
docker build -t wangtao/social-square:1.0 .