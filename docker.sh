# 获取脚本所在目录
basedir=$(cd "$(dirname "$0")"; pwd)
# 进入项目根目录
cd "$basedir"

# 打包
mvn clean package -Pprd -DskipTests

# 构建镜像
docker build -f ./social-gateway/Dockerfile -t wangtao/social-gateway:1.0 ./social-gateway
docker build -f ./social-biz/social-user/Dockerfile -t wangtao/social-user:1.0 ./social-biz/social-user
docker build -f ./social-biz/social-square/Dockerfile -t wangtao/social-square:1.0 ./social-biz/social-square
docker build -f ./social-biz/social-file-storage/Dockerfile -t wangtao/social-file-storage:1.0 ./social-biz/social-file-storage