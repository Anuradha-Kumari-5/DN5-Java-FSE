# Hands-on Lab: Ignoring Unwanted Files with `.gitignore`

## Objectives
- Explain `git ignore`
- Explain how to ignore unwanted files using `.gitignore`
- Implement the `.gitignore` mechanism to ignore unwanted files and folders

## What is `.gitignore`?
`.gitignore` is a plain text file placed at the root of a Git repository that
tells Git which files and folders it should **not** track. This is useful for
excluding things like log files, build artifacts, temporary files, IDE
settings, and credentials that shouldn't be committed to version control.

Git checks every file in the working directory against the patterns listed in
`.gitignore`. Any file or folder that matches a pattern is:
- Left out of `git status` (won't show as "untracked")
- Skipped by `git add .` / `git add -A`
- Never included in a commit (unless it was already tracked before being ignored)

## Files included in this folder
```
gitignore-lab/
├── .gitignore          # Ignore rules (*.log and log/ folder)
├── README.md           # Sample tracked file
├── app.js              # Sample tracked file
├── app.log             # Sample file that SHOULD be ignored
├── log/                # Sample folder that SHOULD be ignored
│   └── debug.log
└── LAB_INSTRUCTIONS.md # This file
```

## Step-by-Step Instructions

### Step 1: Initialize (or use existing) Git repository
If this folder is not already a Git repo, initialize it:
```bash
cd gitignore-lab
git init
```

### Step 2: Check current status (before ignoring)
Temporarily rename `.gitignore` to see what Git would track without it:
```bash
mv .gitignore .gitignore.bak
git status
```
You'll notice `app.log` and the `log/` folder show up as **untracked files**.

Restore the file:
```bash
mv .gitignore.bak .gitignore
```

### Step 3: Review the `.gitignore` rules
Open `.gitignore` (e.g., in Notepad++) and confirm it contains:
```
*.log
log/
```
- `*.log` → ignores any file ending in `.log`, anywhere in the repo
- `log/` → ignores the entire `log` folder and its contents

### Step 4: Verify with `git status`
```bash
git status
```
Expected result:
- `app.log` and `log/` are **not** listed as untracked
- `.gitignore`, `README.md`, and `app.js` **are** listed as untracked
  (assuming this is a fresh repo with no commits yet)

### Step 5: Stage and commit the tracked files
```bash
git add .
git commit -m "Initial commit with .gitignore configured"
```
Run `git status` again — it should say "nothing to commit, working tree clean,"
confirming `app.log` and `log/` were never staged.

### Step 6: Connect to your GitLab/GitHub remote repository
```bash
git remote add origin <your-remote-repo-url>
git branch -M main
git push -u origin main
```

### Step 7: Confirm on the remote
Open your GitLab/GitHub repository in the browser and confirm that:
- `app.log` is **not** present
- The `log/` folder is **not** present
- `.gitignore`, `README.md`, and `app.js` **are** present

## Verification Checklist
| Check | Expected Result |
|---|---|
| `git status` before commit | `app.log` and `log/` NOT listed as untracked |
| `git add .` then `git status` | `app.log` and `log/` NOT staged |
| After `git commit` | `app.log` and `log/` NOT in local repository history |
| After `git push` | `app.log` and `log/` NOT visible in remote GitLab repo |

## Notes
- If a file was already tracked by Git **before** you added it to `.gitignore`,
  adding the rule alone won't stop it from being tracked. You must remove it
  from tracking first:
  ```bash
  git rm --cached app.log
  ```
- `.gitignore` rules apply to the working directory going forward; they do not
  retroactively remove already-committed files from history.
