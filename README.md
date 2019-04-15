## Phenomena: Contextual source code behavior integration 
Phenomena is designed to support any type of graph-based representation of source code.
Phenomena's base structure uses the omnilingual source code schema provided by [OmniSRC](https://github.com/CodeBrig/OmniSRC).
This schema can be extended with custom definitions and inference rules which are introduced by using a CodeObserver.

Phenomena comes with the following code observers:

### Structure

#### Description
[CodeStructureObserver](https://github.com/CodeBrig/Phenomena/blob/master/src/main/groovy/com/codebrig/phenomena/code/structure/CodeStructureObserver.groovy)
is the base observer which is required to use Phenomena.
This observer creates nodes and edges which contain the structure of the source code in the form of an abstract syntax graph.

#### Observers

| Structure                   | Supported language(s)              |
| --------------------------- | ---------------------------------- |
| Abstract syntax tree        | [Go, Java, JavaScript, Python](https://github.com/CodeBrig/Phenomena/blob/v0.3.1-alpha/src/main/groovy/com/codebrig/phenomena/code/structure/CodeStructureObserver.groovy) |
| Semantic roles              | [Go, Java, JavaScript, Python](https://github.com/CodeBrig/Phenomena/blob/v0.3.1-alpha/src/main/groovy/com/codebrig/phenomena/code/structure/CodeStructureObserver.groovy) |

#### Schema
[OmniSRC_Omnilingual_Base_Structure (v0.3-alpha)](https://github.com/CodeBrig/OmniSRC/blob/v0.3-alpha/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Base_Structure.gql)

### Dependence

#### Description

The dependence observers create edges between program statements and the preceding statements of which they depend on.

#### Observers

| Metric                      | Supported language(s)              |
| --------------------------- | ---------------------------------- |
| Identifier access           | [Java](https://github.com/CodeBrig/Phenomena/blob/v0.3.1-alpha/src/main/groovy/com/codebrig/phenomena/code/analysis/language/java/dependence/JavaIdentifierAccess.groovy) |
| Method call                 | [Java](https://github.com/CodeBrig/Phenomena/blob/v0.3.1-alpha/src/main/groovy/com/codebrig/phenomena/code/analysis/language/java/dependence/JavaMethodCallObserver.groovy) |

### Metric

#### Description

The metric observers create attributes on correlating source code nodes with calculated metric data.

#### Observers

| Metric                      | Supported language(s)              |
| --------------------------- | ---------------------------------- |
| Cyclomatic complexity       | [Go, Java, JavaScript, Python](https://github.com/CodeBrig/Phenomena/blob/v0.3.1-alpha/src/main/groovy/com/codebrig/phenomena/code/analysis/metric/CyclomaticComplexity.groovy) |
