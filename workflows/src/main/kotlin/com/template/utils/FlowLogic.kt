package com.template.utils

import net.corda.core.flows.FlowLogic
import net.corda.core.utilities.ProgressTracker

/**
 * @author nature
 * @date 3/9/21 下午2:51
 * @email 924943578@qq.com
 */
fun <T> FlowLogic<T>.setStep(step: ProgressTracker.Step) {
    progressTracker!!.currentStep = step
    logger.info("In ${this::class.simpleName} - current step: ${progressTracker!!.currentStep.label}")
}
