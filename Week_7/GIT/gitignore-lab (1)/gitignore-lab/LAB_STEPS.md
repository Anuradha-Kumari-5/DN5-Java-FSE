# Git Ignore Hands-on Lab — Steps to Run

## Files in this folder
- `.gitignore` — ignores `*.log` files and the `log/` folder
- `app.log` — sample log file (should be ignored)
- `log/debug.log` — sample log inside the log folder (should be ignored)
- `app.js` — normal source file (should be tracked)

## Steps

1. Copy all these files into your existing Git repo's working directory
   (the folder where `.git` lives).

2. Check status BEFORE adding `.gitignore` (optional, just to see the difference):
   ```
   git status
   ```
   Without `.gitignore`, `app.log` and `log/` would show as untracked.

3. Make sure `.gitignore` is in place, then check status again:
   ```
   git status
   ```
   Expected output: `app.js` and `.gitignore` show as untracked/new files.
   `app.log` and `log/` do NOT appear at all — Git is ignoring them.

4. Stage and commit:
   ```
   git add .
   git commit -m "Add gitignore to exclude log files and log folder"
   ```

5. Verify nothing log-related got committed:
   ```
   git ls-files
   ```
   You should only see `.gitignore` and `app.js` — no `.log` files, no `log/` folder.

6. Push to your GitLab/GitHub remote:
   ```
   git push origin main
   ```
   (replace `main` with your actual branch name if different)

## Note on already-tracked log files
If a `.log` file was committed BEFORE you added `.gitignore`, ignoring it alone
won't remove it from tracking. You'd additionally need:
```
git rm --cached path/to/old.log
git commit -m "Stop tracking old log file"
```
