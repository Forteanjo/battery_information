package sco.carlukesoftware.batteryinformation.utils.listeners

/**
 * Interface for listening to updates of a specific data type.
 *
 * Implement this interface to receive notifications when the data of type [T] is updated.
 *
 * @param T The type of data that the listener is interested in.
 */
interface IListenerOnUpdate<T> {
    fun onUpdate(data: T)
}
