package com.inkpot.core.domain.hook

internal class HookHolder {

    private val before = HashMap<String, ArrayList<Hook>>()
    private val after = HashMap<String, ArrayList<Hook>>()

    fun addBefore(name: String, hook: Hook) {
        update(before, name, hook)
    }

    fun addAfter(name: String, hook: Hook) {
        update(after, name, hook)
    }

    fun executeBefore(name: String) {
        execute(before, name)
    }

    fun executeAfter(name: String) {
        execute(after, name)
    }

    private fun update(
        map: HashMap<String, ArrayList<Hook>>,
        name: String,
        hook: Hook
    ) {
        if (!map.containsKey(name)) {
            map[name] = ArrayList()
        }
        map[name]?.add(hook)
    }

    private fun execute(
        map: HashMap<String, ArrayList<Hook>>,
        name: String
    ) {
        map[name]?.asSequence()?.forEach { hook -> hook.execute() }
    }

}
