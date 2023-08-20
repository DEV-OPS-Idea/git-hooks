# git-hooks
commit_msg=$(cat "${1:?Missing commit message file}")
echo $commit_msg
java -jar git-jira-integration-1.0-SNAPSHOT-jar-with-dependencies.jar "$commit_msg"
