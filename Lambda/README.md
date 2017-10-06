### To do
1. Repodriller to analyze diffs
2. AST to analyze lambda expressions
### Related to 1
Able to extract commits from a repo
- DiffVisitor
- DrillingMain
### Related to 2
- Main,Person,Test initial exploration of AST usage to identify lambda expression
- SourceAnalysis, just test source code
- Parse, parses a whole repo and counts lambda expressions, outputs log to results.txt, you need to download the repo and add the path in line 98
### Links
- LambdaExpression node def https://github.com/eclipse/org.aspectj.shadows/blob/master/org.eclipse.jdt.core/dom/org/eclipse/jdt/core/dom/LambdaExpression.java
- LambdaExpression node doc
https://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FLambdaExpression.html&anchor=getBody--
- Some code examples that work with the node
https://www.programcreek.com/java-api-examples/index.php?class=org.eclipse.jdt.core.dom.LambdaExpression&method=getBody
- Repodriller plugin for AST
https://github.com/mauricioaniche/repodriller-plugin-jdt