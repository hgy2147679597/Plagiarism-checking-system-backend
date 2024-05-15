# 查重系统-后端

#### 介绍
{**以下是 Gitee 平台说明，您可以替换此简介**
Gitee 是 OSCHINA 推出的基于 Git 的代码托管平台（同时支持 SVN）。专为开发者提供稳定、高效、安全的云端软件开发协作平台
无论是个人、团队、或是企业，都能够用 Gitee 实现代码托管、项目管理、协作开发。企业项目请看 [https://gitee.com/enterprises](https://gitee.com/enterprises)}

#### 软件架构
springboot + mybatisplus + mysql + jwt


#### 具体需求

#### 1.登录管理
1. 登录分为用户登录和管理员登录，系统采用jwt进行登录认证
2. 用户登录需要通过调用学校认证平台判断是否实名，然后解析返回的用户信息，通过本系统的jwt工具生成token
#### 2.文件管理
1. 文件上传，文件第一次上传审核不合格后需要有重新上传功能，重新上传将覆盖上一次上传的文件
2. 文件展示，用户可以看到自己历史上传的文件信息，管理员可以看到所有上传的文件信息，文件按照上传时间排序展示
3. 文件查重，管理员对某个文件内容查重，通过算法将其与其他文件内容进行分析，筛选出重复率较高的样本
#### 3.管理员管理
1. 管理员的增删改查

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
