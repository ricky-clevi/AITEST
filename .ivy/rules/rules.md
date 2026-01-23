# Project Rules
<!-- ivy:auto-generated -->

These rules are loaded into Ivy's system prompt for every task. Edit freely.

## Always Follow
- Read and follow `.ivy/memory.md` and `.ivy/rules/rules.md` before doing any work.
- Follow any repository agent rules (e.g. `AGENTS.md`, `CLAUDE.md`, `.cursorrules`) when present.
- Keep changes minimal and scoped to the user's request.
- Prefer fixing root causes over superficial patches.

## Tooling & Workflow
- Use the repository's existing package manager/lockfile conventions.


- Keep formatting consistent with surrounding code.




- Run the most relevant tests/build steps after changes when feasible.

## Constraints / Limitations

- Avoid destructive actions (deleting files, resetting git history) unless explicitly requested.
- Don't introduce new dependencies unless necessary and approved by the user.
