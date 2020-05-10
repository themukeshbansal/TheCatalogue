# All of Git

## Quick Use Case Scenarios

### Bring a local folder to remote git repo

```git
git init
git add .
git commit -m "Fist commit"
git remote add origin <remote repository URL>
git remote -v
```

## TroubleShooting

1. Cannot rewrite branches: You have unstaged changes
You add and commit the tracked changes or you stash them.

2. refusing to merge unrelated histories.
You can use --allow-unrelated-histories to force the merge to happen.
