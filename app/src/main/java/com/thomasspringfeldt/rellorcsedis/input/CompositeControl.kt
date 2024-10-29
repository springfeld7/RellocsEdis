package com.thomasspringfeldt.rellorcsedis.input

class CompositeControl(vararg inputs: InputManager) : InputManager() {

    private val inputs = ArrayList<InputManager>()

    init {
        for (im in inputs) {
            this.inputs.add(im)
        }
    }

    fun addInput(im: InputManager) {
        inputs.add(im)
    }

    fun setInput(im: InputManager) {
        onPause()
        onStop()
        inputs.clear()
        addInput(im)
    }

    override fun update(deltaTime: Float) {
        isJumping = false
        horizontalFactor = 0f
        verticalFactor = 0f
        for (im in inputs) {
            im.update(deltaTime)
            isJumping = isJumping || im.isJumping
            horizontalFactor += im.horizontalFactor
            verticalFactor += im.verticalFactor
        }
        clampInputs()
    }

    override fun onStart() {
        for (im in inputs) {
            im.onStart()
        }
    }

    override fun onStop() {
        for (im in inputs) {
            im.onStop()
        }
    }

    override fun onPause() {
        for (im in inputs) {
            im.onPause()
        }
    }

    override fun onResume() {
        for (im in inputs) {
            im.onResume()
        }
    }
}