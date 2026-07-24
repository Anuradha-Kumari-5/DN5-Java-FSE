# Hands-on Lab: Branching and Merging in Git / GitLab

## Objectives
- Explain branching and merging
- Explain how to create a branch in GitLab
- Explain how to create a merge request in GitLab
- Construct a branch, make changes in it, and merge it back into `master` (trunk)

## Prerequisites
- Git environment set up (Git Bash)
- P4Merge installed and configured as the visual diff/merge tool
- A local Git repository connected to a remote GitLab repository

## What is Branching and Merging?
A **branch** is an independent line of development that lets you work on a
feature, fix, or experiment without affecting the main codebase (`master`/
`main`, also called the **trunk**). Once the work on a branch is complete and
tested, it is **merged** back into the trunk, combining both histories.

This workflow keeps `master` stable while allowing parallel work.

---

## Part A: Branching

### Step 1 — Create a new branch
```bash
git branch GitNewBranch
```
This creates the branch but does **not** switch to it yet.

### Step 2 — List all local and remote branches
```bash
git branch -a
```
Sample output:
```
  GitNewBranch
* master
```
The `*` marks the branch you are currently on (HEAD). Notice `master` is
still active even though `GitNewBranch` now exists.

### Step 3 — Switch to the new branch and add files
```bash
git checkout GitNewBranch
```
Output:
```
Switched to branch 'GitNewBranch'
```
Now create some sample files with content:
```bash
echo "console.log('This is a new feature added in GitNewBranch');" > feature.js
echo "This file was added while working on GitNewBranch." > feature.txt
```

### Step 4 — Commit the changes to the branch
```bash
git add .
git commit -m "Add feature.js and feature.txt on GitNewBranch"
```
Sample output:
```
[GitNewBranch 3cd37a0] Add feature.js and feature.txt on GitNewBranch
 2 files changed, 3 insertions(+)
 create mode 100644 feature.js
 create mode 100644 feature.txt
```

### Step 5 — Check status
```bash
git status
```
Output:
```
On branch GitNewBranch
nothing to commit, working tree clean
```
This confirms all changes on the branch are committed.

### (GitLab) Creating the branch on GitLab
If working through the GitLab UI instead of / in addition to Git Bash:
1. Open your project in GitLab.
2. Go to **Repository → Branches**.
3. Click **New branch**.
4. Enter `GitNewBranch` as the branch name and select `master` as the source.
5. Click **Create branch**.
Alternatively, push the locally created branch so it appears in GitLab:
```bash
git push -u origin GitNewBranch
```

---

## Part B: Merging

### Step 1 — Switch back to master
```bash
git checkout master
```
Output:
```
Switched to branch 'master'
```
*(Optional, to make the exercise realistic: make a small unrelated change on
master too, e.g., edit `README.md` and commit it, so master and the branch
have diverged.)*

### Step 2 — List differences between trunk and branch (CLI)
```bash
git diff master GitNewBranch
```
Sample output:
```
diff --git a/feature.js b/feature.js
new file mode 100644
index 0000000..0821a57
--- /dev/null
+++ b/feature.js
@@ -0,0 +1 @@
+console.log('This is a new feature added in GitNewBranch');

diff --git a/feature.txt b/feature.txt
new file mode 100644
index 0000000..dd94e04
--- /dev/null
+++ b/feature.txt
@@ -0,0 +1,2 @@
+This file was added while working on GitNewBranch.
+It demonstrates a change made in a feature branch before merging.
```
This shows, line by line, exactly what changed between the two branches.

### Step 3 — View the visual difference using P4Merge
```bash
git difftool -t p4merge master GitNewBranch
```
This launches P4Merge, showing the two branches side-by-side with additions
and deletions color-highlighted — useful for reviewing larger or more complex
changes visually rather than reading raw diff text.

### Step 4 — Merge the branch into the trunk
```bash
git merge GitNewBranch --no-ff -m "Merge GitNewBranch into master"
```
Sample output:
```
Merge made by the 'ort' strategy.
 feature.js  | 1 +
 feature.txt | 2 ++
 2 files changed, 3 insertions(+)
 create mode 100644 feature.js
 create mode 100644 feature.txt
```
> `--no-ff` forces a merge commit even if a fast-forward is possible, which
> keeps a clear record in history that a branch was merged.

### (GitLab) Creating a Merge Request instead of merging locally
In real projects you typically don't merge locally — you push the branch and
raise a **Merge Request (MR)** so teammates can review the change first:
1. Push the branch: `git push -u origin GitNewBranch`
2. In GitLab, go to **Merge Requests → New merge request**.
3. Select `GitNewBranch` as the source branch and `master` as the target.
4. Add a title/description, assign a reviewer if needed, and click
   **Create merge request**.
5. Once approved, click **Merge** in the GitLab UI.

### Step 5 — Observe the log after merging
```bash
git log --oneline --graph --decorate --all
```
Sample output:
```
*   d05b410 (HEAD -> master) Merge GitNewBranch into master
|\
| * 3cd37a0 (GitNewBranch) Add feature.js and feature.txt on GitNewBranch
* | cc416d6 Update README on master
|/
* c497685 Initial commit on master
```
The graph clearly shows:
- The two branches diverging from the initial commit
- Independent commits on each branch
- The merge commit joining both histories back together

### Step 6 — Delete the branch after merging
Once merged, the branch is no longer needed locally:
```bash
git branch -d GitNewBranch
```
Output:
```
Deleted branch GitNewBranch (was 3cd37a0).
```
> Use `-d` (safe delete) — Git will refuse to delete if the branch has
> unmerged changes. Use `-D` only if you intentionally want to force-delete.

If the branch was pushed to GitLab, delete it there too:
```bash
git push origin --delete GitNewBranch
```
(In the GitLab UI, this also happens automatically if you tick **Delete
source branch** when merging the MR.)

### Verify final status
```bash
git status
```
```
On branch master
nothing to commit, working tree clean
```
```bash
git branch -a
```
```
* master
```
Both confirm the branch is fully merged and removed, and `master` is clean
and up to date.

---

## Verification Checklist
| Step | Command | Expected Result |
|---|---|---|
| Branch created | `git branch -a` | `GitNewBranch` and `master` listed, `*` on current branch |
| Changes committed on branch | `git status` (on GitNewBranch) | "nothing to commit, working tree clean" |
| Diff reviewed | `git diff master GitNewBranch` | Shows added files/lines |
| Merge completed | `git log --oneline --graph --decorate --all` | Shows merge commit joining both branches |
| Branch deleted | `git branch -a` | Only `master` remains |
| Final state clean | `git status` | "nothing to commit, working tree clean" |

## Notes
- `git branch <name>` only creates a branch; `git checkout <name>` (or
  `git switch <name>`) is required to move onto it.
- `git checkout -b <name>` combines both steps into one command.
- Prefer Merge Requests over local merges when working on a shared GitLab
  repository, so changes get reviewed before joining `master`.
