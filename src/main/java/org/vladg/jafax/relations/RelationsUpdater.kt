package org.vladg.jafax.relations

import org.vladg.jafax.io.model.Relations

class RelationsUpdater(
        val sourceName: String
) {

    private val relationsByTarget: MutableMap<String, Relations> = HashMap()

    fun updateRelationsForTarget(targetName: String, updater: (Relations) -> Unit) =
            updater(
                relationsByTarget.computeIfAbsent(targetName) {
                    Relations(
                            source = sourceName,
                            target = targetName
                    )
                }
            )

    fun getRelations() = relationsByTarget.values
}