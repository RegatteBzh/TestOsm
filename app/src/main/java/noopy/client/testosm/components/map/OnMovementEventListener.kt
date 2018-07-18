package noopy.client.testosm.components.map

/**
 * Created by cmeichel on 12/8/17.
 */

interface OnMovementEventListener {
    /**
     * Event thrown when movement starts
     */
    fun onMovementStart()

    /**
     * Event thrown when movement stops
     */
    fun onMovementEnd()


}