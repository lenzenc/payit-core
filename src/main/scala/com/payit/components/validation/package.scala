package com.payit.components

import com.payit.components.validation.rules.{StringRules, OrderingRules, GeneralRules}

package object validation extends GeneralRules with OrderingRules with StringRules {

}
