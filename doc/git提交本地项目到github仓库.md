# git提交本地项目到github仓库

标签（空格分隔）： git github

---

## 创建好本地仓库
MyEclipse中创建新的项目AutoDeployTool

## git初始化
通过git bash进入AutoDeployTool目录，执行`git init`

## 关联远程仓库
git remote add origin git@github.com:zhuangbiman/AutoDeployTool.git

此时直接向远程仓库push代码是会报错，一般需要先pull一下，可以理解为更新一下

git pull origin master



## 从远程仓库clone代码至本地##

git clone git@github.com:zhuangbiman/AutoDeployTool.git




## 提交代码
- `git add *`, 向“缓存区”中提交本地内容
- `git commit -m "comments"`， 提交代码
- `git push -u origin master`，向远程仓库提交代码


  

## 分支学习##

