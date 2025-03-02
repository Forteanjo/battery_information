package sco.carlukesoftware.batteryinformation.utils.listeners

/**
 * Interface for managing listeners that are notified about updates of a specific type.
 *
 * This interface provides methods to register and unregister listeners that are interested
 * in receiving updates of type [T].
 *
 * @param T The type of data that the listeners are interested in.
 */
interface IRegisterListener<T> {
    fun registerMyListener(listener: IListenerOnUpdate<T>)
    fun unregisterMyListener(listener: IListenerOnUpdate<T>)
}
