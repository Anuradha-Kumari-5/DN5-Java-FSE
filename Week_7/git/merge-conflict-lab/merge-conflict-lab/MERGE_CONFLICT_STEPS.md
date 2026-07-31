# Hands-on Lab: Resolving Merge Conflicts (Git-T03-HOL_001)

Run these in Git Bash, inside your existing local repo, in order.

---

### 1. Verify master is in a clean state
```
git checkout master
git status
```
Expect: `nothing to commit, working tree clean`

### 2. Create branch "GitWork" and add hello.xml
```
git checkout -b GitWork
echo "<root><message>Hello from GitWork branch</message></root>" > hello.xml
git status
```

### 3. Update hello.xml content, observe status
```
echo "<root><message>Hello from GitWork branch - updated</message></root>" > hello.xml
git status
```
Expect: `hello.xml` shows as modified/untracked (depending on whether step 2 was committed yet).

### 4. Commit the changes to the branch
```
git add hello.xml
git commit -m "Add and update hello.xml in GitWork branch"
```

### 5. Switch to master
```
git checkout master
```

### 6. Add hello.xml to master with different content
```
echo "<root><message>Hello from master branch</message></root>" > hello.xml
git status
```

### 7. Commit the changes to master
```
git add hello.xml
git commit -m "Add hello.xml in master branch"
```

### 8. Observe the branch/commit graph
```
git log --oneline --graph --decorate --all
```
You'll see master and GitWork have diverged — each has its own commit touching `hello.xml`.

### 9. Check differences with git diff
```
git diff master GitWork -- hello.xml
```

### 10. Visualize differences with P4Merge (optional external tool)
```
git difftool --tool=p4merge master GitWork -- hello.xml
```
(Requires P4Merge installed and configured as a difftool: `git config --global diff.tool p4merge`)

### 11. Merge GitWork into master
```
git merge GitWork
```
Expect: `CONFLICT (add/add): Merge conflict in hello.xml`

### 12. Observe the conflict markup
```
cat hello.xml
```
You'll see:
```
<<<<<<< HEAD
<root><message>Hello from master branch</message></root>
=======
<root><message>Hello from GitWork branch - updated</message></root>
>>>>>>> GitWork
```

### 13. Resolve with a 3-way merge tool
```
git mergetool --tool=p4merge
```
In the tool: LOCAL = master's version, REMOTE = GitWork's version, BASE = common ancestor (empty here, since both added the file independently). Pick/combine the final content in the MERGED pane, then save and close.

If you don't have a GUI tool available, resolve manually: open `hello.xml`, delete the `<<<<<<<`, `=======`, `>>>>>>>` lines, keep the content you want, save.

### 14. Commit the resolved merge
```
git add hello.xml
git commit -m "Resolve merge conflict in hello.xml between master and GitWork"
```

### 15. Check status, add backup file to .gitignore
```
git status
echo "*.orig" >> .gitignore
echo "*.bak" >> .gitignore
```
(Merge tools often leave `.orig` backup files behind — these two lines ignore them.)

### 16. Commit the .gitignore update
```
git add .gitignore
git commit -m "Ignore merge tool backup files"
```

### 17. List all available branches
```
git branch -a
```

### 18. Delete the branch that was merged into master
```
git branch -d GitWork
```
(Use `-d`, not `-D` — `-d` only deletes if it's already fully merged, which confirms your merge succeeded.)

### 19. Observe the final log
```
git log --oneline --graph --decorate
```
You should see a single unified history on master, with the merge commit visible and `GitWork` gone from the branch list.
