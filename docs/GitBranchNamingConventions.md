## Branch Naming Convention


Format: `<type>/<description>` or `<type>/<ticket>-<description>`


Types:


* feature/ New functionality

* bugfix/ Bug fixes for existing features

* hotfix/ Urgent production issues

* refactor/ Code improvements without behavior changes

* docs/ Documentation only changes

* test/ Test additions or modifications

* chore/ Build process, dependency updates, etc.


Rules:


* Use lowercase

* Separate words with hyphens

* Keep descriptions under 50 characters

* Be specific: feature/user-auth, not feature/auth

* Include ticket ID when available: feature/PROJ-123-user-auth


## PR Naming Convention  

**Format:**  
`<type>: <short description>`  

or (if using tickets):  
`<type>(<ticket>): <short description>`  

---

**Types:**  

* feat: New feature implemented  
* fix: Bug fix  
* hotfix: Critical urgent fix  
* refactor: Code restructuring without changing behavior  
* docs: Documentation updates only  
* test: Adding or updating tests  
* chore: Maintenance, dependencies, config changes  

---

**Rules:**  

* Use lowercase for type  
* Add a colon after the type (`feat:` not `feat`)  
* Keep the description concise (ideally under 50–72 characters)  
* Use present tense (e.g., `add login screen`, not `added` or `adding`)  
* Be specific and meaningful (avoid vague titles like `update code`)  
* Reference ticket ID if available: `feat(PROJ-123): add user authentication`  
* Start with the most important change  
* Avoid unnecessary words (e.g., `this PR does…`)  
* One PR = one clear purpose  

---

**PR Description (Required):**  

* Clearly explain **what** was done and **why**  
* Mention key changes in bullet points  
* Reference related tickets/issues  
* Include screenshots or recordings for UI changes  
* Highlight any breaking changes or important notes for reviewers  
* Add testing steps (how to verify the changes)  