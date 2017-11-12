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

· 用于修改只合入但分支的bug或者新功能

1. git checkout -b branch123 
2. 修改代码
3. git add/git commit
4. git checkout master 切换回目标分支（不一定是master，可以是任意分支）
5. git merge branch123 代码合入目标分支
6. git push origin master 推送到远程代码仓库
7. git branch -d branch123 删除无用分支

· 在进行一个任务的同时插入了紧急开发任务

1. git checkout -b temp1
2. git add / git commit若干
3. 插入临时开发任务
4. git checkout master
5. git checkout -b emergency
6. 开发临时任务并commit到emergency分支
7. git checkout master
8. git merge emergency
9. git push origin master
10. git branch -d emergency //紧急任务完成，删除紧急分支emergency
11. git checkout temp1  //切换回temp1分支
12. git  rebase master  // rebase 相当于新的base，git帮助你把commit节点重合一遍
13. git add/commit若干
14. git checkout master
15. git merge temp1   //合并temp1分支
16. git branch -d temp1 //删除temp1分支
17. git push origin master

