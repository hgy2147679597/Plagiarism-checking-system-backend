FROM gradle:8.4-jdk11-jammy as builder

# 设置工作目录
WORKDIR /app

# 复制整个项目目录到容器中
COPY . .

# 进入sztu_check目录
WORKDIR /app/sztu_check

# 构建Spring Boot应用程序，使用本地Maven
RUN gradle bootJar --info

FROM eclipse-temurin:11-jre-jammy as environment

# 设置工作目录
WORKDIR /app

COPY scripts/model.py scripts/model.py

#COPY model/doc2vec_model.model model/doc2vec_model.model

COPY --from=builder /app/sztu_check/build/libs/sztu_check-1.0-SNAPSHOT.jar ./sztu_checking.jar

RUN echo "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy main restricted universe multiverse" > /etc/apt/sources.list
RUN echo "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ jammy-backports main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://security.ubuntu.com/ubuntu/ jammy-security main restricted universe multiverse" >> /etc/apt/sources.list

# 安装build-tools，因为alpine不自带
RUN apt update && apt install python3 python3-pip -y

# 复制Python依赖文件（requirements.txt）到容器中（如果需要的话）
COPY ./scripts/requirements.txt .

# 安装Python依赖库（如果需要的话）
RUN pip3 install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt

# 暴露应用程序的端口（如果你的Spring Boot应用程序需要使用8080端口，否则根据实际情况修改）
EXPOSE 8080

# 启动Spring Boot应用程序
ENTRYPOINT java -jar ./sztu_checking.jar
