package noopy.client.testosm.model.wind

class WindSpeed(var u: Double, var v: Double) {

    fun valueMeterPerSecond(): Double {
        return Math.sqrt(Math.pow(this.u, 2.0) + Math.pow(this.u, 2.0))
    }

    fun valueKnot(): Double {
        return valueMeterPerSecond() * 1.9438444924574
    }

    fun bearing(): Double {
        return (90 - Math.atan2(this.v, this.u) * 180 / Math.PI + 360) % 360
    }
}