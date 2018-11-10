package com.codebrig.phenomena.code

import ai.grakn.GraknTxType
import ai.grakn.client.Grakn
import ai.grakn.graql.QueryBuilder
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import gopkg.in.bblfsh.sdk.v1.uast.generated.Node

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class CodeObserverVisitor {

    private final Grakn.Session session
    private final List<CodeObserver> observers
    private final IdentityHashMap<CodeObserver, ContextualNode> previousNodes
    private final IdentityHashMap<Node, ContextualNode> contextualNodes
    private final boolean saveToGrakn

    CodeObserverVisitor() {
        this.saveToGrakn = false
        this.session = null
        this.observers = new ArrayList<>()
        this.previousNodes = new IdentityHashMap<>()
        this.contextualNodes = new IdentityHashMap<>()
    }

    CodeObserverVisitor(Grakn.Session session) {
        this.saveToGrakn = true
        this.session = Objects.requireNonNull(session)
        this.observers = new ArrayList<>()
        this.previousNodes = new IdentityHashMap<>()
        this.contextualNodes = new IdentityHashMap<>()
    }

    void addObserver(CodeObserver observer) {
        observers.add(Objects.requireNonNull(observer))
    }

    List<CodeObserver> getObservers() {
        return new ArrayList<>(observers)
    }

    void visit(SourceLanguage language, Node rootNode) {
        Objects.requireNonNull(language)
        Objects.requireNonNull(rootNode)
        contextualNodes.putIfAbsent(rootNode, new ContextualNode(this, rootNode, language, rootNode))

        def observed = false
        def contextualRootNode = contextualNodes.get(rootNode)
        observers.each {
            if (it.filter.evaluate(contextualRootNode)) {
                observed = true
                previousNodes.put(it, contextualRootNode)
                it.applyObservation(contextualRootNode, null, null)
            }
        }

        def transaction = null
        def queryBuilder = null
        if (saveToGrakn) {
            transaction = session.transaction(GraknTxType.WRITE)
            queryBuilder = transaction.graql()
            if (observed) {
                contextualRootNode.save(queryBuilder)
            }
        }
        visitRecursively(queryBuilder, contextualRootNode, contextualRootNode.children)
        transaction?.commit()
        transaction?.close()
    }

    private void visitRecursively(QueryBuilder qb, ContextualNode parentNode, Iterator<SourceNode> childNodes) {
        childNodes.each { child ->
            contextualNodes.putIfAbsent(child.underlyingNode, new ContextualNode(this, child))
            def contextualChildNode = contextualNodes.get(child.underlyingNode)

            def observed = false
            observers.each {
                if (it.filter.evaluate(contextualChildNode)) {
                    observed = true
                    if (previousNodes.containsKey(it)) {
                        it.applyObservation(contextualChildNode, parentNode, previousNodes.get(it))
                    } else {
                        it.applyObservation(contextualChildNode, parentNode, null)
                    }
                    previousNodes.put(it, contextualChildNode)
                }
            }
            if (observed && saveToGrakn) {
                contextualChildNode.save(qb)
            }

            visitRecursively(qb, contextualChildNode, child.children)
        }
    }

    ContextualNode getContextualNode(Node node) {
        return contextualNodes.get(node)
    }
}
