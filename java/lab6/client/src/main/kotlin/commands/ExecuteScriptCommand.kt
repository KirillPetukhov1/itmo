package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import objects.Product

@Serializable
class ExecuteScriptCommand<K : Comparable<K>, V : Product> : Command<K, V>() {
    @Transient
    override val description =
        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."

    @Transient
    override val isShouldBeSent = true

    public override fun start(args: Array<String>) {
        if (args.size != 1) {
            throw IllegalArgumentException("Number of arguments is wrong.")
        }
        // TODO
    }

    public override fun finish() {
        println("The collection has been successfully cleared.")
    }
}