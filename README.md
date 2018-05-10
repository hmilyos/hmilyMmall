
后面提交代码 git命令： 
提交代码 
    git pull origin master; 
    git add -A; git commit -m "内容";
    git push -u origin master;

创建分支：git branch newBranchName;
 把新创建的分支提交到远程仓库： git push origin newBranchName;
  查看目前仓库的所有分支：git branch -a; 
  从master分支切换到newBranchName分支： git checkout newBranchName; 
  再把当前修改的提交到newBranchName分支：
    git commit -m "info";
    git push -u origin newBranchName;